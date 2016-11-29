package com.imageloader.view;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 作者：guofeng
 * ＊ 日期:16/11/25
 * baseView to rendering bitmap,BaseImageView to extend this
 */

public interface BaseRenderingView {
    /**
     * Returns width of image aware.This value is used to define scale size for original image
     * Can return 0 if width is undefined
     */
    int getWidth();

    /**
     * Returns height of image aware.This value is used to define scale size for original image.
     * Can return 0 if height is undefined
     */
    int getHeight();

    /**
     * Sets image bitmap into this image aware view
     *
     * @return <b>true</b> if bitmap was set successfully;<b>false<b/> -otherwise
     */
    boolean setImageBitmap(Bitmap bitmap);

    /**
     * Sets image resource into this image aware view
     *
     * @return <b>true</b> if this resource was set successfully <b>false</b> -otherwise
     */
    boolean setImageResource(int resource);

    /**
     * Returns Id of image aware view. Point of ID is similar to Object's hashCode.This ID should be Unique for every
     * image view instance and should be the for same instances .This ID identifies processing task in  ImageLoader
     * it cancels old task with this ID(if any)and starts new task
     *
     * @return <b>true<b/> -if view is collected by GC and ImageLoader should processing this image aware view
     */
    int getId();

    /**
     * Returns a flag whether image aware is collected by GC or whatsoever . If so then ImageLoader stop processing
     * <b>false<b/>- otherwise
     */
    boolean isCollected();

    /**
     * Returns ImageView
     * @return
     */
    View getImageView();
}
