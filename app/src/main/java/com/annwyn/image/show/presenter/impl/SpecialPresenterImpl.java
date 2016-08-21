package com.annwyn.image.show.presenter.impl;

import android.content.Context;
import android.widget.Toast;

import com.annwyn.image.show.R;
import com.annwyn.image.show.connector.SpecialConnector;
import com.annwyn.image.show.model.Special;
import com.annwyn.image.show.presenter.SpecialPresenter;
import com.annwyn.image.show.utils.HttpUtils;
import com.annwyn.image.show.utils.JSONUtils;
import com.annwyn.image.show.utils.ParamUtils;
import com.annwyn.image.show.utils.WebAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

public class SpecialPresenterImpl implements SpecialPresenter {

    private SpecialConnector connector;

    private Context context;

    @SuppressWarnings("UnusedParameters")
    public SpecialPresenterImpl(Context context, SpecialConnector connector) {
        this.context = context;
        this.connector = connector;
    }

    @Override
    public void loadData(final int page) {
        String url = WebAPI.getSpecialList(page);
        HttpUtils.getInstance().executeAsync(url, new HttpUtils.JSONCallback() {
            @Override
            public void onResult(Call call, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    List<Special> specials = JSONUtils.getInstance().parseSpecialList(jsonArray);
                    if (ParamUtils.isEmpty(specials)) {
                        Toast.makeText(context, R.string.no_more_page, Toast.LENGTH_LONG).show();
                        connector.loadComplete();
                    } else {
                        connector.setPageNumber(page);
                        connector.loadDataComplete(specials);
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
