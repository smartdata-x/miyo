package com.miglab.miyo.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class PushMessageInfo implements Serializable {
    public SimpleUser userInfo;
    public SongInfo songInfo;
    public String type;
    public Detail detail;
    public PushMessageInfo(JSONObject json) {
        detail = (Detail) Detail.initWithJsonObject(Detail.class,json.optJSONObject("detail"));
        songInfo = new SongInfo(json.optJSONObject("music"));
        type = json.optString("type");
        userInfo = (SimpleUser) SimpleUser.initWithJsonObject(SimpleUser.class,json.optJSONObject("userinfo"));
    }




}
