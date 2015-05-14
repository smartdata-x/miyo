package com.miglab.miyo.ui;

import android.view.View;
import com.miglab.miyo.R;
import com.miglab.miyo.util.DisplayUtil;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener{
    @Override
    protected void setLayout() {
        resourceID = R.layout.fm_user;
    }

    @Override
    protected void init() {
        intiViews();
    }

    private void intiViews() {
        DisplayUtil.setListener(vRoot,this);
    }

    @Override
    public void onClick(View v) {

    }
}
