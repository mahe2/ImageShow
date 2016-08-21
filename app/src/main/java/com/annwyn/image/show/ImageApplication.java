package com.annwyn.image.show;

import android.app.Application;
import android.util.Log;

import com.annwyn.image.show.utils.DisplayUtils;
import com.annwyn.image.show.utils.HttpUtils;
import com.annwyn.image.show.utils.ImageLoaderUtils;

/**
 *
 * Created by annwyn on 2016/7/16.
 */
public class ImageApplication extends Application {

    private static ImageApplication instance;

    public static String width;

    public static String height;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        width = String.valueOf(DisplayUtils.getScreenWidth(this));
        height = String.valueOf(DisplayUtils.getScreenHeight(this));

        HttpUtils.getInstance().initialize(this);
        ImageLoaderUtils.getInstance().initialize(this);
    }

    public static ImageApplication getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.exit(0);
    }
}

