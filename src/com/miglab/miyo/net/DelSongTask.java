package com.miglab.miyo.net;

import android.os.Handler;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.SongInfo;
import org.json.JSONObject;

/**
 * Created by tudou on 2015/5/17.
 */
public class DelSongTask extends BaseTask{
    private SongInfo song;//¸èÇúid
    public DelSongTask( Handler handler, SongInfo song) {
        this.song = song;
        this.handler = handler;
    }

    @Override
    protected String request() throws Exception {
        String url = ApiDefine.DOMAIN + ApiDefine.MUSIC_HATE_SONG;
        String params = MiyoUser.getApiBasicParams() + "&songid=" + song.id;
        return ApiRequest.getRequest(url + params);
    }

    @Override
    protected boolean preResult(JSONObject jresult) {
        int status = jresult.optInt("status");
        handler.sendMessage(handler.obtainMessage(ApiDefine.GET_HATE_SONG_SUCCESS, status,status,song));
        return true;
    }
}
