package com.crs.musicapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.crs.musicapp.bean.MusicPlayList;
import com.crs.musicapp.util.MediaUtil;

/**
 * Created by qf on 2016/12/15.
 */

public class ReceiverBroadcastAction extends BroadcastReceiver {
    private View containerNone;
    private View container;
    private TextView mToTalTime;
    private MediaUtil mMediaUtil;
    private String mAction;
    private TextView mSongName;
    private TextView mSingerName;
    private TextView songName;
    private TextView singerName;

    public ReceiverBroadcastAction() {
    }
    public ReceiverBroadcastAction(View containerNone,View container,TextView songName, TextView singerName) {
        this.containerNone = containerNone;
        this.container = container;
        this.songName = songName;
        this.singerName = singerName;
    }

    public ReceiverBroadcastAction(TextView totalTimeTv,TextView songNameTv,TextView singerNameTv, MediaUtil mediaUtil) {
        this.mToTalTime = totalTimeTv;
        this.mMediaUtil = mediaUtil;
        this.mSongName = songNameTv;
        this.mSingerName = singerNameTv;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mAction = intent.getAction();
        if (mAction.equals("com.crs.musicapp.Actions.SyncPlayingMusicState")) {
//            刷新所有正在播放歌曲信息
            if (mToTalTime != null) {
//                设置歌曲总时长,接到播放广播时才刷新
                mToTalTime.setText(mMediaUtil.getmMediaTotalTime());
                mSongName.setText(MusicPlayList.getCurrentAddSong().getSongname());
                mSingerName.setText(MusicPlayList.getCurrentAddSong().getSingername());
            }
            if (songName != null) {
                containerNone.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
                songName.setText(MusicPlayList.getCurrentAddSong().getSongname());
                singerName.setText(MusicPlayList.getCurrentAddSong().getSingername());

            }
        }
    }
}
