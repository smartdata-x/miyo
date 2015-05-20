package com.miglab.miyo.ui;

import android.content.Intent;
import com.miglab.miyo.R;



public class StartActivity extends BaseActivity {
    @Override
    protected void init() {
        setContentView(R.layout.ac_start);
        autoLogin();
        uiHandler.postDelayed(runnable, 1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //todo MusicActivity
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
    };

    private void autoLogin() {
        //todo µÇÂ¼Ä£¿é
    }


}
