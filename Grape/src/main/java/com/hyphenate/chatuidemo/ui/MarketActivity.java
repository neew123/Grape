package com.hyphenate.chatuidemo.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.GridView;
import android.widget.ImageButton;

import com.hyphenate.bmob.domain.Post;
import com.hyphenate.bmob.model.PostModel;
import com.hyphenate.bmob.model.impl.PostModelImpl;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.R;
import com.hyphenate.chatuidemo.widget.ObservableScrollView;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
/**
 * Created by cheng on 16-12-1.
 */
public class MarketActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "MarketActivity";

    private SwipeRefreshLayout refreshLayout;
    private ObservableScrollView scrollView;
    private ImageButton fab;
    private EaseTitleBar titlebar;
    private GridView gridview;
    private float showFabTranslationY;
    private float hideFabTranslationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_market);
        initView();
        Log.e(TAG, "onCreate: "+EMClient.getInstance().getCurrentUser());
        Log.e(TAG,""+ DemoHelper.getInstance().getCurrentUsernName());
        Log.e(TAG,""+ EaseUserUtils.getUserInfo(DemoHelper.getInstance().getCurrentUsernName()));
//        testPost();
    }

    private void testPost() {
        Post post = new Post();
        post.setAuthor(EMClient.getInstance().getCurrentUser());
        post.setContent("第一条朋友圈");
        PostModel postModel = new PostModel();
        postModel.createPost(post, new PostModelImpl.PostListener<String>() {
            @Override
            public void getSuccess(String s) {
                Log.e(TAG, "getSuccess: ");
            }
            @Override
            public void getFailure() {
                Log.e(TAG,"Failure");
            }
        });
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        fab = (ImageButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);
        titlebar = (EaseTitleBar)findViewById(R.id.title_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int blue = getResources().getColor(R.color.holo_blue_bright,getTheme());
            int green = getResources().getColor(R.color.holo_green_light,getTheme());
            int orange = getResources().getColor(R.color.holo_orange_light,getTheme());
            int red = getResources().getColor(R.color.holo_red_light,getTheme());
            refreshLayout.setColorSchemeColors(blue,green,orange,red);
        }

        showFabTranslationY = fab.getTranslationY();
        hideFabTranslationY = showFabTranslationY+500f;

        titlebar.setRightImageResource(R.drawable.ic_search_white_36dp);
        titlebar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        scrollView.setScrollListener(new ObservableScrollView.ScrollListener() {
            @Override
            public void scrollOritention(int oritention) {
                if(oritention == ObservableScrollView.SCROLL_UP && fab.getVisibility() == View.VISIBLE ){
                    hideFabAnim();
                }
                if(oritention == ObservableScrollView.SCROLL_DOWN && fab.getVisibility() == View.GONE ){
                    showFabAnim();
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void hideFabAnim(){
        ObjectAnimator hideAnimtor = ObjectAnimator.ofFloat(fab,"translationY",showFabTranslationY,hideFabTranslationY);
        hideAnimtor.setDuration(500);
        hideAnimtor.setInterpolator(new AccelerateInterpolator());
        hideAnimtor.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                fab.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        hideAnimtor.start();
    }

    private void showFabAnim(){
        ObjectAnimator showAnimtor = ObjectAnimator.ofFloat(fab,"translationY",hideFabTranslationY,showFabTranslationY);
        showAnimtor.setDuration(500);
        showAnimtor.setInterpolator(new AccelerateDecelerateInterpolator());
        showAnimtor.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                fab.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animator) {
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        showAnimtor.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                startActivity(new Intent(MarketActivity.this,NewPostActivity.class));
                break;
            default:
                break;
        }
    }

}
