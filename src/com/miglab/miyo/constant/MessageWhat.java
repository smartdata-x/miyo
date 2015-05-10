package com.miglab.miyo.constant;

/**
 * Created by tudou on 2015/5/9.
 */
public interface MessageWhat {
    int NET_ON 						= 0x0400;
    int NET_OFF 					= NET_ON + 1;
    int PHONE_COMING 				= NET_ON + 2;
    int PHONE_ANSWER 				= NET_ON + 3;
    int PHONE_ENDING 				= NET_ON + 4;

    int FILE_CANNOT_DOWNLOAD 	= NET_ON + 21;
    int FILE_DOWNLOAD_COMPLETE 	= NET_ON + 22;
    int FILE_DOWNLOAD_CANCELED 	= NET_ON + 23;
    int FILE_DOWNLOAD_START 	= NET_ON + 24;
    int FILE_DOWNLOAD_FAILED 	= NET_ON + 25;
    int FILE_DOWNLOAD_DONE 		= NET_ON + 26;
    int FILE_DOWNLOADING 		= NET_ON + 27;
    int FILE_DOWNLOAD_NEXT 		= NET_ON + 28;

    int PLAYER_ERROR 			= NET_ON + 41;
    int PLAYER_PREPARED  		= NET_ON + 42;
    int PLAYER_START  			= NET_ON + 43;
    int PLAYING_TIME  			= NET_ON + 44;
    int PLAYER_PAUSE  			= NET_ON + 45;
    int PLAYING_SWITCH  		= NET_ON + 46;
    int PLAYING_STOP  			= NET_ON + 47;
    int AAC_PLAYER  			= NET_ON + 48;
    int WM_PLAYER_EOF  			= NET_ON + 49;

    int WAIT_DATA				= NET_ON + 51;
    int DATA_READY				= NET_ON + 52;
    int NET_WAKE				= NET_ON + 53;
    int STOP_THREAD				= NET_ON + 54;

    int WM_PHONE_GOING				= NET_ON + 60;
    int WM_PHONE_ENDING				= NET_ON + 61;
    int WM_PHONE_COMING				= NET_ON + 62;
    int WM_HEADSET_ON				= NET_ON + 63;
    int WM_HEADSET_OFF				= NET_ON + 64;

    /** 播放器与UI界面/播放Service交互广播 */
    String PLAY_BROADCAST_ACTION_NAME = "com.miglab.abheg.MusicServiceNotification";
}
