package com.miglab.miyo.ui;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import com.miglab.miyo.R;
import com.miglab.miyo.ui.widget.RoundImageView;
import com.miglab.miyo.util.DisplayUtil;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class MusicFragment extends BaseFragment {
    private RoundImageView roundImageView;
    @Override
    protected void setLayout() {
        resourceID = R.layout.fr_music;
    }

    @Override
    protected void init() {
        roundImageView = (RoundImageView) vRoot.findViewById(R.id.music_cd);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBackground() {
        Bitmap b = ((BitmapDrawable) (roundImageView).getDrawable()).getBitmap();
        vRoot.setBackground(new BitmapDrawable(ac.getResources(),DisplayUtil.fastblur(ac, b, 80)));
    }
}
