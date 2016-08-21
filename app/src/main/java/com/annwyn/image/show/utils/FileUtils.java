package com.annwyn.image.show.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 文件相关的工具类
 * Created by Administrator on 2016/7/18.
 */
public final class FileUtils {

    private static final String HTTP_CACHE_DIR = "http";

    private static final String IMAGE_CACHE_DIR = "image";

    public static boolean isSDCardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getHttpCacheDir(Context context) {
        File cacheDir = isSDCardExists() ?
                new File(context.getExternalCacheDir(), HTTP_CACHE_DIR) :
                new File(context.getCacheDir(), HTTP_CACHE_DIR);

        if(!cacheDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public static File getImageCacheDir(Context context) {
        File cacheDir = isSDCardExists() ?
                new File(context.getExternalCacheDir(), IMAGE_CACHE_DIR) :
                new File(context.getCacheDir(), IMAGE_CACHE_DIR);

        if(!cacheDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

}
