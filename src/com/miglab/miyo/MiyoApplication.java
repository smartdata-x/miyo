package com.miglab.miyo;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
//import com.crashlytics.android.Crashlytics;
import com.miglab.miyo.third.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.third.universalimageloader.core.ImageLoaderConfiguration;
import com.miglab.miyo.third.universalimageloader.core.assist.QueueProcessingType;
import com.miglab.miyo.util.LocationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MiyoApplication extends Application {
    private static MiyoApplication instance;
    public static Activity frontActivity;
    private static List<Activity> activityStack = new ArrayList<Activity>();
    public static MiyoApplication getInstance() {
        return instance;
    }

    private LocationUtil locationUtil;
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
        instance = this;
        locationUtil = new LocationUtil(this);
  //      Crashlytics.start(this);
    }

    public LocationUtil getLocationUtil(){
        return locationUtil;
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    public synchronized static void resume(Activity activity) {
        frontActivity = activity;
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
