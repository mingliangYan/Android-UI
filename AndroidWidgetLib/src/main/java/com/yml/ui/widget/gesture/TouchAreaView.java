package com.yml.ui.widget.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TouchAreaView extends View {

    private Paint mPaint;

    String val;

    Status mStatus;

    float x, y;

    private int COLOR_NORMAL;
    private int COLOR_SELECTED;
    private int COLOR_ERROR;



    public TouchAreaView(Context context, String val, int normal, int select, int error) {
        this(context);
        this.COLOR_NORMAL = normal;
        this.COLOR_SELECTED = select;
        this.COLOR_ERROR = error;
        this.val = val;
    }

    public TouchAreaView(Context context) {
        super(context);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mStatus = Status.NORMAL;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int color;
        switch (mStatus) {
            case ERROR:
            case SELECTED:
                color = mStatus == Status.ERROR? COLOR_ERROR: COLOR_SELECTED;
                drawInside(canvas, color);
                break;
            default:
                color = COLOR_NORMAL;
                break;
        }
        drawOutside(canvas, color);
    }

    private void drawOutside(Canvas canvas, int color) {

        if (mStatus == Status.NORMAL) {
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(getMeasuredWidth() / 50);
        } else {
            int gradualColor = Color.argb(Color.alpha(color) >> 1, Color.red(color), Color.green(color), Color.blue(color));
            mPaint.setColor(gradualColor);
            mPaint.setStyle(Paint.Style.FILL);

        }
        canvas.drawCircle(getMeasuredWidth() >> 1, getMeasuredHeight() >> 1, (getMeasuredWidth() >> 1) - 5, mPaint);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        x = (right + left) >> 1;
        y = (bottom + top) >> 1;
    }

    private void drawInside(Canvas canvas, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getMeasuredWidth() >> 1, getMeasuredHeight() >> 1, getMeasuredWidth() >> 2, mPaint);
    }

    public boolean drawIfSelected(float x, float y) {
        boolean selected = x >= getLeft() && x <= getRight() && y >= getTop() && y<= getBottom();
        if (selected && mStatus == Status.NORMAL) {
            mStatus = Status.SELECTED;
            postInvalidate();
        }
        return selected;
    }

    public void drawErrorArea() {
        mStatus = Status.ERROR;
        postInvalidate();
    }

    public void reset() {
        mStatus = Status.NORMAL;
        postInvalidate();
    }

}
