package com.miglab.miyo.ui;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.MessageWhat;
import com.miglab.miyo.constant.MusicServiceDefine;
import com.miglab.miyo.control.MusicService;
import com.miglab.miyo.entity.MusicInfo;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.ui.widget.RoundImageView;
import com.miglab.miyo.ui.widget.RoundProgressBar;
import com.miglab.miyo.util.DisplayUtil;

import java.util.ArrayList;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MusicFragment extends BaseFragment {
    private RoundImageView iv_cd;
    private RoundProgressBar roundProgressBar;
    private TextView tv_songName;
    private TextView tv_songType;
    private PlayerReceiver playerReceiver = null;

    private SongInfo songInfo;// 记录正在播放的歌曲信息
    private Dimension dimension;

    private Animation rotateAnimation;
    private int totaltime;
    private boolean isplaying;

    @Override
    protected void setLayout() {
        resourceID = R.layout.fr_music;
    }

    @Override
    protected void init() {
        iv_cd = (RoundImageView) vRoot.findViewById(R.id.music_cd);
        roundProgressBar = (RoundProgressBar) vRoot.findViewById(R.id.cd_progress);
        tv_songName = (TextView) vRoot.findViewById(R.id.music_name);
        tv_songType = (TextView) vRoot.findViewById(R.id.music_type);
        rotateAnimation = AnimationUtils.loadAnimation(ac, R.anim.cd_rotate_anim);
        registerPlayerReceiver();
        initMusicData();
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBackground() {
        Bitmap b = ((BitmapDrawable) (iv_cd).getDrawable()).getBitmap();
        vRoot.setBackground(new BitmapDrawable(ac.getResources(),DisplayUtil.fastblur(ac, b, 80)));
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

                case MusicServiceDefine.MUSIC_PAUSE:
                    cdStopAnimation();
                    break;

                case MusicServiceDefine.MUSIC_RESUME:
                    cdStartAnimation();
                    break;

                case MusicServiceDefine.MUSIC_STOP:
                    break;

                case MusicServiceDefine.ACTIVITY_CLOSE:
                    break;
            }
        }
    }

    private void initMusicData() {
        if (MusicService.isBackPlaying()) {
            showPlayingContent();
        } else {
            MusicInfo info = null;//(MusicInfo) i.getSerializableExtra("music");
            if (info != null) {
                changeAlbum(Dimension.getDimension(info.getDimension(),
                        info.getSid()));
            } else {
                startPlayService();
            }
        }
    }

    /**
     * 启动后台播放
     *
     * @throws Exception
     */
    private void startPlayService() {
        Intent intent = new Intent(ac, MusicService.class);
        intent.putExtra(MusicServiceDefine.INTENT_ACTION,
                MusicServiceDefine.ALBUM_START);
        ac.startService(intent);
    }

    /** 停止后台播放 */
    private void stopPlayService() {
        Intent intent = new Intent(ac, MusicService.class);
        intent.putExtra(MusicServiceDefine.INTENT_ACTION,
                MusicServiceDefine.ACTION_STOP);
        ac.startService(intent);
        cancelNotification();
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
        iv_cd.startAnimation(rotateAnimation);
    }

    void cdStopAnimation() {
        rotateAnimation.cancel();
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
            if (!TextUtils.isEmpty(song.name))
                tv_songName.setText(song.name);
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
    }
}
