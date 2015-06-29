package com.miglab.miyo.third.share;


import android.app.Activity;
import android.graphics.Bitmap;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.ui.MainActivity;

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
    public String album;
    public Bitmap bitmap;
    protected Activity ac;
    public Share(Activity ac, SongInfo songInfo) {
        this.ac = ac;
        if(songInfo != null) {
            this.title = songInfo.name;
            this.summary = songInfo.artist;
            this.url = songInfo.url;
            this.imgURL = songInfo.pic;
            this.appName = ac.getString(R.string.app_name);
        }
    }

    public void share(){

    }
}
