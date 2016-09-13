package com.jake.smart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Jake on 2016/9/12.
 * Perhaps I would be single for the rest of my life
 */
class SmartButton extends View implements ValueAnimator.AnimatorUpdateListener {

    private float mPercent;
    /**
     * the max length of X
     */
    private int mLength;

    private float mMinRadius = 6;

    private float mMaxRadius = mMinRadius;


    private float mCenterX;

    private float mCenterY;

    private Paint mPaint;

    private Paint mBackgroundPaint;

    private RectF mLeftRectF = new RectF();

    private RectF mRightRectF = new RectF();

    public SmartButton(Context context) {
        super(context);
        init();
    }

    public SmartButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.parseColor("#b4282d"));
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setShadowLayer(15,0,0,Color.parseColor("#40000000"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawCenter(canvas);
        drawContent(canvas);
    }

    private void drawCenter(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mMinRadius, mPaint);
    }

    private void adjustCanvas(Canvas canvas, float angle) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(angle);
        canvas.translate(-mCenterX, -mCenterY);
    }

    private void drawContent(Canvas canvas) {
        int length = (int) ((mLength - 2 * mMinRadius) * mPercent);
        float deltaRadius = mMaxRadius - mMinRadius;
        float radius = mMinRadius + mPercent * deltaRadius;
        drawLeft(canvas, radius, length);
        drawRight(canvas, radius, length);
    }

    private void drawLeft(Canvas canvas, float radius, int length) {
        adjustCanvas(canvas, 45 * mPercent);
        float start = mCenterX - mLength / 2;
        mLeftRectF.set(start, mCenterY - radius, start + length + 2 * mMinRadius, mCenterY + radius);
        canvas.drawRoundRect(mLeftRectF, radius, radius, mPaint);
        canvas.restore();

    }


    private void drawRight(Canvas canvas, float radius, int length) {
        adjustCanvas(canvas, -45 * mPercent);
        float start = mCenterX + mLength / 2;
        mRightRectF.set(start - length - 2 * mMinRadius, mCenterY - radius, start, mCenterY + radius);
        canvas.drawRoundRect(mRightRectF, radius, radius, mPaint);
        canvas.restore();
    }

    private void drawBackground(Canvas canvas) {

        canvas.drawCircle(mCenterX, mCenterY, getMeasuredWidth() / 2 - 10, mBackgroundPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size);
        mCenterX = getMeasuredWidth() / 2.f;
        mCenterY = getMeasuredHeight() / 2.f;
        mLength = 80;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mPercent = (float) valueAnimator.getAnimatedValue() / 100.f;
        invalidate();
    }


    Paint getBackgroundPaint(){
        return mBackgroundPaint;
    }
}
