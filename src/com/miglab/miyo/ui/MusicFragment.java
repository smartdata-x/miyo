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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.MessageWhat;
import com.miglab.miyo.constant.MusicServiceDefine;
import com.miglab.miyo.control.MusicService;
import com.miglab.miyo.entity.MusicInfo;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.third.universalimageloader.core.DisplayImageOptions;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.third.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.miglab.miyo.ui.widget.RoundImageView;
import com.miglab.miyo.ui.widget.RoundProgressBar;
import com.miglab.miyo.util.DisplayUtil;

import java.util.ArrayList;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MusicFragment extends BaseFragment implements View.OnClickListener{
    private RoundImageView iv_cd;
    private RoundProgressBar roundProgressBar;
    private ImageView iv_cdPlayer;
    private RelativeLayout ry_cd;
    private TextView tv_songName;
    private TextView tv_songType;
    private PlayerReceiver playerReceiver = null;

    private SongInfo songInfo;// ��¼���ڲ��ŵĸ�����Ϣ
    private Dimension dimension;

    private Animation rotateAnimation;
    private int totaltime;
    private boolean isplaying;

    private DisplayImageOptions options;

    @Override
    protected void setLayout() {
        resourceID = R.layout.fr_music;
    }

    @Override
    protected void init() {
        intiViews();
        rotateAnimation = AnimationUtils.loadAnimation(ac, R.anim.cd_rotate_anim);
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
        iv_cdPlayer = (ImageView) vRoot.findViewById(R.id.cd_palyer);
        iv_cdPlayer.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if(v == ry_cd){
            Toast.makeText(ac,"hah",Toast.LENGTH_SHORT).show();
        }
    }

    public enum Dimension {
        PD_HUAYU(1, "chl", 1, "��������"), PD_OUMEI(2, "chl", 2, "ŷ������"), PD_YAOGUN(
                3, "chl", 7, "ҡ��rock"),

        XQ_SHILIAN(4, "mm", 1, "ʧ������"), XQ_CUOZHE(5, "mm", 2, "�ܵ�����"), XQ_ANLIAN(
                6, "mm", 5, "����"),

        GR_HONGXIN(7, "clt", 20, "�ҵĺ���"); /*GR_TUIJIAN(8, "mhx", 8, "�Ƽ�ƫ��");*/

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
                Toast.makeText(ac,"���ų���",Toast.LENGTH_SHORT).show();
                return;
            }

            switch (commend) {
                case MusicServiceDefine.ALBUN_NULL:
                    Toast.makeText(ac,"��̨û������",Toast.LENGTH_SHORT).show();
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
     * ������̨����
     *
     * @throws Exception
     */
    private void startPlayService() {
        Intent intent = new Intent(ac, MusicService.class);
        intent.putExtra(MusicServiceDefine.INTENT_ACTION,
                MusicServiceDefine.ALBUM_START);
        ac.startService(intent);
    }

    /** ֹͣ��̨���� */
    private void stopPlayService() {
        Intent intent = new Intent(ac, MusicService.class);
        intent.putExtra(MusicServiceDefine.INTENT_ACTION,
                MusicServiceDefine.ACTION_STOP);
        ac.startService(intent);
        cancelNotification();
    }

    /** �л���̨ */
    void changeAlbum(Dimension d) {
        Intent intent = new Intent(ac, MusicService.class);
        intent.putExtra(MusicServiceDefine.INTENT_ACTION,
                MusicServiceDefine.ALBUM_CHANGE);
        intent.putExtra(MusicServiceDefine.INTENT_DIMENSION, d.index);
        ac.startService(intent);
    }

    /** ��̨������ʾ */
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


    /** ע����²��Ž��ȵ�Receiver ��oncreate��ʱ����� */
    private void registerPlayerReceiver() {
        if (playerReceiver == null) {
            IntentFilter filter = new IntentFilter(
                    MessageWhat.PLAY_BROADCAST_ACTION_NAME);
            playerReceiver = new PlayerReceiver();
            ac.registerReceiver(playerReceiver, filter);
        }
    }

    /** �Ƴ�ע����²��Ž��ȵ�Receiver �˳���ҳ��ǰ���� */
    private void unRegisterPlayerReceiver() {
        if (playerReceiver != null) {
            ac.unregisterReceiver(playerReceiver);
            playerReceiver = null;
        }
    }

    void setMusicInfo(SongInfo song) {
        if (song != null && !TextUtils.isEmpty(song.url)) {
            if (!TextUtils.isEmpty(song.name)) {
                tv_songName.setText(song.name);
            }
            if (song.like == 0){
                //todo �Ǻ���
            }else if (song.like == 1) {
                //todo ����
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
    }
}
