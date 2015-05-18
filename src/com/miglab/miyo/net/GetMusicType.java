package com.miglab.miyo.net;

import android.os.Handler;
import com.miglab.miyo.MyUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.ui.BaseFragment;
import org.json.JSONObject;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/18.
 */
public class GetMusicType extends BaseTask{
    private String latitude;
    private String longitude;
    public GetMusicType(Handler handler,double latitude,double longitude) {
        this.handler = handler;
    }

    @Override
    protected String request() throws Exception {
        String url = ApiDefine.DOMAIN + ApiDefine.MUSIC_TYPE;
        String params = MyUser.getApiBasicParams() + "&latitude=" + latitude + "&longitude=" + longitude;
        return ApiRequest.getRequest(url + params);
    }

    @Override
    protected boolean parseResult(JSONObject jresult) {
        handler.sendMessage(handler.obtainMessage(ApiDefine.GET_MUSIC_TYPE_SUCCESS, jresult));
        return true;
    }
}
