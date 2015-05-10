package com.miglab.miyo.control;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import com.miglab.miyo.constant.MessageWhat;

/**
 * Created by tudou on 2015/5/10.
 */
public class HeadsetPlugController {
    private Context context;
    private Handler handler;
    private HeadsetPlugReceiver receiver;

    public HeadsetPlugController(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void registerReceiver() {
        receiver = new HeadsetPlugReceiver(handler);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.HEADSET_PLUG");
        context.registerReceiver(receiver, filter);
    }

    public void cancelReceiver() {
        if (receiver != null) {
            context.unregisterReceiver(receiver); // È¡Ïû¼àÌý
            receiver = null;
        }
    }

    class HeadsetPlugReceiver extends BroadcastReceiver {
        private Handler handler;
        int type = -1;

        public HeadsetPlugReceiver(Handler handler) {
            super();
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            try {
                if (handler == null)
                    return;

                if (intent.hasExtra("state")) {
                    int state = intent.getIntExtra("state", 0);
                    if(type != state){
                        type = state;
                        if (state == 1) {
                            handler.sendEmptyMessage(MessageWhat.WM_HEADSET_ON);
                        } else if (state == 0) {
                            handler.sendEmptyMessage(MessageWhat.WM_HEADSET_OFF);
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public boolean checkHeadsetOn(Context con) {
        AudioManager am = (AudioManager) con.getSystemService(Activity.AUDIO_SERVICE);
        if (!am.isWiredHeadsetOn() || am.isSpeakerphoneOn()) {
            return false;
        } else {
            return true;
        }
    }
}
