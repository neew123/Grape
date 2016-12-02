package com.hyphenate.chatuidemo.task;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by cheng on 16-12-2.
 * glide保存到本地
 */
public class GlideDownloadImage implements Runnable {

    public interface ImageDownLoadCallBack {
        void onDownLoadSuccess(File file);
        void onDownLoadFailed();
    }

    private String url;
    private Context context;
    private ImageDownLoadCallBack callBack;

    public GlideDownloadImage(Context context, String url, ImageDownLoadCallBack callBack) {
        this.url = url;
        this.callBack = callBack;
        this.context = context;
    }

    @Override
    public void run() {
        File file = null;
        try {
            file = Glide.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                callBack.onDownLoadSuccess(file);
            } else {
                callBack.onDownLoadFailed();
            }
        }
    }
}