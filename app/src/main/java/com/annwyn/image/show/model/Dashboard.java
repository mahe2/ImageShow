package com.annwyn.image.show.model;

import android.media.Image;

import com.annwyn.image.show.ImageApplication;
import com.annwyn.image.show.utils.ParamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * 分类
 * Created by annwyn on 2016/7/16.
 */
public class Dashboard implements Serializable {

    private static final long serialVersionUID = -7622012052293765277L;

    public Dashboard(){}

    public Dashboard(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.optInt("tid");
        this.link = jsonObject.optString("url");
        this.name = jsonObject.optString("name");
        this.icon = jsonObject.optString("icon");
    }

    private int id;

    private String icon;

    private String name;

    private String link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return MessageFormat.format(this.link, ImageApplication.width, ImageApplication.height, ImageApplication.width, ImageApplication.height);
    }

    public void setLink(String link) {
        this.link = link;
    }
}
