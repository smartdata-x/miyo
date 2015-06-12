package com.miglab.miyo.entity;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class Detail extends BaseEntity{
    private String msg;
    private String send_uid;
    private String time;
    private String to_uid;

    public String getMsg() {
        return msg;
    }

    public String getSend_uid() {
        return send_uid;
    }

    public String getTime() {
        return time;
    }

    public String getTo_uid() {
        return to_uid;
    }
}
