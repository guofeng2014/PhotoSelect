package com.photoselect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imageloader.ImageLoader;
import com.photoselect.R;

import java.util.ArrayList;

/**
 * 作者：guofeng
 * 日期:2016/11/29
 */

public class NetPhotoListAdapter extends BaseAdapter {
   private ArrayList<String> data=new ArrayList<>();
    private Context context;

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public NetPhotoListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_net_photo, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ImageLoader.getInstance().display(holder.ivIcon, getItem(i));
        return view;
    }

    static class ViewHolder {
        ImageView ivIcon;

        public ViewHolder(View view) {
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }
}
