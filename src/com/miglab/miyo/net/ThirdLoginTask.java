package com.miglab.miyo.net;

import android.os.Handler;
import android.text.TextUtils;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.constant.ApiDefine;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhaoLongQuan
 * @version 创建时间：2015-2-8 下午2:49:26
 * 类说明	 第三方登录
 */
public class ThirdLoginTask extends BaseTask {

	public ThirdLoginTask(Handler tHandler) {
		this.init(tHandler);
	}

	@Override
	protected String request() throws UnsupportedEncodingException {
		String url = ApiDefine.DOMAIN + ApiDefine.THIRD_LOGIN;
		MiyoUser user = MiyoUser.getInstance();
		Map<String, String> params = new HashMap<String, String>();
		params.put("machine", "" + user.getMachine());
		params.put("nickname", user.getNickname());
		params.put("source", "" + user.getSource());
		params.put("session", "" + user.getSession());
		if (!TextUtils.isEmpty(user.getImei()))
			params.put("imei", user.getImei());
		params.put("sex", "" + user.getGender());
		if (!TextUtils.isEmpty(user.getBirthday()))
			params.put("birthday", "" + user.getBirthday());
		if (!TextUtils.isEmpty(user.getLocation()))
			params.put("location", user.getLocation());
		if (!TextUtils.isEmpty(user.getHeadUrl()))
			params.put("head", user.getHeadUrl());
		return ApiRequest.postRequest(url, params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
        MiyoUser user = MiyoUser.getInstance();
        user.loginSet(jresult.optJSONObject("userinfo"));
        handler.sendEmptyMessage(ApiDefine.GET_SUCCESS);
        return true;
    }

}
