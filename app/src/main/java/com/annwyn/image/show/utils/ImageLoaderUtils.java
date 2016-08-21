package com.annwyn.image.show.utils;

import android.content.Context;
import android.widget.ImageView;

import com.annwyn.image.show.R;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;

/**
 * 图片加载工具类,使用universal image loader实现
 * Created by Administrator on 2016/7/18.
 */
public final class ImageLoaderUtils {

//    private Context context;

    private static ImageLoaderUtils instance;

    public static ImageLoaderUtils getInstance() {
        if (instance == null)
            instance = new ImageLoaderUtils();
        return instance;
    }

    private ImageLoaderUtils() {
    }

//    public void initialize(Context context) {
//        this.context = context;
//    }
//
//    public void displayImage(String url, ImageView imageView) {
//        Glide.with(this.context)
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(imageView);
//    }

    public void initialize(Context context) {
        try {
            long diskCacheSize = 20 * 1024 * 1024;
            DiskCache imageCache = new LruDiskCache(FileUtils.getImageCacheDir(context), new Md5FileNameGenerator(), diskCacheSize);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageOnLoading(R.mipmap.loading_bg)
                    .build();
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                    .diskCache(imageCache)
                    .defaultDisplayImageOptions(options)
                    .build();
            ImageLoader.getInstance().init(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayImage(String url, ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView);
    }

}
