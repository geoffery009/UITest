package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyLeftScrollToDeleteLinearLayout extends LinearLayout {
    public MyLeftScrollToDeleteLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        setOrientation(HORIZONTAL);
    }

    private float scrollXForDown;//按下时的x点击位置
    private boolean isDeleteShow;
    private float deleteWidth = 0;//删除布局宽度
    private Scroller mScroller;

    private boolean isLongClick;

    public boolean isDeleteShow() {
        return isDeleteShow;
    }

    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
    }

    public float getDeleteWidth() {
        return deleteWidth;
    }

    public void setDeleteWidth(float deleteWidth) {
        this.deleteWidth = deleteWidth;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        Constant.LOG("MyLeftScrollToDeleteFrameLayout dispatchTouchEvent " + deleteWidth);
        if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            scrollXForDown = ev.getX();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (!isLongClick) {
                return super.dispatchTouchEvent(ev);
            }
            if (deleteWidth == 0) {
                return super.dispatchTouchEvent(ev);
            }
            if (ev.getX() - scrollXForDown < 0) {//向左

                if (Math.abs(getScrollX()) + (int) Math.abs((ev.getX() - scrollXForDown)) < deleteWidth) {
                    scrollBy((int) Math.abs((ev.getX() - scrollXForDown)), 0);
                    invalidate();
                } else {
                    scrollBy((int) Math.abs((deleteWidth - Math.abs(getScrollX()))), 0);
                    invalidate();
                }
                if (Math.abs(getScrollX()) >= deleteWidth) {
                    isDeleteShow = true;
                }
                Constant.LOG("MyLeftScrollToDeleteFrameLayout left " + getScrollX() + " , " + isDeleteShow);
            } else if (ev.getX() - scrollXForDown >= 0) {//向右

                if (getScrollX() > 0) {
                    scrollBy(-(int) Math.abs((ev.getX() - scrollXForDown)), 0);
                    invalidate();
                }
                isDeleteShow = false;
                Constant.LOG("MyLeftScrollToDeleteFrameLayout right " + getScrollX() + " , " + isDeleteShow);
            }
            scrollXForDown = ev.getX();
            return true;

        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            dismissDeleteLayout();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void dismissDeleteLayout() {
        if (!isDeleteShow) {
            Constant.LOG("MyLeftScrollToDeleteFrameLayout return " + getScrollX() + " , " + isDeleteShow);
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 500);
            invalidate();
            isLongClick = false;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        boolean flag = mScroller.computeScrollOffset();
        Constant.LOG("MyLeftScrollToDeleteFrameLayout x--" + mScroller.getCurrX() + " , y--" + mScroller.getCurrY());
        //递归终止条件:滑动结束
        if (flag == false) {
            return;
        } else {
            //mScroller.getCurrX(),mScroller.getCurrY()记录的是此刻要滑动达到的目标坐标
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        }
        //递归调用
        invalidate();//或者postInvalidate()
    }


}
