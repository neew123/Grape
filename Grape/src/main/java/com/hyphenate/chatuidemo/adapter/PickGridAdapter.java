package com.hyphenate.chatuidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.chatuidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 16-12-2.
 */
public class PickGridAdapter extends BaseAdapter {
    public final static int NORMAL = 1;
    public final static int ADD = 2;
    private List<String> albumList;
    private Context mContext;

    public PickGridAdapter(Context mContext) {
        albumList = new ArrayList<>();
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        if (albumList.size()<9 && position==getCount()-1)
            return ADD;
        return NORMAL;
    }

    @Override
    public int getCount() {
        if(albumList.size()==9)
            return 9;
        else
            return albumList.size()+1;
    }

    @Override
    public String getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_album_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getItemViewType(position)==NORMAL) {
            String album = albumList.get(position);
            Glide.with(mContext).load(album).into(viewHolder.imageView);
        }else {
            Glide.with(mContext).load(R.drawable.ease_default_avatar).into(viewHolder.imageView);
        }
        return convertView;
    }

    public void setData(List albumList) {
        this.albumList = albumList;
        this.notifyDataSetInvalidated();
    }

    public void removeData(String album) {
        if (albumList != null && albumList.contains(album)) {
            //判断当前的数量
            albumList.remove(album);
            this.notifyDataSetInvalidated();
        }
    }

    public List<String> getData() {
        return albumList;
    }

    private static class ViewHolder {
        public ImageView imageView;
    }
}