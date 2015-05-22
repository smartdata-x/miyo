package com.miglab.miyo.ui;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.constant.Constants;
import com.miglab.miyo.net.ThirdLoginTask;
import com.miglab.miyo.ui.widget.LoadingDialog;
import com.miglab.miyo.util.DisplayUtil;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.tencent.connect.UserInfo;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/21.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private Tencent tencent;
    private AuthInfo authInfo;
    private SsoHandler ssoHandler;
    private LoadingDialog loadingDialog;

    @Override
    protected void init() {
        initViews();
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("加载中...");
    }

    private void initViews() {
        setContentView(R.layout.ac_login);
        DisplayUtil.setListener(findViewById(R.id.center), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qqLogin:
                qqLogin();
                break;
            case R.id.weiboLogin:
                weiboLogin();
                break;
        }
    }

    @Override
    protected void doHandler(Message msg) {
        switch (msg.what) {
            case ApiDefine.GET_SUCCESS:
                if(loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                this.finish();
                break;
        }
    }

    private void weiboLogin() {
        authInfo = new AuthInfo(this, Constants.WEIBO_APP_KEY,
                Constants.REDIRECT_URL,Constants.WEIBO_SCOPE);
        ssoHandler = new SsoHandler(this, authInfo);
        ssoHandler.authorize(new AuthListener());
    }

    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle bundle) {
            final Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
            if (accessToken.isSessionValid()) {
                long uid = Long.parseLong(accessToken.getUid());
                WeiboParameters parameters = new WeiboParameters(Constants.WEIBO_APP_KEY);
                parameters.put("uid", uid);
                parameters.put(Constants.WEIBO_KEY_ACCESS_TOKEN,accessToken.getToken());
                if(loadingDialog != null && !loadingDialog.isShowing())
                    loadingDialog.show();
                new AsyncWeiboRunner(LoginActivity.this).requestAsync(Constants.GET_WEIBO_USERINFO, parameters, "GET",
                        new RequestListener() {
                            @Override
                            public void onComplete(String s) {
                                JSONObject jResult = null;
                                try {
                                    jResult = new JSONObject(s);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                MiyoUser user = MiyoUser.getInstance();
                                user.setNickname(jResult.optString("name"));
                                user.setSource(1);
                                user.setSession(jResult.optString("id"));
                                if(jResult.optString("gender").equals("m"))
                                    user.setGender(1);
                                else
                                    user.setGender(0);
                                user.setLocation(jResult.optString("location"));
                                user.setHeadUrl(jResult.optString("avatar_large"));//avatar_hd：高清头像  profile_image_url：小头像
                                new ThirdLoginTask(uiHandler).execute();
                            }

                            @Override
                            public void onWeiboException(WeiboException e) {
                                if(loadingDialog != null && loadingDialog.isShowing())
                                    loadingDialog.dismiss();
                            }
                        });

            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,
                    "weibo failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {

        }
    }

    private void qqLogin() {
        tencent = Tencent.createInstance(Constants.QQ_APP_KEY, this);

        if(!tencent.isSessionValid()){
            tencent.login(this, com.tencent.connect.common.Constants.PARAM_SCOPE, qqListener);
        }
    }

    private void qqLoginOut() {
        if(tencent != null)
            tencent.logout(this);
    }

    private QQListener qqListener = new QQListener();

    private class QQListener implements IUiListener{

        @Override
        public void onComplete(Object o) {
            UserInfo userInfo = new UserInfo(LoginActivity.this,tencent.getQQToken());
            userInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    if(loadingDialog != null && !loadingDialog.isShowing())
                        loadingDialog.show();
                    JSONObject jResult = (JSONObject) o;
                    MiyoUser user = MiyoUser.getInstance();
                    user.setNickname(jResult.optString("nickname"));
                    user.setSource(3);
                    user.setSession(tencent.getAccessToken());
                    if(jResult.optString("gender").equals("男"))
                        user.setGender(1);
                    else
                        user.setGender(0);
                    user.setLocation(jResult.optString("city"));
                    user.setHeadUrl(jResult.optString("figureurl_qq_2"));
                    new ThirdLoginTask(uiHandler).execute();
                    qqLoginOut();
                }

                @Override
                public void onError(UiError uiError) {
                    if(loadingDialog != null && loadingDialog.isShowing())
                        loadingDialog.dismiss();
                }

                @Override
                public void onCancel() {

                }
            });
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(ssoHandler != null)
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        if(tencent != null)
            tencent.onActivityResult(requestCode, resultCode, data);
    }
}