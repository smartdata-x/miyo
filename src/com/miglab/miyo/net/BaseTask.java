package com.miglab.miyo.net;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.util.NetUtil;
import org.json.JSONException;
import org.json.JSONObject;

//todo
public class BaseTask extends AsyncTask<Void, Void, Void> {

	protected String TAG = "BaseTask";
	protected Handler handler;
	protected boolean canReadCache = false;

	protected void init(Handler handler) {
		this.handler = handler;
		TAG = this.getClass().getSimpleName();
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (handler == null)
			return null;

		if (paramsFail()) {
			handler.sendEmptyMessage(ApiDefine.ERROR_PARAMS);
			return null;
		}

		try {
			if (canReadCache) {
				readCache();
			}

			if (MiyoApplication.frontActivity != null
					&& !NetUtil.isNetConnection(MiyoApplication.frontActivity)) {
				handler.sendEmptyMessage(ApiDefine.NET_OFF);
				return null;
			}

			String request = request();
			if (TextUtils.isEmpty(request)) { // 超时
				handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
				return null;
			}

			int code = 0;
			String errorMsg = ApiDefine.ERRORMSG_UNKNOWN;
			try {
				JSONObject json = new JSONObject(request);
				if (json != null) {
					code = json.optInt("status");
					if (code == 1) {
//						if(MusicActivity.collectsong == 1){
//							handler.sendMessage(handler.obtainMessage(ApiDefine.GET_COLLECT_SONG_SUCCESS, 0, code));
//							MusicActivity.collectsong = 0;
//						}
//						if(MusicActivity.deletecollectsong == 1){
//							handler.sendMessage(handler.obtainMessage(ApiDefine.GET_DELECT_COLLECT_SONG_SUCCESS, 1, code));
//							MusicActivity.deletecollectsong = 0;
//						}
//						if(MusicActivity.hatesong == 1){
//							handler.sendMessage(handler.obtainMessage(ApiDefine.GET_HATE_SONG_SUCCESS, 2, code));
//							MusicActivity.hatesong = 0;
//						}
						JSONObject jresult = json.optJSONObject("result");
						if (jresult != null && parseResult(jresult)) {
							return null;
						}
					}else if(code != 1){
//						if(MusicActivity.collectsong == 1 || MusicActivity.deletecollectsong == 1
//								|| MusicActivity.hatesong == 1){
//							MusicActivity.collectsong = 0;
//							MusicActivity.deletecollectsong = 0;
//							MusicActivity.hatesong = 0;
//						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			handler.sendMessage(handler.obtainMessage(
					code == 0 ? ApiDefine.ERROR_UNKNOWN : code, errorMsg));

		} catch (Exception e1) {
			handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
			e1.printStackTrace();
		}

		return null;
	}

	protected String request() throws Exception {
		return "";
	}

	protected boolean paramsFail() {
		return false;
	}

	protected boolean parseResult(JSONObject jresult) {
		return false;
	}

	protected void readCache() {
	}
}
