package com.miglab.miyo.ui;

import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.constant.MessageWhat;
import com.miglab.miyo.constant.MusicServiceDefine;
import com.miglab.miyo.control.MusicService;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.net.DelSongTask;
import com.miglab.miyo.third.universalimageloader.core.DisplayImageOptions;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.third.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.miglab.miyo.ui.widget.RoundImageView;
import com.miglab.miyo.ui.widget.RoundProgressBar;
import com.miglab.miyo.util.DisplayUtil;


/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MusicFragment extends BaseFragment implements View.OnClickListener{
    private RoundImageView iv_cd;
    private RoundProgressBar roundProgressBar;
    private ImageView iv_heart;
    private RelativeLayout ry_cd;
    private TextView tv_songName;
    private TextView tv_songType;
    private PlayerReceiver playerReceiver = null;

    private MusicService  musicService;
    private SongInfo songInfo;// 记录正在播放的歌曲信息
    private Dimension dimension;

    private ObjectAnimator anim;
    private int totaltime;
    private boolean isplaying;
    private int songFlag = 0;// 记录收藏未收藏状态，0没有操作，1收藏成功之后，2取消收藏成功之后
    private int palyFlag = 0;// 歌曲播放状态 0-正在播放 1-暂停

    private DisplayImageOptions options;

    @Override
    protected void setLayout() {
        resourceID = R.layout.fr_music;
    }

    @Override
    protected void init() {
        intiViews();
        anim = ObjectAnimator.ofFloat(iv_cd, "rotation", 0, 360);
        anim.setDuration(50000);
        anim.setRepeatCount(-1);
        anim.setRepeatMode(ObjectAnimator.RESTART);
        anim.setInterpolator(new LinearInterpolator());
        registerPlayerReceiver();
        initMusicData();
        initDisplayImageOptions();
    }

    private void intiViews() {
        iv_cd = (RoundImageView) vRoot.findViewById(R.id.music_cd);
        roundProgressBar = (RoundProgressBar) vRoot.findViewById(R.id.cd_progress);
        tv_songName = (TextView) vRoot.findViewById(R.id.music_name);
        tv_songType = (TextView) vRoot.findViewById(R.id.music_type);
        ry_cd = (RelativeLayout) vRoot.findViewById(R.id.music_player);
        iv_heart = (ImageView) vRoot.findViewById(R.id.heart_music);
        DisplayUtil.setListener(vRoot,this);
    }

    private void initDisplayImageOptions() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
         //       .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @SuppressWarnings("deprecation")
    public void setBackground() {
        Bitmap b = ((BitmapDrawable) (iv_cd).getDrawable()).getBitmap();
        if(Build.VERSION.SDK_INT < 16) {
            vRoot.setBackgroundDrawable(new BitmapDrawable(ac.getResources(), DisplayUtil.fastblur(ac, b, 80)));
        }else {
            vRoot.setBackground(new BitmapDrawable(ac.getResources(), DisplayUtil.fastblur(ac, b, 80)));
        }
    }
//todo 事件监听
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.del_music:
                delMusic();
                break;
            case R.id.heart_music:
                break;
            case R.id.play_music:
                nextMusic();
                break;
            case R.id.cd_palyer:
                pauseMusic();
                break;
        }
    }



    public enum Dimension {
        PD_HUAYU(1, "chl", 1, "华语流行"), PD_OUMEI(2, "chl", 2, "欧美金曲"), PD_YAOGUN(
                3, "chl", 7, "摇滚rock"),
        XQ_SHILIAN(4, "mm", 1, "失恋疗伤"), XQ_CUOZHE(5, "mm", 2, "受到挫折"), XQ_ANLIAN(
                6, "mm", 5, "暗恋"),
        GR_HONGXIN(7, "clt", 20, "我的红星"); /*GR_TUIJIAN(8, "mhx", 8, "推荐偏好");*/

        public int index;
        public String dim;
        public int sid;
        public String name;

        Dimension(int index, String dim, int sid, String name) {
            this.index = index;
            this.dim = dim;
            this.sid = sid;
            this.name = name;
        }

        public static Dimension getDimension(String dim, int sid) {
            for (Dimension d : values()) {
                if (d.sid == sid && dim.equals(d.dim)) {
                    return d;
                }
            }
            return PD_HUAYU;
        }

        public static Dimension getDimension(int index) {
            for (Dimension d : values()) {
                if (d.index == index) {
                    return d;
                }
            }
            return PD_HUAYU;
        }
    }

    class PlayerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            int commend = bundle.getInt(MusicServiceDefine.PLAY_WHAT);
            if (commend <= 0) {
                Toast.makeText(ac,"播放出错",Toast.LENGTH_SHORT).show();
                return;
            }

            switch (commend) {
                case MusicServiceDefine.ALBUN_NULL:
                    Toast.makeText(ac,"电台没有音乐",Toast.LENGTH_SHORT).show();
                    break;

                case MusicServiceDefine.MUSIC_CHANGE:
                    if (bundle.containsKey(MusicServiceDefine.PLAY_INFO)) {
                        SongInfo info = (SongInfo) bundle
                                .getSerializable(MusicServiceDefine.PLAY_INFO);
                        if (info != null) {
                            songInfo = info;
                            setMusicInfo(songInfo);
                        }

                        int d = bundle.getInt(MusicServiceDefine.PLAY_PARAM1);
                        dimension = Dimension.getDimension(d);
                        tv_songType.setText(dimension.name);
                    }
                    break;

                case MusicServiceDefine.MUSIC_PREPARE:
                    int time1 = bundle.getInt(MusicServiceDefine.PLAY_PARAM1);
                    playPrepare(time1);
                    break;

                case MusicServiceDefine.MUSIC_PLAYING:
                    int time2 = bundle.getInt(MusicServiceDefine.PLAY_PARAM1);
                    playTimeUpdate(time2);
                    break;

                case MusicServiceDefine.MUSIC_STOP:
                    break;

                case MusicServiceDefine.ACTIVITY_CLOSE:
                    break;
            }
        }
    }

    private void initMusicData() {
        bindMusicService();
    }

    /** 下一首 */
    private void nextMusic() {
        musicService.nextMusic();
    }

    /** 暂停或开始 */
    private void pauseMusic() {
        if(palyFlag == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                anim.pause();
            } else {
                anim.cancel();
            }
            musicService.toggleMusic();
            palyFlag = 1;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                anim.resume();
            } else {
                anim.start();
            }
            musicService.toggleMusic();
            palyFlag = 0;
        }

    }

    /**
     * 删除歌曲
     * 切到下一首，然后删除 */
    private void delMusic() {
        nextMusic();
        if (songInfo != null) {
            new DelSongTask(ac, handler, songInfo).execute();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.LocalService)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };



    /**
     * 启动后台播放
     *
     * @throws Exception
     */
    private void bindMusicService() {
        Intent intent = new Intent(ac, MusicService.class);
        intent.putExtra(MusicServiceDefine.INTENT_ACTION,
                MusicServiceDefine.ALBUM_START);
        ac.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /** 切换电台 */
    void changeAlbum(Dimension d) {
        Intent intent = new Intent(ac, MusicService.class);
        intent.putExtra(MusicServiceDefine.INTENT_ACTION,
                MusicServiceDefine.ALBUM_CHANGE);
        intent.putExtra(MusicServiceDefine.INTENT_DIMENSION, d.index);
        ac.startService(intent);
    }

    /** 后台播放显示 */
    private void showPlayingContent() {
        Intent intent = new Intent(ac, MusicService.class);
        intent.putExtra(MusicServiceDefine.INTENT_ACTION,
                MusicServiceDefine.FROM_NOTICE);
        ac.startService(intent);
    }

    void playPrepare(int time) {
        totaltime = time;
        isplaying = true;
        cdStartAnimation();
        roundProgressBar.setMax(time / 100);
    }

    void playTimeUpdate(int curTime) {
        roundProgressBar.setProgress(curTime / 100);
    }

    void cdStartAnimation() {
        anim.start();
 //       iv_cd.startAnimation(rotateAnimation);
    }

    void cdStopAnimation() {
//        rotateAnimation.cancel();
    }


    /** 注册更新播放进度的Receiver 在oncreate的时候调用 */
    private void registerPlayerReceiver() {
        if (playerReceiver == null) {
            IntentFilter filter = new IntentFilter(
                    MessageWhat.PLAY_BROADCAST_ACTION_NAME);
            playerReceiver = new PlayerReceiver();
            ac.registerReceiver(playerReceiver, filter);
        }
    }

    /** 移除注册更新播放进度的Receiver 退出该页面前调用 */
    private void unRegisterPlayerReceiver() {
        if (playerReceiver != null) {
            ac.unregisterReceiver(playerReceiver);
            playerReceiver = null;
        }
    }

    void setMusicInfo(SongInfo song) {
        if (song != null && !TextUtils.isEmpty(song.url)) {
            if (!TextUtils.isEmpty(song.name)) {
                if(!TextUtils.isEmpty(song.artist)) {
                    tv_songName.setText(song.artist + "-" + song.name);
                }else{
                    tv_songName.setText(song.name);
                }
            }
            if (song.like == 0){
                iv_heart.setImageResource(R.drawable.heart_music_selector);
            }else if (song.like == 1) {
                iv_heart.setImageResource(R.drawable.music_menu_dark_heart_sel);
            }
            if(!TextUtils.isEmpty(song.pic)) {
                ImageLoader.getInstance().displayImage(song.pic,iv_cd,options,new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        setBackground();
                    }
                });
            }
        }
    }

    void cancelNotification() {
        NotificationManager mNotificationManager = (NotificationManager) ac
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(MusicServiceDefine.MUSIC_PLAYER_NOTIFY_ID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterPlayerReceiver();
        ac.unbindService(serviceConnection);
    }

    @Override
    protected void doHandler(Message msg) {
        super.doHandler(msg);
        switch (msg.what){
            case ApiDefine.GET_HATE_SONG_SUCCESS:
                SongInfo songTemp = (SongInfo) msg.obj;
                Toast.makeText(ac,songTemp.name+"歌曲删除成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
