package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;

public class MyBtnScroller extends RelativeLayout {
    Scroller mScroller;

    public MyBtnScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(getContext());
    }


    public void skip() {
        if (getMeasuredWidth() > 0) {
            boolean flag = (getMeasuredWidth() - getPaddingLeft() - getPaddingLeft()) > (Math.abs(mScroller.getCurrX()) + getChildAt(0).getMeasuredWidth());
            Constant.LOG("flag: " + flag);
            mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(),
                    (flag ? -1 : 1) * 100, -100,2000);

            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        boolean flag = mScroller.computeScrollOffset();
        Constant.LOG("x--" + mScroller.getCurrX() + " , y--" + mScroller.getCurrY()
                + " ,getMeasuredWidth--" + getMeasuredWidth()
                + " , child getMeasuredWidth--" + getChildAt(0).getMeasuredWidth()
                + " , getPaddingLeft--" + getPaddingLeft());
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
