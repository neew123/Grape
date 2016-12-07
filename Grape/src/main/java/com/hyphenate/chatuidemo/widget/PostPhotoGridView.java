package com.hyphenate.chatuidemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by cheng on 16-12-7.
 */
public class PostPhotoGridView extends GridView {
    public PostPhotoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostPhotoGridView(Context context) {
        super(context);
    }

    public PostPhotoGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}