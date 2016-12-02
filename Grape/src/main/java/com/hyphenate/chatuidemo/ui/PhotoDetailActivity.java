package com.hyphenate.chatuidemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chatuidemo.R;
import com.hyphenate.chatuidemo.task.GlideDownloadImage;
import com.hyphenate.chatuidemo.utils.FileUtils;
import com.hyphenate.chatuidemo.widget.PhotoView;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.io.File;

/**
 * Created by cheng on 16-12-2.
 */
public class PhotoDetailActivity extends BaseActivity {

    public static final int DELETE_CODE = 2000;
    private PhotoView photoview;
    private String url;
    private EaseTitleBar titleBar;
    private int mode;//1：代表预览保存，2：代表预览删除
    private Handler handler;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_photodetial);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        mode = intent.getIntExtra("mode",1);
        initView();
    }

    private void initView() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Toast.makeText(PhotoDetailActivity.this,"保存到"+msg.getData().getString("path","null"),Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(PhotoDetailActivity.this,"保存失败1",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(PhotoDetailActivity.this,"保存失败2 file为空",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        titleBar = (EaseTitleBar)findViewById(R.id.title_bar);
        titleBar.setTitle("浏览");
        photoview = (PhotoView)findViewById(R.id.photo_view);
        // 启用图片缩放功能
        photoview.enable();
        if(url !=null){
            Glide.with(this).load(url).into(photoview);
        }else {
            Glide.with(this).load(R.drawable.ease_default_avatar).into(photoview);
        }

        if(mode == 1) {//保存图片
            titleBar.setRightImageResource(R.drawable.ic_save_white_36dp);
            titleBar.setRightLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlideDownloadImage service = new GlideDownloadImage(PhotoDetailActivity.this,
                            url, new GlideDownloadImage.ImageDownLoadCallBack() {
                                @Override
                                public void onDownLoadSuccess(File from) {
                                    // 在这里执行图片保存方法
                                    String file_path = Environment.getExternalStorageDirectory() + "/" + FileUtils.FOLDER + "/" + System.currentTimeMillis()+".jpg";
                                    File to = new File(file_path);
                                    FileUtils.createFolder();
                                    if(FileUtils.CopyFile(from,to)){
                                        Message msg = Message.obtain();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("path",file_path);
                                        msg.setData(bundle);
                                        msg.what = 1;
                                        handler.sendMessage(msg);
//                                        Toast.makeText(PhotoDetailActivity.this,"保存到"+file_path,Toast.LENGTH_SHORT).show();
                                    }else {
                                        // 图片保存失败
                                        handler.sendEmptyMessage(2);
//                                        Toast.makeText(PhotoDetailActivity.this,"保存失败1",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onDownLoadFailed() {
                                    // 图片保存失败
                                    handler.sendEmptyMessage(3);
//                                    Toast.makeText(PhotoDetailActivity.this,"保存失败2",Toast.LENGTH_SHORT).show();
                                }
                            });
                    new Thread(service).start();
                }
            });
        }

        if(mode == 2) {//删除图片
            titleBar.setRightImageResource(R.drawable.ease_mm_title_remove);
            titleBar.setRightLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("url",url);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }
    }
}
