package com.miglab.miyo.ui;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.net.GetWeatherTask;
import com.miglab.miyo.third.universalimageloader.core.DisplayImageOptions;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.util.DisplayUtil;
import org.json.JSONObject;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener{
    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_address;
    private ImageView iv_genderIcon;
    private ImageView iv_weatherIcon;
    @Override
    protected void setLayout() {
        resourceID = R.layout.fm_user;
    }

    @Override
    protected void init() {
        intiViews();
    }

    private void intiViews() {
        iv_head = (ImageView) vRoot.findViewById(R.id.header);
        tv_name = (TextView) vRoot.findViewById(R.id.name);
        tv_address = (TextView) vRoot.findViewById(R.id.address);
        iv_genderIcon = (ImageView) vRoot.findViewById(R.id.genderIcon);
        iv_weatherIcon = (ImageView) vRoot.findViewById(R.id.weatherIcon);
        DisplayUtil.setViewListener(vRoot, this);
        DisplayUtil.setGropListener(vRoot, this);
        initHeadIcon();
        initUserInfo();
        initWeather();
    }

    private void initUserInfo() {
        MiyoUser user = MiyoUser.getInstance();
        if(user.getGender() == 1)
            iv_genderIcon.setImageResource(R.drawable.man_icon);
        else
            iv_genderIcon.setImageResource(R.drawable.woman_icon);
        tv_name.setText(user.getNickname());
        tv_address.setText(user.getLocation());
    }

    private void initHeadIcon() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .build();
        String url = MiyoUser.getInstance().getHeadUrl();
        ImageLoader.getInstance().displayImage(url, iv_head, options, null);
    }

    private void initWeather() {
        new GetWeatherTask(handler,
                MiyoApplication.getInstance().getLocationUtil().getLatitude(),
                MiyoApplication.getInstance().getLocationUtil().getLongitude()).execute();
    }

    private void displayWeather(String weather, String temp, String address) {
        tv_address.setText(address);
        int resID = getResources().getIdentifier(weather, "drawable", MiyoApplication.getInstance().getPackageName());
        iv_weatherIcon.setImageResource(resID);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.user_photo_click:
                ac.startActivity(new Intent(ac,LoginActivity.class));
                break;
        }
    }

    @Override
    protected void doHandler(Message msg) {
        super.doHandler(msg);
        switch (msg.what) {
            case ApiDefine.GET_WEATHER_SUCCESS:
                JSONObject jResult = (JSONObject) msg.obj;
                String weather = jResult.optString("weather");
                String temp = jResult.optString("temp");
                String address = jResult.optString("city");
                weather.replace("-", "_");
                displayWeather(weather.toLowerCase(), temp, address);
                break;
        }
    }
}
