package com.hyphenate.bmob.model.impl;

import android.webkit.WebView;

import com.hyphenate.bmob.domain.MyUser;

/**
 * Created by cheng on 16-11-30.
 * 测试使用
 */
public  abstract class UserModelImpl {
    public abstract void getUser(String username,String password,UserListener<MyUser> listener);

    public abstract void createUser(MyUser user,UserListener<MyUser> listener);

    public interface UserListener<T>{
        void getSuccess(T t);
        void getFailure();
    }
}
