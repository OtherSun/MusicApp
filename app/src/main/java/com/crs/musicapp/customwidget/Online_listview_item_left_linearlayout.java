package com.crs.musicapp.customwidget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.crs.musicapp.TopDetailActivity;
import com.crs.musicapp.util.CommonConst;

/**
 * Created by qf on 2016/12/18.
 */

public class Online_listview_item_left_linearlayout extends LinearLayout {
    public Online_listview_item_left_linearlayout(Context context) {
        this(context,null);
    }

    public Online_listview_item_left_linearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(CommonConst.TAG, "LinearLayout " + "dispatchTouchEvent " + "ACTION_DOWN: ");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(CommonConst.TAG, "LinearLayout " + "dispatchTouchEvent " + "ACTION_UP: ");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(CommonConst.TAG, "LinearLayout " + "onTouchEvent " + "ACTION_DOWN: ");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(CommonConst.TAG, "LinearLayout " + "onTouchEvent " + "ACTION_UP: ");
                getContext().startActivity(new Intent(getContext(), TopDetailActivity.class));
                break;
            default:
                break;
        }
//        super.onTouchEvent(event)
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.e(CommonConst.TAG, "LinearLayout " + "onInterceptTouchEvent " + "ACTION_DOWN: ");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(CommonConst.TAG, "LinearLayout " + "onInterceptTouchEvent " + "ACTION_UP: ");
                break;
            default:
                break;
        }
//        super.onInterceptTouchEvent(ev)
        return true;
    }
}
