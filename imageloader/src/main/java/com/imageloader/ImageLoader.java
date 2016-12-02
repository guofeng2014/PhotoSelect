package com.imageloader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.imageloader.cache.memory.MemoryCache;
import com.imageloader.config.ImageLoaderConfig;
import com.imageloader.download.ImageDownLoader;
import com.imageloader.download.task.LocalTaskRunnable;
import com.imageloader.download.task.NetTaskRunnable;
import com.imageloader.info.ImageInfo;
import com.imageloader.sync.ImageLoaderSync;
import com.imageloader.util.AnimationUtils;
import com.imageloader.util.StringUtil;
import com.imageloader.view.BaseRenderingView;
import com.imageloader.view.ImageViewAware;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：guofeng
 * 日期:2016/11/28
 */

public class ImageLoader {

    private ImageLoader() {
    }

    private static ImageLoader imageLoader;

    public static synchronized ImageLoader getInstance() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader();
        }
        return imageLoader;
    }

    private ImageLoaderConfig loaderConfig;

    public void initConfig(ImageLoaderConfig loaderConfig) {
        this.loaderConfig = loaderConfig;
        if (loaderConfig == null) {
            throw new NullPointerException("ImageLoaderConfig can not be null");
        }
    }

    /**
     * 加载展示图片
     *
     * @param imageView 显示图片控件
     * @param uri       图片的路径
     */
    public void display(ImageView imageView, String uri) {
        if (loaderConfig == null) {
            throw new NullPointerException("ImageLoaderConfig can not be null");
        }
        if (imageView == null) {
            throw new NullPointerException("ImageView can not be null");
        }
        if (TextUtils.isEmpty(uri)) {
            throw new NullPointerException("Image Uri can not be null or empty");
        }
        //打包数据格式
        ImageViewAware imageAware = new ImageViewAware(imageView);
        //生成缓存key
        String cacheKey = StringUtil.generateCacheKey(uri, imageAware.getWidth(), imageAware.getHeight());
        //获得ImageView和cacheKey关联
        Map<Integer, String> associateImageAndUri = loaderConfig.getAssociateImageAndUri();
        associateImageAndUri.put(imageAware.getId(), cacheKey);
        //从缓存取数据
        MemoryCache memoryCache = loaderConfig.getmLruCache();
        Bitmap bitmap = memoryCache.get(cacheKey);
        //打包数据
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setBitmap(bitmap);
        imageInfo.setCacheKey(cacheKey);
        imageInfo.setImageView(imageAware);
        imageInfo.setPath(uri);
        imageInfo.setReentrantLock(ImageLoaderSync.get(uri));
        imageInfo.setHasAnimation(loaderConfig.isOpenAnimation());
        //缓存存在,刷新UI
        if (bitmap != null && !bitmap.isRecycled()) {
            Message message = Message.obtain();
            message.obj = imageInfo;
            uiHandler.sendMessage(message);
        } else {
            //缓存不存在,加载图片
            imageView.setImageResource(loaderConfig.getDefaultResourceId());//显示加载站位图
            chooseTask(imageInfo, memoryCache, associateImageAndUri,
                    imageAware.getWidth(), imageAware.getHeight()); //启动线程
        }
    }

    /**
     * 选择启动任务类型[网络/本地]
     */
    private void chooseTask(ImageInfo imageInfo,
                            MemoryCache memoryCache, Map<Integer, String> cacheKeyForImageAware,
                            int width, int height) {
        String uri = imageInfo.getPath();
        Runnable taskRunnable = null;
        //网络请求
        if (uri.startsWith(ImageDownLoader.HEAD_HTTP) || uri.startsWith(ImageDownLoader.HEAD_HTTPS)) {
            taskRunnable = new NetTaskRunnable(imageInfo, loaderConfig.getImageConfig(), memoryCache,
                    cacheKeyForImageAware, width, height,
                    uiHandler, loaderConfig.getFileCache());
        }
        //加载本地文件
        else if (uri.startsWith(ImageDownLoader.HEAD_FILE)) {
            imageInfo.setPath(ImageDownLoader.crop(uri));
            taskRunnable = new LocalTaskRunnable(imageInfo, loaderConfig.getImageConfig(),
                    memoryCache, cacheKeyForImageAware, width, height, uiHandler);
        }
        if (taskRunnable == null) return;
        ExecutorService threadPools = loaderConfig.getThreadPool();
        //重置线程池
        if (threadPools == null || threadPools.isShutdown()) {
            threadPools = Executors.newFixedThreadPool(loaderConfig.getThreadCount());
        }
        //添加任务到线程池
        threadPools.execute(taskRunnable);
    }


    /**
     * 统一刷新UI
     */
    private Handler uiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            ImageInfo imageInfo = (ImageInfo) message.obj;
            BaseRenderingView view = imageInfo.getImageView();
            if (view == null || view.isCollected()) return true;
            Bitmap b = imageInfo.getBitmap();
            Map<Integer, String> associateImageAndUri = loaderConfig.getAssociateImageAndUri();
            String imageCacheKey = associateImageAndUri.get(view.getId());
            String pathCacheKey = imageInfo.getCacheKey();
            boolean hasAnimation = imageInfo.isHasAnimation();
            if (TextUtils.equals(imageCacheKey, pathCacheKey)) {
                //文件加载失败
                if (b == null) {
                    view.setImageResource(loaderConfig.getErrorResourceId());
                    return true;
                }
                //文件正常
                view.setImageBitmap(b);
                //显示加载动画
                if (hasAnimation) {
                    View imageView = view.getImageView();
                    if (imageView != null) {
                        AnimationUtils.alphaAnimation(imageView);
                    }
                }
            }
            return false;
        }
    });

    /**
     * 是否缓存
     */
    public void release() {
        imageLoader = null;
        //清空缓存
        loaderConfig.getmLruCache().clear();
        //清空路径同步锁
        ImageLoaderSync.clear();
    }
}

