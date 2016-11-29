package com.photoselect.util;

import android.content.Context;

/**
 * 作者：guofeng
 * ＊ 日期:16/11/16
 */

public class Scale {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
