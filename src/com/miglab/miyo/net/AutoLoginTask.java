package com.miglab.miyo.net;

import android.os.Handler;
import android.text.TextUtils;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.ui.BaseActivity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tudou on 2015/5/23.
 */
public class AutoLoginTask extends BaseTask{
    public AutoLoginTask(Handler handler) {
        this.init(handler);
    }

    @Override
    protected String request() throws UnsupportedEncodingException {
        String url = ApiDefine.DOMAIN + ApiDefine.AUTO_LOGIN;
        MiyoUser user = MiyoUser.getInstance();
        Map<String, String> params = new HashMap<String, String>();
        params.put("plt","" + user.getPlat());
        params.put("machine", "" + user.getMachine());
        params.put("token", user.getToken());
        params.put("uid","" + user.getUserId());
        return ApiRequest.postRequest(url, params);
    }

    @Override
    protected boolean preResult(JSONObject json) {
        super.preResult(json);
        MiyoUser user = MiyoUser.getInstance();
        user.loginSet(jresult.optJSONObject("userinfo"));
        handler.sendEmptyMessage(ApiDefine.GET_SUCCESS);
        return true;
    }
}
