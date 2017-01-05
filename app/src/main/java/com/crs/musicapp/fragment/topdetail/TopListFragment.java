package com.crs.musicapp.fragment.topdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crs.musicapp.R;

/**
 * Created by qf on 2016/12/23.
 */

public class TopListFragment extends Fragment {

    private View inflateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflateView == null) {
            inflateView = inflater.inflate(R.layout.top_detail_listview,container,false);
        }
        return inflateView;
    }
}
