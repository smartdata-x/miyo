package com.miglab.miyo.constant;

/**
 * Created by tudou on 2015/5/10.
 */
public interface ApiDefine {
    String DOMAIN = "http://112.124.49.59/cgi-bin/miyo";

    String THIRD_LOGIN = "/user/v1/thirdlogin.fcgi";
    String AUTO_LOGIN = "/user/v1/loginrecord.fcgi";
    String FOUND_FM = "/music/v1/getdimension.fcgi";
    String MUSIC_COLLECT_SONG = "/music/v1/collectsong.fcgi";
    String MUSIC_DELECT_COLLECT_SONG = "/music/v1/delcltsong.fcgi";
    String MUSIC_HATE_SONG = "/music/v1/hatesong.fcgi";
    String GET_CLTSONGS = "/music/v1/getcltsongs.fcgi";
    String WEATHER = "/soc/v1/getlocation.fcgi";
    String MUSIC_TYPE = "/music/v1/dimensioninfo.fcgi";
    String CHAT = "/soc/v1/getbarragecomm.fcgi";
    String RECORD_MUSIC = "/music/v1/recordcursong.fcgi";
    String GET_MESSAGE = "/soc/v1/getpushmsg.fcgi";

    int GET_SUCCESS = 100001;
    int GET_MUSIC_LIST_SUCCESS = 100002;
    int GET_CLTSONGS_SUCCESS = 100003;
    int GET_WEATHER_SUCCESS = 100004;
    int GET_MUSIC_TYPE_SUCCESS = 100005;
    int GET_CHAT_SUCCESS = 100006;
    int GET_MESSAGE_SUCCESS = 100007;

    int ERROR_TIMEOUT = 200000;
    int ERROR_UNKNOWN = 200001;
    int ERROR_PARAMS = 200002;
    int NET_OFF = 200003;


    int GET_COLLECT_SONG_SUCCESS = 400001;
    int GET_DELECT_COLLECT_SONG_SUCCESS = 400002;
    int GET_HATE_SONG_SUCCESS = 400003;

    String ERRORMSG_UNKNOWN = "Î´Öª´íÎóÔ­Òò";


}
