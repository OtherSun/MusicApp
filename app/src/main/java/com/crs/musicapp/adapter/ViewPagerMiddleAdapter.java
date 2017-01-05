package com.crs.musicapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qf on 2016/11/29.
 */

public class ViewPagerMiddleAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments = new ArrayList<>();

    public ViewPagerMiddleAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
