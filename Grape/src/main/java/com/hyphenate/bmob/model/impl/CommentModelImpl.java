package com.hyphenate.bmob.model.impl;

import com.hyphenate.bmob.domain.Comment;

import java.util.List;

/**
 * Created by cheng on 16-12-1.
 */
public abstract class CommentModelImpl {
    public abstract void createComment(Comment comment,CommentLisenter<String> lisenter);
    public abstract void getComment(String postId, CommentLisenter<List<Comment>> lisenter);

    public interface CommentLisenter<T>{
        void getSuccess(T t);
        void getFailure();
    }
}