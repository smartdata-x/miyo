package com.miglab.miyo.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import com.miglab.miyo.MiyoApplication;

import java.lang.ref.WeakReference;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        MiyoApplication.register(this);
        init();
    }

    protected void init() {

    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MiyoApplication.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class UIHandler extends Handler {
        private final WeakReference<BaseActivity> mActivity;

        public UIHandler(BaseActivity activity) {
            mActivity = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = mActivity.get();
            if (activity != null) {
                activity.doHandler(msg);
            }
        }
    }

    protected UIHandler uiHandler = new BaseActivity.UIHandler(this);

    protected void doHandler(Message msg) {
    }
}