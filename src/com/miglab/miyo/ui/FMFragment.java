package com.miglab.miyo.ui;

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
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class FMFragment extends BaseFragment {
    private List<MusicType> list = new ArrayList<MusicType>();

    private ListView listView;
    private TextView titleText;
    private MusicInterface musicInterface;
    @Override
    protected void setLayout() {
        resourceID = R.layout.fr_my_fm;
    }

    @Override
    protected void init() {
        initViews();
        initData();
        initListener();
    }

    public void setMusicInterface(MusicInterface musicInterface) {
        this.musicInterface = musicInterface;
    }

    private void initViews() {
        listView = (ListView) vRoot.findViewById(R.id.listView);
        titleText = (TextView) vRoot.findViewById(R.id.type_title);
    }

    private void initData() {
        new GetMusicType(handler, MiyoApplication.getInstance().getLocationUtil().getLatitude(),
                MiyoApplication.getInstance().getLocationUtil().getLongitude()).execute();
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicType musicType = list.get(position);
                new DimensionFMTask(handler,musicType.getDim(),musicType.getId()).execute();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < list.size() && visibleItemCount > 0) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleText
                            .getLayoutParams();
                    View itemView = view.getChildAt(1);
                    int top = 0;
                    if (list.get(firstVisibleItem + 1).getIsTitle()) {
                        top = itemView.getTop() - itemView.getHeight();
                    }
                  if(!list.get(firstVisibleItem).getIsTitle())
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
            case ApiDefine.GET_DEMENSION_SUCCESS:
                if (msg.obj != null) {
                    ArrayList<SongInfo> list = (ArrayList<SongInfo>) msg.obj;
                    if (list != null && !list.isEmpty()) {
                        musicInterface.updateMusicList(list);
                        break;
                    }
                }
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
        MusicTypeAdapter adapter = new MusicTypeAdapter(ac,list);
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

    public interface MusicInterface {
        public void updateMusicList(List<SongInfo> list);
    }


}
