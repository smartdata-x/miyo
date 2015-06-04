package com.miglab.miyo.third.share;

import android.app.Activity;
import android.widget.Toast;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.Constants;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.ui.MainActivity;
import com.sina.weibo.sdk.api.*;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by tudou on 2015/5/31.
 */
public class Share2Weibo extends Share{


    public IWeiboShareAPI iWeiboShareAPI;
    public Share2Weibo(Activity ac, SongInfo songInfo) {
        super(ac, songInfo);
    }

    public void setiWeiboShareAPI(IWeiboShareAPI iWeiboShareAPI) {
        this.iWeiboShareAPI = iWeiboShareAPI;
    }

    private void initWeiboAPI() {
        iWeiboShareAPI = WeiboShareSDK.createWeiboAPI(ac, Constants.WEIBO_APP_ID);
        iWeiboShareAPI.registerApp();
        iWeiboShareAPI.handleWeiboResponse(ac.getIntent(), (WBShareActivity) ac);
    }

    @Override
    public void share() {
        if (iWeiboShareAPI.isWeiboAppSupportAPI()) {
            int supportApi = iWeiboShareAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                sendMultiMessage(true, true, false, true, true, true);
            }
        }else {
            Toast.makeText(ac, ac.getString(R.string.weibo_not_support_api), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link com.sina.weibo.sdk.api.share.IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
                                  boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }

        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        iWeiboShareAPI.sendRequest((WBShareActivity)ac, request);
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getShareText();

        return textObject;
    }

    private String getShareText() {
        String format;
        format = ac.getString(R.string.weibo_share_music_template);
        return String.format(format, album, title, appName+"miyo");
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（musici）消息对象。
     *
     * @return 多媒体（music）消息对象。
     */
    private MusicObject getMusicObj() {
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = "";
        musicObject.description = summary;
        musicObject.setThumbImage(bitmap);

        musicObject.actionUrl = Constants.MIYO_JUMP_URL;//"http://music.sina.com.cn/yueku/i/2850305.html";
        musicObject.dataUrl = Constants.MIYO_JUMP_URL;
        musicObject.duration = 10;

        return musicObject;
    }
}
