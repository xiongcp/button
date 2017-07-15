package com.julyone.view.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;



public class CircleButton extends RelativeLayout {

    public final static String TAG = "CircleButton";
    private static final int UPDATE_VIEW_MESSAGE = 10001;


    private static final int CIRCLE_COLOR = 0x20FFFFFF;


    private static final long POST_DURATION = 10;
    private static final int DRAW_COUNT = 35;


    private float mShadowRadius;
    private int mCurrentDrawCount = 0;
    private boolean mPressed = false;
    private boolean mDraw = false;

    private Paint mPaint;
    private float mMaxRadius;
    private float mTouchX = 0;
    private float mTouchY = 0;


    public CircleButton(Context context) {
        this(context, null);
    }


    public CircleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(mOnTouchListener);
        setClickable(true);
    }
    private Paint getPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(CIRCLE_COLOR);
            mPaint.setAntiAlias(true);
        }
        return mPaint;
    }
    public void setTouchColor(int color, int alpha) {


        getPaint().setColor(color);
        if (alpha >= 0 && alpha <= 255) {
            getPaint().setAlpha(alpha);
        }
    }
    private float getMaxRadius() {


        float width = getWidth();
        float height = getHeight();
        Log.e(TAG, "width = " + width);
        Log.e(TAG, "height = " + height);
        float drawHeight;
        float drawWidth;
        if (mTouchX > width / 2) {
            if (mTouchY > height / 2) {
                drawHeight = mTouchY;
                drawWidth = mTouchX;
            } else {
                drawHeight = height - mTouchY;
                drawWidth = mTouchX;
            }
        } else {
            if (mTouchY > height / 2) {
                drawHeight = mTouchY;
                drawWidth = width - mTouchX;
            } else {
                drawHeight = height - mTouchY;
                drawWidth = width - mTouchX;
            }
        }
        mMaxRadius = (float) Math.sqrt((drawHeight * drawHeight)
                + (drawWidth * drawWidth));


        return mMaxRadius;
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mTouchX = event.getX();
                    mTouchY = event.getY();
                    Log.e(TAG, "mTouchX = " + mTouchX);
                    Log.e(TAG, "mTouchY = " + mTouchY);
                    startDrawCircle();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mPressed = false;
                    if (!mDraw) {
                        postInvalidateDelayed(POST_DURATION);
                    }
                    break;
                default:
                    break;
            }
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.dispatchTouchEvent(event);
            }
            return false;
        }
    };

    private void startDrawCircle() {
        mPressed = true;
        mDraw = true;
        mCurrentDrawCount = 1;
        postInvalidateDelayed(1);


        mHandler.removeMessages(UPDATE_VIEW_MESSAGE);
        mHandler.sendEmptyMessage(UPDATE_VIEW_MESSAGE);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VIEW_MESSAGE:
                    mCurrentDrawCount++;
                    if (mCurrentDrawCount > DRAW_COUNT) {
                        mCurrentDrawCount = DRAW_COUNT;
                        mDraw = false;
                        if (!mPressed) {
                            postInvalidateDelayed(POST_DURATION);
                        }
                    }else {
                        mShadowRadius = getMaxRadius() * mCurrentDrawCount
                                / DRAW_COUNT;
                        postInvalidateDelayed(1);
                        mHandler.removeMessages(UPDATE_VIEW_MESSAGE);
                        mHandler.sendEmptyMessageDelayed(UPDATE_VIEW_MESSAGE,
                                POST_DURATION);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!mPressed && !mDraw) {
            super.dispatchDraw(canvas);
            return;
        }
        canvas.drawCircle(mTouchX, mTouchY, mShadowRadius, getPaint());
        super.dispatchDraw(canvas);
    }
}
