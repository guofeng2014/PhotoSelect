package com.photoselect.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.imageloader.download.ImageDownLoader;
import com.photoselect.R;
import com.photoselect.cache.ImagePools;

import java.util.ArrayList;

/**
 * 作者：guofeng
 * 日期:16/11/22
 */

public class PhotoDetailAdapter extends PagerAdapter {

    /**
     * Photo path source data
     */
    private ArrayList<String> data = new ArrayList<>();

    /**
     * ImageView Cache pools
     */
    private ImagePools pools;

    public PhotoDetailAdapter(Context context) {
        pools = new ImagePools(context);
    }

    public void addData(ArrayList<String> data) {
        if (data == null || data.size() == 0) return;
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public ArrayList<String> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object.equals(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View parent = pools.get();
        ImageView ivDetail = (ImageView) parent.findViewById(R.id.iv_detail);
        String uri = data.get(position);
        if (uri.startsWith(ImageDownLoader.HEAD_HTTP) || uri.startsWith(ImageDownLoader.HEAD_HTTPS)) {
            com.imageloader.ImageLoader.getInstance().display(ivDetail, uri);
        } else {
            com.imageloader.ImageLoader.getInstance().display(ivDetail, ImageDownLoader.HEAD_FILE + uri);
        }
        container.addView(parent, 0);
        return parent;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        pools.remove((LinearLayout) object);
    }

    /**
     * clear data and release reference
     */
    public void clear() {
        data.clear();
        pools.clear();
    }
}
