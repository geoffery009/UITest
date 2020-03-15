package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {
    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //计算子布局
        Constant.LOG("MyViewGroup--------measureChild--1");
        measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec);
        Constant.LOG("MyViewGroup--------measureChild--2");
        measureChild(getChildAt(1), widthMeasureSpec, heightMeasureSpec);
        Constant.LOG("MyViewGroup--------measureChild--3");
        measureChild(getChildAt(2), widthMeasureSpec, heightMeasureSpec);

        //根据子布局总和得到布局大小
        //由于是排列方式，直接宽总和，高总和
        int totalWidth = measureWidth(widthMeasureSpec);
        int totalHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(totalWidth, totalHeight);
    }

    private int measureWidth(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = MeasureSpec.AT_MOST;
        if (specMode == MeasureSpec.AT_MOST) {//包裹内容，计算所有子布局宽度

            result = 2 * width + padding;

        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }


    private int measureHeight(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = MeasureSpec.AT_MOST;
        if (specMode == MeasureSpec.AT_MOST) {//包裹内容，计算所有子布局高度
            result = height + padding + height+ height;
        } else if (specMode == MeasureSpec.EXACTLY) {
            Constant.LOG("MyViewGroup:  MeasureSpec.EXACTLY" + "," + specSize);
            result = specSize;
        }
        return result;
    }

    int width = 300;
    int height = 300;
    int padding = 20;

    //为子布局分配布局可用空间
    @Override
    protected void onLayout(boolean b, int l, int i1, int i2, int i3) {
        showMsg("left: " + l + " ,top: " + i1 + ",right: " + i2 + ", bottom: " + i3);
        int count = getChildCount();


        View view = getChildAt(0);
        view.layout(0, 0, width, height);

        View view1 = getChildAt(1);
        view1.layout(width + 20, 0, width + 20 + width, height);

        View view2 = getChildAt(2);
        view2.layout(0, height + 20, width * 2 + 20, width + 20 + width * 2);

    }

    private void showMsg(String msg) {
        Log.d(Constant.TAG, msg);
    }
}
