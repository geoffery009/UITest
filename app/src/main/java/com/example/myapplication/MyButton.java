package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class MyButton extends androidx.appcompat.widget.AppCompatButton {
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {showMsg("MyButton.dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        showMsg("MyButton.onTouchEvent-----------");
        return super.onTouchEvent(event);
    }


    private void showMsg(String s) {
        Log.d("test------", s);
    }
}
