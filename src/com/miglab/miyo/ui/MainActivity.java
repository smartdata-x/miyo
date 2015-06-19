package com.miglab.miyo.ui;

import android.content.*;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;

import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import com.miglab.miyo.MiyoApplication;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.R;
import com.miglab.miyo.constant.ApiDefine;
import com.miglab.miyo.constant.MessageWhat;
import com.miglab.miyo.constant.MusicServiceDefine;
import com.miglab.miyo.control.IPlayAction;
import com.miglab.miyo.control.MusicManager;
import com.miglab.miyo.control.MusicService;
import com.miglab.miyo.control.PlayerReceiver;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.entity.SongInfo;
import com.miglab.miyo.net.CollectSongTask;
import com.miglab.miyo.net.DelCollectSongTask;
import com.miglab.miyo.net.DelSongTask;
import com.miglab.miyo.ui.widget.LoadingDialog;
import com.miglab.miyo.util.DisplayUtil;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MainActivity extends FragmentActivity implements IWeiboHandler.Response, IPlayAction {
    private TextView findMusic;
    private TextView myFM;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    private MusicFragment musicFragment;
    private FMFragment fmFragment;
    private UserFragment userFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private MusicService musicService;
    private PlayerReceiver playerReceiver = null;

    private LoadingDialog loadingDialog;

    private int select_R, select_G, select_B;
    private int color_Dif_R, color_Dif_G, color_Dif_B;
    private IWeiboShareAPI weiboShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        initFragments();
        initViews();
        initTitleColor();
        initProgressDlg();
        setListener();
        bindMusicService();
        registerPlayerReceiver();
    }



    private void initProgressDlg() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("加载中");
    }

    private void setListener() {
        findMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        unRegisterPlayerReceiver();
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

    private int getNewColor(float f) {
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        findMusic = (TextView) findViewById(R.id.title_find_music);
        myFM = (TextView) findViewById(R.id.title_my_fm);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(myPageListener);
        findMusic.setSelected(true);
        myFM.setSelected(false);
    }


    private ViewPager.OnPageChangeListener myPageListener = new ViewPager.OnPageChangeListener() {
        /**
         *
         * @param position 当前视图前面 一个view 的位置
         * @param positionOffset 当前视图前面 一个view相对于当前视图的偏移量0-1
         * @param positionOffsetPixels
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffsetPixels != 0) {
                findMusic.setTextColor(getNewColor(positionOffset));
                myFM.setTextColor(getNewColor(1 - positionOffset));
            }
        }

        @Override
        public void onPageSelected(int i) {
            if (i == 0) {
                findMusic.setSelected(true);
                myFM.setSelected(false);
            } else {
                findMusic.setSelected(false);
                myFM.setSelected(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void getMusicListByType(MusicType musicType) {
        musicService.getMusicListByType(musicType);
    }

    public Bitmap getCurMusicBitmap() {
        return DisplayUtil.imageZoom(musicFragment.getCurMusicBitmap(), 32764);//32768=='耀'
    }

    public void collectMusic() {
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        MusicType musicType = MusicManager.getInstance().getMusicType();
        if (songInfo == null || musicType == null)
            return;
        //收藏歌曲
        if (songInfo.like == 0) {
            new CollectSongTask(handler, songInfo, musicType).execute();
        }
        //取消收藏
        if (songInfo.like == 1) {
            new DelCollectSongTask(handler, songInfo).execute();
        }
    }

    public void delMusic() {
        nextMusic();
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        if (songInfo != null) {
            new DelSongTask(handler, songInfo).execute();
        }
    }

    public void delLocateMusic(SongInfo info) {
        musicService.delLocateMusic(info);
    }

    public void nextMusic() {
        musicService.nextMusic();
    }

    public void pauseMusic() {
        musicService.toggleMusic();
        musicFragment.updateMusicState(musicService.bePause());
        fmFragment.updateMusicState(musicService.bePause());
    }

    public void updateMusicType(MusicType music) {
        saveMusicType(music);
        musicFragment.updateMusicType();
        fmFragment.updateMusicType();
    }

    private void saveMusicType(MusicType musicType) {
        SharedPreferences preferences = getSharedPreferences("musictype", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("dim", musicType.getDim());
        editor.putInt("sid", musicType.getId());
        editor.putString("name", musicType.getName());
        editor.commit();
    }

    public void showMusicFragment() {
        viewPager.setCurrentItem(0);
        if (!loadingDialog.isShowing())
            loadingDialog.show();
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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (weiboShareAPI != null)
            weiboShareAPI.handleWeiboResponse(intent, this);
    }

    public void setWeiboShareAPI(IWeiboShareAPI weiboShareAPI) {
        this.weiboShareAPI = weiboShareAPI;
        weiboShareAPI.handleWeiboResponse(getIntent(), this);
    }

    private class FragmentAdapter extends FragmentPagerAdapter {

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
            if(drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawers();
                return true;
            }
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.LocalService) service).getService();
            MusicManager.getInstance().setMusicService(musicService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    private void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra(MusicServiceDefine.INTENT_ACTION,
                MusicServiceDefine.ALBUM_START);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 注册更新播放进度的Receiver 在oncreate的时候调用
     */
    private void registerPlayerReceiver() {
        if (playerReceiver == null) {
            IntentFilter filter = new IntentFilter(
                    MessageWhat.PLAY_BROADCAST_ACTION_NAME);
            playerReceiver = new PlayerReceiver(this);
            registerReceiver(playerReceiver, filter);
        }
    }

    /**
     * 移除注册更新播放进度的Receiver 退出该页面前调用
     */
    private void unRegisterPlayerReceiver() {
        if (playerReceiver != null) {
            unregisterReceiver(playerReceiver);
            playerReceiver = null;
        }
    }

    //    class PlayerReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Bundle bundle = intent.getExtras();
//
//            int commend = bundle.getInt(MusicServiceDefine.PLAY_WHAT);
//            if (commend <= 0) {
//                Toast.makeText(MainActivity.this, "播放出错", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            switch (commend) {
//                case MusicServiceDefine.ALBUN_NULL:
//                    Toast.makeText(MainActivity.this, "电台没有音乐", Toast.LENGTH_SHORT).show();
//                    break;
//
//                case MusicServiceDefine.MUSIC_CHANGE:
//                    if (bundle.containsKey(MusicServiceDefine.PLAY_INFO)) {
//                        SongInfo info = (SongInfo) bundle
//                                .getSerializable(MusicServiceDefine.PLAY_INFO);
//                        if (info != null) {
//                            songInfo = info;
//                            musicFragment.updateHeartMusic(songInfo.like == 1);
//                            fmFragment.updateHeartMusic(songInfo.like == 1);
//                            musicFragment.updateMusicName(songInfo.artist + "-" + songInfo.name);
//                            musicFragment.updatePicInfo(songInfo.pic);
//                            fmFragment.updatePicInfo(songInfo.pic);
//                        }
//
//                        MusicType musicType = (MusicType) bundle.getSerializable(MusicServiceDefine.PLAY_PARAM1);
//                        updateMusicType(musicType);
//                    }
//                    break;
//
//                case MusicServiceDefine.MUSIC_PREPARE:
//                    int time1 = bundle.getInt(MusicServiceDefine.PLAY_PARAM1);
//                    musicFragment.playPrepare(time1);
//                    fmFragment.playPrepare(time1);
//                    break;
//
//                case MusicServiceDefine.MUSIC_PLAYING:
//                    int time2 = bundle.getInt(MusicServiceDefine.PLAY_PARAM1);
//                    musicFragment.playTimeUpdate(time2);
//                    fmFragment.playTimeUpdate(time2);
//                    break;
//
//                case MusicServiceDefine.ACTIVITY_CLOSE:
//                    break;
//                case MusicServiceDefine.UPDATE_LIST_SUCCESS:
//                    if(loadingDialog.isShowing())
//                        loadingDialog.dismiss();
//                    break;
//                case MusicServiceDefine.UPDATE_LIST_NONE:
//                    if(loadingDialog.isShowing())
//                        loadingDialog.dismiss();
//                    Toast.makeText(MainActivity.this,getString(R.string.no_music),Toast.LENGTH_SHORT).show();
//                    break;
//                case MusicServiceDefine.STOP_TO_NEXT:
//                    //切换到下一首时，对当前播放歌曲界面处理
//                    musicFragment.updatePicInfo(null);
//                    fmFragment.updatePicInfo(null);
//                    musicFragment.setProgress(0);
//                    fmFragment.setProgress(0);
//                    musicFragment.updateMusicState(true);
//                    fmFragment.updateMusicState(true);
//                    break;
//            }
//        }
//    }
    @Override
    public void albumNull() {
        Toast.makeText(MainActivity.this, "电台没有音乐", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void musicChange() {
        SongInfo songInfo = MusicManager.getInstance().getSongInfo();
        musicFragment.updateHeartMusic(songInfo.like == 1);
        fmFragment.updateHeartMusic(songInfo.like == 1);
        musicFragment.updateMusicName(songInfo.artist + "-" + songInfo.name);
        musicFragment.updatePicInfo(songInfo.pic);
        fmFragment.updatePicInfo(songInfo.pic);
        MusicType musicType = MusicManager.getInstance().getMusicType();
        updateMusicType(musicType);
    }

    @Override
    public void musicPrepare(int time) {
        musicFragment.playPrepare(time);
        fmFragment.playPrepare(time);
    }

    @Override
    public void musicPlaying(int time) {
        musicFragment.playTimeUpdate(time);
        fmFragment.playTimeUpdate(time);
    }

    @Override
    public void updateListSuccess() {
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    @Override
    public void updateListNone() {
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();
        Toast.makeText(MainActivity.this, getString(R.string.no_music), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stopToNext() {
        musicFragment.updatePicInfo(null);
        fmFragment.updatePicInfo(null);
        musicFragment.setProgress(0);
        fmFragment.setProgress(0);
        musicFragment.updateMusicState(true);
        fmFragment.updateMusicState(true);
    }

    public static class UIHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public UIHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                activity.doHandler(msg);
            }
        }
    }

    protected UIHandler handler = new MainActivity.UIHandler(this);

    private void doHandler(Message msg) {
        switch (msg.what) {
            case ApiDefine.GET_COLLECT_SONG_SUCCESS:
                SongInfo songTemp1 = (SongInfo) msg.obj;
                Toast.makeText(MainActivity.this, songTemp1.name + " 收藏成功", Toast.LENGTH_SHORT).show();
                if (songTemp1.id == MusicManager.getInstance().getSongInfo().id) {
                    MusicManager.getInstance().getSongInfo().like = 1;
                    musicFragment.updateHeartMusic(true);
                    fmFragment.updateHeartMusic(true);

                }
                break;
            case ApiDefine.GET_HATE_SONG_SUCCESS:
                SongInfo songTemp = (SongInfo) msg.obj;
                Toast.makeText(MainActivity.this, songTemp.name + " 删除成功", Toast.LENGTH_SHORT).show();
                break;
            case ApiDefine.GET_DELECT_COLLECT_SONG_SUCCESS:
                SongInfo songTemp2 = (SongInfo) msg.obj;
                Toast.makeText(this, songTemp2.name + " 取消收藏成功", Toast.LENGTH_SHORT).show();
                //如果当前在我的红心歌单，则切到下一首同时删除该曲目
                if (MusicManager.getInstance().getMusicType().getId() == MiyoUser.getInstance().getUserId()) {
                    nextMusic();
                    delLocateMusic(songTemp2);

                }
                if (songTemp2.id == MusicManager.getInstance().getSongInfo().id) {
                    MusicManager.getInstance().getSongInfo().like = 0;
                    musicFragment.updateHeartMusic(false);
                    fmFragment.updateHeartMusic(false);
                }
                break;
        }
    }


}
