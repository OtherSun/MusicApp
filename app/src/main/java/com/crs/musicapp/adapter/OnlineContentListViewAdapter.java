package com.crs.musicapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crs.musicapp.R;
import com.crs.musicapp.bean.MusicTopBean;
import com.crs.musicapp.util.CommonConst;
import com.crs.musicapp.util.HttpUtil;
import com.crs.musicapp.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qf on 2016/12/6.
 */

public class OnlineContentListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private LayoutInflater inflater;
    private Context mContext;
    private List<Map<String, Object>> mMusicTopBeanList = new ArrayList<>();
    private LruCache<String, Bitmap> bitmapCache;
    private View onlineContentTop;
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
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonConst.HANDLER_MSG_TOP_IMAGE_CODE:
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    public void addData(Map<String, Object> musicTopBeanMap) {
        mMusicTopBeanList.add(musicTopBeanMap);
        notifyDataSetChanged();
    }

    public void updateData(Map<String, Object> musicTopBeanMap, int position) {
        this.mMusicTopBeanList.set(position, musicTopBeanMap);
        notifyDataSetChanged();
    }

    public OnlineContentListViewAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        int bitmapMemorySize = (int) (Runtime.getRuntime().totalMemory() / 8);
        bitmapCache = new LruCache<>(bitmapMemorySize);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.online_listview_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
//            找控件,放入viewholder
            viewHolder.onlineListViewItemTopName = (TextView) convertView.findViewById(R.id.online_music_top_name);
            viewHolder.musicTopOneImage = (ImageView) convertView.findViewById(R.id.online_top1_img);
            viewHolder.musicTopOneName = (TextView) convertView.findViewById(R.id.online_music_top1);
            viewHolder.musicTopTwoName = (TextView) convertView.findViewById(R.id.online_music_top2);
            viewHolder.musicTopThreeName = (TextView) convertView.findViewById(R.id.online_music_top3);
            viewHolder.musicTopFourName = (TextView) convertView.findViewById(R.id.online_music_top4);
            viewHolder.musicTopFiveName = (TextView) convertView.findViewById(R.id.online_music_top5);
            convertView.setTag(viewHolder);
        }
//        找控件
        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        Map<String, Object> musicTopBeanMap = mMusicTopBeanList.get(position);
        viewHolder.onlineListViewItemTopName.setText(musicTopBeanMap.get("name") + "榜");
        MusicTopBean musicTopBean = (MusicTopBean) musicTopBeanMap.get("data");
        if (musicTopBean != null && musicTopBean.getShowapi_res_body() != null && musicTopBean.getShowapi_res_body().getPagebean() != null) {
            List<MusicTopBean.Showapi_res_body.Pagebean.Song> songlist = musicTopBean.getShowapi_res_body().getPagebean().getSonglist();
            for (int i = 0; i < CommonConst.TOP_FIVE; i++) {
                MusicTopBean.Showapi_res_body.Pagebean.Song song = songlist.get(i);
                String songName = song.getSongname();
                String singerName = song.getSingername();
                switch (i) {
                    case 0:
                        viewHolder.musicTopOneName.setText("TOP1." + songName + " - " + singerName);
                        final String albumpic_big_url = song.getAlbumpic_big();
                        Bitmap topBitmap = bitmapCache.get(albumpic_big_url);
                        if (topBitmap == null) {
                            viewHolder.musicTopOneImage.setImageResource(R.mipmap.online_top_default_image);
                            ThreadPoolUtil.execute(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap topBitmap = HttpUtil.getTopBitmap(albumpic_big_url);
                                    if (topBitmap != null) {
                                        Message msg = Message.obtain();
                                        msg.what = CommonConst.HANDLER_MSG_TOP_IMAGE_CODE;
                                        msg.obj = viewHolder.musicTopOneImage;
                                        bitmapCache.put(albumpic_big_url, topBitmap);
                                        mHandler.sendMessage(msg);
                                    }
                                }
                            });
                        } else {
                            viewHolder.musicTopOneImage.setImageBitmap(topBitmap);
                        }
                        break;
                    case 1:
                        viewHolder.musicTopTwoName.setText("TOP2." + songName + " - " + singerName);
                        break;
                    case 2:
                        viewHolder.musicTopThreeName.setText("TOP3." + songName + " - " + singerName);
                        break;
                    case 3:
                        viewHolder.musicTopFourName.setText("TOP4." + songName + " - " + singerName);
                        break;
                    case 4:
                        viewHolder.musicTopFiveName.setText("TOP5." + songName + " - " + singerName);
                        break;
                    default:
                        break;
                }
            }
        }
        return convertView;
    }

    //    按钮点击事件
    @Override
    public void onClick(View view) {
        // TODO: 2016/12/19
        switch (view.getId()) {
            case R.id.Top_head:
                break;
            case R.id.online_top1_img:
                break;
            default:
                break;
        }
    }

    private class ViewHolder {
        private TextView onlineListViewItemTopName;
        private ImageView musicTopOneImage;
        private TextView musicTopOneName;
        private TextView musicTopTwoName;
        private TextView musicTopThreeName;
        private TextView musicTopFourName;
        private TextView musicTopFiveName;
    }

    @Override
    public int getCount() {
        return mMusicTopBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusicTopBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
