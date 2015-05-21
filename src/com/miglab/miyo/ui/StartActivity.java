package com.miglab.miyo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.net.ThirdLoginTask;


public class StartActivity extends BaseActivity {
    private int[] imageID = {R.drawable.loading,R.drawable.loading1, R.drawable.loading2, R.drawable.loading3};
    private ViewPager viewPager;
    boolean bFirst = false;

    @Override
    protected void init() {
        setContentView(R.layout.ac_start);
        SharedPreferences sharedPrefs = getSharedPreferences("first_use", Context.MODE_PRIVATE);
        if (sharedPrefs != null)
            bFirst = sharedPrefs.getBoolean("firststart", true);
        if (bFirst)
            initFirstStart();
        else
            autoLogin();
    }

    public void initFirstStart() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageID.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                if (position != imageID.length - 1) {
                    ImageView imageView = new ImageView(StartActivity.this);
                    imageView.setImageResource(imageID[position]);
                    container.addView(imageView);
                    return imageView;
                } else {
                    View view = LayoutInflater.from(StartActivity.this).inflate(
                            R.layout.ly_start_pager_item, null);
                    ImageView iv_start = (ImageView) view.findViewById(R.id.imageView);
                    iv_start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sharedPrefs = MiyoApplication.getInstance()
                                    .getSharedPreferences("first_use", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putBoolean("firststart",false);
                            editor.commit();
                            autoLogin();
                        }
                    });
                    container.addView(view);
                    return view;
                }

            }
        });
        viewPager.setVisibility(View.VISIBLE);
    }

    private void autoLogin() {
        MiyoUser user = MiyoUser.getInstance();
        user.readRecord();
        if (user.hasRecoder())
            new ThirdLoginTask(uiHandler).execute();
        else {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void doHandler(Message msg) {
        super.doHandler(msg);
        if (msg.what == ApiDefine.GET_SUCCESS) {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
    }


}
