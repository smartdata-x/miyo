package com.miglab.miyo.ui.widget;

import com.miglab.miyo.entity.Emoticon;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/16.
 */
public interface IView {
    void onItemClick(Emoticon emoticon);
    void onItemDisplay(Emoticon emoticon);
    void onPageChangeTo(int position);
}
