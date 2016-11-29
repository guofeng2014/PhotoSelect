package com.imageloader.download.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.imageloader.cache.file.FileCache;
import com.imageloader.cache.file.FileCacheImp;
import com.imageloader.cache.memory.MemoryCache;
import com.imageloader.download.stream.NetFileStream;
import com.imageloader.error.NotMountedSDCardException;
import com.imageloader.error.TaskCancelException;
import com.imageloader.info.ImageInfo;
import com.imageloader.util.FileUtil;
import com.imageloader.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static android.graphics.BitmapFactory.decodeStream;

/**
 * 作者：guofeng
 * ＊ 日期:2016/11/29
 */

public class NetTaskRunnable implements Runnable {
    /**
     * 打包数据
     */
    private ImageInfo imageInfo;
    /**
     * 图片配置信息
     */
    private Bitmap.Config bitmapConfig;
    /**
     * 缓存
     */
    private MemoryCache memoryCache;
    /**
     * 绑定 ImageView 和 cacheKey
     */
    private Map<Integer, String> cacheKeyForImageAware;
    /**
     * 图片宽度
     */
    private int width;
    /**
     * 图片高度
     */
    private int height;
    /**
     * 刷新UIHandler
     */
    private Handler uIHandler;
    /**
     * 读取本地文件流
     */
    private NetFileStream netFileStream;
    /**
     * 文件缓存
     */
    private FileCache fileCache;

    public NetTaskRunnable(ImageInfo imageInfo, Bitmap.Config bitmapConfig,
                           MemoryCache memoryCache, Map<Integer, String> cacheKeyForImageAware,
                           int width, int height, Handler uIHandler, FileCache fileCache) {
        this.imageInfo = imageInfo;
        this.bitmapConfig = bitmapConfig;
        this.memoryCache = memoryCache;
        this.cacheKeyForImageAware = cacheKeyForImageAware;
        this.width = width;
        this.height = height;
        this.uIHandler = uIHandler;
        this.netFileStream = new NetFileStream(imageInfo.getPath());
        this.fileCache = fileCache;
    }

    @Override
    public void run() {
        try {
            //检查是否被回收或重复加载
            netFileStream.checkViewCollectedAndReLoading(imageInfo, cacheKeyForImageAware);
        } catch (TaskCancelException e) {
            e.printStackTrace();
            return;
        }
        //从缓存获取
        Bitmap bitmap = memoryCache.get(imageInfo.getCacheKey());
        //缓存不存在,从文件获取
        if (bitmap == null) {
            //文件缓存没有开启，网络加载
            if (fileCache == null) {
                loadPhotoFromNet();
            }
            //开启文件缓存
            else {
                try {
                    String fileNameEncode = StringUtil.generateNetPhotoCacheKey(imageInfo.getPath());
                    String savePath = FileCacheImp.CACHE_PATH + fileNameEncode;
                    //从本地缓存是否存在
                    boolean contain = fileCache.contain(fileNameEncode);
                    //本地缓存不存在,下载图片到本地
                    if (!contain) {
                        InputStream inputStream = netFileStream.getFileInputStream();
                        FileUtil.saveToSdCard(inputStream, savePath);
                    }
                    //修改URL为本地缓存路径
                    imageInfo.setPath(savePath);
                    //从本地读取图片
                    new LocalTaskRunnable(imageInfo, bitmapConfig,
                            memoryCache, cacheKeyForImageAware, width, height, uIHandler).run();
                } catch (NotMountedSDCardException e) {
                    e.printStackTrace();
                    //没有挂载SD卡和网络处理一样
                    loadPhotoFromNet();
                }
            }
        }
        //缓存存在,刷新Ui
        else {
            refreshUI();
        }
    }

    /**
     * 从网络加载图片流程
     */
    private void loadPhotoFromNet() {
        InputStream inputStream = netFileStream.getFileInputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeStream(inputStream, null, options);
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        try {
            inputStream.reset();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        options.inSampleSize = netFileStream.calculateInSampleSize(srcWidth, srcHeight, width, height);
        options.inJustDecodeBounds = false;
        //重新获得输入流
        inputStream = netFileStream.getFileInputStream();
        Bitmap decodeBitmap = BitmapFactory.decodeStream(inputStream, null, options);
        try {
            inputStream.reset();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //保存到缓存
        memoryCache.add(imageInfo.getCacheKey(), decodeBitmap);
        //同步数据到ImageInfo
        imageInfo.setBitmap(decodeBitmap);
        //刷新UI
        refreshUI();
    }

    /**
     * 回调刷新UI
     */
    private void refreshUI() {
        Message message = uIHandler.obtainMessage();
        message.obj = imageInfo;
        message.sendToTarget();
    }
}
