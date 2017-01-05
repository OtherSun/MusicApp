package com.crs.musicapp.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.crs.musicapp.bean.MusicPlayList;
import com.crs.musicapp.bean.PlaySongBean;

import java.io.IOException;

/**
 * Created by qf on 2016/12/10.
 */

public class MediaUtil implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static MediaPlayer mMediaPlayer;
    private Context mContext;
    // TODO: 2016/12/15
    private PlaySongBean tempCurrentAddSong;
    private static int tempCurrentTotalTime;

    //    构造方法
    public MediaUtil(Context context) {
        this.mContext = context;
    }

    public static MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        MediaUtil.mMediaPlayer = mediaPlayer;
    }

    /**
     * 歌曲当前事件格式化
     */
    public String getmMediaCurrentTime() {
        if (mMediaPlayer == null)
            return "00:00";
        String s = "00";
        String m = "00";
        int second = mMediaPlayer.getCurrentPosition() / 1000 % 60;
        int minute = mMediaPlayer.getCurrentPosition() / 1000 / 60;
        if (second < 10) {
            s = "0" + String.valueOf(second);
        } else {
            s = String.valueOf(second);
        }
        if (minute < 10) {
            m = "0" + String.valueOf(minute);
        } else {
            m = String.valueOf(minute);
        }
        return m + ":" + s;
    }

    /**
     * 歌曲总时长格式化
     */
    public static String getmMediaTotalTime() {
        if (mMediaPlayer == null)
            return "00:00";
        String s = "00";
        String m = "00";
//        音乐准备好时获取到正确时间
        int second = tempCurrentTotalTime / 1000 % 60;
        int minute = tempCurrentTotalTime / 1000 / 60;
        if (second < 10) {
            s = "0" + String.valueOf(second);
        } else {
            s = String.valueOf(second);
        }
        if (minute < 10) {
            m = "0" + String.valueOf(minute);
        } else {
            m = String.valueOf(minute);
        }
        return m + ":" + s;
    }


    /**
     * 播放歌曲
     *
     * @param playSongBean 播放列表歌曲对象
     */
    public void playMusic(PlaySongBean playSongBean) {
        tempCurrentAddSong = playSongBean;
        Uri songUri = Uri.parse(playSongBean.getM4a());
        try {
//            实例化MediaPlayer
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
//
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mContext, songUri);
//            错误监听事件
            mMediaPlayer.setOnErrorListener(this);
//            准备事件接口回调
            mMediaPlayer.setOnPreparedListener(this);
//            准备事件新建子线程
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playPreviousMusic() {
        if (MusicPlayList.mMusicPlayList != null && MusicPlayList.mMusicPlayList.size() != 0) {
            int indexOf = MusicPlayList.getIndex();
            if (indexOf > 0) {
                PlaySongBean playSongBean = MusicPlayList.mMusicPlayList.get(indexOf - 1);
                playMusic(playSongBean);
            }
            if (indexOf == 0) {
                PlaySongBean playSongBean = MusicPlayList.mMusicPlayList.get(indexOf);
                playMusic(playSongBean);
            }
        } else {
            Toast.makeText(mContext, "无可播放歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    public void playNextMusic() {
        if (MusicPlayList.mMusicPlayList != null && MusicPlayList.mMusicPlayList.size() != 0) {
            int indexOf = MusicPlayList.getIndex();
            if (indexOf + 1 < MusicPlayList.mMusicPlayList.size()) {
                PlaySongBean playSongBean = MusicPlayList.mMusicPlayList.get(indexOf + 1);
                playMusic(playSongBean);
            }
            if (indexOf + 1 == MusicPlayList.mMusicPlayList.size()) {
                PlaySongBean playSongBean = MusicPlayList.mMusicPlayList.get(indexOf);
                playMusic(playSongBean);
            }
        } else {
            Toast.makeText(mContext, "无可播放歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 释放音频占用的资源
     */
    public static void releaseMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    //    音乐准备事件，接口实现方法
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        tempCurrentTotalTime = mp.getDuration();
        MusicPlayList.addMusicPlayList(tempCurrentAddSong);
//        发送播放广播
        Intent intent = new Intent();
        intent.setAction("com.crs.musicapp.Actions.SyncPlayingMusicState");
        mContext.sendBroadcast(intent);
    }

    //    错误事件接口实现方法
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mp != null && what == 261) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            Toast.makeText(mContext, "此歌曲资源不存在", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void isPlay() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
        } else {
            Toast.makeText(mContext, "未添加播放的歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    //    音乐播放完事件监听
    @Override
    public void onCompletion(MediaPlayer mp) {
        int currentPosition = mp.getCurrentPosition();
        int totalPosition = mp.getDuration();
//        Log.e(CommonConst.TAG, "onCompletion: "+"currentPosition : "+currentPosition+" totalPosition : "+totalPosition);
//        获取的音乐播放完的时间与总时间不一致，需要相差50毫秒左右
        if (currentPosition >= totalPosition - 50) {
            int indexOf = MusicPlayList.getIndex();
//            控制循环播放，最后一首重0开始，否则下一曲
            if (indexOf + 1 == MusicPlayList.mMusicPlayList.size()) {
                PlaySongBean playSongBean = MusicPlayList.mMusicPlayList.get(0);
                playMusic(playSongBean);
                return;
            }
            playNextMusic();
        }
    }
}
