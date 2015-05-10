package com.miglab.miyo.ui;

import android.content.Intent;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.R;


public class LoadActivity extends BaseActivity {
    @Override
    protected void init() {
        setContentView(R.layout.ac_start);
        autoLogin();
        uiHandler.postDelayed(runnable,1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //todo MusicActivity
            startActivity(new Intent(LoadActivity.this,MainActivity.class));
            finish();
        }
    };

    private void autoLogin() {
        //todo µÇÂ¼Ä£¿é
    }


}
