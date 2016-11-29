package com.photoselect.cache;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.photoselect.R;

/**
 * 作者：guofeng
 * 日期:16/11/25
 */

public class ImagePools extends ViewPools<LinearLayout> {

    private Context context;

    public ImagePools(Context context) {
        this.context = context;
    }

    public View createView() {
        return LayoutInflater.from(context).inflate(R.layout.view_photo_detail_item, null);
    }
}
