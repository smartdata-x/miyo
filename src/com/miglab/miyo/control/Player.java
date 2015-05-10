package com.miglab.miyo.control;

import android.media.MediaPlayer;
import android.os.Handler;
import com.miglab.miyo.constant.MessageWhat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tudou on 2015/5/9.
 */
public class Player {
    private String musicURL;// 歌曲地址
    private int totalTime;// 歌曲总时间
    public boolean bPaused;

    private boolean isTimerStart = false;
    private Timer mTimer = null;
    private TimerTask mTimerTask; // 定时器任务

    /* MediaPlayer对象 */
    public MediaPlayer mediaPlayer = null;
    private Handler handler;// 上层监听Handler

    public Player(Handler h){
        this.handler = h;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                totalTime = mediaPlayer.getDuration();
                if (handler != null)
                    handler.sendMessage(handler.obtainMessage(
                            MessageWhat.PLAYER_PREPARED, totalTime, 0));
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer arg0) {
                if (!bPaused) {
                    // 播放完成
                    if (handler != null)
                        handler.sendMessage(handler
                                .obtainMessage(MessageWhat.WM_PLAYER_EOF));// 后面改成宏定义

                    if (isTimerStart) {
                        timerStop();
                    }
                }
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(
                            MessageWhat.PLAYER_ERROR, what, extra));
                }

                return false;
            }
        });
    }

    public boolean setMusicInfo(String url) {
        musicURL = url;
        totalTime = 0;
        bPaused = false;

        try {
			/* 重置MediaPlayer */
            mediaPlayer.reset();
			/* 设置要播放的文件的路径 */
            mediaPlayer.setDataSource(musicURL);
			/* 准备播放 */
            mediaPlayer.prepare();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (handler != null) {
                handler.sendEmptyMessage(MessageWhat.PLAYER_ERROR);
            }
            return false;
        }
    }

    public void start(boolean startTimer) {
		/* 开始播放 */
        mediaPlayer.start();
        bPaused = false;
        totalTime = mediaPlayer.getDuration();

        isTimerStart = startTimer;
        if (isTimerStart) {
            timerStart();
        }

    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    public void Stop() {
		/* 是否正在播放 */
        if (mediaPlayer.isPlaying()) {
            // 重置MediaPlayer到初始状态
            mediaPlayer.stop();
            // mediaPlayer.reset();//使用reset，在三星pad上会触发complete接口。导致触发录制完成消息，程序提前finish。by

            if (isTimerStart) {
                timerStop();
            }
        }
    }

    public void Resume() {
        mediaPlayer.start();
        bPaused = false;
    }

    public void Pause() {
        if (mediaPlayer.isPlaying()) {
			/* 暂停 */
            try {
                bPaused = true;
                mediaPlayer.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  得到当前播放时间
     *  @return 毫秒
     */
    public int getCurrnetTime() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 得到歌曲总时间
     * @return 毫秒
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * 是否正在播放中
     * @return
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    /**
     * 开启定时器，此定时器用于循环获取播放进度 循环间隔:500毫秒
     */
    public void timerStart() {
        if (mTimer == null) {
            mTimer = new Timer();
            mTimerTask = new MusicTimerTask();
            mTimer.schedule(mTimerTask, 0, 500);
        }
    }

    /**
     * 停止定时器
     */
    public void timerStop() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    class MusicTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                if (isPlaying()) {
                    handler.sendMessage(handler.obtainMessage(
                            MessageWhat.PLAYING_TIME, getCurrnetTime(), 0));
                }else if(bPaused){
                    Thread.sleep(50);
                }
            } catch (Exception e) {
                timerStop();
                e.printStackTrace();
            }
        }
    }

}
