package com.crs.musicapp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qf on 2016/12/13.
 */

public class MusicPlayList {
    public static List<PlaySongBean> mMusicPlayList = new ArrayList();

    private static PlaySongBean currentAddSong;

    public static PlaySongBean getCurrentAddSong() {
        return currentAddSong;
    }

    public static void updateMusicPlayList(List<PlaySongBean> musicPlayList) {
        mMusicPlayList.addAll(musicPlayList);
    }

    public static int getIndex() {
        int indexOf = -1;
        if (currentAddSong != null) {
            indexOf = mMusicPlayList.indexOf(currentAddSong);
        }
        return indexOf;
    }

    public static void addMusicPlayList(PlaySongBean playSong) {
        currentAddSong = playSong;
        if (!mMusicPlayList.contains(playSong)) {
            mMusicPlayList.add(playSong);
        }
    }

    public static void delMusicPlaySong(PlaySongBean playSong) {
        mMusicPlayList.remove(playSong);
    }
}
