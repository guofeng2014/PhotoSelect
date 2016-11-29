package com.imageloader.download.stream;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.imageloader.download.ImageDownLoader;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 作者：guofeng
 * 日期:2016/11/28
 */

public class LocalFileStream extends DecodeAndStream {

    private String filePath;


    public LocalFileStream(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public InputStream getFileInputStream() {
        String path = ImageDownLoader.crop(filePath);
        if (isVideoFileUri()) {
            return getVideoThumbnailStream(path);
        } else {
            BufferedInputStream imageStream = null;
            try {
                imageStream = new BufferedInputStream(new FileInputStream(path), BUFFER_SIZE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return imageStream;
        }
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private InputStream getVideoThumbnailStream(String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            Bitmap bitmap = ThumbnailUtils
                    .createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            if (bitmap != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                return new ByteArrayInputStream(bos.toByteArray());
            }
        }
        return null;
    }

    private boolean isVideoFileUri() {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return mimeType != null && mimeType.startsWith("video/");
    }
}
