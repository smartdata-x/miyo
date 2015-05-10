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
    private String musicURL;// ������ַ
    private int totalTime;// ������ʱ��
    public boolean bPaused;

    private boolean isTimerStart = false;
    private Timer mTimer = null;
    private TimerTask mTimerTask; // ��ʱ������

    /* MediaPlayer���� */
    public MediaPlayer mediaPlayer = null;
    private Handler handler;// �ϲ����Handler

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
                    // �������
                    if (handler != null)
                        handler.sendMessage(handler
                                .obtainMessage(MessageWhat.WM_PLAYER_EOF));// ����ĳɺ궨��

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
			/* ����MediaPlayer */
            mediaPlayer.reset();
			/* ����Ҫ���ŵ��ļ���·�� */
            mediaPlayer.setDataSource(musicURL);
			/* ׼������ */
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
		/* ��ʼ���� */
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
		/* �Ƿ����ڲ��� */
        if (mediaPlayer.isPlaying()) {
            // ����MediaPlayer����ʼ״̬
            mediaPlayer.stop();
            // mediaPlayer.reset();//ʹ��reset��������pad�ϻᴥ��complete�ӿڡ����´���¼�������Ϣ��������ǰfinish��by

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
			/* ��ͣ */
            try {
                bPaused = true;
                mediaPlayer.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  �õ���ǰ����ʱ��
     *  @return ����
     */
    public int getCurrnetTime() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * �õ�������ʱ��
     * @return ����
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * �Ƿ����ڲ�����
     * @return
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    /**
     * ������ʱ�����˶�ʱ������ѭ����ȡ���Ž��� ѭ�����:500����
     */
    public void timerStart() {
        if (mTimer == null) {
            mTimer = new Timer();
            mTimerTask = new MusicTimerTask();
            mTimer.schedule(mTimerTask, 0, 500);
        }
    }

    /**
     * ֹͣ��ʱ��
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
