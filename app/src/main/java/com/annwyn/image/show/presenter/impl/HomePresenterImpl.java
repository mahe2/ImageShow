package com.annwyn.image.show.presenter.impl;

import android.content.Context;
import android.widget.Toast;

import com.annwyn.image.show.R;
import com.annwyn.image.show.connector.HomeConnector;
import com.annwyn.image.show.model.Detail;
import com.annwyn.image.show.model.Special;
import com.annwyn.image.show.presenter.HomePresenter;
import com.annwyn.image.show.utils.HttpUtils;
import com.annwyn.image.show.utils.JSONUtils;
import com.annwyn.image.show.utils.WebAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

public class HomePresenterImpl implements HomePresenter {

    private HomeConnector connector;

    private Context context;

    @SuppressWarnings("UnusedParameters")
    public HomePresenterImpl(Context context, HomeConnector connector) {
        this.context = context;
        this.connector = connector;
    }

    @Override
    public void loadData(final int page) {
        String url = WebAPI.getHome(page);
        HttpUtils.getInstance().executeAsync(url, new HttpUtils.JSONCallback() {
            @Override
            public void onResult(Call call, JSONObject response) {
                try {
                    List<Detail> details = parseJSON(response);
                    showResult(details, page);
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

    private List<Detail> parseJSON(JSONObject response) throws JSONException {
        JSONArray jsonArray = response.optJSONArray("slider");
        if (jsonArray != null && jsonArray.length() != 0) {
            List<Special> specials = JSONUtils.getInstance().parseSpecialList(jsonArray);
            this.connector.initializeBanner(specials);
        }
        JSONArray dataArray = response.getJSONArray("data");
        return JSONUtils.getInstance().parseDetailList(dataArray);
    }

    private void showResult(List<Detail> details, int page) {
        if (details.isEmpty()) {
            Toast.makeText(context, R.string.no_more_page, Toast.LENGTH_LONG).show();
            connector.loadComplete();
        } else {
            connector.setPageNumber(page);
            connector.loadDataComplete(details);
        }
    }
}
