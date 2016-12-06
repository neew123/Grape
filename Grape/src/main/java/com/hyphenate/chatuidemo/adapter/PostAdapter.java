package com.hyphenate.chatuidemo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.bmob.domain.Post;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.R;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 16-12-6.
 */
public class PostAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PostAdataper";
    private static final int VIEW_ITEM = 0;
    private static final int VIEW_PROG = 1;
    private static final int VIEW_NODATA = 2;
    private static final int VIEW_BACKGROUND = 3;
    private List<Post> list;
    private Context context;
    private RecyclerView mRecyclerView;
    private LayoutInflater inflater;
    private boolean isLoading;
    private boolean hasData;
    //无数据加载
    private int totalItemCount;
    private int lastVisibleItemPosition;
    //当前滚动的position下面最小的items的临界值
    private int visibleThreshold = 2;

    public PostAdapter(Context context, RecyclerView recyclerView){
        this.context = context;
        this.mRecyclerView = recyclerView;
        this.list = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        hasData = false;
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            //mRecyclerView添加滑动事件监听
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//                    Log.d("test", "totalItemCount =" + totalItemCount + "---->" + "lastVisibleItemPosition =" + lastVisibleItemPosition);
                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                        //此时是刷新状态
                        if (mMoreDataListener != null && hasData ==true) {
                            isLoading = true;
                            list.add(null);
                            //加入null值此时adapter会判断item的type
                            mMoreDataListener.loadMoreData();
                        }
                    }
                }
            });
        }
    }

    /**
     * @param list 数据
     * @param hasData 是否还有数据
     */
    public void setList(List list, Boolean hasData) {
        setHasData(hasData);
        this.list.clear();
        this.list.addAll(list);
        Log.e(TAG, "setLoaded: "+list.size() );
        if(hasData==false)
            this.list.add(null);
        isLoading = false;
        PostAdapter.this.notifyDataSetChanged();
    }

    public void setHasData(Boolean hasData){
        this.hasData = hasData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_BACKGROUND){
            View view = inflater.inflate(R.layout.activity_post_item_background,parent,false);
            return new BackgroundHolder(view);
        }
        if(viewType == VIEW_ITEM) {
            View view = inflater.inflate(R.layout.activity_post_item, parent, false);
            return new PostViewHolder(view);
        }else if(viewType == VIEW_PROG){
            View view = inflater.inflate(R.layout.activity_post_item_footer, parent, false);
            return new ProgressViewHolder(view);
        }else {
            View view = inflater.inflate(R.layout.activity_post_item_footer_nodata, parent, false);
            return new NoDatazViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PostViewHolder){
            final Post post = list.get(position-1);
            final EaseUser user = EaseUserUtils.getUserInfo(post.getAuthor());
            Log.e(TAG, "onBindViewHolder: "+user.getAvatar());

            ((PostViewHolder)holder).nickname.setText(user.getNick());
            if(post.getContent()!=null && post.getContent()!="") {
                ((PostViewHolder) holder).content.setText(post.getContent());
            }

            if(user.getAvatar()==null) {
                DemoHelper.getInstance().getUserProfileManager().asyncGetUserInfo(post.getAuthor(), new EMValueCallBack<EaseUser>() {
                    @Override
                    public void onSuccess(EaseUser user) {
                        if (user != null) {
                            DemoHelper.getInstance().saveContact(user);
                            if (!TextUtils.isEmpty(user.getAvatar())) {
                                Glide.with(context).load(user.getAvatar()).placeholder(R.drawable.em_default_avatar).into(((PostViewHolder) holder).photo);
                            } else {
                                Glide.with(context).load(R.drawable.em_default_avatar).into(((PostViewHolder) holder).photo);
                            }
                        }
                    }
                    @Override
                    public void onError(int i, String s) {
                        Glide.with(context).load(R.drawable.em_default_avatar).into(((PostViewHolder) holder).photo);
                    }
                });
            }
            else {
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(((PostViewHolder) holder).photo);
            }

        }else if(holder instanceof ProgressViewHolder){
            if (((ProgressViewHolder) holder).pb != null)
                ((ProgressViewHolder) holder).pb.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list==null ? 1:list.size()+1;
    }

    public void setList(List list){
        this.list = list;
    }

    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return VIEW_BACKGROUND;
        if(list.get(position-1)!=null)
            return VIEW_ITEM;
        if(hasData==true)
            return VIEW_PROG;
        return VIEW_NODATA;
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar pb;
        public ProgressViewHolder(View itemView) {
            super(itemView);
            pb = (ProgressBar) itemView.findViewById(R.id.new_post_item_pb);
        }
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
        private ImageView photo;
        private TextView nickname;
        private TextView sendtime;
        private TextView content;

        public PostViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView)itemView.findViewById(R.id.post_photo);
            nickname = (TextView)itemView.findViewById(R.id.post_nickname);
            sendtime = (TextView)itemView.findViewById(R.id.post_sendtime);
            content = (TextView)itemView.findViewById(R.id.post_content);
        }
    }

    class NoDatazViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public  NoDatazViewHolder(View itemView){
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.post_nodata);
        }
    }

    class BackgroundHolder extends RecyclerView.ViewHolder{
        private ImageView backgroud;
        public BackgroundHolder(View itemView) {
            super(itemView);
            backgroud = (ImageView)itemView.findViewById(R.id.post_background_image);
        }
    }

    private LoadMoreDataListener mMoreDataListener;

    //加载更多监听方法
    public void setOnMoreDataLoadListener(LoadMoreDataListener onMoreDataLoadListener) {
        mMoreDataListener = onMoreDataLoadListener;
    }

    private RecyclerOnItemClickListener mOnitemClickListener;

    //点击事件监听方法
    public void setOnItemClickListener(RecyclerOnItemClickListener onItemClickListener) {
        mOnitemClickListener = onItemClickListener;
    }

    /**
     * recycle item点击事件
     */
    public interface RecyclerOnItemClickListener {
        void onClick(View view, Post post);
    }

    /**
     * recycle 下滑加载
     */
    public interface LoadMoreDataListener {
        void loadMoreData();
    }

}
