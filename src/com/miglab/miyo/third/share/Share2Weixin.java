package com.miglab.miyo.third.share;

import android.app.Activity;
import android.graphics.Bitmap;
import com.miglab.miyo.constant.Constants;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.ui.MainActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by tudou on 2015/5/31.
 */
public class Share2Weixin extends Share {
    public static final int WEIXIN_SHARE = 0;
    public static final int FRIENDS_SHARE = 1;
    private int scene;

    public Share2Weixin(Activity ac, SongInfo songInfo) {
        super(ac, songInfo);
    }

    /** 0--朋友圈 1--微信 */
    public void setReqScene(int type) {
        this.scene = type==FRIENDS_SHARE ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

    }

    @Override
    public void share() {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(ac, Constants.WEIXIN_APP_ID, true);
        wxApi.registerApp(Constants.WEIXIN_APP_ID);
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = url;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = summary;

        Bitmap thumb = ((MainActivity)ac).getCurMusicBitmap();
        msg.thumbData = bmpToByteArray(thumb, false);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = this.scene;
        wxApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
