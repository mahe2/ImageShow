package com.annwyn.image.show.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * Created by annwyn on 2016/7/16.
 */
public class BaseDao implements Closeable {

    protected SQLiteOpenHelper sqLiteOpenHelper;

    public BaseDao(Context context) {
        this.sqLiteOpenHelper = new DatabaseHelper(context);
    }

    @Override
    public void close() throws IOException {
        this.sqLiteOpenHelper.close();
    }

    public void closeTransaction(SQLiteDatabase sqLiteDatabase) {
        if(sqLiteDatabase != null) {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
    }
}
