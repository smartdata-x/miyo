package com.miglab.miyo.net;

import android.os.Handler;
import com.miglab.miyo.MyUser;
import com.miglab.miyo.constant.ApiDefine;
import org.json.JSONObject;


/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/18.
 */
public class GetWeatherTask extends BaseTask {
    private String latitude;
    private String longitude;
    public GetWeatherTask(Handler h,double latitude,double longitude) {
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
        this.handler = h;
    }

    @Override
    protected String request() throws Exception {
        String url = ApiDefine.DOMAIN + ApiDefine.WEATHER;
        String params = MyUser.getApiBasicParams() + "&latitude=" + latitude + "&longitude=" + longitude;
        return ApiRequest.getRequest(url + params);
    }

    @Override
    protected boolean parseResult(JSONObject jresult) {
        handler.sendMessage(handler.obtainMessage(ApiDefine.GET_WEATHER_SUCCESS, jresult));
        return true;
    }
}
