package com.photoselect.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imageloader.ImageLoader;
import com.imageloader.download.ImageDownLoader;
import com.photoselect.R;

import java.util.ArrayList;


public class AlbumListAdapter extends BaseAdapter {
    private ArrayList<String> data;
    private LayoutInflater inflater;
    private int height;

    public AlbumListAdapter(Context context, ArrayList<String> data) {
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        height = dm.widthPixels / 3;
    }

    public void setData(ArrayList<String> list) {
        this.data = list;
    }

    public ArrayList<String> getData() {
        return data;
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public String getItem(int position) {
        if (data == null || position >= data.size()) {
            return "";
        }
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 2448-3264   1080-1777
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_image, null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.ivStatus = (ImageView) convertView.findViewById(R.id.iv_status);
            ViewGroup.LayoutParams lp = holder.ivImage.getLayoutParams();
            lp.height = height;
            lp.width = height;
            holder.ivImage.setLayoutParams(lp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String path = getItem(position);
        ImageLoader.getInstance().display(holder.ivImage, ImageDownLoader.HEAD_FILE+path);
        holder.ivStatus.setImageResource(R.mipmap.ic_album_uncheck);
        return convertView;
    }

    static class ViewHolder {
        ImageView ivImage;
        ImageView ivStatus;
    }
}
