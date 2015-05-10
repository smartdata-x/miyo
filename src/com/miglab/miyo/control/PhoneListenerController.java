package com.miglab.miyo.control;

import android.content.Context;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.miglab.miyo.constant.MessageWhat;

/**
 * Created by tudou on 2015/5/10.
 */
public class PhoneListenerController {
    private Context context;
    private Handler handler;
    private TelephonyManager tm;
    private MyPhoneCallListener myPhoneCallListener;

    public PhoneListenerController(Context context, Handler handler){
        this.context = context;
        this.handler = handler;
    }

    public void registerPhoneListener(){
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        myPhoneCallListener = new MyPhoneCallListener(handler);
        //  ÉèÖÃµç»°×´Ì¬¼àÌýÆ÷
        tm.listen(myPhoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void cancelPhoneListener(){
        tm.listen(myPhoneCallListener, PhoneStateListener.LISTEN_NONE);
    }

    class MyPhoneCallListener extends PhoneStateListener{
        private final static String TAG = "MyPhoneCallListener";
        Handler handler;

        public MyPhoneCallListener(Handler handler){
            this.handler = handler;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state){
                //  Í¨»°×´Ì¬
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if(handler != null)
                        handler.sendEmptyMessage(MessageWhat.WM_PHONE_GOING);
                    break;

                //  ¹Ò¶Ï×´Ì¬
                case TelephonyManager.CALL_STATE_IDLE:
                    if(handler != null)
                        handler.sendEmptyMessageDelayed(MessageWhat.WM_PHONE_ENDING, 3000);
                    break;

                //  ÏìÁå×´Ì¬
                case TelephonyManager.CALL_STATE_RINGING:
                    if(handler != null)
                        handler.sendEmptyMessage(MessageWhat.WM_PHONE_COMING);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }

    }
}
