package com.miglab.miyo.third.share;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.Constants;
import com.miglab.miyo.entity.SongInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/26.
 */
public class Share2QQ extends Share {

    public Share2QQ(Activity ac,SongInfo songInfo) {
        super(ac,songInfo);
    }

    @Override
    public void share() {
        Tencent tencent = Tencent.createInstance(Constants.QQ_APP_ID, ac);
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Constants.MIYO_JUMP_URL);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgURL);
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
        tencent.shareToQQ(ac, params, new IUiListener() {
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
