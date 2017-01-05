package com.crs.musicapp.customwidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.crs.musicapp.util.CommonConst;

/**
 * Created by qf on 2016/12/1.
 */

public class CircleHeadImage extends ImageView {
    private Drawable myDrawable;
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private boolean isAdd;

    public CircleHeadImage(Context context) {
        this(context, null);
    }

    public CircleHeadImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
//        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
       /* switch (modeWidth) {
            case MeasureSpec.AT_MOST:
                Log.e("test", "MeasureSpec.AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.e("test", "MeasureSpec.EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.e("test", "MeasureSpec.UNSPECIFIED");
                break;
            default:
                break;
        }
        switch (modeHeight) {
            case MeasureSpec.AT_MOST:
                Log.e("test", "MeasureSpec.AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.e("test", "MeasureSpec.EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.e("test", "MeasureSpec.UNSPECIFIED");
                break;
            default:
                break;
        }*/
/*        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heigth = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("test", "modeWidth: " + modeWidth + "modeHeight: " + modeHeight);
        Log.e("test", "width: " + width + "heigth: " + heigth);*/
//        myDrawable = getDrawable();
        if (myDrawable != null) {
            Bitmap bitmap = ((BitmapDrawable) myDrawable).getBitmap();  //获取到ImageView的bitmap
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("test", "widthMeasureSpec: " + getMeasuredWidth() + "heightMeasureSpec: " + getMeasuredHeight());
    }


    private void setImage() {
//        myDrawable = getDrawable();
        Log.e("test", "myDrawable: " + myDrawable);
        Bitmap bitmap = ((BitmapDrawable) myDrawable).getBitmap();
        mBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
//        mCanvas.drawColor(0x00000000);

//        画个圆
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        int radius = Math.min(w, h) >> 1;
        mCanvas.drawCircle(w >> 1, h >> 1, radius, mPaint);

//        图层重叠模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mCanvas.drawBitmap(bitmap, 0, 0, mPaint);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (myDrawable != null) {
            if (!isAdd)
                setImage();
            isAdd = true;
            canvas.drawBitmap(mBitmap, 0, 0, null);
            Log.e(CommonConst.TAG, "mBitmap: " + mBitmap);
        } else {
            Log.e(CommonConst.TAG, "onDraw: ");
            super.onDraw(canvas);
        }
    }

    private void init() {
        myDrawable = getDrawable();
        mPaint = new Paint();
//        mPaint.setColor(0xffff0000);
        mPaint.setAntiAlias(true);
        Log.e("test", "init: ");
    }

}
