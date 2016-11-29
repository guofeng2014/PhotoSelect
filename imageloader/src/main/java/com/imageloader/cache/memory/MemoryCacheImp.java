package com.imageloader.cache.memory;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.LruCache;


public class MemoryCacheImp implements MemoryCache<String,Bitmap> {

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache;


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    public MemoryCacheImp() {
        // 获取应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //每行占用空间数X行数
                return value.getByteCount();
            }

        };
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public void add(String path, Bitmap bitmap) {
        if (bitmap == null) return;
        if (get(path) == null) {
            mLruCache.put(path, bitmap);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public Bitmap get(String path) {
        return mLruCache.get(path);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public void clear() {
        mLruCache.evictAll();
    }
}
