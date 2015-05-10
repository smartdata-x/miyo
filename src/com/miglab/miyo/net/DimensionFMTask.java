package com.miglab.miyo.net;

import android.os.Handler;

import com.miglab.miyo.MyUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.SongInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class DimensionFMTask extends BaseTask {
	String dimension;
	int mId;

	public DimensionFMTask(Handler tHandler, String dimension, int mid) {
		this.init(tHandler);
		this.dimension = dimension;
		this.mId = mid;
	}

	@Override
	protected boolean paramsFail() {
		return dimension == null || mId <= 0;

	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.FOUND_FM;
		String params = MyUser.getApiBasicParams() + "&dimension=" + dimension
				+ "&sid=" + mId;

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

		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_DEMENSION_SUCCESS, list));

		return false;

	}

}
