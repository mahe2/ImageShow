package com.annwyn.image.show.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;

import com.annwyn.image.show.R;

import java.io.Closeable;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public final class ParamUtils {

    private static final char[] chars = new char[]{ //
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', //
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', //
            'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', //
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', //
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public enum DateFormat {
        yMd("yyyy-MM-dd"), yMdHms("yyyy-MM-dd HH:mm:ss"), Hm("HH:mm");

        private String format;

        DateFormat(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String uuid() {
        StringBuilder uuid = new StringBuilder();
        String tmp = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            uuid.append(chars[Integer.parseInt(tmp.substring(i * 4, (i + 1) * 4), 16) % 0x3E]);
        }
        return uuid.toString();
    }


    public static Date parseDate(String date, DateFormat format) {
        Date result = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.getFormat(), Locale.CHINA);
            result = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String parseDate(long date, DateFormat format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.getFormat(), Locale.CHINA);
        return simpleDateFormat.format(new Date(date));
    }

    public static String parseDate(Date date, DateFormat format) {
        if (date != null && format != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.getFormat(), Locale.CHINA);
            return simpleDateFormat.format(date);
        } else {
            return "";
        }
    }

    public static int findMax(int... array) {
        if (array == null || array.length == 0) {
            return 0;
        } else if (array.length == 1) {
            return array[0];
        } else {
            int max = array[0];
            for (int i = array.length - 1; i > 0; i--) {
                max = Math.max(max, array[i]);
            }
            return max;
        }
    }

    public static String getCursorString(Cursor cursor, String key) {
        int index = cursor.getColumnIndex(key);
        return index == -1 ? null : cursor.getString(index);
    }

    public static int getCursorInt(Cursor cursor, String key) {
        int index = cursor.getColumnIndex(key);
        return index == -1 ? 0 : cursor.getInt(index);
    }

    public static void setProgressOffset(Context context, SwipeRefreshLayout layout) {
        layout.setProgressViewOffset(false, 0, context.getResources().getDimensionPixelOffset(R.dimen.progress_offset));
    }

    public static AlertDialog showNetworkDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        return showNetworkDialog(context, R.string.positive, onClickListener);
    }

    public static AlertDialog showNetworkDialog(Context context, int positiveMsg, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context)
                .setMessage(R.string.network_error_msg)
                .setPositiveButton(positiveMsg, onClickListener)
                .setCancelable(false)
                .create();
    }

}
