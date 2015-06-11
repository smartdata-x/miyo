package com.miglab.miyo.control;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/11.
 */
public interface IPlayAction {
    void albumNull();
    void musicChange();
    void musicPrepare(int time);
    void musicPlaying(int time);
    void updateListSuccess();
    void updateListNone();
    void stopToNext();
}
