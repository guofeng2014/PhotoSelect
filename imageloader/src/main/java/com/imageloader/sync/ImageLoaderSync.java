package com.imageloader.sync;

import android.text.TextUtils;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 作者：guofeng
 * 日期:2016/11/30
 * 给每个加载的路径添加一个同步锁,防止在ListView/GridView 等控件中重复加载
 */

public class ImageLoaderSync {

    private static Map<String, ReentrantLock> lockMap = new WeakHashMap<>();

    /**
     * 获得该路径对应的同步锁,有则返回,没有则创建
     *
     * @param path
     * @return 返回同步锁
     */
    public static ReentrantLock get(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new NullPointerException("path can not be null or empty");
        }
        ReentrantLock reentrantLock = lockMap.get(path);
        if (reentrantLock == null) {
            reentrantLock = new ReentrantLock();
            lockMap.put(path, reentrantLock);
        }
        return reentrantLock;
    }

    /**
     * 该路径是否已经创建同步锁
     *
     * @param path 文件的路径
     * @return true 则存在,false不存在
     */
    public static boolean isContain(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new NullPointerException("path can not be null or empty");
        }
        return lockMap.get(path) != null;
    }

    /**
     * 退出清空同步锁数据
     */
    public static void clear() {
        lockMap.clear();
    }
}
