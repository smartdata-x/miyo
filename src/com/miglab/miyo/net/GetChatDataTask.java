package com.miglab.miyo.net;

import android.os.Handler;
import android.widget.Toast;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.BaseEntity;
import com.miglab.miyo.entity.SimpleMsgInfo;
import com.miglab.miyo.entity.MusicType;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/19.
 */
public class GetChatDataTask extends BaseTask{
    private MusicType musicType;
    public GetChatDataTask(Handler h, MusicType musicType){
        this.handler = h;
        this.musicType = musicType;
    }

    @Override
    protected String request() throws Exception {
        String url = ApiDefine.DOMAIN + ApiDefine.CHAT;
        String params = MiyoUser.getApiBasicParams()
                + "&ttype=" + musicType.getDim()
                + "&tid=" + musicType.getId()
                + "&msgid=0&platform=10000";
        return ApiRequest.getRequest(url + params);
    }

    @Override
    protected boolean preResult(JSONObject json) {
        super.preResult(json);
        List<SimpleMsgInfo> list = (List<SimpleMsgInfo>) BaseEntity.initWithsJsonObjects(SimpleMsgInfo.class, jresult.optJSONArray("barrage"));
        handler.sendMessage(handler.obtainMessage(ApiDefine.GET_CHAT_SUCCESS, list));
        return true;
    }
}
