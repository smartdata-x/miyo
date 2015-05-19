package com.miglab.miyo.ui;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.widget.*;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.adapter.MusicTypeAdapter;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.net.DimensionFMTask;
import com.miglab.miyo.net.GetMusicType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class FMFragment extends PlayBaseFragment {
    private List<MusicType> list = new ArrayList<MusicType>();

    private ListView listView;
    private TextView titleText;
    @Override
    protected void setLayout() {
        resourceID = R.layout.fr_my_fm;
    }

    @Override
    protected void init() {
        super.init();
        initData();
        initListener();
    }

    @Override
    protected void initViews() {
        super.initViews();
        listView = (ListView) vRoot.findViewById(R.id.listView);
        titleText = (TextView) vRoot.findViewById(R.id.type_title);
    }

    private void initData() {
        new GetMusicType(handler, MiyoApplication.getInstance().getLocationUtil().getLatitude(),
                MiyoApplication.getInstance().getLocationUtil().getLongitude()).execute();
    }

    private void initListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < list.size() && visibleItemCount > 0) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleText.getLayoutParams();
                    View itemView = view.getChildAt(1);
                    int top = 0;
                    if (list.get(firstVisibleItem + 1).getIsTitle()) {
                        top = itemView.getTop() - itemView.getHeight();
                    }
                    if (!list.get(firstVisibleItem).getIsTitle())
                        titleText.setText(list.get(firstVisibleItem).getParentResID());
                    else
                        titleText.setText(list.get(firstVisibleItem).getName());
                    params.setMargins(0, top, 0, 0);
                    titleText.setLayoutParams(params);
                }
            }
        });
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
        MusicType musicChl = new MusicType(getString(R.string.chl),true);
        MusicType musicMm = new MusicType(getString(R.string.mm),true);
        MusicType musicMs = new MusicType(getString(R.string.ms),true);
        list.add(musicChl);
        list.addAll(getMusicList(chlArray, "chl"));
        list.add(musicMm);
        list.addAll(getMusicList(mmArray, "mm"));
        list.add(musicMs);
        list.addAll(getMusicList(msArray, "ms"));
        MusicTypeAdapter adapter = new MusicTypeAdapter(ac,list,handler);
        listView.setAdapter(adapter);
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

    @SuppressWarnings("deprecation")
    public void updateBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            vRoot.setBackgroundDrawable(drawable);
        } else {
            vRoot.setBackground(drawable);
        }
    }

    public void updateCD(Drawable drawable) {
        iv_cd.setImageDrawable(drawable);
    }


}
