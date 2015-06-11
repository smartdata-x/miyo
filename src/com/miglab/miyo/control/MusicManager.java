package com.miglab.miyo.control;

import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.entity.SongInfo;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/11.
 */
public class MusicManager {
    private static MusicManager instance;

    private SongInfo songInfo;
    private MusicType musicType;

    private MusicService musicService;
    private MusicManager() {

    }
    public static MusicManager getInstance() {
        if(instance == null) {
            synchronized (MusicManager.class) {
                if(instance == null)
                    instance = new MusicManager();
            }
        }
        return instance;
    }

    public SongInfo getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = songInfo;
    }

    public MusicType getMusicType() {
        return musicType;
    }

    public void setMusicType(MusicType musicType) {
        this.musicType = musicType;
    }

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }


    public MusicService getMusicService() {
        return musicService;
    }
}
