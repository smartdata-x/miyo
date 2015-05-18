package com.miglab.miyo.ui;

import android.os.Message;
import android.widget.ListView;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.MusicTypeAdapter;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.BaseEntity;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.net.GetMusicType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class FMFragment extends BaseFragment {
    private List<MusicType> list = new ArrayList<>();

    private ListView listView;
    @Override
    protected void setLayout() {
        resourceID = R.layout.fr_my_fm;
    }

    @Override
    protected void init() {
        initViews();
        initData();
    }

    private void initViews() {
        listView = (ListView) vRoot.findViewById(R.id.listView);
    }

    private void initData() {
        new GetMusicType(handler, MiyoApplication.getInstance().getLocationUtil().getLatitude(),
                MiyoApplication.getInstance().getLocationUtil().getLongitude()).execute();
    }

    @Override
    protected void doHandler(Message msg) {
        super.doHandler(msg);
        switch (msg.what){
            case ApiDefine.GET_MUSIC_TYPE_SUCCESS:
                JSONObject jResult = (JSONObject) msg.obj;
                initMusicTypeList(jResult);
                break;
        }
    }

    private void initMusicTypeList(JSONObject jResult) {
        JSONArray chlArray = jResult.optJSONArray("chl");
        JSONArray mmArray = jResult.optJSONArray("mm");
        JSONArray msArray = jResult.optJSONArray("ms");
        MusicType musicChl = new MusicType("频道",true);
        MusicType musicMm = new MusicType("心情",true);
        MusicType musicMs = new MusicType("玩笑",true);
        list.add(musicChl);
        list.addAll(getMusicList(chlArray, "chl"));
        list.add(musicMm);
        list.addAll(getMusicList(mmArray, "mm"));
        list.add(musicMs);
        list.addAll(getMusicList(msArray, "ms"));
        MusicTypeAdapter adapter = new MusicTypeAdapter(ac,list);
    }

    private List<MusicType> getMusicList(JSONArray jArray,String channel){
        List<MusicType> list = new ArrayList<MusicType>();
        for(int i = 0; i < jArray.length(); i++){
            JSONObject jObj = (JSONObject) jArray.opt(i);
            MusicType musicType = new MusicType(jObj.optString("name"),false);
            musicType.setDim(channel);
            musicType.setId(jObj.optInt("id"));
            list.add(musicType);
        }
        return list;
    }
}
