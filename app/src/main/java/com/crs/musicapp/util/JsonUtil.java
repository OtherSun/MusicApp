package com.crs.musicapp.util;

import com.crs.musicapp.bean.MusicTopBean;
import com.crs.musicapp.bean.SearchContentBean;
import com.google.gson.Gson;

/**
 * Created by qf on 2016/12/3.
 */

public class JsonUtil {
    public static SearchContentBean parseQueryJson(String json){
        Gson gson = new Gson();
        SearchContentBean searchContentBean = gson.fromJson(json, SearchContentBean.class);
        return searchContentBean;
    }
    public static MusicTopBean parseMusicTopJson(String json){
        Gson gson = new Gson();
        if (json != null) {
            MusicTopBean musicTopBean = gson.fromJson(json, MusicTopBean.class);
            return musicTopBean;
        }
        return null;
    }
}
