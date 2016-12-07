package com.hyphenate.chatuidemo.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hyphenate.bmob.domain.Post;
import com.hyphenate.bmob.model.PostModel;
import com.hyphenate.bmob.model.impl.PostModelImpl;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.R;
import com.hyphenate.chatuidemo.adapter.PostAdapter;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 16-12-1.
 */
public class MarketActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "MarketActivity";

    private SwipeRefreshLayout refreshLayout;
    private ImageButton fab;
    private EaseTitleBar titlebar;
    private GridView gridview;
    private float showFabTranslationY;
    private float hideFabTranslationY;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList = null;
    private int pageNum = 0;
    private long[] mHits = new long[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_market);
        initView();
        Log.e(TAG, "onCreate: "+EMClient.getInstance().getCurrentUser());
        Log.e(TAG,""+ DemoHelper.getInstance().getCurrentUsernName());
        Log.e(TAG,""+ EaseUserUtils.getUserInfo(DemoHelper.getInstance().getCurrentUsernName()));
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postList.clear();
                pageNum = 0;
                updateData(pageNum++,true);
            }
        });

        initRecycleView();

        titlebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //实现左移，然后最后一个位置更新为距离开机的时间，如果最后一个时间和最开始时间小于2000，即n击
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                long spaceTime = mHits[mHits.length - 1] - mHits[0];
                if ( 1000 >= spaceTime  ) {
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,null,0);
                }
            }
        });
    }



    private void initRecycleView() {
        postList = new ArrayList();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postAdapter = new PostAdapter(this,recyclerView);
        recyclerView.setAdapter(postAdapter);
        postAdapter.setOnMoreDataLoadListener(
                new PostAdapter.LoadMoreDataListener() {
                    @Override
                    public void loadMoreData() {
                        updateData(pageNum++,false);
                    }
                });

        updateData(pageNum++,true);

        postAdapter.setOnItemClickListener(new PostAdapter.RecyclerOnItemClickListener(){
            @Override
            public void onClick(View view,Post post) {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            final int mTouchSlop = ViewConfiguration.get(MarketActivity.this).getScaledTouchSlop();
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // dy <0 表示 上滑， dy>0 表示下滑
                if (!recyclerView.canScrollVertically(-1) && fab.getVisibility() == View.GONE ) {//顶部
                    showFabAnim();
                } else if (!recyclerView.canScrollVertically(1)  && fab.getVisibility() == View.VISIBLE) {//底部
                    hideFabAnim();
                } else if(dy<0 && Math.abs(dy)>=mTouchSlop && fab.getVisibility() == View.GONE){
                    showFabAnim();
                } else if(dy>0 && Math.abs(dy)>=mTouchSlop && fab.getVisibility() == View.VISIBLE){
                    hideFabAnim();
                }
            }
        });
    }

    private void updateData(int pageNum,boolean showRefresh){
        Log.e(TAG, "updateData: loading" );

        if(refreshLayout!=null && !refreshLayout.isRefreshing() && showRefresh){
            refreshLayout.setProgressViewOffset(false, 0, DensityUtil.dip2px(MarketActivity.this, 24));
            refreshLayout.setRefreshing(true);
        }

        new PostModel().getPost(pageNum, new PostModelImpl.PostListener<List<Post>>() {
            @Override
            public void getSuccess(List<Post> posts) {
                postList.addAll(posts);
                Log.e(TAG, "updateData: loading"+posts.size() );
                if(posts.size()>=10){
                    postAdapter.setList(postList,true);
                }else {
                    postAdapter.setList(postList,false);
                }
                if(refreshLayout!=null && refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void getFailure() {
                Toast.makeText(MarketActivity.this,"信息获取失败",Toast.LENGTH_SHORT).show();
                if(refreshLayout!=null && refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
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
