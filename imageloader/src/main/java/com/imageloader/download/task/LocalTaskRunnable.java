package com.imageloader.download.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.imageloader.cache.memory.MemoryCache;
import com.imageloader.download.stream.LocalFileStream;
import com.imageloader.error.TaskCancelException;
import com.imageloader.info.ImageInfo;
import com.imageloader.sync.ImageLoaderSync;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 作者：guofeng
 * ＊ 日期:2016/11/29
 */

public class LocalTaskRunnable implements Runnable {
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
    private MemoryCache<String, Bitmap> memoryCache;
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
    private LocalFileStream localFileStream;


    public LocalTaskRunnable(ImageInfo imageInfo, Bitmap.Config bitmapConfig,
                             MemoryCache<String,Bitmap> memoryCache, Map<Integer, String> cacheKeyForImageAware,
                             int width, int height, Handler uIHandler) {
        this.imageInfo = imageInfo;
        this.bitmapConfig = bitmapConfig;
        this.memoryCache = memoryCache;
        this.cacheKeyForImageAware = cacheKeyForImageAware;
        this.width = width;
        this.height = height;
        this.uIHandler = uIHandler;
        localFileStream = new LocalFileStream(imageInfo.getPath());
    }

    @Override
    public void run() {
        try {
            //检查是否被回收或重复加载
            localFileStream.checkViewCollectedAndReLoading(imageInfo, cacheKeyForImageAware);
        } catch (TaskCancelException e) {
            e.printStackTrace();
            return;
        }
        ReentrantLock reentrantLock = null;
        try {
            //其他线程已经在下载
            if (ImageLoaderSync.isContain(imageInfo.getPath())) return;
            //获得该路径的同步锁对象
            reentrantLock = imageInfo.getReentrantLock();
            //给路径加锁
            reentrantLock.lock();
            //从缓存获取
            Bitmap bitmap = memoryCache.get(imageInfo.getCacheKey());
            //缓存不存在,[压缩图片][保存到缓存][刷新UI]
            if (bitmap == null) {
                //压缩图片
                bitmap = decodeBitmap(imageInfo.getPath());
                //保存到缓存
                memoryCache.add(imageInfo.getCacheKey(), bitmap);
            }
            //刷新ImageInfo的bitmap对象
            imageInfo.setBitmap(bitmap);
        } finally {
            //释放锁
            if (reentrantLock != null) {
                reentrantLock.unlock();
            }
            //刷新UI
            refreshUi();
        }
    }


    private Bitmap decodeBitmap(String localPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 获取图片的宽高但是不把图片加载到内存中
        options.inJustDecodeBounds = true;
        //读取文件信息
        BitmapFactory.decodeFile(localPath, options);
        //原图的宽度
        int srcWidth = options.outWidth;
        //原图的高度
        int srcHeight = options.outHeight;
        //开始加载图片内容
        options.inJustDecodeBounds = false;
        //压缩比例
        options.inSampleSize = localFileStream.calculateInSampleSize(srcWidth, srcHeight, width, height);
        //图片质量
        options.inPreferredConfig = bitmapConfig;
        //返回压缩后的bitmap对象
        return BitmapFactory.decodeFile(localPath, options);
    }

    private void refreshUi() {
        Message message = uIHandler.obtainMessage();
        message.obj = imageInfo;
        message.sendToTarget();
    }
}
