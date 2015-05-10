package com.miglab.miyo.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MainActivity extends FragmentActivity {
    private TextView findMusic;
    private TextView myFM;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    private BaseFragment musicFragment,fmFragment,userFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    //
    private int titleSelectColor,titleUnselectColor;
    private int select_R,select_G,select_B;
    private int color_Dif_R,color_Dif_G,color_Dif_B;

    private boolean isInit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        initFragments();
        initViews();
        initTitleColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MiyoApplication.resume(this);
    }

    private void initTitleColor() {
        titleSelectColor = getResources().getColor(R.color.title_select);
        titleUnselectColor = getResources().getColor(R.color.title_unselect);
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
        fmFragment = new FMFragment();
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
//            Log.e("haha---","position="+position+";positionOffset="+positionOffset+";positionOffsetPixels"+positionOffsetPixels);
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && !isInit){
            ((MusicFragment)musicFragment).setBackground();
            isInit =  true;
        }
    }
}
