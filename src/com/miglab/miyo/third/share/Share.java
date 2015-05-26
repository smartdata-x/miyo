package com.miglab.miyo.third.share;


import android.app.Activity;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/26.
 */
public class Share {
    public String title;
    public String summary;
    public String url;
    public String imgURL;
    public String appName;
    protected Activity ac;
    public Share(Activity ac) {
        this.ac = ac;
    }

    public void share(){

    }
}
