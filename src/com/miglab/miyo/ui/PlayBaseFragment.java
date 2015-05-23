package com.miglab.miyo.ui;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.miglab.miyo.R;

import com.miglab.miyo.third.universalimageloader.core.DisplayImageOptions;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.third.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.miglab.miyo.ui.widget.RoundImageView;
import com.miglab.miyo.ui.widget.RoundProgressBar;
import com.miglab.miyo.util.DisplayUtil;


/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/19.
 */
public class PlayBaseFragment extends BaseFragment implements View.OnClickListener{
    protected ImageView iv_cd;
    protected ImageView iv_heart;
    protected RoundProgressBar roundProgressBar;

    protected ObjectAnimator anim;
    protected MainActivity mainActivity;

    private DisplayImageOptions options;

    @Override
    protected void init(){
        initDisplayImageOptions();
        initViews();
        initAnim();
        mainActivity = (MainActivity) ac;
    }

    protected void initViews() {
        iv_cd = (RoundImageView) vRoot.findViewById(R.id.music_cd);
        roundProgressBar = (RoundProgressBar) vRoot.findViewById(R.id.cd_progress);
        iv_heart = (ImageView) vRoot.findViewById(R.id.heart_music);
        DisplayUtil.setListener(vRoot, this);
    }

    protected void initAnim() {
        anim = ObjectAnimator.ofFloat(iv_cd, "rotation", 0, 360);
        anim.setDuration(50000);
        anim.setRepeatCount(-1);
        anim.setRepeatMode(ObjectAnimator.RESTART);
        anim.setInterpolator(new LinearInterpolator());
    }

    private void initDisplayImageOptions() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(false)
                .showImageForEmptyUri(R.drawable.music_default_cd)
                .build();
    }

    protected void setMaxProgressBar(int i) {
        roundProgressBar.setMax(i);
    }

    protected void cdStartAnimation() {
        anim.start();
    }

    protected void setProgress (int time){
        roundProgressBar.setProgress(time);
    }

    public void playPrepare(int time) {
        cdStartAnimation();
        setMaxProgressBar(time / 100);
    }

    public void playTimeUpdate(int curTime) {
        setProgress(curTime / 100);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.del_music:
                delMusic();
                break;
            case R.id.heart_music:
                collectMusic();
                break;
            case R.id.next_music:
                nextMusic();
                break;
            case R.id.cd_palyer:
                pauseMusic();
                break;
            case R.id.icon_player:
                pauseMusic();
                break;
        }
    }

    protected void pauseMusic() {
        mainActivity.pauseMusic();
    }

    protected void nextMusic() {
        mainActivity.nextMusic();
    }


    protected void collectMusic() {
        mainActivity.collectMusic();
    }

    protected void delMusic() {
        mainActivity.delMusic();
    }

    public void updateHeartMusic(boolean bLike) {
        if(bLike) {
            iv_heart.setImageResource(R.drawable.delcollect_music_selector);
        } else{
            iv_heart.setImageResource(R.drawable.collect_music_selector);
        }
    }

    public void updatePicInfo(String url) {
            ImageLoader.getInstance().displayImage(url, iv_cd, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if(!TextUtils.isEmpty(imageUri))
                        setBackground();
                }
            });

    }

    @SuppressWarnings("deprecation")
    public void setBackground() {
        Drawable drawable = iv_cd.getDrawable();
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Drawable drawableBg = new BitmapDrawable(ac.getResources(), DisplayUtil.fastblur(ac, b, 80));
        if (Build.VERSION.SDK_INT < 16) {
            vRoot.setBackgroundDrawable(drawableBg);
        } else {
            vRoot.setBackground(drawableBg);
        }
    }

    public void updateMusicState(boolean bPause) {
        if (bPause) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                anim.pause();
            } else {
                anim.cancel();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                anim.resume();
            } else {
                anim.start();
            }
        }
    }
}
