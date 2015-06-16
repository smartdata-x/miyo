package com.miglab.miyo.entity;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public class Emoticon {
    public final static int FACE_TYPE_NOMAL = 0;
    public final static int FACE_TYPE_DEL = 1;
    public final static int FACE_TYPE_USERDEF = 2;

    /**
     * 点击处理事件类型
     */
    public int eventType;
    /**
     * 表情图标
     */
    public String iconUri;
    /**
     * 内容
     */
    public String key;

    public Emoticon(int eventType, String iconUri, String key){
        this.eventType = eventType;
        this.iconUri = iconUri;
        this.key = key;
    }
}
