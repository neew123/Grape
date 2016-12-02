package com.hyphenate.bmob.domain;

import java.io.Serializable;
import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by cheng on 16-12-1.
 */
public class Post extends BmobObject implements Serializable{

    public Post(){
        this.setTableName("Post");
    }

    private String author;
    private String content;
    private ArrayList<BmobFile> photoList;
    private ArrayList<String> likeList;

    public ArrayList<BmobFile> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<BmobFile> photoList) {
        this.photoList = photoList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<String> getLikeList() {
        return likeList;
    }

    public void setLikeList(ArrayList<String> likeList) {
        this.likeList = likeList;
    }
}
