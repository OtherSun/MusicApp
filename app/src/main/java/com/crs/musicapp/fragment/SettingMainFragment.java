package com.crs.musicapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crs.musicapp.R;

/**
 * Created by qf on 2016/11/27.
 */

public class SettingMainFragment extends Fragment implements View.OnClickListener {
    private View inflateView;
    private View settingHideBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflateView == null) {
            inflateView = inflater.inflate(R.layout.fragment_setting_main,container,false);
            settingHideBtn = inflateView.findViewById(R.id.setting_hide_button);
        }
        settingHideBtn.setOnClickListener(this);
        return inflateView;
    }

    @Override
    public void onClick(View view) {
        if (clickListener == null)
            return;
        clickListener.onActionBarItemClick(view);
    }

    private OnMainFragmentClickListener clickListener;

    public void setClickListener(OnMainFragmentClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnMainFragmentClickListener {
        void onActionBarItemClick(View view);
    }
}
