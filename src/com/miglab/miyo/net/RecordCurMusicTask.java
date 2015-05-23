package com.miglab.miyo.net;

import android.os.Handler;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.entity.SongInfo;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tudou on 2015/5/24.
 */
public class RecordCurMusicTask extends BaseTask{
    private SongInfo songInfo;
    private MusicType musicType;
    public RecordCurMusicTask(Handler handler,SongInfo songInfo,MusicType musicType) {
        this.init(handler);
        this.songInfo = songInfo;
        this.musicType = musicType;
    }
    @Override
    protected String request() throws Exception {
        String url = ApiDefine.DOMAIN + ApiDefine.RECORD_MUSIC;
        MiyoUser user = MiyoUser.getInstance();
        Map<String, String> params = new HashMap<String, String>();
        params.put("token",user.getToken());
        params.put("uid", "" + user.getUserId());
        params.put("mode", musicType.getDim());
        params.put("typeid", ""+musicType.getId());
        params.put("cursong", ""+songInfo.id);
        params.put("lastsong","0");
        params.put("name",songInfo.name);
        params.put("singer", songInfo.artist);
        params.put("state", "0");
        return ApiRequest.postRequest(url, params);
    }

    @Override
    protected boolean parseResult(JSONObject jresult) {
        return super.parseResult(jresult);
    }
}
