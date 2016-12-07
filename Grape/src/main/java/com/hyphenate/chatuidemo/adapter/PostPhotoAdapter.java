package com.hyphenate.chatuidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.chatuidemo.R;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by cheng on 16-12-7.
 */
public class PostPhotoAdapter extends BaseAdapter {

    private List<BmobFile> data;
    private Context context;

    public PostPhotoAdapter(Context context, List<BmobFile> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return this.data==null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.post_album_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(data.get(position).getUrl()).into(viewHolder.imageView);
        return convertView;
    }

    private static class ViewHolder {
        public ImageView imageView;
    }
}
