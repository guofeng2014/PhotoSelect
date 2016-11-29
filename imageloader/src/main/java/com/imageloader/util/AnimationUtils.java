package com.imageloader.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;

/**
 * 作者：guofeng
 * ＊ 日期:16/7/4
 */
public class AnimationUtils {
    /**
     * 动画时间200毫秒
     */
    private static final long DURATION = 200;

    public static void alphaAnimation(View view) {
        AlphaAnimation animation = new AlphaAnimation(0.5f, 1);
        animation.setDuration(DURATION);
        animation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(animation);
    }
}
