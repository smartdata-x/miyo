package com.miglab.miyo.control;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.text.TextUtils;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.constant.MessageWhat;
import com.miglab.miyo.constant.MusicServiceDefine;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.entity.SongInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.control.AudioController.AudioState;
import com.miglab.miyo.net.DimensionFMTask;
import com.miglab.miyo.net.RecordCurMusicTask;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/9.
 */
public class MusicService extends Service implements AudioControllerListener{

    private static final int LIKE_SECONDS = 10000;
    // ========播放音乐======//
    ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
    Player player;
    static int playState;
    int playIndex, totalTime;
    MusicType musicType;
    SongInfo mSong;
    boolean isMusicActivityOpen = true;
    private AudioController audioController;
    private final LocalService localService = new LocalService();

    public int getTotalTime() {
        return totalTime;
    }


    public class LocalService extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                doMusic(bundle);
            }
        }
        return localService;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (player == null)
            player = new Player(handler);

        audioController = AudioController.get();
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
        /**todo
         * 初始化歌单
         */
        musicType = new MusicType();
        SharedPreferences preferences=getSharedPreferences("musictype", Context.MODE_PRIVATE);
        musicType.setDim(preferences.getString("dim", "chl"));
        musicType.setName(preferences.getString("name", "华语流行"));
        musicType.setId(preferences.getInt("sid", 1));

        getDimensionMusics();
    }

    /** 获取音乐列表 */
    private void getDimensionMusics() {
       new DimensionFMTask(handler, musicType, 0).execute();
    }

    /** 得到音乐列表 */
    void setMusicList(ArrayList<SongInfo> list) {
        if (list == null || list.isEmpty()) {
            sendBroadCast(MusicServiceDefine.ALBUN_NULL);
            return;
        }

        if(songs.size() > 20){
            for(;playIndex > 2; playIndex--){
                songs.remove(0);
            }
        }
        songs.addAll(list);
        if(playState == MusicServiceDefine.PLAYER_PLAYING)
            return;
        if (checkSong(0)) {
            sendBroadCast(MusicServiceDefine.ALBUN_NULL);
        }

    }

    public void delLocateMusic(SongInfo info) {
        if (songs == null || songs.isEmpty()) {
            return;
        }else {
            synchronized (songs) {
                for ( int i = 0; i < songs.size(); i++) {
                    SongInfo value = songs.get(i);
                    if (value.id == info.id) {
                        songs.remove(value);
                        break;
                    }
                }
            }
        }
    }

    public void getMusicListByType(MusicType musicType) {
        new DimensionFMTask(handler, musicType, 0).execute();
    }

    //更新歌单结果
    public void clearMusicList(List<SongInfo> list) {
        stopMusic();
        songs.clear();
        songs.addAll(list);
        sendBroadCast(MusicServiceDefine.UPDATE_LIST_SUCCESS);
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
            sendBroadCast(MusicServiceDefine.MUSIC_CHANGE, musicType, mSong);
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

    private void sendBroadCast(int what, MusicType musicType, SongInfo info) {
        if (isMusicActivityOpen) {
            Intent intent = new Intent(MessageWhat.PLAY_BROADCAST_ACTION_NAME);
            intent.putExtra(MusicServiceDefine.PLAY_WHAT, what);
            intent.putExtra(MusicServiceDefine.PLAY_PARAM1, musicType);
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

    public void toggleMusic() {
        if (playState == MusicServiceDefine.PLAYER_PLAYING) {
            player.Pause();
            playState = MusicServiceDefine.PLAYER_PAUSE;
        } else if (playState == MusicServiceDefine.PLAYER_PAUSE) {
            player.Resume();
            playState = MusicServiceDefine.PLAYER_PLAYING;
        }
    }

    public void stopMusic() {
        if (player != null && playState > MusicServiceDefine.PLAYER_IDLE) {
            player.Stop();
            playState = MusicServiceDefine.PLAYER_IDLE;
        }// 停止
        audioController.setAudioFinish(AudioState.LISTEN_MUSIC);
    }

    public boolean bePause() {
        return playState == MusicServiceDefine.PLAYER_PAUSE;
    }

    public void nextMusic() {
        if (player != null && playState > MusicServiceDefine.PLAYER_IDLE) {
            player.Stop();
            sendBroadCast(MusicServiceDefine.STOP_TO_NEXT);
        }// 停止

        if(musicType.getId() != MiyoUser.getInstance().getUserId()){
            //播放到最后3首的时候
            if (++playIndex >= songs.size() - 4) {
                new DimensionFMTask(handler, musicType, 1).execute();
            }
        }else{
            ++playIndex;
        }
        if (checkSong(playIndex)/* && checkSong(0)*/) {
            sendBroadCast(MusicServiceDefine.ALBUN_NULL);
        }

    }

    boolean checkSong(int start) {
        boolean isEnd = true;
        for (int i = start; i < songs.size(); i++) {
            mSong = songs.get(i);

            if (mSong != null && !TextUtils.isEmpty(mSong.url)) {
                playIndex = i;
                sendBroadCast(MusicServiceDefine.MUSIC_CHANGE, musicType,
                        mSong);
                startMusic(mSong.url);
                handler.removeCallbacks(recordLikeRunnable);
                handler.postDelayed(recordLikeRunnable, LIKE_SECONDS);
                isEnd = false;
                break;
            }
            songs.remove(i);
            i--;
        }

        return isEnd;
    }

    //一首歌超过10秒则统计一次数据
    Runnable recordLikeRunnable = new Runnable() {
        @Override
        public void run() {
            new RecordCurMusicTask(handler,mSong,musicType).execute();
        }
    };

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
                    case ApiDefine.GET_MUSIC_LIST_SUCCESS:
                        if (msg.obj != null) {
                            Map<String,Object> map = (HashMap<String,Object>)msg.obj;
                            ArrayList<SongInfo> list = (ArrayList<SongInfo>) map.get("songList");
                            int type = (Integer) map.get("getType");
                            if (list != null && !list.isEmpty()) {
                                musicService.musicType = (MusicType) map.get("musicType");
                                if (type == 1)
                                    musicService.setMusicList(list);
                                if (type == 0)
                                    musicService.clearMusicList(list);
                            }
                            if (list == null || list.isEmpty()) {
                                //请求成功，没有数据
                                musicService.sendBroadCast(MusicServiceDefine.UPDATE_LIST_NONE);
                                return;
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
