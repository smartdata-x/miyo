package com.miglab.miyo.net;

import android.os.Handler;

import com.miglab.miyo.MyUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.ui.MusicFragment.Dimension;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class DimensionFMTask extends BaseTask {
	private Dimension dimension;

	public DimensionFMTask(Handler tHandler, Dimension dimension) {
		this.init(tHandler);
		this.dimension = dimension;
	}

	@Override
	protected boolean paramsFail() {
		return dimension == null || dimension.sid <= 0;

	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.FOUND_FM;
		String params = MyUser.getApiBasicParams() + "&dimension=" + dimension.dim
				+ "&sid=" + dimension.sid;

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
