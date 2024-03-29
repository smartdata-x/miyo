package com.miglab.miyo.net;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import com.miglab.miyo.constant.ApiDefine;
import org.json.JSONException;
import org.json.JSONObject;

//todo
public class BaseTask extends AsyncTask<Void, Void, Void> {
	protected Handler handler;
	protected JSONObject jresult;
	protected void init(Handler handler) {
		this.handler = handler;
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
			String request = request();
			if (TextUtils.isEmpty(request)) { // 超时
				handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
				return null;
			}
			int code = 0;
			String errorMsg = ApiDefine.ERRORMSG_UNKNOWN;
			try {
				JSONObject json = new JSONObject(request);
				preResult(json);
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

	protected boolean preResult(JSONObject json) {
		jresult = json.optJSONObject("result");
		return false;
	}

}
