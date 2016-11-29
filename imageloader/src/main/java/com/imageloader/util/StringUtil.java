package com.imageloader.util;


import android.os.Environment;

import com.imageloader.download.ImageDownLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 作者：guofeng
 * ＊ 日期:2016/11/28
 */

public class StringUtil {
    /**
     * 根据文件的路径生成缓存的key
     *
     * @param uri 文件的路径
     * @return 缓存文件key
     */
    public static String generateCacheKey(String uri, int width, int height) {
        //返回本地图片缓存格式
        if (uri.startsWith(ImageDownLoader.HEAD_FILE)) {
            return generateLocalPhotoCacheKey(uri, width, height);
        }
        //返回网络图片缓存格式
        else {
            return generateNetPhotoCacheKey(uri, width, height);
        }
    }

    public static String generateLocalPhotoCacheKey(String uri, int width, int height) {
        //本地文件去掉(file://)头部数据
        uri = ImageDownLoader.crop(uri);
        return encode(uri) + "-" + width + "X" + height;
    }

    public static String generateNetPhotoCacheKey(String uri, int width, int height) {
        int position = uri.lastIndexOf("/");
        return encode(uri.substring(position)) + "-" + width + "X" + height;
    }

    public static String generateNetPhotoCacheKey(String uri) {
        int position = uri.lastIndexOf("/");
        return encode(uri.substring(position));
    }


    /**
     * 保存到缓存前需要encode
     *
     * @param value
     * @return
     */
    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获得decode数据
     *
     * @param encodeValue
     * @return
     */
    public static String decode(String encodeValue) {
        try {
            return URLEncoder.encode(encodeValue, "UTF-8").toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeValue;
    }

    /**
     * 是否挂载SD卡
     *
     * @return
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
