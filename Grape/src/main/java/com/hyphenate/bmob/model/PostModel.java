package com.hyphenate.bmob.model;

import android.util.Log;

import com.hyphenate.bmob.domain.Comment;
import com.hyphenate.bmob.domain.Post;
import com.hyphenate.bmob.model.impl.PostModelImpl;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by cheng on 16-12-1.
 */
public class PostModel extends PostModelImpl {
    @Override
    public void createPost(Post post, final PostListener<String> listener) {
        post.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Log.e("bmob","创建成功：");
                    listener.getSuccess(objectId);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    listener.getFailure();
                }
            }
        });
    }

    @Override
    public void getPost(int page, final PostListener<List<Post>> listener) {
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.setLimit(10);
        if(page>0)
            query.setSkip(page*10);
        query.order("-createdAt");//按创建时间排序，（- ：降序；+ 升序）
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> ary, BmobException e) {
                if(e==null){
                    Log.e("bmob","查询成功："+ary.toString());
                    listener.getSuccess(ary);
                }else{
                    Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    listener.getFailure();
                }
            }
        });
    }
}
