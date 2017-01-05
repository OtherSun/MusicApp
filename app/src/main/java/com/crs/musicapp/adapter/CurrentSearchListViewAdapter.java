package com.crs.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crs.musicapp.R;
import com.crs.musicapp.bean.SearchContentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qf on 2016/12/3.
 */

public class CurrentSearchListViewAdapter extends BaseAdapter {
    private Context mContext;

    public List<SearchContentBean.Showapi_res_body.Pagebean.QuerySong> getContentlist() {
        return contentlist;
    }

    private List<SearchContentBean.Showapi_res_body.Pagebean.QuerySong> contentlist = new ArrayList<>();
    private LayoutInflater inflater;
    public void updateData(List<SearchContentBean.Showapi_res_body.Pagebean.QuerySong> contentlist){
        this.contentlist = contentlist;
        notifyDataSetChanged();
    }
    public void addData(List<SearchContentBean.Showapi_res_body.Pagebean.QuerySong> contentlist){
        this.contentlist.addAll(contentlist);
        notifyDataSetChanged();
    }

    public CurrentSearchListViewAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contentlist.size();
    }

    @Override
    public Object getItem(int position) {
        return contentlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.current_search_content_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.songName = (TextView) convertView.findViewById(R.id.song_name);
            holder.singerAlbumInfo = (TextView) convertView.findViewById(R.id.singer_album_info);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        SearchContentBean.Showapi_res_body.Pagebean.QuerySong searchSong = contentlist.get(position);
        holder.songName.setText(searchSong.getSongname());
        holder.singerAlbumInfo.setText(searchSong.getSingername()+" - "+searchSong.getAlbumname());
        return convertView;
    }



    private class ViewHolder {
        private TextView songName;
        private TextView singerAlbumInfo;
    }
}
