package com.hyphenate.bmob.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by cheng on 16-12-1.
 */
public class Comment extends BmobObject implements Serializable {
    public Comment(){
        this.setTableName("Comment");
    }

    private String author;
    private String content;
    private boolean reply;
    private String replyId;
    private Post post;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }
}
