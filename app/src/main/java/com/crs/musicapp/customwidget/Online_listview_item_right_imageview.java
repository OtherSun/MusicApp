package com.crs.musicapp.customwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.crs.musicapp.util.CommonConst;

/**
 * Created by qf on 2016/12/18.
 */

public class Online_listview_item_right_imageview extends ImageView {
    public Online_listview_item_right_imageview(Context context) {
        this(context,null);
    }

    public Online_listview_item_right_imageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(CommonConst.TAG, "ImageView " + "dispatchTouchEvent " + "ACTION_DOWN: ");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(CommonConst.TAG, "ImageView " + "dispatchTouchEvent " + "ACTION_UP: ");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(CommonConst.TAG, "ImageView " + "onTouchEvent " + "ACTION_DOWN: ");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(CommonConst.TAG, "ImageView " + "onTouchEvent " + "ACTION_UP: ");
                String tag = (String) getTag();
                Log.e(CommonConst.TAG, "onTouchEvent: "+ tag);
                break;
            default:
                break;
        }
        return true;
    }

}
