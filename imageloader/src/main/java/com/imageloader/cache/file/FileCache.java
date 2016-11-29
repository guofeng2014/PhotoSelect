package com.imageloader.cache.file;

import com.imageloader.error.NotMountedSDCardException;

import java.io.InputStream;

/**
 * 作者：guofeng
 * ＊ 日期:2016/11/29
 */

public interface FileCache {
    /**
     * 添加到文件缓存目录
     *
     * @param inputStream
     * @param savePath
     */
    void add(InputStream inputStream, String savePath) throws NotMountedSDCardException;

    /**
     * 是否存在与文件缓存
     *
     * @param uri
     * @return
     */
    boolean contain(String uri) throws NotMountedSDCardException;

    /**
     * 清空文件缓存
     */
    void clear();
}
