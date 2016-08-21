package com.annwyn.image.show.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http请求工具类,使用okHttp实现
 * Created by Administrator on 2016/7/18.
 */
public final class HttpUtils {

    private static HttpUtils instance;

    private OkHttpClient client;

    private boolean isConnect = false;

    private Handler handler;

    private HttpUtils() {}

    public static HttpUtils getInstance() {
        if (instance == null)
            instance = new HttpUtils();
        return instance;
    }

    public void initialize(Context context) {
        long maxSize = 10 * 1024 * 1024;
        Cache httpCache = new Cache(FileUtils.getHttpCacheDir(context), maxSize);
        this.client = new OkHttpClient.Builder()
                .cache(httpCache)
                .readTimeout(3, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        this.handler = new Handler(Looper.getMainLooper());
        this.checkConnect(context);
    }

    public boolean checkConnect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        this.isConnect = networkInfo != null && networkInfo.isAvailable();
        return this.isConnect;
    }

    public boolean isConnect() {
        return this.isConnect;
    }

    public Response execute(Request request) throws IOException {
        Call call = this.client.newCall(request);
        return call.execute();
    }

    public Response execute(String url) throws IOException {
        return this.execute(this.generatorRequest(url));
    }

    public Request generatorRequest(String url) {
        return new Request.Builder()
                .get()
                .url(url)
                .build();
    }

    public Call executeAsync(String url, SimpleCallback<?> callback) {
        Request request = this.generatorRequest(url);
        return this.executeAsync(request, callback);
    }

    public Call executeAsync(Request request, SimpleCallback<?> callback) {
        Call call = this.client.newCall(request);
        call.enqueue(new AsyncCallback<>(callback));
        return call;
    }

    /**
     * 该回调在okHttp的工作线程中执行,执行完成或会调用handler.post(runnable)回到ui线程中刷新ui
     */
    private class AsyncCallback<T> implements Callback {

        private SimpleCallback<T> callback;

        private AsyncCallback(SimpleCallback<T> callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(final Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(call, e); // UI线程中
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(call.isCanceled()) {
                this.onFailure(call, new CancelRequestException("request is canceled"));
                return;
            }
            if(!response.isSuccessful()) {
                this.onFailure(call, new IOException("request is fail"));
                return;
            }
            try {
                this.postSuccessResponse(call, response);
            } finally {
                ParamUtils.close(response.body());
            }
        }

        private void postSuccessResponse(final Call call, Response response) throws IOException {
            final T t = callback.parseNetworkResponse(call, response);
            if(t == null)
                this.onFailure(call, new IOException("parse object error"));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onResult(call, t); // UI线程中
                }
            });
        }
    }

    /**
     * 运行在ui线程中的回调
     * @param <T>
     */
    public abstract static class SimpleCallback<T> {
        /**
         * 在ui线程中回调
         * @param call Call
         * @param e Exception
         */
        public void onFailure(Call call, Exception e){}

        /**
         * 在okHttp线程中回调
         * @param call Call
         * @param response Response
         * @return T
         * @throws IOException
         */
        public abstract T parseNetworkResponse(Call call, Response response) throws IOException;

        /**
         * 在ui线程中回调
         * @param call Call
         * @param response T
         */
        public abstract void onResult(Call call, T response);
    }

    public abstract static class JSONCallback extends SimpleCallback<JSONObject> {

        @Override
        public JSONObject parseNetworkResponse(Call call, Response response) throws IOException {
            try {
                return new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static class CancelRequestException extends IOException {

        private static final long serialVersionUID = 5840889527670301926L;

        public CancelRequestException(String detailMessage) {
            super(detailMessage);
        }

    }

}
