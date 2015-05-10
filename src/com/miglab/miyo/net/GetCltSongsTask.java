package com.miglab.miyo.net;

import android.os.Handler;

import com.miglab.miyo.MyUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.SongInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author 赵龙权
 * @version 创建时间：2015-5-2 下午11:08:11
 * 类说明	红星歌单
 */
public class GetCltSongsTask extends BaseTask {

	String mTid;//用户id

	public GetCltSongsTask(Handler tHandler, String mid) {
		this.init(tHandler);
		this.mTid = mid;
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.GET_CLTSONGS;
		String params = MyUser.getApiBasicParams() + "&mTid=" + mTid;
		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		ArrayList<SongInfo> list = new ArrayList<SongInfo>();
		JSONArray array = jresult.optJSONArray("song");
		if(array != null && array.length() > 0){
			for (int j = 0; j < array.length(); j++) {
				list.add(new SongInfo(array.optJSONObject(j)));
			}
		}
		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_CLTSONGS_SUCCESS, list));
		return false;

	}

	
}
