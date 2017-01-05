package com.crs.musicapp.fragment.middle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.crs.musicapp.R;
import com.crs.musicapp.TopDetailActivity;
import com.crs.musicapp.adapter.OnlineContentListViewAdapter;
import com.crs.musicapp.bean.MusicTopBean;
import com.crs.musicapp.util.CommonConst;
import com.crs.musicapp.util.HttpUtil;
import com.crs.musicapp.util.JsonUtil;
import com.crs.musicapp.util.ThreadPoolUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qf on 2016/11/29.
 */

public class OnlineSecondaryFragment extends Fragment implements AdapterView.OnItemClickListener {
    private View inflateView;
    private OnlineContentListViewAdapter onlineContentListViewAdapter;
    private ListView musicTopsListView;
    private int[] topIdArray = new int[]{
            26,
            23,
            5,
            6,
            3,
            19,
            18,
            16,
            17,
    };
    private String[] topNameArray = new String[]{
            "热歌",
            "销量",
            "内地",
            "港台",
            "欧美",
            "摇滚",
            "民谣",
            "韩国",
            "日本",
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO: 2016/12/8
            switch (msg.what) {
                case CommonConst.HANDLER_MSG_TOP_SUCCESS_CODE:
                    Map<String, Object> musicTopBeanMap = (Map<String, Object>) msg.obj;
                    onlineContentListViewAdapter.updateData(musicTopBeanMap, msg.arg1);
                    break;
                default:
                    break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflateView == null) {
            inflateView = inflater.inflate(R.layout.fragment_online_secondary, container, false);
//            找控件
            musicTopsListView = (ListView) inflateView.findViewById(R.id.music_tops_listView);
//            适配器设置
            onlineContentListViewAdapter = new OnlineContentListViewAdapter(inflater.getContext());
            musicTopsListView.setAdapter(onlineContentListViewAdapter);
//            初始化item，并下载更新item
            for (int i = 0; i < topIdArray.length; i++) {
                final int topPosition = i;
                final Map<String, Object> musicTopBeanMap = new HashMap<>();
                musicTopBeanMap.put("name", topNameArray[topPosition]);
                musicTopBeanMap.put("data", null);
                onlineContentListViewAdapter.addData(musicTopBeanMap);
                ThreadPoolUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: 2016/12/8
                        String json = HttpUtil.getTop(CommonConst.TOP_PATH, topIdArray[topPosition]);
                        if (json != null) {
                            MusicTopBean musicTopBean = JsonUtil.parseMusicTopJson(json);
                            musicTopBeanMap.put("data", musicTopBean);
                            Message msg = Message.obtain();
                            msg.what = CommonConst.HANDLER_MSG_TOP_SUCCESS_CODE;
                            msg.obj = musicTopBeanMap;
                            msg.arg1 = topPosition;
                            mHandler.sendMessage(msg);
                        }
                    }
                });
            }

        }
//        listView监听事件
        musicTopsListView.setOnItemClickListener(this);
        return inflateView;
    }


    //    listview点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        启动榜单详情页
        Intent intent = new Intent();
        intent.setClass(getContext(), TopDetailActivity.class);
        intent.putExtra("name", "第" + position + "页面");
        startActivity(intent);
    }

}
