package com.annwyn.image.show.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 *
 * Created by annwyn on 2016/7/16.
 */
public class Special implements Serializable {

    private static final long serialVersionUID = 6633855766567582710L;

    private String name;

    private String description;

    private String detail;

    private String image;

    public Special(){}

    public Special(JSONObject jsonObject) {
        this.name = jsonObject.optString("name");
        this.detail = jsonObject.optString("detail");
        this.image = jsonObject.optString("image");
        this.description = jsonObject.optString("description");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
