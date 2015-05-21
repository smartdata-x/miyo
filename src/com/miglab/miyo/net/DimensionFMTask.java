package com.miglab.miyo.net;

import android.os.Handler;

import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.entity.SongInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;


public class DimensionFMTask extends BaseTask {
	private MusicType musicType;
	private Integer getType;  //0---ÇÐ»»¸èµ¥ 1--×·¼Ó¸èµ¥

	public DimensionFMTask(Handler tHandler, MusicType musicType, Integer type) {
		this.init(tHandler);
		this.musicType = musicType;
		this.getType = type;
	}

	@Override
	protected boolean paramsFail() {
		return musicType.getDim() == null || musicType.getId() <= 0;

	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.FOUND_FM;
		String params = MiyoUser.getApiBasicParams() + "&dimension=" + musicType.getDim()
				+ "&sid=" + musicType.getId();

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
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("songList",list);
		map.put("musicType", musicType);
		map.put("getType", getType);
		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_MUSIC_LIST_SUCCESS, map));

		return false;

	}

}
