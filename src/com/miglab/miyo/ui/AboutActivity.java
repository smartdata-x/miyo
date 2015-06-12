package com.miglab.miyo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.miglab.miyo.R;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class AboutActivity extends BaseActivity {
    private TextView title;
    @Override
    protected void init() {
        setContentView(R.layout.ac_about);
        title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.about));
    }

}