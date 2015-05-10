package com.miglab.miyo.control;

import com.miglab.miyo.control.AudioController.AudioState;

/**
 * Created by tudou on 2015/5/10.
 */
public interface AudioControllerListener {
    /** 暂停（比如来电去电会暂停） */
    void bePaused(AudioState state);

    /** 恢复（比如电话结束会恢复） */
    void beResumed(AudioState state);

    /** 停止（同一界面内，优先级高的碰上优先级低的，把优先级低的停了） */
    void beStopped(AudioState state);

    /** 关闭界面（不同界面的音频冲突，后一个把前一个关了） */
    void beClosed(AudioState state);

    /** 插上了耳机 */
    void headsetOn(AudioState state);

    /** 拔下了耳机 */
    void headsetOff(AudioState state);
}
