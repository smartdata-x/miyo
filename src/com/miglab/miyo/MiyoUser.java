package com.miglab.miyo;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/21.
 */
public class MiyoUser {
    public static int QQ_LOGIN = 2;
    public static int WEIBO_LOGIN = 1;

    private int machine; //1-android 2-iphone
    private String nickname;
    private int source; // 登陆来源 1 新浪微博 2 QQ 3 微信 4豆瓣
    private String session; // 第三方平台返回唯一凭据
    private String imei;
    private int gender; // 0 女 1男
    private String birthday;
    private String location; //地区
    private String headUrl;
    private String latitude;
    private String longitude;
    private int plat; //MIYO官方平台号
    private String channel; //推广渠道号

    private String token; //登录成功，服务端返回唯一标识
    private int userId; // 登录成功，服务端返回用户id

    static MiyoUser user = new MiyoUser();

    private MiyoUser() {
        machine = 1;
        plat = 10000;
        channel = "10000";
    }

    public static MiyoUser getInstance() {
        return user;
    }

    public int getPlat() {
        return plat;
    }

    public String getChannel() {
        return channel;
    }

    public int getUserId() {
        return userId;
    }

    public int getMachine() {
        return machine;
    }

    public String getToken() {
        return token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getImei() {
        if (imei == null || imei.length() == 0) {
            TelephonyManager t = (TelephonyManager) MiyoApplication.getInstance()
                    .getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = t.getDeviceId();
        }
        return imei;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean hasRecoder() {
        return userId > 0 && token.length() > 0;
    }

    public void loginSet(JSONObject json) {
        if (json != null) {
            userId = json.optInt("uid");
            location = json.optString("location");
            nickname = json.optString("nickname");
            token = json.optString("token");
            headUrl = json.optString("head");
            writeRecord();
        }
    }

    public boolean isLogin() {
        return false;
    }

    public static String getApiBasicParams() {
        return "?uid=" + user.userId + "&token=" + user.token;
    }

    public void readRecord() {
        if (userId <= 0) {
            SharedPreferences sharedPrefs = MiyoApplication.getInstance().getSharedPreferences("miuser",
                    Context.MODE_PRIVATE);
            if (sharedPrefs != null) {
                userId = sharedPrefs.getInt("userId", 0);
                session = sharedPrefs.getString("id", "");
                token = sharedPrefs.getString("token", "");
                nickname = sharedPrefs.getString("nickname", "");
                gender = sharedPrefs.getInt("gender", 0);
                headUrl = sharedPrefs.getString("headUrl", "");
                birthday = sharedPrefs.getString("birthday", "");
                location = sharedPrefs.getString("location", "");
                imei = sharedPrefs.getString("imei", "");
                source = sharedPrefs.getInt("source",3);
            }
        }
    }

    public void writeRecord() {
        SharedPreferences sharedPrefs = MiyoApplication.getInstance()
                .getSharedPreferences("miuser", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("userId", userId);
        editor.putInt("source", source);
        editor.putString("token", token);
        editor.putString("nickname", nickname);
        editor.putInt("gender", gender);
        editor.putString("headUrl", headUrl);
        editor.putString("birthday", birthday);
        editor.putString("location", location);
        editor.putString("imei", imei);
        editor.commit();
    }
}
