package com.example.myapplication.Main5View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.example.myapplication.Constant;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TableLineView extends View {
    int xScale, yScale;

    List<List<XY>> datas;
    int[] lineColor = new int[]{R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark};

    public TableLineView(Context context) {
        super(context);
        init();
    }

    public TableLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        xScale = 20;
        yScale = 20;
        addData();
    }

    private void addData() {

        Random ra = new Random();
        List<XY> dataXY = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            XY xy = new XY();
            xy.setX(i * 10);
            xy.setY(ra.nextInt(30) + 1);
            dataXY.add(xy);
        }

        List<XY> dataXY1 = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            XY xy = new XY();
            xy.setX(i * 10);
            xy.setY(ra.nextInt(30) + 1);
            dataXY1.add(xy);
        }

        List<XY> dataXY2 = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            XY xy = new XY();
            xy.setX(i * 10);
            xy.setY(ra.nextInt(30) + 1);
            dataXY2.add(xy);
        }

        datas = new ArrayList<>();
        datas.add(dataXY);
        datas.add(dataXY1);
//        datas.add(dataXY2);

        animCurrentYAllPoints = new ArrayList<>();
        animCurrentYAllPoints.add(new int[dataXY.size()]);
        animCurrentYAllPoints.add(new int[dataXY1.size()]);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawYAxisArea(canvas);
        canvas.save();
        canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - yTextAndLineWidth);
        canvas.translate(0, offset * yScale);


        drawYAxis(canvas);

        drawEach(canvas);
        canvas.restore();

        drawXAxisArea(canvas);

        drawXAxis(canvas);
        if (offset == 0) {
            drawZero(canvas, "0");
        } else {
            drawZero(canvas, "");
        }
    }

    private int pointToXLineSize(int pointX) {
        return pointX * xScale + xTextAndLineWidth;
    }

    private int pointToYLineSize(int pointY) {
        return getMeasuredHeight() - pointY * yScale + yTextAndLineWidth;
    }

    int xTextAndLineWidth;
    int yTextAndLineWidth;

    private void drawEach(Canvas canvas) {
        xTextAndLineWidth = String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) + axisLineSize / 2 - getFontHeight(textSize);
        yTextAndLineWidth = -(getFontHeight(textSize) + axisLineSize / 2);

        for (int l = 0; l < datas.size(); l++) {
            List<XY> oneLine = datas.get(l);

            Paint paint1 = new Paint();
            paint1.setAntiAlias(true);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setStrokeWidth(axisLineSize);
            paint1.setColor(getResources().getColor(lineColor[l]));

            Path path1 = new Path();

            path1.moveTo(pointToXLineSize(oneLine.get(0).getX()),
                    pointToYLineSize(oneLine.get(0).getY()));

            int xPoint;
            for (int i = 1; i < oneLine.size(); i++) {
                xPoint = (oneLine.get(i - 1).getX() + oneLine.get(i).getX()) / 2;

                path1.cubicTo(pointToXLineSize(xPoint), pointToYLineSize(oneLine.get(i - 1).getY()),
                        pointToXLineSize(xPoint), pointToYLineSize(oneLine.get(i).getY()),
                        pointToXLineSize(oneLine.get(i).getX()), pointToYLineSize(oneLine.get(i).getY()));
            }
            canvas.drawPath(path1, paint1);

            Paint paint2 = new Paint();
            paint2.setStyle(Paint.Style.FILL);
            paint2.setStrokeWidth(axisLineSize);
            paint2.setColor(getResources().getColor(lineColor[l]));

            Path path2 = new Path();
            path2.set(path1);
            path2.lineTo(pointToXLineSize(oneLine.get(oneLine.size() - 1).getX()), pointToYLineSize(oneLine.get(oneLine.size() - 1).getY()));
            path2.lineTo(pointToXLineSize(oneLine.get(oneLine.size() - 1).getX()), pointToYLineSize(0));
            path2.lineTo(pointToXLineSize(oneLine.get(0).getX()), pointToYLineSize(0));
            path2.lineTo(pointToXLineSize(oneLine.get(0).getX()), pointToYLineSize(oneLine.get(0).getY()));
            path2.close();
            canvas.drawPath(path2, paint2);
        }
    }

    private List<int[]> animCurrentYAllPoints;

    private void startAnim(int lineIndex, int yPointIndex, int startYPoint) {
        int[] line = animCurrentYAllPoints.get(lineIndex);

        ValueAnimator animator = ValueAnimator.ofInt(0, startYPoint);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                line[yPointIndex] = (int) valueAnimator.getAnimatedValue();
            }
        });
    }

    private void drawLine(Path path, int perYPoint, int yPoint, Canvas canvas) {

    }

    private int maxXAxisValue = 200;
    private int maxYAxisValue = 200;

    private void drawXAxisArea(Canvas canvas) {

        //x axis bg
        Paint bg = new Paint();
        bg.setColor(Color.parseColor("#FFFFFF"));

        RectF f = new RectF(String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize) - axisLineSize,
                getMeasuredHeight() - getFontHeight(textSize), getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRect(f, bg);
    }

    private void drawXAxis(Canvas canvas) {

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(axisLineSize);
        paint.setColor(getResources().getColor(axisLineColor));


        Paint paint1 = new Paint();
        paint1.setTextSize(textSize);
        paint1.setColor(getResources().getColor(axisLineColor));

        for (int i = 0; i < maxXAxisValue - 1; i++) {
            canvas.drawLine(String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) + i * xScale - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize),
                    String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) + (i + 1) * xScale - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize), paint);
            if (i > 0) {
                if (i % 10 == 0) {
                    canvas.drawLine(String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) + i * xScale - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize),
                            String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) + i * xScale - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize) + getFontHeight(textSize) / 6, paint);

                    //
                    canvas.drawText(i + "", String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) + i * xScale - getFontHeight(textSize) - 20, getMeasuredHeight() - 20, paint1);
                } else {
                    if (i % 5 == 0) {
                        canvas.drawLine(String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) + i * xScale - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize),
                                String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) + i * xScale - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize) + getFontHeight(textSize) / 8, paint);
                    }
                }
            }
        }
    }

    private void drawYAxisArea(Canvas canvas) {
        //y axis bg
        Paint bg = new Paint();
        bg.setColor(Color.parseColor("#FFFFFF"));

        RectF f = new RectF(0 - offset, 0 - offset, String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize), getMeasuredHeight());
        canvas.drawRect(f, bg);
    }

    private void drawYAxis(Canvas canvas) {

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(axisLineSize);
        paint.setColor(getResources().getColor(axisLineColor));

        Paint paint1 = new Paint();
        paint1.setTextSize(textSize);
        paint1.setTextAlign(Paint.Align.RIGHT);
        paint1.setColor(getResources().getColor(axisLineColor));

        for (int i = 0; i < maxYAxisValue - 1; i++) {
            canvas.drawLine(String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize) - i * yScale,
                    String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize) - (i + 1) * yScale, paint);

            if (i > 0) {
                if (i % 10 == 0) {
                    canvas.drawLine(String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize) - i * yScale,
                            String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize) - 20, getMeasuredHeight() - getFontHeight(textSize) - i * yScale, paint);

                    //
                    canvas.drawText(i + "", String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize) - 34, getMeasuredHeight() - getFontHeight(textSize) - i * yScale + 10, paint1);
                } else {
                    if (i % 5 == 0) {
                        canvas.drawLine(String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize), getMeasuredHeight() - getFontHeight(textSize) - i * yScale,
                                String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize) - 10, getMeasuredHeight() - getFontHeight(textSize) - i * yScale, paint);
                    }
                }
            }
        }

    }

    private void drawZero(Canvas canvas, String text) {

        //zero bg
        Paint bg = new Paint();
        bg.setColor(Color.parseColor("#FFFFFF"));

        RectF f = new RectF(0, getMeasuredHeight() - getFontHeight(textSize),
                String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize),
                getMeasuredHeight());
        canvas.drawRect(f, bg);

        Paint paint1 = new Paint();
        paint1.setTextSize(textSize);
        paint1.setTextAlign(Paint.Align.RIGHT);
        paint1.setColor(getResources().getColor(axisLineColor));
        canvas.drawText(text, String.valueOf(maxYAxisValue).length() * getFontHeight(textSize) - getFontHeight(textSize) - 34, getMeasuredHeight() - 20, paint1);
    }

    private int offset = 0;

    int axisLineSize = 4;
    int axisLineColor = R.color.colorPrimaryDark;

    int textSize = 28;

    /**
     * 根据字体大小获取控件高度
     *
     * @param fontSize
     * @return
     */
    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return ((int) Math.ceil(fm.descent - fm.top) + 2) * 2;
    }

    //touch

    private float startTouchY, startTouchX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            startTouchY = event.getY();
            startTouchX = event.getX();
        } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            int temp = (int) (event.getY() - startTouchY);
            if (getMeasuredHeight() < yScale * maxYAxisValue) {

//                Constant.LOG("offset--" + offset);
                if (offset + temp >= 0 && (offset + temp <= maxYAxisValue - getMeasuredHeight() / yScale)) {
                    offset += temp;
                    invalidate();
                }
                startTouchY = event.getY();
            }
        }
        return super.onTouchEvent(event);
    }

    class XY {
        int x, y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
