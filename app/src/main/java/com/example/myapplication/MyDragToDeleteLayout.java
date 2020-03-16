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
import androidx.customview.widget.ViewDragHelper;
import androidx.customview.widget.ViewDragHelper.Callback;

public class MyDragToDeleteLayout extends FrameLayout {
    ViewDragHelper viewDragHelper;

    private float startX, startY;

    public MyDragToDeleteLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, new Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                startX = child.getX();
                startY = child.getY();
                return true;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                isCloseDelete = true;
                if (releasedChild == getChildAt(0)) {
                    if (releasedChild.getLeft() <= -Math.abs(deleteLayoutWidth)/2) {
                        viewDragHelper.settleCapturedViewAt(-Math.abs(deleteLayoutWidth), (int) startY);//open
                        isCloseDelete = false;
                    } else {
                        viewDragHelper.settleCapturedViewAt(0, 0);
                    }
                } else if (releasedChild == getChildAt(1)) {
                    if (releasedChild.getLeft() == getMeasuredWidth() - deleteLayoutWidth) {
                        viewDragHelper.settleCapturedViewAt(getMeasuredWidth() - deleteLayoutWidth, (int) startY);//open
                        isCloseDelete = false;
                    } else {
                        viewDragHelper.settleCapturedViewAt(getMeasuredWidth(), 0);
                    }
                }
                invalidate();


                Constant.LOG("onViewReleased " + isCloseDelete);
                if (onDeleteListener != null) {
                    onDeleteListener.onLayoutChange();
                }
            }


            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (changedView == getChildAt(0)) {
                    getChildAt(1).offsetLeftAndRight(dx);
                } else if (changedView == getChildAt(1)) {
                    getChildAt(0).offsetLeftAndRight(dx);
                }
                if (getChildAt(0).getLeft() < 0) {

                    Constant.LOG("onSliding ");
                    if (onDeleteListener != null) {
                        onDeleteListener.onSliding();
                    }
                }
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                int finalLeft = left;
                if (child == getChildAt(0)) {
                    if (left > 0) {
                        finalLeft = 0;
                    } else if (left < -Math.abs(deleteLayoutWidth)) {
                        finalLeft = -Math.abs(deleteLayoutWidth);
                    }
                }
                if (child == getChildAt(1)) {
                    Constant.LOG("left: " + left);
                    if (left < getMeasuredWidth() - deleteLayoutWidth) {
                        finalLeft = getMeasuredWidth() - deleteLayoutWidth;
                    }
                }
                return finalLeft;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return 0;
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private int deleteLayoutWidth;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getChildAt(0).layout(l, t, r, getChildAt(0).getBottom());
        deleteLayoutWidth = getChildAt(1).getMeasuredWidth();
        getChildAt(1).layout(l, t, r, getChildAt(0).getBottom());
        getChildAt(1).offsetLeftAndRight(r);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }


    private boolean isCloseDelete;

    public void closeDelete(boolean isSmooth) {
        if (isSmooth) {
            viewDragHelper.smoothSlideViewTo(getChildAt(0), 0, 0);//动画在继续
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            getChildAt(0).layout(0, 0, getMeasuredWidth(), getChildAt(0).getBottom());
            getChildAt(1).layout(0, 0, getMeasuredWidth(), getChildAt(0).getBottom());
            getChildAt(1).offsetLeftAndRight(getMeasuredWidth());
        }
    }

    public void openDelete(boolean isSmooth) {
        if (isSmooth) {
            viewDragHelper.smoothSlideViewTo(getChildAt(0), -Math.abs(deleteLayoutWidth), 0);//动画在继续
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            getChildAt(0).layout(-deleteLayoutWidth, 0, getMeasuredWidth() - deleteLayoutWidth, getChildAt(0).getBottom());
            getChildAt(1).layout(getMeasuredWidth() - deleteLayoutWidth, 0, getMeasuredWidth(), getChildAt(0).getBottom());
        }
    }

    public boolean isCloseDelete() {
        return isCloseDelete;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    private OnDeleteListener onDeleteListener;

    public interface OnDeleteListener {
        void onLayoutChange();//结束滑动时

        void onSliding();//开始滑动打开
    }
}
