package com.miglab.miyo.net;

import android.os.Handler;
import com.miglab.miyo.MyUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.ui.BaseFragment;
import org.json.JSONObject;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/18.
 */
public class DelCollectSongTask extends BaseTask{
    private SongInfo songInfo;

    public DelCollectSongTask(Handler handler, SongInfo songInfo) {
        this.handler = handler;
        this.songInfo = songInfo;
    }

    @Override
    protected String request() throws Exception {
        String url = ApiDefine.DOMAIN + ApiDefine.MUSIC_DELECT_COLLECT_SONG;
        String params = MyUser.getApiBasicParams() + "&songid=" + songInfo.id;
        return ApiRequest.getRequest(url + params);
    }

    @Override
    protected boolean preResult(JSONObject jresult) {
        int status = jresult.optInt("status");
        handler.sendMessage(handler.obtainMessage(ApiDefine.GET_DELECT_COLLECT_SONG_SUCCESS, status,status,songInfo));
        return true;
    }
}
