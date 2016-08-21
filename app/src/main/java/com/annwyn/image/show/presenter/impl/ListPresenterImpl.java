package com.annwyn.image.show.presenter.impl;

import android.content.Context;
import android.widget.Toast;

import com.annwyn.image.show.R;
import com.annwyn.image.show.connector.ListConnector;
import com.annwyn.image.show.model.Detail;
import com.annwyn.image.show.presenter.ListPresenter;
import com.annwyn.image.show.utils.HttpUtils;
import com.annwyn.image.show.utils.JSONUtils;
import com.annwyn.image.show.utils.ParamUtils;
import com.annwyn.image.show.utils.WebAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

public class ListPresenterImpl implements ListPresenter {

    private ListConnector connector;

    private Context context;

    @SuppressWarnings("UnusedParameters")
    public ListPresenterImpl(Context context, ListConnector connector) {
        this.context = context;
        this.connector = connector;
    }

    @Override
    public void loadData(String href, final int page) {
        String url = WebAPI.getDetailList(href, page);
        HttpUtils.getInstance().executeAsync(url, new HttpUtils.JSONCallback() {
            @Override
            public void onResult(Call call, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    List<Detail> details = JSONUtils.getInstance().parseDetailList(jsonArray);
                    if(ParamUtils.isEmpty(details)) {
                        Toast.makeText(context, R.string.no_more_page, Toast.LENGTH_LONG).show();
                        connector.loadComplete();
                    } else {
                        connector.setPageNumber(page);
                        connector.loadDataComplete(details);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Exception e) {
                connector.showError("", e);
            }
        });
    }

}
