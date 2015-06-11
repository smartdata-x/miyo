package com.miglab.miyo.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.R;
import com.miglab.miyo.adapter.MessageAdapter;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.constant.MessageWhat;
import com.miglab.miyo.control.IPlayAction;
import com.miglab.miyo.control.MusicManager;
import com.miglab.miyo.control.MusicService;
import com.miglab.miyo.control.PlayerReceiver;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.net.CollectSongTask;
import com.miglab.miyo.net.DelCollectSongTask;
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
 * Date: 2015/6/11.
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener,IPlayAction{
    protected ImageView iv_cd;
    protected ImageView iv_heart;
    protected RoundProgressBar roundProgressBar;
    private RelativeLayout parent;
    private ListView listView;

    protected ObjectAnimator anim;
    private DisplayImageOptions options;
    private PlayerReceiver playerReceiver = null;
    @Override
    protected void init() {
        setContentView(R.layout.ac_message);
        initDisplayImageOptions();
        initViews();
        initAnim();
        registerPlayerReceiver();
        initMusicPre();
    }

    private void initMusicPre() {
        MusicService musicService = MusicManager.getInstance().getMusicService();
        musicPrepare(musicService.getTotalTime());
        updateMusicState(MusicManager.getInstance().getMusicService().bePause());
        updatePicInfo(MusicManager.getInstance().getSongInfo().pic);
    }

    protected void initViews() {
        View vRoot = findViewById(R.id.root);
        iv_cd = (RoundImageView) findViewById(R.id.music_cd);
        roundProgressBar = (RoundProgressBar) findViewById(R.id.cd_progress);
        iv_heart = (ImageView) findViewById(R.id.heart_music);
        parent = (RelativeLayout) findViewById(R.id.parent);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new MessageAdapter(this));
        DisplayUtil.setViewListener(vRoot, this);
    }

    protected void initAnim() {
        anim = ObjectAnimator.ofFloat(iv_cd, "rotation", 0, 360);
        anim.setDuration(50000);
        anim.setRepeatCount(-1);
        anim.setRepeatMode(ObjectAnimator.RESTART);
        anim.setInterpolator(new LinearInterpolator());
    }

    private void initDisplayImageOptions() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(false)
                .showImageForEmptyUri(R.drawable.music_default_cd)
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterPlayerReceiver();
    }

    private void registerPlayerReceiver() {
        if (playerReceiver == null) {
            IntentFilter filter = new IntentFilter(
                    MessageWhat.PLAY_BROADCAST_ACTION_NAME);
            playerReceiver = new PlayerReceiver(this);
            registerReceiver(playerReceiver, filter);
        }
    }

    /**
     * 移除注册更新播放进度的Receiver 退出该页面前调用
     */
    private void unRegisterPlayerReceiver() {
        if (playerReceiver != null) {
            unregisterReceiver(playerReceiver);
            playerReceiver = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.del_music:
                delMusic();
                break;
            case R.id.heart_music:
                collectMusic();
                break;
            case R.id.next_music:
                nextMusic();
                break;
            case R.id.cd_palyer:
                pauseMusic();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void pauseMusic() {
        MusicManager.getInstance().getMusicService().toggleMusic();
        updateMusicState(MusicManager.getInstance().getMusicService().bePause());
    }

    private void updateMusicState(boolean bPause) {
        if (bPause) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                anim.pause();
            } else {
                anim.cancel();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                anim.resume();
            } else {
                anim.start();
            }
        }
    }

    private void collectMusic() {
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        MusicType musicType = MusicManager.getInstance().getMusicType();
        if (songInfo == null || musicType == null)
            return;
        //收藏歌曲
        if (songInfo.like == 0) {
            new CollectSongTask(uiHandler, songInfo, musicType).execute();
        }
        //取消收藏
        if (songInfo.like == 1) {
            new DelCollectSongTask(uiHandler, songInfo).execute();
        }
    }

    public void nextMusic() {
        MusicManager.getInstance().getMusicService().nextMusic();
    }

    private void delMusic() {
        nextMusic();
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        if (songInfo != null) {
            new DelSongTask(uiHandler, songInfo).execute();
        }
    }




    @Override
    public void albumNull() {
        Toast.makeText(MessageActivity.this, "电台没有音乐", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void musicChange() {
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        updateHeartMusic(songInfo.like == 1);
        updatePicInfo(songInfo.pic);
    }

    @Override
    public void musicPrepare(int time) {
        cdStartAnimation();
        setMaxProgressBar(time / 100);
    }

    @Override
    public void musicPlaying(int time) {
        setMusicProgress(time / 100);
    }

    @Override
    public void updateListSuccess() {

    }

    @Override
    public void updateListNone() {

    }

    @Override
    public void stopToNext() {
        updatePicInfo(null);
        setMusicProgress(0);
        updateMusicState(true);
    }

    public void updateHeartMusic(boolean bLike) {
        if(bLike) {
            iv_heart.setImageResource(R.drawable.delcollect_music_selector);
        } else{
            iv_heart.setImageResource(R.drawable.collect_music_selector);
        }
    }

    public void updatePicInfo(String url) {
        ImageLoader.getInstance().displayImage(url, iv_cd, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(!TextUtils.isEmpty(imageUri))
                    setBackground();
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void setBackground() {
        Drawable drawable = iv_cd.getDrawable();
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Drawable drawableBg = new BitmapDrawable(getResources(), DisplayUtil.fastblur(this, b, 80));
        if (Build.VERSION.SDK_INT < 16) {
            parent.setBackgroundDrawable(drawableBg);
        } else {
            parent.setBackground(drawableBg);
        }
    }

    protected void setMaxProgressBar(int i) {
        roundProgressBar.setMax(i);
    }

    protected void cdStartAnimation() {
        anim.start();
    }

    public void setMusicProgress(int time){
        roundProgressBar.setProgress(time);
    }

    public void delLocateMusic(SongInfo info) {
        MusicManager.getInstance().getMusicService().delLocateMusic(info);
    }

    @Override
    protected void doHandler(Message msg) {
        switch (msg.what) {
            case ApiDefine.GET_COLLECT_SONG_SUCCESS:
                SongInfo songTemp1 = (SongInfo) msg.obj;
                Toast.makeText(this, songTemp1.name + " 收藏成功", Toast.LENGTH_SHORT).show();
                if (songTemp1.id == MusicManager.getInstance().getSongInfo().id) {
                    MusicManager.getInstance().getSongInfo().like = 1;
                    updateHeartMusic(true);

                }
                break;
            case ApiDefine.GET_HATE_SONG_SUCCESS:
                SongInfo songTemp = (SongInfo) msg.obj;
                Toast.makeText(this, songTemp.name + " 删除成功", Toast.LENGTH_SHORT).show();
                break;
            case ApiDefine.GET_DELECT_COLLECT_SONG_SUCCESS:
                SongInfo songTemp2 = (SongInfo) msg.obj;
                Toast.makeText(this, songTemp2.name + " 取消收藏成功", Toast.LENGTH_SHORT).show();
                //如果当前在我的红心歌单，则切到下一首同时删除该曲目
                if (MusicManager.getInstance().getMusicType().getId() == MiyoUser.getInstance().getUserId()) {
                    nextMusic();
                    delLocateMusic(songTemp2);

                }
                if (songTemp2.id == MusicManager.getInstance().getSongInfo().id) {
                    MusicManager.getInstance().getSongInfo().like = 0;
                    updateHeartMusic(false);
                }
                break;
        }
    }
}