package com.miglab.miyo.entity;

import java.io.Serializable;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/18.
 */
public class SocChatMsg implements Serializable {
    private String msg;
    private String fromUserId;
    private String toUserId;
    private String fromUserHeadURL;
    private String toUserHeadURL;
    private String time;
    private int msgType;
    private boolean isMe;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserHeadURL() {
        return fromUserHeadURL;
    }

    public void setFromUserHeadURL(String fromUserHeadURL) {
        this.fromUserHeadURL = fromUserHeadURL;
    }

    public String getToUserHeadURL() {
        return toUserHeadURL;
    }

    public void setToUserHeadURL(String toUserHeadURL) {
        this.toUserHeadURL = toUserHeadURL;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setIsMe(boolean isMe) {
        this.isMe = isMe;
    }

}
