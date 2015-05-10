package com.miglab.miyo.control;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.miglab.miyo.constant.MessageWhat;

import java.lang.ref.WeakReference;

/**
 * Created by tudou on 2015/5/10.
 */
public class AudioController {

    private final static AudioController instance = new AudioController();
    public AudioState now = AudioState.NONE;
    public AudioState old = AudioState.NONE;
    private AudioControllerListener oldListener, nowListener;

    // ===========����ʱ�ر���Ƶ============//
    PhoneListenerController phoneListenerController;
    boolean isPhoneComing = false;

    // ===========���������ʾ============//
    HeadsetPlugController headsetController;
    boolean isHeadSetOn = false;

    public static AudioController get() {
        return instance;
    }

    private AudioController() {

    }

    // ===========��Ƶ����============//
    public enum AudioState {
        NONE(0, 0, "û�в���"), LISTEN_MUSIC(1, 1, "����");

        public int type;
        public int priority;
        public String content;

        /**
         * ��Ƶ����
         *
         * @param type
         *            ���� 1 ��Ƶ�� 2 ��Ƶ
         * @param priority
         *            ���ȼ�
         * @param content
         *            ����
         */
        AudioState(int type, int priority, String content) {
            this.type = type;
            this.priority = priority;
            this.content = content;
        }
    }

    public boolean canChange(AudioState state) {
        if (now.type != state.type || state.priority >= now.priority)
            return true;

        return false;
    }

    public boolean setAudioStart(final AudioState state,
                                 final AudioControllerListener listener) {
        if (canChange(state)) {
            if (state != now) {
                if (oldListener != null) {
                    oldListener.beStopped(old);
                }

                old = now;
                oldListener = nowListener;
                now = state;
                nowListener = listener;

                cancel();

                if (isPhoneComing) {
                    handler.sendEmptyMessageDelayed(MessageWhat.WM_PHONE_GOING,
                            1000);
                }

                handler.sendEmptyMessageDelayed(
                        isHeadSetOn ? MessageWhat.WM_HEADSET_ON
                                : MessageWhat.WM_HEADSET_OFF, 500);

            }

            return true;
        }

        return false;
    }

    public boolean setAudioFinish(AudioState state) {
        if (state == now) {
            if (old != AudioState.NONE) {
                now = old;
                nowListener = oldListener;
                old = AudioState.NONE;
                oldListener = null;

            } else {
                now = AudioState.NONE;
                nowListener = null;
            }

            return true;
        } else if (state == old) {
            old = AudioState.NONE;
            oldListener = null;
            return true;
        }

        return false;
    }

    private void cancel() {
        if (old == AudioState.NONE)
            return;

        if (now.type != old.type) {
            oldListener.beClosed(old);

            old = AudioState.NONE;
            oldListener = null;
        } else {
            // now�����ȼ����ڻ����old����old������
            oldListener.beStopped(old);

            old = AudioState.NONE;
            oldListener = null;
        }
    }

    public void resumeNowState() {
        if (nowListener != null) {
            nowListener.beResumed(now);
        }
    }

    public void registerPhoneListener(Context con) {
        if (phoneListenerController == null) {
            phoneListenerController = new PhoneListenerController(con, handler);
            phoneListenerController.registerPhoneListener();
        }
    }

    public void cancelPhoneListener() {
        if (phoneListenerController != null) {
            phoneListenerController.cancelPhoneListener();
        }
    }

    public void registerHeadsetPlugListener(Context con) {
        if (headsetController == null) {
            headsetController = new HeadsetPlugController(con, handler);
            headsetController.registerReceiver();

            isHeadSetOn = headsetController.checkHeadsetOn(con);
        }
    }

    public void cancelHeadsetPlugListener() {
        if (headsetController != null) {
            headsetController.cancelReceiver();
        }
    }

    public boolean getHeadSetState() {
        return isHeadSetOn;
    }



    private AudioControllerHandler handler = new AudioControllerHandler(this);

    public static class AudioControllerHandler extends Handler{
        private WeakReference<AudioController> audioControllerWeakReference;
        public AudioControllerHandler(AudioController a){
            audioControllerWeakReference = new WeakReference<AudioController>(a);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AudioController audioController = audioControllerWeakReference.get();
            switch (msg.what) {
                case MessageWhat.WM_PHONE_COMING:
                case MessageWhat.WM_PHONE_GOING:
                    audioController.isPhoneComing = true;
                    if (audioController.nowListener != null) {
                        audioController.nowListener.bePaused(audioController.now);
                    }
                    if (audioController.oldListener != null) {
                        audioController.oldListener.bePaused(audioController.old);
                    }
                    break;

                case MessageWhat.WM_PHONE_ENDING:
                    if (audioController.isPhoneComing && audioController.nowListener != null) {
                        audioController.nowListener.beResumed(audioController.now);
                    }
                    if (audioController.isPhoneComing && audioController.oldListener != null) {
                        audioController.oldListener.beResumed(audioController.old);
                    }
                    audioController.isPhoneComing = false;
                    break;

			/* ���϶��� */
                case MessageWhat.WM_HEADSET_ON:
                    audioController.isHeadSetOn = true;
                    if (audioController.nowListener != null) {
                        audioController.nowListener.headsetOn(audioController.now);
                    }
                    if (audioController.oldListener != null) {
                        audioController.oldListener.headsetOn(audioController.old);
                    }

                    break;

			/* ���¶��� */
                case MessageWhat.WM_HEADSET_OFF:
                    if (audioController.nowListener != null) {
                        audioController.nowListener.headsetOff(audioController.now);
                    }
                    if (audioController.oldListener != null) {
                        audioController.oldListener.headsetOff(audioController.old);
                    }
                    audioController.isHeadSetOn = false;
                    break;

            }
        }
    }

}
