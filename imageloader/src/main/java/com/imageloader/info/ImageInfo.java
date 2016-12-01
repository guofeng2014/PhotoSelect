package com.imageloader.info;

import android.graphics.Bitmap;

import com.imageloader.view.BaseRenderingView;

import java.util.concurrent.locks.ReentrantLock;


public class ImageInfo {

    private Bitmap bitmap;
    private String path;
    private String cacheKey;
    private boolean hasAnimation;
    private BaseRenderingView imageView;
    private ReentrantLock reentrantLock;


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

    public void setReentrantLock(ReentrantLock reentrantLock) {
        this.reentrantLock = reentrantLock;
    }

    public ReentrantLock getReentrantLock() {
        return reentrantLock;
    }
}
