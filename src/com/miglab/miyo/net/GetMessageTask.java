package com.miglab.miyo.net;

import android.os.Handler;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.PushMessageInfo;
import com.miglab.miyo.util.LocationUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/12.
 */
public class GetMessageTask extends BaseTask{
    public GetMessageTask(Handler handler) {
        this.init(handler);
    }

    @Override
    protected String request() throws Exception {
        String url = ApiDefine.DOMAIN + ApiDefine.GET_MESSAGE;
        LocationUtil location = MiyoApplication.getInstance().getLocationUtil();
        String param = MiyoUser.getApiBasicParams() + "&latitude=" + location.getLatitude() + "&longitude=" + location.getLongitude();
        return ApiRequest.getRequest(url + param);
    }

    @Override
    protected boolean preResult(JSONObject json) {
        List<PushMessageInfo> list = new ArrayList<PushMessageInfo>();
        if(json.optInt("status") ==1) {
            JSONArray jarray = json.optJSONArray("result");
            for(int i=0; i<jarray.length(); i++){
                PushMessageInfo info = new PushMessageInfo(jarray.optJSONObject(i));
                list.add(info);
            }
            handler.sendMessage(handler.obtainMessage(ApiDefine.GET_MESSAGE_SUCCESS,list));
        }

        return true;
    }


}
