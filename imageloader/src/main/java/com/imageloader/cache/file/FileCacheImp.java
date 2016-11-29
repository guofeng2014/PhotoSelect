package com.imageloader.cache.file;

import android.os.Environment;

import com.imageloader.error.NotMountedSDCardException;
import com.imageloader.util.FileUtil;
import com.imageloader.util.StringUtil;

import java.io.InputStream;

/**
 * 作者：guofeng
 * ＊ 日期:2016/11/28
 */
public class FileCacheImp implements FileCache {

    public static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImgCache/";

    @Override
    public void add(InputStream inputStream, String savePath) throws NotMountedSDCardException {
        if (!StringUtil.hasSdcard()) throw new NotMountedSDCardException();
        FileUtil.saveToSdCard(inputStream, savePath);
    }

    @Override
    public boolean contain(String uri) throws NotMountedSDCardException {
        if (!StringUtil.hasSdcard()) throw new NotMountedSDCardException();
        return FileUtil.contains(uri, CACHE_PATH);
    }

    @Override
    public void clear() {
        FileUtil.clearFonder(CACHE_PATH);
    }
}
