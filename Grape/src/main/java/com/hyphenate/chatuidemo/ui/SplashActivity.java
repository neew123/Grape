package com.hyphenate.chatuidemo.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.bmob.domain.MyUser;
import com.hyphenate.bmob.model.UserModel;
import com.hyphenate.bmob.model.impl.UserModelImpl;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.R;
import com.hyphenate.util.EasyUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {

	private static final int sleepTime = 3000;
	private static final String TAG = "SplashActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		initBmobConfig();

//		testBmob();

		setContentView(R.layout.em_activity_splash);
		super.onCreate(arg0);

		RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
		TextView versionText = (TextView) findViewById(R.id.tv_version);

		versionText.setText(getVersion());
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		rootLayout.startAnimation(animation);

	}

	/**
	 * 测试使用
	 */
	public void testBmob(){
		UserModelImpl userModel = new UserModel();
		MyUser user = new MyUser();
		user.setUsername("kk");
		user.setPassword("123");
		user.setPhotoUrl("www.123.com");

		Log.e(TAG, "initView: 测试Bmob1" );
		userModel.createUser(user, new UserModelImpl.UserListener<MyUser>() {
			@Override
			public void getSuccess(MyUser myUser) {
				Log.e("bmob","创建成功："+myUser.toString());
				Toast.makeText(SplashActivity.this,"出马成功"+myUser.toString(),Toast.LENGTH_SHORT);
			}
			@Override
			public void getFailure() {
				Log.e("bmob","创建失败");
				Toast.makeText(SplashActivity.this,"出马是吧",Toast.LENGTH_SHORT);
			}
		});
	}

	private void initBmobConfig() {
		//设置BmobConfig：允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)
		BmobConfig config = new BmobConfig.Builder(this)
				//设置appkey(必填)
				.setApplicationId("f0851f30648856a68e1a846db6dfb492")
				//请求超时时间（单位为秒）：默认15s
				.setConnectTimeout(30)
				//文件分片上传时每片的大小（单位字节），默认512*1024
				.setUploadBlockSize(1024 * 1024)
				//文件的过期时间(单位为秒)：默认1800s
				.setFileExpiration(5500)
				.build();
		Bmob.initialize(config);
	}

	@Override
	protected void onStart() {
		super.onStart();

		new Thread(new Runnable() {
			public void run() {
				if (DemoHelper.getInstance().isLoggedIn()) {
					// auto login mode, make sure all group and conversation is loaed before enter the main screen
					long start = System.currentTimeMillis();
					EMClient.getInstance().groupManager().loadAllGroups();
					EMClient.getInstance().chatManager().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					//wait
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					String topActivityName = EasyUtils.getTopActivityName(EMClient.getInstance().getContext());
					if (topActivityName != null && (topActivityName.equals(VideoCallActivity.class.getName()) || topActivityName.equals(VoiceCallActivity.class.getName()))) {
						// nop
						// avoid main screen overlap Calling Activity
					} else {
						//enter main screen
						startActivity(new Intent(SplashActivity.this, MainActivity.class));
					}
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			}
		}).start();

	}

	/**
	 * get version
	 */
	private String getVersion() {
//	    return EMClient.getInstance().getChatConfig().getVersion();
		try {
			PackageInfo pi=this.getPackageManager().getPackageInfo(getPackageName(), 0);
			return pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return " ";
		}
	}
}
