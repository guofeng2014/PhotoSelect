package com.imageloader.download.stream;

import com.imageloader.error.TaskCancelException;
import com.imageloader.info.ImageInfo;
import com.imageloader.view.BaseRenderingView;

import java.io.InputStream;
import java.util.Map;

/**
 * 作者：guofeng
 * 日期:2016/11/28
 * 压缩图片获得图片输入流
 */
public abstract class DecodeAndStream {

    int BUFFER_SIZE = 32 * 1024; // 32 Kb

    /**
     * 返回Sd卡或网络文件流
     */
    abstract InputStream getFileInputStream();

    /**
     * 计算图片压缩比例
     *
     * @param srcWidth
     * @param srcHeight
     * @param width
     * @param height
     * @return
     */
    public int calculateInSampleSize(int srcWidth, int srcHeight, int width, int height) {
        int inSampleSize = 1;
        if (srcWidth > width || srcHeight > height) {
            int widthRatio = Math.round(srcWidth * 1.0f / width);
            int heightRatio = Math.round(srcHeight * 1.0f / height);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    /**
     * 检查图片是否被回收
     *
     * @param imageInfo
     * @param cacheKeyForImageAware
     * @throws TaskCancelException
     */
    public void checkViewCollectedAndReLoading(ImageInfo imageInfo, Map<Integer, String> cacheKeyForImageAware) throws TaskCancelException {
        if (imageInfo.getImageView().isCollected()) {
            throw new TaskCancelException();
        }
        BaseRenderingView view = imageInfo.getImageView();
        if (!view.isCollected()) {
            String cacheKey = cacheKeyForImageAware.get(view.getId());
            if (!imageInfo.getCacheKey().equals(cacheKey)) {
                throw new TaskCancelException();
            }
        }
    }


}
