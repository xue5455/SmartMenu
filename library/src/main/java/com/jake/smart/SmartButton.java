package com.jake.smart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jake on 2016/9/12.
 * Perhaps I would be single for the rest of my life
 */
public class SmartButton extends View implements ValueAnimator.AnimatorUpdateListener {
    private final double COS_45 = Math.sqrt(2) / 2;


    private float mPercent;
    /**
     * the max length of X
     */
    private int mLength;

    private float mMinRadius = 5;

    private float mMaxRadius = mMinRadius + 3;


    private float mCenterX;

    private float mCenterY;

    private Paint mPaint;

    public SmartButton(Context context) {
        super(context);
        init();
    }

    public SmartButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawCenter(canvas);
        drawLeft(canvas);
        drawRight(canvas);
    }

    private void drawCenter(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mMinRadius, mPaint);
    }

    private void drawLeft(Canvas canvas) {
        canvas.save();
        float angle = 45 * mPercent;
        canvas.rotate(angle);
        float length = mLength * mPercent;
        float deltaRadius = mMaxRadius - mMinRadius;
        float radius = mMinRadius + mPercent * deltaRadius;
        double l = mLength / Math.cos(arcToDegree(angle));
        float start = (float) ((l - length) / 2);
        mPaint.setStrokeWidth(2 * radius);
        canvas.drawCircle(radius, mCenterY, radius, mPaint);
        canvas.drawLine(0, mCenterY, 0 + length, mCenterY, mPaint);
        canvas.restore();
    }

    private void drawRight(Canvas canvas) {
       /* canvas.save();
        canvas.rotate(-45 * mPercent);*/
        canvas.drawCircle(mLength-mMinRadius,mCenterY,mMinRadius,mPaint);
    }

    private void drawBackground(Canvas canvas){
        mPaint.setColor(Color.RED);
        canvas.drawCircle(mCenterX,mCenterY,getMeasuredWidth()/2,mPaint);
        mPaint.setColor(Color.WHITE);
    }

    private double arcToDegree(float angle) {
        return angle * Math.PI / 180;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = getMeasuredWidth() / 2.f;
        mCenterY = getMeasuredHeight() / 2.f;
        mLength = getMeasuredWidth();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mPercent = (float) valueAnimator.getAnimatedValue();
        invalidate();
    }

    public void open() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(this);
        valueAnimator.start();
    }
}
