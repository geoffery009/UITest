package com.example.myapplication.Main5View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.text.DecimalFormat;

public class SportCircleView extends View {
    private int circleColor;//外圆颜色
    private int circleInColor;//内圆颜色
    private int circleValueColor;//圆环颜色
    private float valueProgress;//圆环进度值
    private int ringWidth;//圆环宽度
    private int r;//圆半径

    public SportCircleView(Context context) {
        super(context);
        init();
    }

    public SportCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SportCircle);
        circleColor = typedArray.getColor(R.styleable.SportCircle_circleColor, context.getResources().getColor(R.color.colorPrimary));
        circleInColor = typedArray.getColor(R.styleable.SportCircle_circleColor, Color.parseColor("#FFFFFF"));
        circleValueColor = typedArray.getColor(R.styleable.SportCircle_circleValueColor, context.getResources().getColor(R.color.colorAccent));
        valueProgress = typedArray.getFloat(R.styleable.SportCircle_valueProgress, 0);
        typedArray.recycle();
        init();
    }

    int mViewCenterX, mViewCenterY;
    RectF mRectf;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mViewCenterX = getMeasuredWidth() / 2;
        mViewCenterY = getMeasuredHeight() / 2;


        r = Math.min(getMeasuredHeight(), getMeasuredWidth()) / 2;//半径为内容的最小值-圆环宽度

        ringWidth = r * 28 / 100;
        r = r - ringWidth;
        mRectf = new RectF(mViewCenterX - r - ringWidth / 2, mViewCenterY - r - ringWidth / 2,
                mViewCenterX + r + ringWidth / 2, mViewCenterY + r + ringWidth / 2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //外圆
        paint.setColor(circleColor);
        canvas.drawCircle(mViewCenterX, mViewCenterY, r + ringWidth, paint);

        //内圆
        paint.setColor(circleInColor);
        canvas.drawCircle(mViewCenterX, mViewCenterY, r, paint);

        //圆环
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(ringWidth);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeCap(Paint.Cap.ROUND);

//        paint1.setColor(circleValueColor);
        //渐变起始位置调整
        SweepGradient sweepGradient = new SweepGradient(mViewCenterX, mViewCenterY, color, new float[]{0, 0.4f, 0.7f, 1.0f});
        Matrix matrix = new Matrix();
        matrix.setRotate(-90 - 10, mViewCenterX, mViewCenterY);
        sweepGradient.setLocalMatrix(matrix);
        paint1.setShader(sweepGradient);

        //调整显示的进度位置
        showProgress = tempProgress;
        if (tempProgress <= 4) {
            showProgress = 1;
        }
        if (tempProgress > 4) {
            showProgress = tempProgress - 4;
        }
        if (tempProgress == 100) {
            showProgress = 100;
        }
        canvas.drawArc(mRectf, 270, (showProgress) * 360 / 100, false, paint1);

        //字体
        paint.setColor(circleColor);
        paint.setTextSize(ringWidth);
        canvas.drawText(tempProgress + "%", mViewCenterX - ringWidth / 2, mViewCenterY + ringWidth / 2, paint);
    }

    private int tempProgress;
    private int showProgress;

    private void init() {
        tempProgress = 0;
        int progress = valueProgress <= 0 ? 0 : (int) valueProgress;
        progress = progress >= 100 ? 100 : progress;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, progress);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            tempProgress = (int) valueAnimator1.getAnimatedValue();
            invalidate();
        });
        valueAnimator.start();


        color[0] = getResources().getColor(android.R.color.holo_red_dark);
        color[1] = getResources().getColor(android.R.color.holo_blue_dark);
        color[2] = getResources().getColor(android.R.color.holo_green_dark);
        color[3] = getResources().getColor(android.R.color.holo_red_dark);
    }

    private int color[] = new int[4];   //渐变颜色
}
