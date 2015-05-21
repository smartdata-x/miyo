package com.miglab.miyo.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import com.miglab.miyo.MiyoUser;
import com.miglab.miyo.R;
import com.miglab.miyo.third.universalimageloader.core.DisplayImageOptions;
import com.miglab.miyo.third.universalimageloader.core.ImageLoader;
import com.miglab.miyo.util.DisplayUtil;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener{
    private ImageView iv_head;
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
        DisplayUtil.setListener(vRoot, this);
        initHeadIcon();
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.user_photo_click:
                ac.startActivity(new Intent(ac,LoginActivity.class));
                break;
        }
    }
}
