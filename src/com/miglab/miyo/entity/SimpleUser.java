package com.miglab.miyo.entity;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class SimpleUser extends BaseEntity{
    private String birthday;
    private String cur_music;
    private Double distance;
    private String head;
    private String location;
    private String nickname;
    private Integer sex;
    private Integer source;
    private Integer userid;

    public String getBirthday() {
        return birthday;
    }

    public String getCur_music() {
        return cur_music;
    }

    public Double getDistance() {
        return distance;
    }

    public String getHead() {
        return head;
    }

    public String getLocation() {
        return location;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public Integer getSource() {
        return source;
    }

    public Integer getUserid() {
        return userid;
    }
}
