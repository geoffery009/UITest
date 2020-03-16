package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.customview.widget.ViewDragHelper;

public class MyDragToDetailLayout extends ViewGroup {
    ViewDragHelper viewDragHelper;

    private float startY;

    public MyDragToDetailLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                startY = child.getTop();
                return true;
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (changedView == getChildAt(0)) {
                    getChildAt(1).offsetTopAndBottom(dy);//上下布局一起移动
                } else if (changedView == getChildAt(1)) {
                    getChildAt(0).offsetTopAndBottom(dy);//上下布局一起移动
                }
                ViewCompat.postInvalidateOnAnimation(MyDragToDetailLayout.this);
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {

                if (releasedChild == getChildAt(0)) {
                    if (yvel < -1000 || releasedChild.getTop() < -200) {//上滑动条件
                        startY = -releasedChild.getHeight();
                    }
                }
                if (releasedChild == getChildAt(1)) {
                    if (yvel > 1000 || releasedChild.getTop() > 200) {//下滑动条件
                        startY = releasedChild.getHeight();
                    }
                }
                Constant.LOG("yvel: " + yvel + " , height: " + startY);

                if (viewDragHelper.settleCapturedViewAt(0, (int) startY)) {
                    ViewCompat.postInvalidateOnAnimation(MyDragToDetailLayout.this);
                }
            }


            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                return 0;//不可水平移动
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                int finalTop = top;
                if (child == getChildAt(0)) {
                    finalTop = finalTop > 0 ? 0 : finalTop;//只允许上拉
                } else if (child == getChildAt(1)) {
                    finalTop = finalTop < 0 ? 0 : finalTop;//只允许下拉
                }
                return finalTop;
            }

        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean isConflict = false;
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (getChildAt(1).getScrollY() > 0) {//处理冲突
                isConflict = true;
            }
        }

        if (!isConflict) {
            viewDragHelper.processTouchEvent(ev);
            return super.onInterceptTouchEvent(ev);
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Constant.LOG("MyDragToDetailLayout onTouchEvent");
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildAt(0).getTop() == 0) {
            getChildAt(0).layout(l, t, r, b);
            getChildAt(1).layout(l, t, r, b);
            getChildAt(1).offsetTopAndBottom(getChildAt(0).getMeasuredHeight());
        } else {
            getChildAt(0).layout(l, getChildAt(0).getTop(), r, getChildAt(0).getBottom());
            getChildAt(1).layout(l, getChildAt(1).getTop(), r, getChildAt(1).getBottom());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }
}
