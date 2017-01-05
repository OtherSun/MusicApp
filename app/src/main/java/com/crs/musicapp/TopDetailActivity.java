package com.crs.musicapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.crs.musicapp.adapter.TopDetailFragmentPagerAdapter;
import com.crs.musicapp.fragment.topdetail.TopListFragment;
import com.crs.musicapp.fragment.topdetail.TopLyricsFragment;
import com.crs.musicapp.fragment.topdetail.TopSummaryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qf on 2016/12/18.
 */

public class TopDetailActivity extends AppCompatActivity {
    private TextView textView;
    private TopListFragment topListFragment;
    private TopSummaryFragment topSummaryFragment;
    private TopLyricsFragment topLyricsFragment;
    private List<Fragment> fragments;
    private FragmentManager fragmentManager;
    private ViewPager topDetailContentViewPager;
    private TopDetailFragmentPagerAdapter topDetailFragmentPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_detail);
//        找控件
        topDetailContentViewPager = (ViewPager) findViewById(R.id.top_detail_content_viewpager);
//        fragment集合
        fragments = new ArrayList<Fragment>();
        topListFragment = new TopListFragment();
        topSummaryFragment = new TopSummaryFragment();
        topLyricsFragment = new TopLyricsFragment();
        fragments.add(topListFragment);
        fragments.add(topSummaryFragment);
        fragments.add(topLyricsFragment);
        fragmentManager = getSupportFragmentManager();
        topDetailFragmentPagerAdapter = new TopDetailFragmentPagerAdapter(fragmentManager, fragments);
        topDetailContentViewPager.setAdapter(topDetailFragmentPagerAdapter);
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("name");
//        textView = (TextView) findViewById(R.id.top_text);
//        textView.setText(name);
    }

}
