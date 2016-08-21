package com.annwyn.image.show.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 *
 * Created by Administrator on 2016/7/21.
 */
public class DownloadUtils {

    private DownloadManager manager;

    public DownloadUtils(Context context) {
        this.manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public long download(String url, String title) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, title + ".webp");
        request.setTitle(title);
        return manager.enqueue(request);
    }

}
