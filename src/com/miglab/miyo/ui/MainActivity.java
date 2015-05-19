package com.miglab.miyo.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.R;
import com.miglab.miyo.control.MusicService;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.net.GetWeatherTask;
import com.miglab.miyo.util.LocationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MainActivity extends FragmentActivity implements FMFragment.MusicInterface,MusicFragment.NotifyFmInterface{
    private TextView findMusic;
    private TextView myFM;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    private MusicFragment musicFragment;
    private FMFragment fmFragment;
    private UserFragment userFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LocationUtil locationUtil;

    private int select_R,select_G,select_B;
    private int color_Dif_R,color_Dif_G,color_Dif_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        initFragments();
        initViews();
        initTitleColor();
        setListener();
    }

    private void setListener() {
        findMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0,true);
                findMusic.setSelected(true);
                myFM.setSelected(false);
            }
        });
        myFM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
                myFM.setSelected(true);
                findMusic.setSelected(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MiyoApplication.resume(this);
    }

    private void initTitleColor() {
        int titleSelectColor = getResources().getColor(R.color.title_select);
        int titleUnselectColor = getResources().getColor(R.color.title_unselect);
        select_R = Color.red(titleSelectColor);
        select_G = Color.green(titleSelectColor);
        select_B = Color.blue(titleSelectColor);
        color_Dif_R = Color.red(titleUnselectColor) - select_R;
        color_Dif_G = Color.green(titleUnselectColor) - select_G;
        color_Dif_B = Color.blue(titleUnselectColor) - select_B;
    }

    private int getNewColor(float f){
        int newR, newG, newB;
        newR = (int) (color_Dif_R * f) + select_R;
        newG = (int) (color_Dif_G * f) + select_G;
        newB = (int) (color_Dif_B * f) + select_B;
        return Color.rgb(newR, newG, newB);
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initFragments() {
        fragmentManager = getSupportFragmentManager();
        musicFragment = new MusicFragment();
        musicFragment.setNotifyFmInterface(this);
        fmFragment = new FMFragment();
        fmFragment.setMusicInterface(this);
        userFragment = new UserFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user, userFragment);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentList.add(musicFragment);
        fragmentList.add(fmFragment);

    }

    private void initViews() {
        setContentView(R.layout.ac_main);
        findMusic = (TextView) findViewById(R.id.title_find_music);
        myFM = (TextView) findViewById(R.id.title_my_fm);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(myPageListener);
        findMusic.setSelected(true);
        myFM.setSelected(false);
    }

    private ViewPager.OnPageChangeListener myPageListener = new ViewPager.OnPageChangeListener(){
        /**
         *
         * @param position 当前视图前面 一个view 的位置
         * @param positionOffset 当前视图前面 一个view相对于当前视图的偏移量0-1
         * @param positionOffsetPixels
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(positionOffsetPixels != 0) {
                findMusic.setTextColor(getNewColor(positionOffset));
                myFM.setTextColor(getNewColor(1 - positionOffset));
            }
        }

        @Override
        public void onPageSelected(int i) {
            if(i == 0){
                findMusic.setSelected(true);
                myFM.setSelected(false);
            }else{
                findMusic.setSelected(false);
                myFM.setSelected(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    public void updateMusicList(List<SongInfo> list) {
        musicFragment.updateMusicList(list);
    }

    @Override
    public void updateBackground(Drawable drawable) {
       fmFragment.updateBackground(drawable);
    }

    @Override
    public void updateCD(Drawable drawable) {
        fmFragment.updateCD(drawable);
    }

    private class FragmentAdapter extends FragmentPagerAdapter{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
