package com.miglab.miyo.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;

import com.miglab.miyo.entity.ChatMsgInfo;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.net.GetWeatherTask;

import com.miglab.miyo.net.GetChatDataTask;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MusicFragment extends PlayBaseFragment{

    private ImageView icon_player;
    private ImageView icon_weather,cd_palyer;
    private RelativeLayout ry_cd;
    private TextView tv_songName;
    private TextView tv_songType;
    private TextView tv_address;
    private TextView tv_temperature;
    private TextView tv_chat;

    private List<ChatMsgInfo> list = new ArrayList<ChatMsgInfo>();
    private Animation animation;

    @Override
    protected void setLayout() {
        resourceID = R.layout.fr_music;
    }

    @Override
    protected void init() {
        super.init();
        initWeather();
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0
                ,Animation.RELATIVE_TO_SELF,0
                ,Animation.RELATIVE_TO_SELF,0
                ,Animation.RELATIVE_TO_SELF,-1.0f);
        animation.setDuration(2000);
        animation.setRepeatMode(2);
        animation.setFillAfter(true);
    }

    @Override
    protected void initViews() {
        super.initViews();
        tv_songName = (TextView) vRoot.findViewById(R.id.music_name);
        tv_songType = (TextView) vRoot.findViewById(R.id.music_type);
        ry_cd = (RelativeLayout) vRoot.findViewById(R.id.music_player);
        icon_player = (ImageView) vRoot.findViewById(R.id.icon_player);
        icon_weather = (ImageView) vRoot.findViewById(R.id.weather_icon);
        tv_address = (TextView) vRoot.findViewById(R.id.address);
        tv_temperature = (TextView) vRoot.findViewById(R.id.temperature);
        tv_chat = (TextView) vRoot.findViewById(R.id.chat);
        cd_palyer = (ImageView) vRoot.findViewById(R.id.cd_palyer);
        cd_palyer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharePopwindow p = new SharePopwindow(mainActivity);
                p.showAtLocation(vRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                return true;
            }
        });
    }

    private void initWeather() {
        new GetWeatherTask(handler,
                MiyoApplication.getInstance().getLocationUtil().getLatitude(),
                MiyoApplication.getInstance().getLocationUtil().getLongitude()).execute();
    }

    private void displayWeather(String weather, String temp, String address) {
        tv_temperature.setText(temp);
        tv_address.setText(address);
        int resID = getResources().getIdentifier(weather, "drawable", MiyoApplication.getInstance().getPackageName());
        icon_weather.setImageResource(resID);
    }

    @Override
    protected void cdStartAnimation() {
        super.cdStartAnimation();
        icon_player.setVisibility(View.GONE);
    }

    public void updateMusicType(MusicType musicType) {
        tv_songType.setText(musicType.getName());
        new GetChatDataTask(handler,musicType).execute();
    }

    public void updateMusicName(String name) {
        tv_songName.setText(name);
    }

    @Override
    public void updateMusicState(boolean bPause) {
        super.updateMusicState(bPause);
        if (bPause) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                anim.pause();
            } else {
                anim.cancel();
            }
            icon_player.setVisibility(View.VISIBLE);
        } else {
            icon_player.setVisibility(View.GONE);
        }

    }



    @Override
    protected void doHandler(Message msg) {
        super.doHandler(msg);
        switch (msg.what) {
            case ApiDefine.GET_WEATHER_SUCCESS:
                JSONObject jResult = (JSONObject) msg.obj;
                String weather = jResult.optString("weather");
                String temp = jResult.optString("temp");
                String address = jResult.optString("city");
                weather.replace("-","_");
                displayWeather(weather.toLowerCase(), temp, address);
                break;
            case ApiDefine.GET_CHAT_SUCCESS:
                if(list != null && list.size() > 0) {
                    list.clear();
                }
                list.addAll((List < ChatMsgInfo >) msg.obj);
                StringBuffer chat = new StringBuffer();
                for(ChatMsgInfo chatMsgInfo: list){
                    chat.append(chatMsgInfo.getNickname());
                    chat.append("： ");
                    chat.append(chatMsgInfo.getMsg());
                    chat.append("        ");
                }
                tv_chat.setText(chat);
                break;
        }
    }

    public Bitmap getCurMusicBitmap() {
        return ((BitmapDrawable)iv_cd.getDrawable()).getBitmap();
    }

}
