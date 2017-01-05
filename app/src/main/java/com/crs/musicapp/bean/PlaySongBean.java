package com.crs.musicapp.bean;

/**
 * Created by qf on 2016/12/13.
 */

public class PlaySongBean {
    public PlaySongBean() {
    }

    public PlaySongBean(String m4a, String albumname, String albumpic_big, String albumpic_small, String downUrl, String singerid, String singername, String songid, String songmid, String songname) {
        this.m4a = m4a;
        this.albumname = albumname;
        this.albumpic_big = albumpic_big;
        this.albumpic_small = albumpic_small;
        this.downUrl = downUrl;
        this.singerid = singerid;
        this.singername = singername;
        this.songid = songid;
        this.songmid = songmid;
        this.songname = songname;
    }

    public String getM4a() {
        return m4a;
    }

    public void setM4a(String m4a) {
        this.m4a = m4a;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAlbumpic_big() {
        return albumpic_big;
    }

    public void setAlbumpic_big(String albumpic_big) {
        this.albumpic_big = albumpic_big;
    }

    public String getAlbumpic_small() {
        return albumpic_small;
    }

    public void setAlbumpic_small(String albumpic_small) {
        this.albumpic_small = albumpic_small;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getSingerid() {
        return singerid;
    }

    public void setSingerid(String singerid) {
        this.singerid = singerid;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getSongmid() {
        return songmid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    private String m4a;
//    private String albumid;
    private String albumname;
    private String albumpic_big;
    private String albumpic_small;
    private String downUrl;
    private String singerid;
    private String singername;
    private String songid;
    private String songmid;
    private String songname;
}
