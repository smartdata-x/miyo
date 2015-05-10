package com.miglab.miyo.control;

import com.miglab.miyo.control.AudioController.AudioState;

/**
 * Created by tudou on 2015/5/10.
 */
public interface AudioControllerListener {
    /** ��ͣ����������ȥ�����ͣ�� */
    void bePaused(AudioState state);

    /** �ָ�������绰������ָ��� */
    void beResumed(AudioState state);

    /** ֹͣ��ͬһ�����ڣ����ȼ��ߵ��������ȼ��͵ģ������ȼ��͵�ͣ�ˣ� */
    void beStopped(AudioState state);

    /** �رս��棨��ͬ�������Ƶ��ͻ����һ����ǰһ�����ˣ� */
    void beClosed(AudioState state);

    /** �����˶��� */
    void headsetOn(AudioState state);

    /** �����˶��� */
    void headsetOff(AudioState state);
}
