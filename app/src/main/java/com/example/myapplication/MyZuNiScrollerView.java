package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class MyZuNiScrollerView extends NestedScrollView {
    public MyZuNiScrollerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private float startDownYForDown;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Constant.LOG("MyZuNiScrollerView ACTION--" + getScrollY() + " , " + getChildAt(0).getMeasuredHeight() + " , " + getHeight());
        if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            if (getScrollY() == 0) {//下滑
                startDownYForDown = ev.getY();
            }
            if (getChildAt(0).getMeasuredHeight() == getScrollY() + getMeasuredHeight()) {//上滑:scrollview达到底部
                startDownYForDown = ev.getY();
            }
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {

            if (getScrollY() == 0 && (ev.getY() - startDownYForDown) > 0) {//下滑:scrollview达到顶部
                Constant.LOG("MyZuNiScrollerView top move --down");
                MyZuNiFrameLayout viewGroup = (MyZuNiFrameLayout) getParent();
                viewGroup.setLastY(startDownYForDown);//传入起始点击位置，防止下滑跳跃
                viewGroup.onTouchEvent(ev);
                startDownYForDown = ev.getY();

            }

            if (getChildAt(0).getMeasuredHeight() == getScrollY() + getMeasuredHeight() &&
                    (ev.getY() - startDownYForDown) < 0) {//上滑:scrollview达到底部
                Constant.LOG("MyZuNiScrollerView top move --up");
                MyZuNiFrameLayout viewGroup = (MyZuNiFrameLayout) getParent();
                viewGroup.setLastY(startDownYForDown);//传入起始点击位置，防止下滑跳跃
                viewGroup.onTouchEvent(ev);
                startDownYForDown = ev.getY();
            }

            MyZuNiFrameLayout layout = (MyZuNiFrameLayout) getParent();
            if (layout.getZuNiTotalY() != 0) {
                MyZuNiFrameLayout viewGroup = (MyZuNiFrameLayout) getParent();
                viewGroup.setLastY(startDownYForDown);//传入起始点击位置，防止下滑跳跃
                viewGroup.onTouchEvent(ev);
                startDownYForDown = ev.getY();
                return true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            MyZuNiFrameLayout layout = (MyZuNiFrameLayout) getParent();
            if (layout.getZuNiTotalY() != 0) {//阻尼滑动
                Constant.LOG("MyZuNiScrollerView top up");
                ViewGroup viewGroup = (ViewGroup) getParent();
                viewGroup.onTouchEvent(ev);
            }
            startDownYForDown = 0;
        }
        return super.dispatchTouchEvent(ev);
    }

}
