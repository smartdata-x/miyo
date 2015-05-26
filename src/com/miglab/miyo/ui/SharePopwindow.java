package com.miglab.miyo.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.Constants;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.third.share.Share;
import com.miglab.miyo.third.share.Share2QQ;
import com.miglab.miyo.util.DisplayUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by tudou on 2015/5/25.
 */
public class SharePopwindow extends PopupWindow implements View.OnClickListener{
    private View parent;
    private Button btn_qq,btn_weibo,btn_weixin;
    private LinearLayout bottom;
    private MainActivity ac;
    public SharePopwindow(MainActivity ac) {
        this.ac = ac;
        LayoutInflater inflater = (LayoutInflater) ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parent = inflater.inflate(R.layout.ppw_share,null);

        bottom = (LinearLayout) parent.findViewById(R.id.bottom);
        btn_qq = (Button) parent.findViewById(R.id.btn_qq_share);
        btn_weibo = (Button) parent.findViewById(R.id.btn_weibo_share);
        btn_weixin = (Button) parent.findViewById(R.id.btn_weixin_share);
        parent.setOnClickListener(this);
        DisplayUtil.setListener(parent, this);
        parent.startAnimation(AnimationUtils.loadAnimation(ac, R.anim.fade_in));
        bottom.startAnimation(AnimationUtils.loadAnimation(ac, R.anim.push_bottom_in));

        setContentView(parent);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0xb0000000));
        setOutsideTouchable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.parent:
                dismiss();
                break;
            case R.id.btn_weixin_share:
                dismiss();
                break;
            case R.id.btn_qq_share:
                qqShare();
                break;
            case R.id.btn_weibo_share:
                break;
        }
    }

    private void qqShare() {
        SongInfo songInfo = ac.getSongInfo();
        Share share = new Share2QQ(ac);
        share.title = songInfo.name;
        share.summary = songInfo.artist;
        share.url = songInfo.url;
        share.imgURL = songInfo.pic;
        share.appName =  ac.getString(R.string.app_name);
        share.share(ac);
//        Tencent tencent = Tencent.createInstance(Constants.QQ_APP_ID, ac);
//        Bundle params = new Bundle();
//
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, songInfo.name);
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  songInfo.artist);
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  songInfo.url);
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, songInfo.pic);
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ac.getString(R.string.app_name));
//        tencent.shareToQQ(ac, params, qqListener);
    }

    private void qqZoneShare() {
        SongInfo songInfo = ac.getSongInfo();
//        Tencent tencent = Tencent.createInstance(Constants.QQ_APP_ID, ac);
//        Bundle params = new Bundle();
//        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
//        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
//        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
//        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "跳转URL");//必填
//        ArrayList<String> list = new ArrayList<String>();
//        list.add(songInfo.url);
//        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
//        tencent.shareToQzone(ac, params, qqListener);
    }

    private void weixinShare() {

    }

    private void friendsShare() {

    }

    private IUiListener qqListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };
}
