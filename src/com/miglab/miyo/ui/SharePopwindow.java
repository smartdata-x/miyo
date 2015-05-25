package com.miglab.miyo.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.Constants;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.util.DisplayUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudou on 2015/5/25.
 */
public class SharePopwindow extends PopupWindow implements View.OnClickListener{
    private View parent;
    private Button btn_qq,btn_weibo,btn_weixin;
    private MainActivity ac;
    public SharePopwindow(MainActivity ac) {
        this.ac = ac;
        LayoutInflater inflater = (LayoutInflater) ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parent = inflater.inflate(R.layout.ppw_share,null);
        btn_qq = (Button) parent.findViewById(R.id.btn_qq_share);
        btn_weibo = (Button) parent.findViewById(R.id.btn_weibo_share);
        btn_weixin = (Button) parent.findViewById(R.id.btn_weixin_share);
        DisplayUtil.setListener(parent,this);
        setContentView(parent);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0xb0000000));
        setOutsideTouchable(true);
        setAnimationStyle(R.style.share_ppw_anim_style);
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
        Tencent tencent = Tencent.createInstance(Constants.QQ_APP_KEY, ac);
        Bundle params = new Bundle();
        SongInfo songInfo = ac.getSongInfo();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, songInfo.name);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  songInfo.artist);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  songInfo.url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, songInfo.pic);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ac.getString(R.string.app_name));
        tencent.shareToQQ(ac, params, qqListener);
    }

    private void qqZoneShare() {
        SongInfo songInfo = ac.getSongInfo();
        Tencent tencent = Tencent.createInstance(Constants.QQ_APP_KEY, ac);
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "跳转URL");//必填
        ArrayList<String> list = new ArrayList<String>();
        list.add(songInfo.url);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        tencent.shareToQzone(ac, params, qqListener);
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
