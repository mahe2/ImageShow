package com.annwyn.image.show.utils;

import com.annwyn.image.show.model.Dashboard;
import com.annwyn.image.show.model.Detail;
import com.annwyn.image.show.model.Special;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/7/18.
 */
public final class JSONUtils {

    private static JSONUtils instance;

    private JSONUtils(){}

    public static JSONUtils getInstance() {
        if(instance == null)
            instance = new JSONUtils();
        return instance;
    }

    /**
     * 解析json为分类实体类(Dashboard)
     * @param jsonArray jsonArray
     * @return List<Dashboard>
     * @throws JSONException
     */
    public List<Dashboard> parseDashboardList(JSONArray jsonArray) throws JSONException {
        List<Dashboard> dashboards = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tmp = jsonArray.getJSONObject(i);
            dashboards.add(new Dashboard(tmp));
        }
        return dashboards;
    }

    public List<Detail> parseDetailList(JSONArray jsonArray) throws JSONException {
        List<Detail> details = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tmp = jsonArray.getJSONObject(i);
            details.add(new Detail(tmp));
        }
        return details;
    }

    public List<Special> parseSpecialList(JSONArray jsonArray) throws JSONException {
        List<Special> specials = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tmp = jsonArray.getJSONObject(i);
            if("shop".equals(tmp.optString("type")))
                continue;
            specials.add(new Special(tmp));
        }
        return specials;
    }

}
