package com.annwyn.image.show.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.annwyn.image.show.model.Dashboard;
import com.annwyn.image.show.utils.ParamUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DashboardDao
 * Created by annwyn on 2016/7/16.
 */
public class DashboardDao extends BaseDao {

    public DashboardDao(Context context) {
        super(context);
    }

    public void save(List<Dashboard> dashboards) {
        SQLiteDatabase sqLiteDatabase = null;
        String sql = "insert into dashboard(id, name, link, icon) values (?, ?, ?, ?)";
        try {
            sqLiteDatabase = this.sqLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            for (Dashboard dashboard : dashboards) {
                sqLiteDatabase.execSQL(sql, new Object[]{
                        dashboard.getId(), dashboard.getName(), dashboard.getLink(), dashboard.getIcon()
                });
            }

            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            this.closeTransaction(sqLiteDatabase);
        }
    }

    public boolean chkEmpty() {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        String sql = "select count(*) as c from dashboard";
        try {
            sqLiteDatabase = this.sqLiteOpenHelper.getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery(sql, null);

            return cursor.moveToFirst() && ParamUtils.getCursorInt(cursor, "c") == 0;
        } finally {
            ParamUtils.close(cursor);
            ParamUtils.close(sqLiteDatabase);
        }
    }

    public void clear() {
        SQLiteDatabase sqLiteDatabase = null;
        String sql = "delete from dashboard";
        try {
            sqLiteDatabase = this.sqLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL(sql);

            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            this.closeTransaction(sqLiteDatabase);
        }
    }

    public List<Dashboard> selectAll() {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        String sql = "select * from dashboard";
        try {
            List<Dashboard> dashboards = new ArrayList<>();
            sqLiteDatabase = this.sqLiteOpenHelper.getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                dashboards.add(this.parseDashboard(cursor));
            }

            return dashboards;
        } finally {
            ParamUtils.close(cursor);
            ParamUtils.close(sqLiteDatabase);
        }
    }

    private Dashboard parseDashboard(Cursor cursor) {
        Dashboard dashboard = new Dashboard();

        dashboard.setId(ParamUtils.getCursorInt(cursor, "id"));
        dashboard.setName(ParamUtils.getCursorString(cursor, "name"));
        dashboard.setIcon(ParamUtils.getCursorString(cursor, "icon"));
        dashboard.setLink(ParamUtils.getCursorString(cursor, "link"));
        return dashboard;
    }

}
