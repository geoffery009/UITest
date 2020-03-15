package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * Scroller恢复+阻尼滑动+SwipeRefreshLayout下拉刷新(NestedScrollView冲突解决)
 */
public class MyZuNiFrameLayoutWithScrollerViewForSwipeRefresh extends MyZuNiFrameLayout {
    float downY;

    float lastY;//记录增量，更新点击滑动的位置scrollBy
    float totalY;//记录释放时控件需要滑动的位置
    float zuNiTotalY;
    Scroller mScroller;

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    private OnScrollHeightListener onScrollHeightListener;

    public float getZuNiTotalY() {
        return zuNiTotalY;
    }

    public interface OnScrollHeightListener {
        void onScrollTopHeight(float value);
    }

    public void onScrollHeightListener(OnScrollHeightListener onScrollHeightListener) {
        this.onScrollHeightListener = onScrollHeightListener;
    }

    public MyZuNiFrameLayoutWithScrollerViewForSwipeRefresh(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        totalY = 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Constant.LOG("MyZuNiFrameLayout onInterceptTouchEvent");
        if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            Constant.LOG("down");
            lastY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getParent();
            NestedScrollView scrollView = (NestedScrollView) getChildAt(0);
            if (lastY - ev.getY() < 0 && swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()
                    && scrollView.getScrollY() == 0) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Constant.LOG("MyZuNiLayout onTouchEvent");
        if (mScroller.computeScrollOffset()) {//滑动没结束
            return super.onTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {

            int moveCount = (int) (ev.getY() - lastY);
            Constant.LOG("move--y: " + moveCount + " ，ev.getY()： " + ev.getY() + " ， lastY: " + lastY + " , iszuli: " + isZuNi);

            zuNiTotalY = totalY;
            if (!isZuNi) {
                scrollBy(0, -moveCount);//点击滑动
            } else {
                zuNiTotalY = (int) zuNiScroll(totalY);
                scrollTo(0, -(int) zuNiTotalY);
            }
            invalidate();

            lastY = ev.getY();//若阻力时，lastY值不是增量滑动的实际值，后面不可用
            totalY += moveCount;

            if (this.onScrollHeightListener != null) {
                this.onScrollHeightListener.onScrollTopHeight(zuNiTotalY);
            }
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            Constant.LOG("down");
            downY = ev.getY();
            lastY = downY;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            Constant.LOG("up--" + getScaleY());

            mScroller.startScroll(0, (int) -zuNiTotalY, 0, (int) zuNiTotalY, 600);//释放返回
            invalidate();

            totalY = 0;//
            zuNiTotalY = 0;
            lastY = 0;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        boolean flag = mScroller.computeScrollOffset();
        Constant.LOG("x--" + mScroller.getCurrX() + " , y--" + mScroller.getCurrY());
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


    private boolean isZuNi = true;

    private float zuNiScroll(float totalY) {//阻尼函数处理
        return totalY / 5;
    }
}
