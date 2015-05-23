package com.miglab.miyo.constant;

/**
 * Created by tudou on 2015/5/9.
 */
public interface MusicServiceDefine {
    String INTENT_ACTION = "action";
    String INTENT_DIMENSION = "dimension";
    String PLAY_WHAT = "what";
    String PLAY_PARAM1 = "play_param1";
    String PLAY_PARAM2 = "play_param2";
    String PLAY_INFO = "playerInfo";
    String INTENT_NOTICE_BACKGROUND_PALYING = "fromNotice";

    int PLAYER_IDLE = 0;
    int PLAYER_PLAYING = 1;
    int PLAYER_PAUSE = 2;

    int ALBUM_START = 80001;
    int ACTION_TOGGLE = 80002;
    int ACTION_STOP = 80004;
    int ACTIVITY_FINISH = 80005;
    int ACTION_SWITCH = 80006;
    int ALBUM_CHANGE = 80007;
    int FROM_NOTICE = 80008;

    int ALBUN_NULL = 80021;
    int MUSIC_CHANGE = 80022;
    int MUSIC_PREPARE = 80023;
    int MUSIC_PLAYING = 80024;
    int MUSIC_PAUSE = 80025;
    int MUSIC_RESUME = 80026;
    int MUSIC_STOP = 80027;
    int ACTIVITY_CLOSE = 80028;
    int UPDATE_LIST_SUCCESS = 80029;
    int UPDATE_LIST_NONE = 80030;
    int STOP_TO_NEXT = 80031;

    int MUSIC_PLAYER_NOTIFY_ID = 80041;

}
