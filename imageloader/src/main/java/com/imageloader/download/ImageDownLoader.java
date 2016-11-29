package com.imageloader.download;

/**
 * 作者：guofeng
 * ＊ 日期:2016/11/28
 */

public class ImageDownLoader {

    public static final String HEAD_HTTP = "http://";
    public static final String HEAD_HTTPS = "https://";
    public static final String HEAD_FILE = "file://";

    /**
     * Removed scheme part ("scheme://") from incoming URI
     */
    public static String crop(String uri) {
        if (uri.startsWith(HEAD_FILE)) {
            return uri.substring(HEAD_HTTP.length());
        }
        return uri;
    }
}
