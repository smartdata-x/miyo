package com.miglab.miyo.control;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.constant.MessageWhat;
import com.miglab.miyo.constant.MusicServiceDefine;
import com.miglab.miyo.entity.SongInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.miglab.miyo.MyUser;
import com.miglab.miyo.control.AudioController.AudioState;
import com.miglab.miyo.net.DimensionFMTask;
import com.miglab.miyo.net.GetCltSongsTask;
import com.miglab.miyo.ui.MusicFragment;
import com.miglab.miyo.ui.MusicFragment.Dimension;
/**
 * Created by tudou on 2015/5/9.
 */
public class MusicService extends Service implements AudioControllerListener{

    private MyUser user;
    // ========播放音乐======//
    ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
    Player player;
    static int playState;
    int playIndex, totalTime;
    Dimension mDimension;
    SongInfo mSong;
    boolean isMusicActivityOpen = true;
    private AudioController audioController;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (player == null)
            player = new Player(handler);

        audioController = AudioController.get();

        user = MyUser.getInstance();
        user.readRecord(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                doMusic(bundle);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void bePaused(AudioState state) {
        if (player != null && playState == MusicServiceDefine.PLAYER_PLAYING) {
            player.Pause();
            playState = MusicServiceDefine.PLAYER_PAUSE;
            sendBroadCast(MusicServiceDefine.MUSIC_PAUSE);
        }
    }

    @Override
    public void beResumed(AudioState state) {
        if (player != null && playState == MusicServiceDefine.PLAYER_PAUSE) {
            player.Resume();
            playState = MusicServiceDefine.PLAYER_PLAYING;
            sendBroadCast(MusicServiceDefine.MUSIC_RESUME);
        }
    }

    @Override
    public void beStopped(AudioState state) {
        stopMusic();
    }

    @Override
    public void beClosed(AudioState state) {
        sendBroadCast(MusicServiceDefine.ACTIVITY_CLOSE);
    }

    @Override
    public void headsetOn(AudioState state) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void headsetOff(AudioState state) {
        // TODO 自动生成的方法存根

    }

    /** 处理判断 */
    public void doMusic(Bundle bundle) {
        try {
            int action = bundle.getInt(MusicServiceDefine.INTENT_ACTION);
            switch (action) {
                case MusicServiceDefine.ALBUM_START:// 开始播放专辑或者切换专辑
                    startAlbum(bundle);
                    break;

                case MusicServiceDefine.ACTION_TOGGLE: // 暂停或者开始
                    toggleMusic();
                    break;

                case MusicServiceDefine.ACTION_STOP:
                    stopMusic();
                    break;

                case MusicServiceDefine.ACTION_SWITCH:
                    nextMusic();
                    break;

                case MusicServiceDefine.ACTIVITY_FINISH:
                    isMusicActivityOpen = false;
                    break;

                case MusicServiceDefine.ALBUM_CHANGE:
                    startAlbum(bundle);
                    break;

                case MusicServiceDefine.FROM_NOTICE:
                    fromNotice();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 启动或切换专辑 */
    void startAlbum(Bundle bundle) {
        if (player == null)
            player = new Player(handler);

        if (isBackPlaying())
            player.Stop();

        playState = MusicServiceDefine.PLAYER_IDLE;
        playIndex = 0;
        isMusicActivityOpen = true;

        int iDimension = bundle.getInt(MusicServiceDefine.INTENT_DIMENSION);
        mDimension = Dimension.getDimension(iDimension);

        getDimensionMusics(mDimension);
    }

    /** 获取音乐列表 */
    void getDimensionMusics(Dimension d) {
        mDimension = d;

        if(mDimension.sid == 20){
            new GetCltSongsTask(handler, user.id).execute();
        }else
            new DimensionFMTask(handler, mDimension.dim, mDimension.sid).execute();
    }

    /** 得到音乐列表 */
    void setMusicList(ArrayList<SongInfo> list) {
        if (list == null || list.isEmpty()) {
            sendBroadCast(MusicServiceDefine.ALBUN_NULL);
            return;
        }

        songs.clear();
        songs.addAll(list);

        if (checkSong(0)) {
            sendBroadCast(MusicServiceDefine.ALBUN_NULL);
        }

    }

    /** 开始播放音乐 */
    void startMusic(String url) {
        if (player != null)
            player.setMusicInfo(url);
    }

    /** 从通知栏打开播放界面 */
    void fromNotice() {
        isMusicActivityOpen = true;

        if (mSong != null && !TextUtils.isEmpty(mSong.url)) {
            sendBroadCast(MusicServiceDefine.MUSIC_CHANGE, mDimension, mSong);
        }

        if (totalTime > 0 && isBackPlaying()) {
            sendBroadCast(MusicServiceDefine.MUSIC_PREPARE, totalTime);
        }

    }

    private void sendBroadCast(int what) {
        if (isMusicActivityOpen) {
            Intent intent = new Intent(MessageWhat.PLAY_BROADCAST_ACTION_NAME);
            intent.putExtra(MusicServiceDefine.PLAY_WHAT, what);
            sendBroadcast(intent);
        }
    }

    private void sendBroadCast(int what, Dimension mDimension, SongInfo info) {
        if (isMusicActivityOpen) {
            Intent intent = new Intent(MessageWhat.PLAY_BROADCAST_ACTION_NAME);
            intent.putExtra(MusicServiceDefine.PLAY_WHAT, what);
            intent.putExtra(MusicServiceDefine.PLAY_PARAM1, mDimension.index);
            intent.putExtra(MusicServiceDefine.PLAY_INFO, info);
            sendBroadcast(intent);
        }
    }

    private void sendBroadCast(int what, int time) {
        if (isMusicActivityOpen) {
            Intent intent = new Intent(MessageWhat.PLAY_BROADCAST_ACTION_NAME);
            intent.putExtra(MusicServiceDefine.PLAY_WHAT, what);
            intent.putExtra(MusicServiceDefine.PLAY_PARAM1, time);
            sendBroadcast(intent);
        }
    }

    void toggleMusic() {
        if (playState == MusicServiceDefine.PLAYER_PLAYING) {
            player.Pause();
            playState = MusicServiceDefine.PLAYER_PAUSE;
            sendBroadCast(MusicServiceDefine.MUSIC_PAUSE);
        } else if (playState == MusicServiceDefine.PLAYER_PAUSE) {
            player.Resume();
            playState = MusicServiceDefine.PLAYER_PLAYING;
            sendBroadCast(MusicServiceDefine.MUSIC_RESUME);
        }
    }

    void stopMusic() {
        if (player != null && playState > MusicServiceDefine.PLAYER_IDLE) {
            player.Stop();
            playState = MusicServiceDefine.PLAYER_IDLE;
        }// 停止
        audioController.setAudioFinish(AudioState.LISTEN_MUSIC);
        sendBroadCast(MusicServiceDefine.MUSIC_STOP);
    }

    void nextMusic() {
        if (player != null && playState > MusicServiceDefine.PLAYER_IDLE) {
            player.Stop();
        }// 停止

        if (++playIndex >= songs.size() - 1) {
            playIndex = 0;
        }
        if (checkSong(playIndex) && checkSong(0)) {
            sendBroadCast(MusicServiceDefine.ALBUN_NULL);
        }

    }

    boolean checkSong(int start) {
        boolean isEnd = true;
        for (int i = start; i < songs.size(); i++) {
            mSong = songs.get(i);

            if (mSong != null && !TextUtils.isEmpty(mSong.url)) {
                playIndex = i;
                sendBroadCast(MusicServiceDefine.MUSIC_CHANGE, mDimension,
                        mSong);
                startMusic(mSong.url);
                isEnd = false;
                break;
            }
            songs.remove(i);
            i--;
        }

        return isEnd;
    }

    public static boolean isBackPlaying() {
        return playState == MusicServiceDefine.PLAYER_PLAYING
                || playState == MusicServiceDefine.PLAYER_PAUSE;
    }

    private MusicServiceHandler handler = new MusicServiceHandler(this);

    public static class MusicServiceHandler extends Handler{
        private WeakReference<MusicService> musicServiceWeakReference;
        public MusicServiceHandler(MusicService musicService){
            musicServiceWeakReference = new WeakReference<MusicService>(musicService);
        }

        @Override
        public void handleMessage(Message msg) {
            MusicService musicService = musicServiceWeakReference.get();
            super.handleMessage(msg);
            try {
                int nCommand = msg.what;
                switch (nCommand) {
                    // 获取专辑数据
                    case ApiDefine.GET_DEMENSION_SUCCESS:
                        if (msg.obj != null) {
                            ArrayList<SongInfo> list = (ArrayList<SongInfo>) msg.obj;
                            if (list != null && !list.isEmpty()) {
                                musicService.setMusicList(list);
                            }
                        }
                        break;

                    // 播放器准备完成，开始播放音乐
                    case MessageWhat.PLAYER_PREPARED:
                        if (musicService.player != null) {
                            musicService.player.start(true);
                            playState = MusicServiceDefine.PLAYER_PLAYING;
                            musicService.totalTime = msg.arg1;
                            musicService.sendBroadCast(MusicServiceDefine.MUSIC_PREPARE,
                                    musicService.totalTime);
                            musicService.audioController.setAudioStart(AudioState.LISTEN_MUSIC,
                                    musicService);
                        }
                        break;

                    case MessageWhat.PLAYING_TIME:
                        if (musicService.player != null) {
                            int curTime = msg.arg1;
                            if (curTime > 0) {
                                musicService.sendBroadCast(MusicServiceDefine.MUSIC_PLAYING,
                                        curTime);
                            }
                        }
                        break;

                    case MessageWhat.WM_PLAYER_EOF:
                        musicService.nextMusic();
                        break;

                    // 获取红星歌单专辑数据
                    case ApiDefine.GET_CLTSONGS_SUCCESS:
                        if (msg.obj != null) {
                            ArrayList<SongInfo> list = (ArrayList<SongInfo>) msg.obj;
                            if (list != null && !list.isEmpty()) {
                                musicService.setMusicList(list);
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
