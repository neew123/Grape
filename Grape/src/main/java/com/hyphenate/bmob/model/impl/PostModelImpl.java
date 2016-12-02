package com.hyphenate.bmob.model.impl;

import com.hyphenate.bmob.domain.Comment;
import com.hyphenate.bmob.domain.Post;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by cheng on 16-12-1.
 */
public abstract class PostModelImpl{

    public abstract void createPost(Post post,PostListener<String> listener);
    public abstract void getPost(int page, PostListener<List<Post>> listener);

    public interface PostListener<T>{
        void getSuccess(T t);
        void getFailure();
    }
}