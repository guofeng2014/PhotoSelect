package com.imageloader.config;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.imageloader.cache.file.FileCache;
import com.imageloader.cache.file.FileCacheImp;
import com.imageloader.cache.memory.MemoryCacheImp;
import com.imageloader.cache.memory.MemoryCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 作者：guofeng
 * 日期:16/6/27
 * 图片配置信息
 */
public final class ImageLoaderConfig {

    /**
     * 图片加载默认显示的图片
     */
    private int defaultResourceId;
    /**
     * 图片加载失败显示的图片
     */
    private int errorResourceId;
    /**
     * 线程数量
     */
    private int threadCount;
    /**
     * 缓存的容器
     */
    private MemoryCache mLruCache;
    /**
     * 文件缓存
     */
    private FileCache fileCache;
    /**
     * 线程池
     */
    private ExecutorService threadPool;
    /**
     * 图片的压缩质量参数
     */
    private Bitmap.Config imageConfig = null;
    /**
     * 关联ImageView 和 文件路径
     */
    private Map<Integer, String> associateImageAndUri;
    /**
     * 加载是否有动画
     */
    private boolean openAnimation;
    /**
     * 是否打开文件缓存
     */
    private boolean openFileCache;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    public ImageLoaderConfig(Builder builder) {
        this.defaultResourceId = builder.getDefaultResourceId();
        this.errorResourceId = builder.getErrorResourceId();
        this.threadCount = builder.getThreadCount() == 0 ? 3 : builder.getThreadCount();
        this.mLruCache = builder.getmLruCache() == null ? new MemoryCacheImp() : builder.getmLruCache();
        this.threadPool = Executors.newFixedThreadPool(getThreadCount());
        this.imageConfig = builder.getImageConfig() == null ? Bitmap.Config.ARGB_8888 : builder.getImageConfig();
        this.associateImageAndUri = new HashMap<>();
        this.openAnimation = builder.isOpenAnimation();
        this.openFileCache = builder.isOpenFileCache();
        this.fileCache = this.openFileCache ? new FileCacheImp() : null;
    }


    public int getDefaultResourceId() {
        return defaultResourceId;
    }

    public int getErrorResourceId() {
        return errorResourceId;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public MemoryCache<String,Bitmap> getmLruCache() {
        return mLruCache;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }


    public Bitmap.Config getImageConfig() {
        return imageConfig;
    }

    public Map<Integer, String> getAssociateImageAndUri() {
        return associateImageAndUri;
    }

    public boolean isOpenAnimation() {
        return openAnimation;
    }

    public FileCache getFileCache() {
        return fileCache;
    }

    public static class Builder {
        /**
         * 图片加载默认显示的图片
         */
        private int defaultResourceId;
        /**
         * 图片加载失败显示的图片
         */
        private int errorResourceId;
        /**
         * 线程数量
         */
        private int threadCount;
        /**
         * 缓存的容器
         */
        private MemoryCacheImp mLruCache;
        /**
         * 图片的压缩质量参数
         */
        private Bitmap.Config imageConfig;

        /**
         * 是否有动画
         */
        private boolean openAnimation;
        /**
         * 是否打开文件缓存
         */
        private boolean openFileCache;

        public Builder setDefaultResourceId(int defaultResourceId) {
            this.defaultResourceId = defaultResourceId;
            return this;
        }

        public Builder setErrorResourceId(int errorResourceId) {
            this.errorResourceId = errorResourceId;
            return this;
        }

        public Builder setThreadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }

        public Builder setmLruCache(MemoryCacheImp mLruCache) {
            this.mLruCache = mLruCache;
            return this;
        }


        public Bitmap.Config getImageConfig() {
            return imageConfig;
        }

        public Builder setImageConfig(Bitmap.Config imageConfig) {
            this.imageConfig = imageConfig;
            return this;
        }

        public Builder setOpenFileCache(boolean openFileCache) {
            this.openFileCache = openFileCache;
            return this;
        }

        public int getDefaultResourceId() {
            return defaultResourceId;
        }

        public int getErrorResourceId() {
            return errorResourceId;
        }

        public int getThreadCount() {
            return threadCount;
        }

        public MemoryCacheImp getmLruCache() {
            return mLruCache;
        }


        public void setOpenAnimation(boolean openAnimation) {
            this.openAnimation = openAnimation;
        }


        public boolean isOpenAnimation() {
            return openAnimation;
        }

        public boolean isOpenFileCache() {
            return openFileCache;
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
        public ImageLoaderConfig build() {
            return new ImageLoaderConfig(this);
        }
    }
}
