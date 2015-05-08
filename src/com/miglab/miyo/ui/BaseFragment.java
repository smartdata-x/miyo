package com.miglab.miyo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/5/8.
 */
public class BaseFragment extends Fragment {
    protected View vRoot;
    protected Activity ac;
    protected int resourceID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ac = getActivity();
        setLayout();
        vRoot = inflater.inflate(resourceID, container, false);
        init();
        return vRoot;
    }

    protected void init(){

    }

    protected void setLayout(){

    }
}
