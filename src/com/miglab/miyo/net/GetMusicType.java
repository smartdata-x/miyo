package com.miglab.miyo.net;

import android.os.Handler;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.constant.ApiDefine;
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
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
    }

    @Override
    protected String request() throws Exception {
        String url = ApiDefine.DOMAIN + ApiDefine.MUSIC_TYPE;
        String params = MiyoUser.getApiBasicParams() + "&latitude=" + latitude + "&longitude=" + longitude;
        return ApiRequest.getRequest(url + params);
    }

    @Override
    protected boolean preResult(JSONObject json) {
        int status = json.optInt("status");
        if(status == 1) {
            JSONObject jresult = json.optJSONObject("result");
            handler.sendMessage(handler.obtainMessage(ApiDefine.GET_MUSIC_TYPE_SUCCESS, jresult));
        }
        return true;
    }
}
