package com.crs.musicapp;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crs.musicapp.bean.MusicPlayList;
import com.crs.musicapp.broadcastreceiver.ReceiverBroadcastAction;
import com.crs.musicapp.util.MediaUtil;

/**
 * Created by qf on 2016/11/29.
 */

public class MusicPlayingActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private View activityMusicBackBtn;
    private View musicPreviousBtn;
    private View musicIsPlayBtn;
    private View musicNextBtn;
    private TextView musicPlayCurrentTime;
    private SeekBar musicPlayCurrentProgress;
    private TextView musicPlayTotalTime;
    private MediaUtil mMediaUtil;
    private Handler mHandler = new Handler();
    private ReceiverBroadcastAction mReceiver;
    private TextView songNameBtn;
    private TextView singerNameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playing);
//        实例化媒体工具类
        if (mMediaUtil == null)
            mMediaUtil = new MediaUtil(this);
//        找控件
        activityMusicBackBtn = findViewById(R.id.activity_music_playing_back);
        musicPreviousBtn = findViewById(R.id.music_previous_btn);
        musicIsPlayBtn = findViewById(R.id.music_isPlay_btn);
        musicNextBtn = findViewById(R.id.music_next_btn);
        songNameBtn = (TextView) findViewById(R.id.playing_music_name);
        singerNameBtn = (TextView) findViewById(R.id.playing_singer_name);
        musicPlayCurrentTime = (TextView) findViewById(R.id.music_playing_current_time);
        musicPlayCurrentProgress = (SeekBar) findViewById(R.id.music_playing_current_progress);
        musicPlayTotalTime = (TextView) findViewById(R.id.music_playing_total_time);
//        监听器
        activityMusicBackBtn.setOnClickListener(this);
        musicPreviousBtn.setOnClickListener(this);
        musicIsPlayBtn.setOnClickListener(this);
        musicNextBtn.setOnClickListener(this);
        musicPlayCurrentProgress.setOnSeekBarChangeListener(this);
//        更新歌曲进度
        updateCurrentState();
//        接收播放广播方法
        receiverPlayingBroadcast();
    }

    private void receiverPlayingBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.crs.musicapp.Actions.SyncPlayingMusicState");
        mReceiver = new ReceiverBroadcastAction(musicPlayTotalTime, songNameBtn, singerNameBtn, mMediaUtil);
        registerReceiver(mReceiver, intentFilter);
//        初始化时没接到播放广播，自己初始化播放歌曲信息
        if (MusicPlayList.getCurrentAddSong() == null)
            return;
        songNameBtn.setText(MusicPlayList.getCurrentAddSong().getSongname());
        singerNameBtn.setText(MusicPlayList.getCurrentAddSong().getSingername());
//        设置歌曲总时长,playing初始化时没接到播放广播,
        musicPlayTotalTime.setText(MediaUtil.getmMediaTotalTime());
    }


    /**
     * 更新正在播放歌曲的时间和进度条
     */
    public void updateCurrentState() {

        if (MediaUtil.getmMediaPlayer() != null) {
            int currentProgress = (int) (100f * MediaUtil.getmMediaPlayer().getCurrentPosition() / MediaUtil.getmMediaPlayer().getDuration());
            musicPlayCurrentProgress.setProgress(currentProgress);
            musicPlayCurrentTime.setText(mMediaUtil.getmMediaCurrentTime());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateCurrentState();
                }
            }, 1000);
        } else {
            musicPlayCurrentProgress.setProgress(0);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.music_previous_btn:
                mMediaUtil.playPreviousMusic();
                break;
            case R.id.music_isPlay_btn:
                mMediaUtil.isPlay();
                break;
            case R.id.music_next_btn:
                mMediaUtil.playNextMusic();
                break;
            case R.id.activity_music_playing_back:
//                playing退出键
                break;
            default:
                break;
        }
    }

    //    seekbar进度改变监听事件
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    //    seekbar进度开始拖动监听事件
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    //    seekbar进度停止拖动监听事件
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if (MediaUtil.getmMediaPlayer() != null) {
            MediaUtil.getmMediaPlayer().seekTo((int) (MediaUtil.getmMediaPlayer().getDuration() * progress * 0.01f));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
