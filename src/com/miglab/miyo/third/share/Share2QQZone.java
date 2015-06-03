package com.miglab.miyo.third.share;

import android.app.Activity;
import android.os.Bundle;
import com.miglab.miyo.constant.Constants;
import com.miglab.miyo.entity.SongInfo;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/26.
 */
public class Share2QQZone extends Share {


    public Share2QQZone(Activity ac, SongInfo songInfo) {
        super(ac, songInfo);
    }

    @Override
    public void share() {
        Tencent tencent = Tencent.createInstance(Constants.QQ_APP_ID, ac);
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, 2);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//±ØÌî
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);//Ñ¡Ìî
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, Constants.MIYO_JUMP_URL);//±ØÌî
 //       params.putString(QzoneShare.SHARE_TO_QQ_AUDIO_URL, url);
        ArrayList<String> list = new ArrayList<String>();
        list.add(imgURL);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        tencent.shareToQzone(ac, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
