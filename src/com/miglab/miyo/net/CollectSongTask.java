package com.miglab.miyo.net;

import android.os.Handler;
import com.miglab.miyo.MyUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.entity.SongInfo;
import org.json.JSONObject;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/18.
 */
public class CollectSongTask extends BaseTask {
    private SongInfo songInfo;
    private MusicType musicType;

    public CollectSongTask(Handler h, SongInfo song, MusicType musicType){
        this.handler = h;
        this.songInfo = song;
        this.musicType = musicType;
    }

    @Override
    protected String request() throws Exception {
        String url = ApiDefine.DOMAIN + ApiDefine.MUSIC_COLLECT_SONG;
        String params = MyUser.getApiBasicParams() + "&songid=" + songInfo.id + "&sid=" + musicType.getId()
                + "&dimension=" + musicType.getDim();
        return ApiRequest.getRequest(url + params);
    }

    @Override
    protected boolean preResult(JSONObject jresult) {
        int status = jresult.optInt("status");
        handler.sendMessage(handler.obtainMessage(ApiDefine.GET_COLLECT_SONG_SUCCESS, status,status,songInfo));
        return true;
    }
}
