package com.hyphenate.bmob.model;

import android.util.Log;

import com.hyphenate.bmob.domain.MyUser;
import com.hyphenate.bmob.model.impl.UserModelImpl;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by cheng on 16-11-30.
 */
public class UserModel extends UserModelImpl {

    @Override
    public void getUser(String username, String password, final UserListener<MyUser> listener) {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", username);
        query.addWhereEqualTo("password", password);
        query.setLimit(1);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> ary, BmobException e) {
                if(e==null){
                    Log.e("bmob","查询成功："+ary.toString());
                    listener.getSuccess(ary.get(0));
                }else{
                    Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    listener.getFailure();
                }
            }
        });
    }

    @Override
    public void createUser(MyUser user, final UserListener<MyUser> listener) {
        user.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
                    Log.e("bmob","创建成功："+myUser.toString());
                    listener.getSuccess(myUser);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    listener.getFailure();
                }
            }
        });
    }
}
