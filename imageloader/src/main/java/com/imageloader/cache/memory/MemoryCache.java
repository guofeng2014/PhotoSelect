package com.imageloader.cache.memory;

import android.graphics.Bitmap;

/**
 * 作者：guofeng
 * 日期:16/6/27
 */
public interface MemoryCache<K, V> {
    /**
     * 添加到缓存
     *
     * @param k 缓存的key
     * @param v 缓存的value对象
     */
    void add(K k, V v);

    /**
     * 从缓存获取数据
     *
     * @param k 缓存的key
     * @return 返回缓存的value对象
     */
    Bitmap get(K k);

    /**
     * 清空缓存
     */
    void clear();
}
