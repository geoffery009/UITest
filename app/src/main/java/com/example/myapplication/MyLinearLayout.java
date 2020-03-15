package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        showMsg("MyLinearLayout.dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        showMsg("MyLinearLayout.onInterceptTouchEvent");
        if (ev.getAction()==MotionEvent.ACTION_MOVE){
            showMsg("move");
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        MotionEvent.AC
        showMsg("MyLinearLayout.onTouchEvent: " + event.getAction());
        return true;
    }

    private void showMsg(String s) {
        Log.d("test------", s);
    }

}
