package com.miglab.miyo.ui;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.net.ThirdLoginTask;
import com.miglab.miyo.ui.widget.LoadingDialog;
import com.miglab.miyo.util.DisplayUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;



/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/21.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private final String QQ_APP_KEY = "100525100";
    private Tencent tencent;
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

    private void qqLogin() {
        tencent = Tencent.createInstance(QQ_APP_KEY, this);

        if(!tencent.isSessionValid()){
            tencent.login(this, Constants.PARAM_SCOPE, qqListener);
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
                    if(loadingDialog != null && !loadingDialog.isShowing())
                        loadingDialog.show();
                }

                @Override
                public void onError(UiError uiError) {

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
}