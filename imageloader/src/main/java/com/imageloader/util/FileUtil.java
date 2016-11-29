package com.imageloader.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.imageloader.cache.file.FileCacheImp.CACHE_PATH;

/**
 * 作者：guofeng
 * 日期:2016/11/28
 */

public class FileUtil {
    /**
     * 创建文件夹
     *
     * @param fonderPath
     */
    public static void createFonder(File fonderPath) {
        if (!fonderPath.exists()) {
            fonderPath.mkdirs();
        }
    }


    /**
     * 清空文件夹
     *
     * @param fonderPath
     */
    public static void clearFonder(String fonderPath) {
        File file = new File(fonderPath);
        if (file.exists()) {
            File files[] = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isFile()) {
                        f.delete();
                    } else {
                        clearFonder(f.toString());
                    }
                }
            }
        }
    }

    /**
     * 检查文件夹是否包涵该文件
     *
     * @param fileName
     * @param fonderPath
     * @return
     */
    public static boolean contains(String fileName, String fonderPath) {
        File file = new File(fonderPath);
        File files[] = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f == null) continue;
                if (fileName.equals(f.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 保存文件到Sd卡
     *
     * @param inputStream
     * @param savePath
     */
    public static void saveToSdCard(InputStream inputStream, String savePath) {
        //创建根目录文件夹
        FileUtil.createFonder(new File(CACHE_PATH));
        FileOutputStream outputStream = null;
        try {
            File file = new File(savePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
