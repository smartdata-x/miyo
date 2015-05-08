package com.miglab.miyo;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MiyoApplication extends Application {
    private static MiyoApplication instance;
    private static List<Activity> activityStack = new ArrayList<>();
    public static MiyoApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public synchronized static void register(Activity activity) {
        if(activityStack != null)
            activityStack.add(activity);
    }

    public synchronized static void unregister(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public synchronized static void exitApp(Context con){
        if(activityStack != null && activityStack.size() > 0) {
            for(Activity ac : activityStack) {
                if(ac != null && !ac.isFinishing()) {
                    ac.finish();
                }
            }
        }
        /*Çå³ýÍ¨ÖªÀ¸ */
        ((NotificationManager) con.getSystemService(android.content.Context.NOTIFICATION_SERVICE)).cancelAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
