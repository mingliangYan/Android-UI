package com.yml.ui.widget.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.yml.ui.R;
import com.yml.ui.widget.gesture.callback.OnResultListener;

import java.util.ArrayList;
import java.util.List;

public class GestureView extends ViewGroup {

    private Context mContext;

    private int mSize;

    private int mPadding;

    private int mSpace;

    private String mPassword;

    private int mCheckoutTime;

    private Paint mPaint;

    private float x, y;


    private int COLOR_DEFAULT;
    private int COLOR_ERROR;
    private int COLOR_SELECTED;
    private int COLOR_LINE_SELECTED;
    private int COLOR_LINE_ERROR;

    private StringBuilder mResult = new StringBuilder();

    private List<TouchAreaView> mSelectedViews = new ArrayList<>();

    public GestureView(Context context) {
        this(context, null);
    }

    public GestureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initAttrs(context, attrs);
        addCircleView();
        setWillNotDraw(false);
        COLOR_LINE_SELECTED = Color.argb(Color.alpha(COLOR_SELECTED) >> 1, Color.red(COLOR_SELECTED), Color.green(COLOR_SELECTED), Color.blue(COLOR_SELECTED));
        COLOR_LINE_ERROR = Color.argb(Color.alpha(COLOR_ERROR) >> 1, Color.red(COLOR_ERROR), Color.green(COLOR_ERROR), Color.blue(COLOR_ERROR));
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(COLOR_LINE_SELECTED);
        mPaint.setStyle(Paint.Style.STROKE);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureSelf(widthMeasureSpec, heightMeasureSpec);
        measureChildren();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (resetting) {
            return true;
        }
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }

        TouchAreaView child = null;
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        this.x = x;
        this.y = y;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isTouchUp = false;
                for (int i = 0; i < childCount; i++) {
                    child = (TouchAreaView) getChildAt(i);
                    if (mSelectedViews.contains(child)) {
                        continue;
                    }

                    if (child.drawIfSelected(x, y)) {
                        mSelectedViews.add(child);
                        mResult.append(child.val);
                        break;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                isTouchUp = true;
                if (mSelectedViews.isEmpty()) {
                    break;
                }
                if (!mResult.toString().equals(mPassword)) {
                    for(TouchAreaView view: mSelectedViews) {
                        view.drawErrorArea();
                        mPaint.setColor(COLOR_LINE_ERROR);
                    }
                    
                    if (mListener != null) {
                        mListener.onError(mResult.toString());
                    }

                } else {
                    if (mListener != null) {
                        mListener.onSuccess(mResult.toString());
                    }

                }

                resetView();
                break;
        }
        postInvalidate();
        return true;
    }

    private boolean isTouchUp;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        TouchAreaView currChild;
        TouchAreaView nextChild;
        for (int i=0; i<mSelectedViews.size(); i++) {
            currChild = mSelectedViews.get(i);
            if (i < mSelectedViews.size() - 1) {
                nextChild = mSelectedViews.get(i + 1);
                canvas.drawLine(currChild.x, currChild.y, nextChild.x, nextChild.y, mPaint);
            } else {
                if (!isTouchUp) {
                    canvas.drawLine(currChild.x, currChild.y, x, y, mPaint);
                }

            }
        }

    }

    private boolean resetting = false;

    private void resetView() {
        resetting = true;
        final int[] count = {0};
        for(TouchAreaView view: mSelectedViews) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.reset();
                    if (count[0] == mSelectedViews.size() - 1) {
                        resetting = false;
                        count[0] = 0;
                        mSelectedViews.clear();
                        mResult.delete(0, mResult.length());
                    }
                    count[0]++;
                }
            }, mCheckoutTime);
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                mPaint.setColor(COLOR_LINE_SELECTED);
                postInvalidate();
            }
        }, mCheckoutTime);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }

        View child;
        int childId = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                child = getChildAt(childId);
                int child_w = child.getMeasuredWidth();
                int child_h = child.getMeasuredHeight();
                int left = j * child_w + j * mSpace + mPadding;
                int top = i * child_h + i * mSpace + mPadding;
                int right = left + child_w;
                int bottom = top + child_h;
                child.layout(left, top, right, bottom);

                childId ++;
            }
        }
    }

    private void measureSelf(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, 300);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(heightSize, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize, widthSize);
        } else {
            int size = Math.min(widthSize, heightSize);
            setMeasuredDimension(size, size);
        }
        mSize = getMeasuredWidth();
    }

    private void measureChildren() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }


        int mode = MeasureSpec.EXACTLY;
        int size = (mSize - mPadding * 2 - mSpace * 2) / 3;
        int measureSpec = MeasureSpec.makeMeasureSpec(size, mode);

        View child;
        //measure children
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            child.measure(measureSpec, measureSpec);
        }
        mPaint.setStrokeWidth(size / 12);
    }

    private int getDefaultPadding() {
        int defaultVal =  Math.max( Math.max(Math.max(getPaddingLeft(), getPaddingTop()), getPaddingRight()), getPaddingBottom());
        return defaultVal == 0? 1: defaultVal;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GestureView);
        mSpace = (int)array.getDimension(R.styleable.GestureView_space, 20);
        int password = array.getInteger(R.styleable.GestureView_password, 1);
        mCheckoutTime= array.getInteger(R.styleable.GestureView_checkout_time, 1000);
        mPassword = String.valueOf(password);

        COLOR_DEFAULT = array.getColor(R.styleable.GestureView_default_color, Color.GRAY);
        COLOR_SELECTED = array.getColor(R.styleable.GestureView_selected_color, Color.GREEN);
        COLOR_ERROR = array.getColor(R.styleable.GestureView_error_color, Color.RED);

        Log.i("Ges", "space = " + mSpace + "    pass = " + mPassword + "    checkout_time = " + mCheckoutTime);
        array.recycle();
        mPadding = getDefaultPadding();
    }

    private void addCircleView() {
        addView(new TouchAreaView(mContext, "1", COLOR_DEFAULT, COLOR_SELECTED, COLOR_ERROR));
        addView(new TouchAreaView(mContext, "2", COLOR_DEFAULT, COLOR_SELECTED, COLOR_ERROR));
        addView(new TouchAreaView(mContext, "3", COLOR_DEFAULT, COLOR_SELECTED, COLOR_ERROR));
        addView(new TouchAreaView(mContext, "4", COLOR_DEFAULT, COLOR_SELECTED, COLOR_ERROR));
        addView(new TouchAreaView(mContext, "5", COLOR_DEFAULT, COLOR_SELECTED, COLOR_ERROR));
        addView(new TouchAreaView(mContext, "6", COLOR_DEFAULT, COLOR_SELECTED, COLOR_ERROR));
        addView(new TouchAreaView(mContext, "7", COLOR_DEFAULT, COLOR_SELECTED, COLOR_ERROR));
        addView(new TouchAreaView(mContext, "8", COLOR_DEFAULT, COLOR_SELECTED, COLOR_ERROR));
        addView(new TouchAreaView(mContext, "9", COLOR_DEFAULT, COLOR_SELECTED, COLOR_ERROR));
    }

    public String getPassword() {
        return mPassword;
    }

    //密码不可重复
    public boolean setPassword(String password) {
        boolean result = false;
        try {
            Integer.parseInt(password);
            this.mPassword = password;
            result = true;
        } catch (NumberFormatException e) {
            Log.e("GestureView", "setPassword error: " + e.getMessage());
        }

        return result;
    }

    private OnResultListener mListener;

    public void setResultListener(OnResultListener listener) {
        this.mListener = listener;
    }
}
