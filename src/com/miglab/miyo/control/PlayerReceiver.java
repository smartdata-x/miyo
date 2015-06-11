package com.miglab.miyo.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.miglab.miyo.constant.MusicServiceDefine;
import com.miglab.miyo.entity.MusicType;
import com.miglab.miyo.entity.SongInfo;

/**
 * Created by fanglei
 * Email: 412552696@qq.com
 * Date: 2015/6/11.
 */
public class PlayerReceiver extends BroadcastReceiver {
    private IPlayAction playAction;

    public PlayerReceiver(IPlayAction i) {
        this.playAction = i;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        int commend = bundle.getInt(MusicServiceDefine.PLAY_WHAT);
        if (commend <= 0) {
            return;
        }

        switch (commend) {
            case MusicServiceDefine.ALBUN_NULL:
                playAction.albumNull();
                break;

            case MusicServiceDefine.MUSIC_CHANGE:
                if (bundle.containsKey(MusicServiceDefine.PLAY_INFO)) {
                    SongInfo info = (SongInfo) bundle
                            .getSerializable(MusicServiceDefine.PLAY_INFO);
                    MusicType musicType = (MusicType) bundle.getSerializable(MusicServiceDefine.PLAY_PARAM1);
                    MusicManager.getInstance().setMusicType(musicType);
                    MusicManager.getInstance().setSongInfo(info);
                    playAction.musicChange();
                }
                break;

            case MusicServiceDefine.MUSIC_PREPARE:
                int time1 = bundle.getInt(MusicServiceDefine.PLAY_PARAM1);
                playAction.musicPrepare(time1);
                break;

            case MusicServiceDefine.MUSIC_PLAYING:
                int time2 = bundle.getInt(MusicServiceDefine.PLAY_PARAM1);
                playAction.musicPlaying(time2);
                break;

            case MusicServiceDefine.ACTIVITY_CLOSE:
                break;
            case MusicServiceDefine.UPDATE_LIST_SUCCESS:
                playAction.updateListSuccess();
                break;
            case MusicServiceDefine.UPDATE_LIST_NONE:
                playAction.updateListNone();
                break;
            case MusicServiceDefine.STOP_TO_NEXT:
                //切换到下一首时，对当前播放歌曲界面处理
                playAction.stopToNext();
                break;
        }
    }
}
