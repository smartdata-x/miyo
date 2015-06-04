package com.miglab.miyo.third.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.Constants;
import com.miglab.miyo.entity.SongInfo;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/4.
 */
public class WBShareActivity extends Activity implements IWeiboHandler.Response {
    private IWeiboShareAPI iWeiboShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.WEIBO_APP_ID);
        iWeiboShareAPI.registerApp();
        initData();
    }

    private void initData() {
        Intent i = getIntent();
        SongInfo songInfo = (SongInfo) i.getSerializableExtra("songInfo");
        Bitmap bitmap = i.getParcelableExtra("bitmap");
        String album = i.getStringExtra("album");
        Share2Weibo share2Weibo = new Share2Weibo(this,songInfo);
        share2Weibo.setiWeiboShareAPI(iWeiboShareAPI);
        share2Weibo.album = album;
        share2Weibo.bitmap = bitmap;
        share2Weibo.share();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(iWeiboShareAPI != null){
            iWeiboShareAPI.handleWeiboResponse(intent, this);
        }
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Toast.makeText(this, R.string.share_success, Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Toast.makeText(this, R.string.share_cancel, Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Toast.makeText(this, getString(R.string.share_fail) + "Error Message: " + baseResponse.errMsg,
                        Toast.LENGTH_LONG).show();
                break;
        }
        finish();
    }
}
