package com.annwyn.image.show.presenter.impl;

import android.content.Context;

import com.annwyn.image.show.connector.DetailConnector;
import com.annwyn.image.show.presenter.DetailPresenter;
import com.annwyn.image.show.utils.DownloadUtils;

public class DetailPresenterImpl implements DetailPresenter{

    private Context context;

    @SuppressWarnings("UnusedParameters")
    public DetailPresenterImpl(Context context, DetailConnector connector) {
        this.context = context;
    }

    @Override
    public void downloadImage(int id, String url) {
        DownloadUtils downloadUtils = new DownloadUtils(this.context);
        downloadUtils.download(url, String.valueOf(id));
    }

}

