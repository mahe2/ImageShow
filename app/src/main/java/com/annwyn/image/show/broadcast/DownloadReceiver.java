package com.annwyn.image.show.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.annwyn.image.show.R;

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            Toast.makeText(context, R.string.download_msg, Toast.LENGTH_LONG).show();
        }
    }
}