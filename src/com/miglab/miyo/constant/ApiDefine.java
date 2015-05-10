package com.miglab.miyo.constant;

/**
 * Created by tudou on 2015/5/10.
 */
public interface ApiDefine {
    String DOMAIN = "http://112.124.49.59/cgi-bin/abheg";

    String THIRD_LOGIN = "/user/1/thirdlogin.fcgi";
    String QUICK_LOGIN = "/user/1/quicklogin.fcgi";

    String RECOMMEND_HOME = "/find/1/main.fcgi";
    String BOOKMALL_HOME = "/find/1/bookstore.fcgi";
    String BOOK_DETAIL = "/book/1/booksummary.fcgi";
    String BOOK_TOPICS = "/book/1/topics.fcgi";
    String BOOK_SEARCHTYPE = "/book/1/searchtype.fcgi";
    String CHAPTER_LIST = "/book/1/chapterlist.fcgi";
    String WANT_GET_BOOK = "/book/1/wanted.fcgi";
    String MY_BOOK_LIST = "/book/1/booklist.fcgi";
    String BOOK_MYLIST = "/book/1/booklist.fcgi";
    String FOUND_FM = "/music/1/getdimension.fcgi";
    String APP_HOME = "/find/1/appstore.fcgi";
    String GAME_HOME = "/find/1/gamestore.fcgi";
    String MOVIE_HOME = "/find/1/moviestore.fcgi";
    String SPECIAL_TOPICS = "/store/1/topics.fcgi";
    String APP_DETAILS = "/store/1/summary.fcgi";
    String MOVIE_DETAILS = "/movie/1/summary.fcgi";
    String MOVIE_SEARCH = "/movie/1/searchtype.fcgi";
    String MOVIE_LIKE = "/movie/1/like.fcgi";
    String MUSIC_COLLECT_SONG = "/music/1/collectsong.fcgi";
    String MUSIC_DELECT_COLLECT_SONG = "/music/1/delcltsong.fcgi";
    String MUSIC_HATE_SONG = "/music/1/hatesong.fcgi";
    String APP_SHAKE = "/store/1/shark.fcgi";
    String BEACON_SHAKE = "/beacon/1/shark.fcgi";
    String ENJOY_THINGS = "/find/1/personal.fcgi";
    String KEY_SEARCH = "/store/1/searchkey.fcgi";
    String GET_CLTSONGS = "/music/1/getcltsongs.fcgi";

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
