package com.miglab.miyo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.miglab.miyo.R;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.third.share.Share2Weixin;
import com.miglab.miyo.util.DisplayUtil;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    @Override
    protected void init() {
        setContentView(R.layout.ac_about);
        title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.about));
        DisplayUtil.setViewListener(findViewById(R.id.bottom_menu),this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareApp:
                Share2Weixin share2Weixin = new Share2Weixin(this,null);
                share2Weixin.setReqScene(Share2Weixin.WEIXIN_SHARE);
                share2Weixin.shareApp();
                break;
            case R.id.ewm:
                break;
            case R.id.question:
                break;
        }
    }
}