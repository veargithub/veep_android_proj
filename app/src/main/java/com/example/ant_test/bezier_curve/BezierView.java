package com.example.ant_test.bezier_curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 通过正玄函数生成曲线
 */
public class BezierView extends View{

    private Paint paint;
    private int waveWidth;
    private Path mPath;
    private List<Point> list;
    private int waterLevel;

    public BezierView(Context context) {
        super(context);
        init();
    }

    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3.0f);
        mPath = new Path();
        list = new LinkedList<Point>();
        waveWidth = 200;
        waterLevel = 0;
    }

    int offsetX = 0;
    int speed = 6;
    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d(">>>>", "offsetX:" + offsetX);
//        path = new Path();
//        path.moveTo(100, 100);
//        //path.cubicTo(150, 50, 250, 150, 300, 100);
//
//        double t = 2 * Math.PI / waveWidth;
//        for (int i = 0; i < waveWidth; i+=2) {
//            float x = i;
//            //float y = (float)(25 * Math.sin(Math.toRadians(360d / waveWidth * i - offsetX)));
//            float y = (float)(25 * Math.sin(360d / waveWidth * x * Math.PI / 180 - offsetX * Math.PI / 180));
//            //float y = (float)(25 * Math.sin((x * t - offsetX * t)));
//            path.lineTo(x + 100, y + 100);
//
//        }
//        path.lineTo(waveWidth + 100, 300);
//        path.lineTo(100, 300);
//        path.lineTo(100, 100);
//        canvas.drawPath(path, paint);
        int width = waveWidth = getWidth();
        int height = getHeight();
        //Log.d(">>>>", "width:" + width + ", height:" + height);
        StringBuilder sb = new StringBuilder();
        if (mPath.isEmpty()) {
            mPath.moveTo(0, height - waterLevel);
            for (int i = 0; i <= waveWidth; i+=2) {
                float x = i;
                float y = (float)(25 * Math.sin(360d / waveWidth * x * Math.PI / 180 - offsetX * Math.PI / 180)) ;
                mPath.lineTo(x + 0, y + height - waterLevel);
                Point point = new Point(x ,y);
                //sb.append("x:" + x + ",y:" + y + ";");
                list.add(point);
            }
            mPath.lineTo(waveWidth, height);
            mPath.lineTo(0, height);
            mPath.lineTo(0, height - waterLevel);
        }
        canvas.drawPath(mPath, paint);
//        Log.d(">>>>", sb.toString());
    }

    public void start() {
        post(runnable);
    }

    public void stop() {
        removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            changeWaterLevel();
            changePath();
            invalidate();
            postDelayed(this, 50);
        }
    };

    private void changePath() {
        int width = waveWidth = getWidth();
        int height = getHeight();
        mPath.reset();
        StringBuilder sb = new StringBuilder();
        if (list.size() > 0) {
            mPath.moveTo(0, height - waterLevel);
            for (int i = 0; i < list.size(); i++) {
                Point current = list.get(i);
                Point next = null;
                if (i == list.size() - 1) {
                    next = list.get(0);
                } else {
                    next = list.get(i + 1);
                }
                current.y = next.y;
                mPath.lineTo(current.x, current.y + height - waterLevel);
                //sb.append("x:" + current.x + ",y:" + current.y + ";");
            }
            mPath.lineTo(waveWidth, height);
            mPath.lineTo(0, height);
            mPath.lineTo(0, height - waterLevel);
        }
//        Log.d(">>>>", sb.toString());
    }

    private void changeWaterLevel() {
        waterLevel += 2;
        if (waterLevel >= getHeight() / 2) {
            waterLevel = getHeight() / 2;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(200, 400);
    }

    static class Point {
        float x;
        float y;
        Point(float x, float y) {this.x = x; this.y = y;}
    }
}
