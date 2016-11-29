package com.photoselect.cache;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：guofeng
 * ＊ 日期:16/11/25
 */

public abstract class ViewPools<T extends View> {

    private List<View> cachePools = new ArrayList<>();

    public View get() {
        int size = cachePools.size();
        if (size == 0) {
            return createView();
        } else {
            View view = cachePools.get(0);
            cachePools.remove(view);
            return view;
        }
    }

    public void remove(T view) {
        cachePools.add(view);
    }

    public void clear() {
        cachePools.clear();
    }

    public abstract View createView();
}
