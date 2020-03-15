package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 支持长按删除
 */
public class MyZuNiRecycleViewWithLongDel extends RecyclerView {
    public MyZuNiRecycleViewWithLongDel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private float startDownYForDown;
    private float startDownXForDel;

    private boolean isLongClick;
    private boolean isShowDelete;

    public void setShowDelete(boolean showDelete) {
        isShowDelete = showDelete;
    }

    public boolean isLongClick() {
        return isLongClick;
    }

    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Constant.LOG("MyZuNiRecycleViewWithLongDel ACTION--" + getScrollY() + " , " +
                getChildAt(0).getMeasuredHeight() + " , " + getHeight() + " , long click: " + isLongClick);
        if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            if (!canScrollVertically(-1)) {//下滑
                startDownYForDown = ev.getY();
            }
            if (!canScrollVertically(1)) {//上滑:scrollview达到底部
                startDownYForDown = ev.getY();
            }

            if (isLongClick) {
                startDownXForDel = ev.getX();
            }
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (!canScrollVertically(-1) && (ev.getY() - startDownYForDown) > 0
                    && !isLongClick) {//下滑:scrollview达到顶部
                Constant.LOG("MyZuNiRecycleViewWithLongDel top move --down , " + isLongClick);
                MyZuNiFrameLayout viewGroup = (MyZuNiFrameLayout) getParent();
                viewGroup.setLastY(startDownYForDown);//传入起始点击位置，防止下滑跳跃
                viewGroup.onTouchEvent(ev);
                startDownYForDown = ev.getY();
            }
            if (!canScrollVertically(1) &&
                    (ev.getY() - startDownYForDown) < 0 &&
                    !isLongClick) {//上滑:scrollview达到底部
                Constant.LOG("MyZuNiRecycleViewWithLongDel top move --up");
                MyZuNiFrameLayout viewGroup = (MyZuNiFrameLayout) getParent();
                viewGroup.setLastY(startDownYForDown);//传入起始点击位置，防止下滑跳跃
                viewGroup.onTouchEvent(ev);
                startDownYForDown = ev.getY();
            }
            MyZuNiFrameLayout layout = (MyZuNiFrameLayout) getParent();
            if (layout.getZuNiTotalY() != 0 && !isLongClick) {//阻尼滑动
                MyZuNiFrameLayout viewGroup = (MyZuNiFrameLayout) getParent();
                viewGroup.setLastY(startDownYForDown);//传入起始点击位置，防止下滑跳跃
                viewGroup.onTouchEvent(ev);
                startDownYForDown = ev.getY();
                return true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            MyZuNiFrameLayout layout = (MyZuNiFrameLayout) getParent();
            if (layout.getZuNiTotalY() != 0) {//阻尼滑动
                Constant.LOG("MyZuNiRecycleViewWithLongDel top up");
                ViewGroup viewGroup = (ViewGroup) getParent();
                viewGroup.onTouchEvent(ev);
            }
            startDownYForDown = 0;
        }
        if (isLongClick && ev.getAction() == MotionEvent.ACTION_MOVE && isShowDelete) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

}
