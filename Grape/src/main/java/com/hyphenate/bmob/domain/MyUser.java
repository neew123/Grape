package com.hyphenate.bmob.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by cheng on 16-11-30.
 */
public class MyUser extends BmobUser implements Serializable {

    private String photoUrl;

    public MyUser(){
        this.setTableName("_User");
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
