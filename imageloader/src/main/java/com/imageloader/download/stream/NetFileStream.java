package com.imageloader.download.stream;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 作者：guofeng
 * 日期:2016/11/28
 */

public class NetFileStream extends DecodeAndStream {
    /**
     * {@value}
     */
    private static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds
    /**
     * {@value}
     */
    private static final int DEFAULT_HTTP_READ_TIMEOUT = 20 * 1000; // milliseconds

    private String url;

    public NetFileStream(String url) {
        this.url = url;
    }

    @Override
    public InputStream getFileInputStream() throws IOException {
        HttpURLConnection conn = createConnection(url);
        return conn.getInputStream();
    }

    protected HttpURLConnection createConnection(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(DEFAULT_HTTP_CONNECT_TIMEOUT);
        conn.setReadTimeout(DEFAULT_HTTP_READ_TIMEOUT);
        return conn;
    }
}
