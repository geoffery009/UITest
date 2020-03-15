package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MyView extends androidx.appcompat.widget.AppCompatTextView {
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Constant.LOG("MyView--widthMeasureSpec");
        int measuredWidth = measureChild(widthMeasureSpec);
        Constant.LOG("MyView--heightMeasureSpec");
        int measuredHeight = measureChild(heightMeasureSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    private int measureChild(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            Constant.LOG("MyView:  MeasureSpec.AT_MOST");
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            Constant.LOG("MyView:  MeasureSpec.EXACTLY" + "," + specSize);
            result = specSize;
        }
        return result;
    }
}
