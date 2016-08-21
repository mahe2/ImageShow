package com.annwyn.image.show.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * Created by annwyn on 2016/7/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String db_name = "annwyn image show";

    private static final int version = 1;

    public DatabaseHelper(Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder builder = new StringBuilder();
        builder.append("create table dashboard(");
        builder.append("    id int primary key, ");
        builder.append("    name varchar(40), ");
        builder.append("    link varchar(1024), ");
        builder.append("    icon varchar(1024));");
        sqLiteDatabase.execSQL(builder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
