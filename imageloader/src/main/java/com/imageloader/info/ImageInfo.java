package com.imageloader.info;

import android.graphics.Bitmap;

import com.imageloader.view.BaseRenderingView;


public class ImageInfo {

    private Bitmap bitmap;
    private String path;
    private String cacheKey;
    private boolean hasAnimation;
    private BaseRenderingView imageView;


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public void setHasAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
    }

    public void setImageView(BaseRenderingView imageView) {
        this.imageView = imageView;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getPath() {
        return path;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public boolean isHasAnimation() {
        return hasAnimation;
    }

    public BaseRenderingView getImageView() {
        return imageView;
    }
}
