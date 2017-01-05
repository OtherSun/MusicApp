package com.crs.musicapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.crs.musicapp.R;
import com.crs.musicapp.adapter.ViewPagerMiddleAdapter;
import com.crs.musicapp.fragment.middle.DiscoverSecondaryFragment;
import com.crs.musicapp.fragment.middle.MineSecondaryFragment;
import com.crs.musicapp.fragment.middle.OnlineSecondaryFragment;
import com.crs.musicapp.util.CommonConst;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qf on 2016/11/26.
 */

public class MiddleMainFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private View inflateView;
    private View searchBtn;
    private View settingBtn;
    private ViewPager viewPagerMiddle;
    private ViewPagerMiddleAdapter viewPagerMiddleAdapter;
    private List<Fragment> fragments;
    private DiscoverSecondaryFragment discoverSecondaryFragment;
    private MineSecondaryFragment mineSecondaryFragment;
    private OnlineSecondaryFragment onlineSecondaryFragment;
    private FragmentManager childFragmentManager;
    private RadioGroup radioGroupMiddle;
    private RadioButton mineRadioBtnMiddle;
    private RadioButton onlineRadioBtnMiddle;
    private RadioButton discoverRadioBtnMiddle;
    private RadioButton childRadioBtn;

    public View getChangBg() {
        return changBg;
    }

    private View changBg;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflateView == null) {
//            布局管理器解析布局，并放入到父容器中
            inflateView = inflater.inflate(R.layout.fragment_middle_main, container, false);
//            找到布局中的控件
            searchBtn = inflateView.findViewById(R.id.main_actionBar_search_btn);
            settingBtn = inflateView.findViewById(R.id.main_actionBar_setting_btn);
            changBg = inflateView.findViewById(R.id.change_bg);
            viewPagerMiddle = (ViewPager) inflateView.findViewById(R.id.middle_main_viewPager);
            radioGroupMiddle = (RadioGroup) inflateView.findViewById(R.id.middle_main_RadioGroup);
            mineRadioBtnMiddle = (RadioButton) radioGroupMiddle.findViewById(R.id.mine_radioBtn);
            onlineRadioBtnMiddle = (RadioButton) radioGroupMiddle.findViewById(R.id.online_radioBtn);
            discoverRadioBtnMiddle = (RadioButton) radioGroupMiddle.findViewById(R.id.discover_radioBtn);
//            新建middle fragment的二级fragment对象
            discoverSecondaryFragment = new DiscoverSecondaryFragment();
            mineSecondaryFragment = new MineSecondaryFragment();
            onlineSecondaryFragment = new OnlineSecondaryFragment();
//            把二级fragment放入到集合中
            fragments = new ArrayList<>();
            fragments.add(mineSecondaryFragment);
            fragments.add(onlineSecondaryFragment);
            fragments.add(discoverSecondaryFragment);
//            获取fragment管理器，因为容器是fragment，所以布局管理器不用getSupportFragment
            childFragmentManager = getChildFragmentManager();
//            新建FramentPagerAdapter对象，需要参数fragment管理器和页面fragment集合，设置ViewPager适配器
//            // TODO: 2016/11/29 适配器设置一次
            viewPagerMiddleAdapter = new ViewPagerMiddleAdapter(childFragmentManager, fragments);
            viewPagerMiddle.setAdapter(viewPagerMiddleAdapter);
        }
//        中间一级fragment的按钮点击监听器
        searchBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        viewPagerMiddle.addOnPageChangeListener(this);
        viewPagerMiddle.setOffscreenPageLimit(CommonConst.LIMIT_DESTROY_PAGE);
        radioGroupMiddle.setOnCheckedChangeListener(this);
        return inflateView;
    }

    //ViewPager翻页监听事件
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < radioGroupMiddle.getChildCount(); i++) {
            childRadioBtn = (RadioButton) radioGroupMiddle.getChildAt(i);
            if (i == position) {
                childRadioBtn.setChecked(true);
                childRadioBtn.setTextAppearance(getContext(), R.style.MainActionBarRadioButton_isChecked);
//                Log.e(CommonConst.TAG, "单机单机");
            } else {
                childRadioBtn.setTextAppearance(getContext(), R.style.MainActionBarRadioButton);
//                Log.e(CommonConst.TAG, "点击点击" );
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //actionBar点击事件
    @Override
    public void onClick(View view) {
        if (clickListener == null)
            return;
        clickListener.onActionBarItemClick(view);
    }

    //单选按钮点击事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mine_radioBtn:
                viewPagerMiddle.setCurrentItem(CommonConst.MINE_VIEWPAGER_POSITION, true);
                break;
            case R.id.online_radioBtn:
                viewPagerMiddle.setCurrentItem(CommonConst.ONLINE_VIEWPAGER_POSITION, true);
                break;
            case R.id.discover_radioBtn:
                viewPagerMiddle.setCurrentItem(CommonConst.DISCOVER_VIEWPAGER_POSITION, true);
                break;
            default:
                break;
        }
    }

    //触发接口回调参数
    private OnMainFragmentClickListener clickListener;

    //设置参数
    public void setClickListener(OnMainFragmentClickListener clickListener) {
        this.clickListener = clickListener;
    }

    //接口回调
    public interface OnMainFragmentClickListener {
        void onActionBarItemClick(View view);
    }
}
