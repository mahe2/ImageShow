package com.annwyn.image.show.presenter.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.annwyn.image.show.connector.SplashConnector;
import com.annwyn.image.show.presenter.SplashPresenter;
import com.annwyn.image.show.task.SplashTask;
import com.annwyn.image.show.ui.BaseActivity;

public class SplashPresenterImpl implements SplashPresenter {

    private SplashConnector connector;

    private Context context;

    public SplashPresenterImpl(Context context, SplashConnector connector) {
        this.connector = connector;
        this.context = context;
    }

    @Override
    public void startLoading(SharedPreferences sharedPreferences) {
        long lastUpdate = sharedPreferences.getLong(BaseActivity.SHARED_LAST_UPDATE, 0L);
        if(System.currentTimeMillis() - lastUpdate > 24 * 3600 * 1000) { // 需要重新更新数据
            SplashTask task = new SplashTask(this.context);
            task.setListener(this.splashTaskListener);
            task.execute();
        } else {
            connector.loadComplete(false);
        }
    }

    private SplashTask.SplashTaskListener splashTaskListener = new SplashTask.SplashTaskListener() {
        @Override
        public void startupEnd(boolean hasRefresh) {
            connector.loadComplete(hasRefresh);
        }
    };
}
