package com.miglab.miyo.third.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.Constants;
import com.miglab.miyo.entity.SongInfo;
import com.tencent.mm.sdk.modelmsg.*;
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
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = summary;
//
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;
//        msg.description = summary;
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
//        req.message = msg;


        WXMusicObject music = new WXMusicObject();
        music.musicUrl = Constants.MIYO_JUMP_URL;
        music.musicDataUrl = url;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = summary;

        Bitmap thumb = bitmap;
        msg.thumbData = bmpToByteArray(thumb, false);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = this.scene;
        wxApi.sendReq(req);
    }

    public void shareApp() {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(ac, Constants.WEIXIN_APP_ID, true);
        wxApi.registerApp(Constants.WEIXIN_APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constants.MIYO_JUMP_URL;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = ac.getResources().getString(R.string.app_name);
        msg.description = ac.getResources().getString(R.string.app_intro);
        Bitmap thumb = BitmapFactory.decodeResource(ac.getResources(), R.drawable.logo);
        msg.thumbData = bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = this.scene;
        wxApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
//
//    public final void setThumbImage(Bitmap bitmap) {
//        ByteArrayOutputStream os = null;
//
//        try {
//            os = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
//            this.thumbData = os.toByteArray();
//        } catch (Exception var12) {
//            var12.printStackTrace();
//            LogUtil.e("Weibo.BaseMediaObject", "put thumb failed");
//        } finally {
//            try {
//                if(os != null) {
//                    os.close();
//                }
//            } catch (IOException var11) {
//                var11.printStackTrace();
//            }
//
//        }
//
//    }

    private byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, output);
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
