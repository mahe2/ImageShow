package com.annwyn.image.show.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.annwyn.image.show.R;
import com.annwyn.image.show.dao.DashboardDao;
import com.annwyn.image.show.model.Dashboard;
import com.annwyn.image.show.utils.HttpUtils;
import com.annwyn.image.show.utils.JSONUtils;
import com.annwyn.image.show.utils.ParamUtils;
import com.annwyn.image.show.utils.WebAPI;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 初始化时更新分类表
 * Created by Administrator on 2016/7/18.
 */
public class SplashTask extends AsyncTask<Boolean, Void, Boolean> {

    public interface SplashTaskListener {
        void startupEnd(boolean hasRefresh);
    }

    private Context context;

    private DashboardDao dashboardDao;

    private SplashTaskListener listener;

    public SplashTask(Context context) {
        this.context = context;
        this.dashboardDao = new DashboardDao(this.context);
    }

    public void setListener(SplashTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Boolean... params) {
        boolean isConnect = HttpUtils.getInstance().checkConnect(this.context);
        if(!isConnect && !this.dashboardDao.chkEmpty()) { // 网络连接不可用,数据表有数据,不需要更新数据
            return false;
        }

        List<Dashboard> dashboards = this.loadDashboards(isConnect);
        return this.save(dashboards);
    }

    private List<Dashboard> loadDashboards(boolean isConnect) {
        try {
            if(isConnect) { // 网络连接可用
                return this.readNetWork(); // 从网络中读取数据
            } else { // 网络连接不可用,数据表没数据
                return this.readLocal();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean save(List<Dashboard> dashboards) {
        if(dashboards == null || dashboards.isEmpty())
            return false;
        this.dashboardDao.clear();
        this.dashboardDao.save(dashboards);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean param) {
        if(this.listener != null) {
            this.listener.startupEnd(param);
        }
    }

    /**
     * 从本地文件中获取json
     */
    private List<Dashboard> readLocal() throws IOException, JSONException {
        BufferedInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.dashboard));
            outputStream = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            String json = outputStream.toString("UTF-8");
            return JSONUtils.getInstance().parseDashboardList(new JSONArray(json));
        } finally {
            ParamUtils.close(inputStream);
            ParamUtils.close(outputStream);
        }
    }

    /**
     * 从网络中获取json
     */
    private List<Dashboard> readNetWork() throws IOException, JSONException {
        ResponseBody body = null;
        try {
            Response response = HttpUtils.getInstance().execute(WebAPI.getDashboardList());
            body = response.body();
            String json = body.string();
            return JSONUtils.getInstance().parseDashboardList(new JSONArray(json));
        } finally {
            ParamUtils.close(body);
        }
    }

}
