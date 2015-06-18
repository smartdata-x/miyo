package com.miglab.miyo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;

import com.miglab.miyo.control.MusicManager;
import com.miglab.miyo.entity.SimpleMsgInfo;
import com.miglab.miyo.entity.MusicType;

import com.miglab.miyo.net.GetChatDataTask;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MusicFragment extends PlayBaseFragment{

    private ImageView icon_player;
    private ImageView cd_palyer;
    private RelativeLayout ry_cd;
    private TextView tv_songName;
    private TextView tv_songType;
    private TextView tv_address;
    private TextView tv_chat;

    private List<SimpleMsgInfo> list = new ArrayList<SimpleMsgInfo>();
    private Animation animation;

    @Override
    protected void setLayout() {
        resourceID = R.layout.fr_music;
    }

    @Override
    protected void init() {
        super.init();
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
        tv_address = (TextView) vRoot.findViewById(R.id.address);
        tv_chat = (TextView) vRoot.findViewById(R.id.chat);
        cd_palyer = (ImageView) vRoot.findViewById(R.id.cd_palyer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.share:
                SharePopwindow p = new SharePopwindow(mainActivity);
                p.showAtLocation(vRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.chat:
                startActivity(new Intent(ac,ChatActivity.class));
                break;
        }
        super.onClick(v);

    }

    @Override
    protected void cdStartAnimation() {
        super.cdStartAnimation();
        icon_player.setVisibility(View.GONE);
    }

    public void updateMusicType() {
        MusicType musicType = MusicManager.getInstance().getMusicType();
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
            icon_player.setVisibility(View.VISIBLE);
        } else {
            icon_player.setVisibility(View.GONE);
        }

    }



    @Override
    protected void doHandler(Message msg) {
        super.doHandler(msg);
        switch (msg.what) {
            case ApiDefine.GET_CHAT_SUCCESS:
                if(list != null && list.size() > 0) {
                    list.clear();
                }
                list.addAll((List <SimpleMsgInfo>) msg.obj);
                StringBuffer chat = new StringBuffer();
                for(SimpleMsgInfo simpleMsgInfo : list){
                    chat.append(simpleMsgInfo.getNickname());
                    chat.append("： ");
                    chat.append(simpleMsgInfo.getMsg());
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
