
package com.crs.musicapp;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crs.musicapp.bean.MusicPlayList;
import com.crs.musicapp.broadcastreceiver.ReceiverBroadcastAction;
import com.crs.musicapp.fragment.MiddleMainFragment;
import com.crs.musicapp.fragment.SearchMainFrament;
import com.crs.musicapp.fragment.SettingMainFragment;

public class MainActivity extends AppCompatActivity implements MiddleMainFragment.OnMainFragmentClickListener, SearchMainFrament.OnMainFragmentClickListener, SettingMainFragment.OnMainFragmentClickListener, View.OnClickListener {
    private MiddleMainFragment middleMainFragment;
    private SearchMainFrament searchMainFrament;
    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private SettingMainFragment settingMainFragment;
    private FragmentTransaction transaction;
    private View musicInfoContainer;
    private boolean isTouch;
    private ReceiverBroadcastAction mReceiver;
    private TextView songNameBtn;
    private TextView singerNameBtn;
    private View musicNoneInfoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        添加中间的碎片布局
        middleMainFragment = new MiddleMainFragment();
        getChangeFragmentView(middleMainFragment, middleMainFragment.isAdded());
        searchMainFrament = new SearchMainFrament();
        settingMainFragment = new SettingMainFragment();
        middleMainFragment.setClickListener(this);
        searchMainFrament.setClickListener(this);
        settingMainFragment.setClickListener(this);
//        找控件
        musicInfoContainer = findViewById(R.id.music_info_container);
        musicNoneInfoContainer = findViewById(R.id.music_none_info_container);
        songNameBtn = (TextView) findViewById(R.id.song_name);
        singerNameBtn = (TextView) findViewById(R.id.singer_name);
//        设置监听事件
        musicInfoContainer.setOnClickListener(this);
        musicNoneInfoContainer.setOnClickListener(this);
//        接收播放广播方法
        receiverPlayingBroadcast();
//        初始化小音乐播放控制台
        initiMusicShowBar();
    }

    private void initiMusicShowBar() {
        if (MusicPlayList.mMusicPlayList.size() == 0) {
            musicInfoContainer.setVisibility(View.GONE);
            musicNoneInfoContainer.setVisibility(View.VISIBLE);
        } else {
            musicNoneInfoContainer.setVisibility(View.GONE);
            musicInfoContainer.setVisibility(View.VISIBLE);
        }
    }

    private void receiverPlayingBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.crs.musicapp.Actions.SyncPlayingMusicState");
        mReceiver = new ReceiverBroadcastAction(musicNoneInfoContainer, musicInfoContainer, songNameBtn, singerNameBtn);
        registerReceiver(mReceiver, intentFilter);
    }

    //    主fragment按钮点击监听事件
    @Override
    public void onActionBarItemClick(View view) {
        switch (view.getId()) {
            case R.id.main_actionBar_search_btn:
                getChangeFragmentView(searchMainFrament, searchMainFrament.isAdded());
                break;
            case R.id.main_actionBar_setting_btn:
                getChangeFragmentView(settingMainFragment, settingMainFragment.isAdded());
                break;
            case R.id.search_hide_button:
                getChangeFragmentView(middleMainFragment, middleMainFragment.isAdded());
                break;
            case R.id.setting_hide_button:
                getChangeFragmentView(middleMainFragment, middleMainFragment.isAdded());
                break;
            default:
                break;
        }
    }

    private void getChangeFragmentView(Fragment fragment, boolean isAdd) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        transaction = fragmentManager.beginTransaction();
        if (!isAdd) {
            transaction.add(R.id.main_container, fragment);
        }
        if (currentFragment != null) {
//            设置fragment切换动画
            if (fragment == middleMainFragment) {
                transaction.setCustomAnimations(R.anim.middle_enter, R.anim.other_out_middle_fragment);
                middleMainFragment.getChangBg().setVisibility(View.GONE);
            } else {
                transaction.setCustomAnimations(R.anim.other_enter_middle_fragment, R.anim.middle_out);
                middleMainFragment.getChangBg().setVisibility(View.VISIBLE);
            }
            transaction.hide(currentFragment);
        }
        transaction.show(fragment);
        transaction.commit();
        currentFragment = fragment;
    }

    //    正在播放界面Activity
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_info_container:
                startActivity(new Intent(this, MusicPlayingActivity.class));
            break;
            case R.id.music_none_info_container:
                Toast.makeText(this, "请添加歌曲", Toast.LENGTH_SHORT).show();
            break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        注销广播
        unregisterReceiver(mReceiver);
    }
}
