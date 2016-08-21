package com.annwyn.image.show.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Detail implements Serializable {

    private static final long serialVersionUID = 3812593292757336120L;

    private int id;

    private int groupID;

    /**
     * 小尺寸图片
     */
    private String small;

    /**
     * 大尺寸图片
     */
    private String original;

    public Detail() {
    }

    public Detail(JSONObject jsonObject) {
        this.id = jsonObject.optInt("file_id");
        this.groupID = jsonObject.optInt("group_id");
        JSONObject image = jsonObject.optJSONObject("image");

        this.small = image.optString("small");
//        this.original = image.optString("vip_original");
        this.original = image.optString("original");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
