package com.jake.smart;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake on 2016/9/12.
 * Perhaps I would be single for the rest of my life
 */
public class SmartMenu extends ViewGroup implements View.OnClickListener,
        ValueAnimator.AnimatorUpdateListener,
        Animator.AnimatorListener {
    /**
     * measurement unit is dp
     */
    private static final int DEFAULT_PADDING = 10;
    /**
     * measurement unit is dp
     */
    private static final int DEFAULT_BTN_SIZE = 70;


    private int mOuterPadding;

    private int mInnerPadding;

    private int mMenuHeight;

    private int mVerticalPadding;

    private SmartButton mSwitchBtn;

    private int mSwitchBtnSize;

    private BaseAdapter mAdapter;

    private ValueAnimator mSwitchAnimation;

    private boolean mOpen = false;

    private RectF mRect = new RectF();

    private ArrayList<ArrayList<View>> mViews = new ArrayList<>();

    private ValueAnimator mScaleAnimator;

    private int mSwitchDuration = 300;

    private int mScaleDuration = 100;

    private int mCurrentTargetPosition = 0;

    private float mDotRadius;

    private int mDotDistance;

    private int mBackgroundColor;

    private int mShadowColor;

    private int mDotColor;

    private void init() {
        mSwitchBtn = new SmartButton(getContext());
        mSwitchBtn.setOnClickListener(this);
        mSwitchBtn.setRadius(mDotRadius);
        mSwitchBtn.setLength(mDotDistance);
        mSwitchBtn.setDotColor(mDotColor);
        mSwitchBtn.setShadowColor(mShadowColor);
        mSwitchBtn.setBackgroundColor(mBackgroundColor);
        initScaleAnimator();
        initSwitchAnimator();
    }

    public SmartMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmartMenu);
        mOuterPadding = ta.getDimensionPixelSize(R.styleable.SmartMenu_outer_padding, dip2px(context, DEFAULT_PADDING));
        mInnerPadding = ta.getDimensionPixelSize(R.styleable.SmartMenu_inner_padding, dip2px(context, DEFAULT_PADDING));
        mVerticalPadding = ta.getDimensionPixelSize(R.styleable.SmartMenu_vertical_padding, dip2px(context, DEFAULT_PADDING));
        mSwitchBtnSize = ta.getDimensionPixelSize(R.styleable.SmartMenu_smart_btn_size, dip2px(context, DEFAULT_BTN_SIZE));
        mDotRadius = ta.getDimensionPixelSize(R.styleable.SmartMenu_dot_radius, dip2px(context, 1));
        mDotDistance = ta.getDimensionPixelSize(R.styleable.SmartMenu_dot_distance, dip2px(context, 25));
        mDotColor = ta.getColor(R.styleable.SmartMenu_dot_color,Color.WHITE);
        mShadowColor = ta.getColor(R.styleable.SmartMenu_shadow_color,Color.parseColor("#40000000"));
        mBackgroundColor = ta.getColor(R.styleable.SmartMenu_bg_color,Color.parseColor("#b4282d"));
        mMenuHeight = mSwitchBtnSize - 2 * mVerticalPadding;
        ta.recycle();
        init();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int layerCount = (int) Math.ceil((getChildCount() - 1) / 2);
        int centerX = getMeasuredWidth() / 2;
        if (layerCount <= 0)
            return;
        left = mOuterPadding;
        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(left, mVerticalPadding, left + view.getMeasuredWidth(), getMeasuredHeight() - mVerticalPadding);
            left += view.getMeasuredWidth() + mInnerPadding;
            if (i == layerCount) {
                left += mInnerPadding + mSwitchBtnSize;
            }
        }
        mSwitchBtn.layout(centerX - mSwitchBtnSize / 2, 0, centerX + mSwitchBtnSize / 2, getMeasuredHeight());
    }

    private void setViewScale(View view, float scale) {
        if (scale == 0)
            view.setVisibility(View.GONE);
        else if (view.getVisibility() != View.VISIBLE)
            view.setVisibility(View.VISIBLE);
        view.setPivotX(view.getMeasuredWidth() / 2);
        view.setPivotY(view.getMeasuredHeight() / 2);
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("setAdapter must be call in UI thread");
        }
        fillLayout();
    }

    private void fillLayout() {
        removeAllViews();
        addView(mSwitchBtn, new LayoutParams(mSwitchBtnSize, mSwitchBtnSize));
        mViews.clear();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View view = mAdapter.getView(i, null, this);
            view.setVisibility(View.GONE);
            addView(view,
                    new LayoutParams(LayoutParams.WRAP_CONTENT, mMenuHeight));
        }
        int j = 0;
        for (int i = (getChildCount() - 1) / 2; i > 0; i--) {
            ArrayList<View> viewList = new ArrayList<>();
            viewList.add(getChildAt(i));
            mViews.add(j++, viewList);
        }
        j = 0;
        for (int i = getChildCount() / 2 + 1; i < getChildCount(); i++) {
            mViews.get(j).add(getChildAt(i));
            j++;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mSwitchBtnSize;
        int childHeightSpec = MeasureSpec.makeMeasureSpec(mMenuHeight, MeasureSpec.AT_MOST);
        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, childHeightSpec);
            width += view.getMeasuredWidth();
            width += mInnerPadding;
        }
        mSwitchBtn.measure(MeasureSpec.makeMeasureSpec(mSwitchBtnSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mSwitchBtnSize, MeasureSpec.EXACTLY));
        width += 2 * mOuterPadding;
        setMeasuredDimension(width, mSwitchBtnSize);
        if (mRect.left == 0)
            mRect.set(getMeasuredWidth() / 2, mVerticalPadding,
                    getMeasuredWidth() / 2, getMeasuredHeight() - mVerticalPadding);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof SmartButton) {
            toggle();
        }
    }

    public void toggle() {
        if (mSwitchAnimation.isRunning() || mScaleAnimator.isRunning())
            return;
        if (mOpen)
            mSwitchAnimation.setFloatValues(100, 0);
        else
            mSwitchAnimation.setFloatValues(0, 100);
        mOpen = !mOpen;
        mSwitchAnimation.start();
    }
    private void initScaleAnimator() {
        if (mScaleAnimator == null) {
            mScaleAnimator = ValueAnimator.ofFloat(0, 1f);
            mScaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    List<View> views = mViews.get(mCurrentTargetPosition);
                    for (View view : views) {
                        setViewScale(view, (Float) valueAnimator.getAnimatedValue());
                    }
                }
            });
            mScaleAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (mOpen) {
                        mCurrentTargetPosition++;
                        showItems();
                    } else {
                        mCurrentTargetPosition--;
                        hideItems();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mScaleAnimator.setDuration(mScaleDuration);
        }

    }
    private void initSwitchAnimator() {
        if (mSwitchAnimation == null) {
            mSwitchAnimation = ValueAnimator.ofFloat(0, 100);
            mSwitchAnimation.setDuration(mSwitchDuration);
            mSwitchAnimation.addUpdateListener(this);
            if (mSwitchBtn != null)
                mSwitchAnimation.addUpdateListener(mSwitchBtn);
            mSwitchAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            mSwitchAnimation.addListener(this);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float start = getMeasuredWidth() / 2;
        float end = getMeasuredWidth() / 2;
        float length = getMeasuredWidth() / 2 - 10;
        float percent = (float) valueAnimator.getAnimatedValue() / 100.f;
        length = length * percent;
        mRect.set(start - length, mVerticalPadding, end + length, getMeasuredHeight() - mVerticalPadding);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mRect != null) {
            canvas.drawRoundRect(mRect, mSwitchBtnSize / 2, mSwitchBtnSize / 2, mSwitchBtn.getBackgroundPaint());
        }
        super.dispatchDraw(canvas);
    }

    private void showItems() {
        if (mCurrentTargetPosition >= mViews.size()) {
            mCurrentTargetPosition = mViews.size() - 1;
            return;
        }
        mScaleAnimator.setFloatValues(0, 1);
        mScaleAnimator.start();
    }

    private void hideItems() {
        if (mCurrentTargetPosition < 0) {
            mCurrentTargetPosition = 0;
            return;
        }
        mScaleAnimator.setFloatValues(1, 0);
        mScaleAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animator) {
        if (!mOpen)
            hideItems();
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (mOpen) {
            showItems();
        }
    }




    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    public void setSwitchDuration(int duration) {
        mSwitchDuration = duration;
    }

    public void setScaleDuration(int duration) {
        mScaleDuration = duration;
    }
}
