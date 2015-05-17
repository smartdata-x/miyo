package com.miglab.miyo.constant;

/**
 * Created by tudou on 2015/5/10.
 */
public interface ApiDefine {
    String DOMAIN = "http://112.124.49.59/cgi-bin/miyo";

    String THIRD_LOGIN = "/user/v1/thirdlogin.fcgi";
    String QUICK_LOGIN = "/user/v1/quicklogin.fcgi";

    String RECOMMEND_HOME = "/find/v1/main.fcgi";
    String BOOKMALL_HOME = "/find/v1/bookstore.fcgi";
    String BOOK_DETAIL = "/book/v1/booksummary.fcgi";
    String BOOK_TOPICS = "/book/v1/topics.fcgi";
    String BOOK_SEARCHTYPE = "/book/v1/searchtype.fcgi";
    String CHAPTER_LIST = "/book/v1/chapterlist.fcgi";
    String WANT_GET_BOOK = "/book/v1/wanted.fcgi";
    String MY_BOOK_LIST = "/book/v1/booklist.fcgi";
    String BOOK_MYLIST = "/book/v1/booklist.fcgi";
    String FOUND_FM = "/music/v1/getdimension.fcgi";
    String APP_HOME = "/find/v1/appstore.fcgi";
    String GAME_HOME = "/find/v1/gamestore.fcgi";
    String MOVIE_HOME = "/find/v1/moviestore.fcgi";
    String SPECIAL_TOPICS = "/store/v1/topics.fcgi";
    String APP_DETAILS = "/store/v1/summary.fcgi";
    String MOVIE_DETAILS = "/movie/v1/summary.fcgi";
    String MOVIE_SEARCH = "/movie/v1/searchtype.fcgi";
    String MOVIE_LIKE = "/movie/v1/like.fcgi";
    String MUSIC_COLLECT_SONG = "/music/v1/collectsong.fcgi";
    String MUSIC_DELECT_COLLECT_SONG = "/music/v1/delcltsong.fcgi";
    String MUSIC_HATE_SONG = "/music/v1/hatesong.fcgi";
    String APP_SHAKE = "/store/v1/shark.fcgi";
    String BEACON_SHAKE = "/beacon/v1/shark.fcgi";
    String ENJOY_THINGS = "/find/v1/personal.fcgi";
    String KEY_SEARCH = "/store/v1/searchkey.fcgi";
    String GET_CLTSONGS = "/music/v1/getcltsongs.fcgi";

    int GET_SUCCESS = 100001;
    int FRESH_IMAGE = 100002;
    int GET_BOOKTOPIC_SUCCESS = 100003;
    int GET_DEMENSION_SUCCESS = 100004;
    int GET_BEACON_SUCCESS = 100005;
    int GET_SHAKE_APP_SUCCESS = 100006;
    int GET_CLTSONGS_SUCCESS = 100007;


    int ERROR_TIMEOUT = 200000;
    int ERROR_UNKNOWN = 200001;
    int ERROR_PARAMS = 200002;
    int NET_OFF = 200003;

    int GET_BOYSLIST_SUCCESS = 300001;
    int GET_GIRLSLIST_SUCCESS = 300002;
    int GET_NEWBOOK_SUCCESS = 300003;
    int GET_HOTBOOK_SUCCESS = 300004;
    int GET_FOLLOWBOOK_SUCCESS = 300005;
    int GET_MYBOOKLIST_SUCCESS = 300006;

    int GET_COLLECT_SONG_SUCCESS = 400001;
    int GET_DELECT_COLLECT_SONG_SUCCESS = 400002;
    int GET_HATE_SONG_SUCCESS = 400003;

    String ERRORMSG_UNKNOWN = "Î´Öª´íÎóÔ­Òò";
}
