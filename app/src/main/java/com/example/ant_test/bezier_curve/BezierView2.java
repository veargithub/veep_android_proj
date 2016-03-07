package com.example.ant_test.bezier_curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 根据4个点，生成平滑曲线
 */
public class BezierView2 extends View{

    private static final String TAG = "BezierView2";
    private Paint paint;
    private Path mPath, mPath2;
    private List<Point> list, smoothList;
    private int currentIndex;
    public BezierView2(Context context) {
        super(context);
        init();
    }

    public BezierView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(1.0f);
        mPath = new Path();
        mPath2 = new Path();
        list = new LinkedList<Point>();

//        list.add(new Point(100, 100));
//        list.add(new Point(200, 200));
//        list.add(new Point(300, 100));
//        list.add(new Point(400, 200));


//        list.add(new Point(0.0f, 0.42303252f));
//        list.add(new Point(0.07692308f, 0.51292145f));
//        list.add(new Point(0.15384616f, 0.517929f));
//        list.add(new Point(0.23076923f, 0.5033224f));
//        list.add(new Point(0.30769232f, 1.0f));
//        list.add(new Point(0.3846154f, 0.40497854f));
//        list.add(new Point(0.46153846f, 0.44317698f));
//        list.add(new Point(0.53846157f, 0.45362929f));
//        list.add(new Point(0.61538464f, -0.0f));
//        list.add(new Point(0.6923077f, 0.5279357f));
//        list.add(new Point(0.7692308f, 0.5279357f));
//        list.add(new Point(0.84615386f, 0.5279357f));
//        list.add(new Point(0.9230769f, 0.5279357f));
//        list.add(new Point(1.0f, 0.5279357f));

        list.add(new Point(100.0f, 226.90976f));
        list.add(new Point(123.07692f, 253.87643f));
        list.add(new Point(146.15384f, 255.37871f));
        list.add(new Point(169.23077f, 250.99672f));
        list.add(new Point(192.3077f, 400.0f));
        list.add(new Point(215.38461f, 221.49356f));
        list.add(new Point(238.46153f, 232.9531f));
        list.add(new Point(261.53845f, 236.08879f));
        list.add(new Point(284.6154f, 100.0f));
        list.add(new Point(307.69232f, 258.3807f));
        list.add(new Point(330.76923f, 258.3807f));
        list.add(new Point(353.84616f, 258.3807f));
        list.add(new Point(376.92307f, 258.3807f));
        list.add(new Point(400.0f, 258.3807f));

        currentIndex = 0;
        smoothList = generateNewPath(list, 30);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < list.size(); i++) {
            Point point = list.get(i);
            float x = point.x;
            float y = point.y;
            Log.d(">>>>", x + ", " + y);
            if (mPath.isEmpty()) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }
        }
        paint.setColor(Color.RED);
        canvas.drawPath(mPath, paint);
        paint.setColor(Color.BLUE);
//        for (int i = 0; i < currentIndex; i++) {
        for (int i = 0; i < smoothList.size() - 2; i++) {
            float x0 = smoothList.get(i).x ;
            float y0 = smoothList.get(i).y ;
            float x1 = smoothList.get(i + 1).x ;
            float y1 = smoothList.get(i + 1).y ;
            canvas.drawLine(x0, y0, x1, y1, paint);
        }
    }

    private List<Point> generateNewPath(List<Point> points, int granularity) {
        if (points.size() < 4) return points;

        List<Point> copy = new ArrayList<Point>(), resultList = new ArrayList<Point>();
        copy.add(points.get(0));
        for (int i = 0; i < points.size(); i++) {
            copy.add(points.get(i));
        }
        copy.add(points.get(points.size() - 1));
        resultList.add(points.get(0));
        for (int index = 1; index < copy.size() - 2; index++) {
            Point p0 = copy.get(index - 1);
            Point p1 = copy.get(index);
            Point p2 = copy.get(index + 1);
            Point p3 = copy.get(index + 2);

            float big, small;
            if (p1.y > p2. y) {
                big = p1.y;
                small = p2.y;
            } else {
                big = p2.y;
                small = p1.y;
            }

            // now add n points starting at p1 + dx/dy up until p2 using Catmull-Rom splines
            for (int i = 1; i < granularity; i++) {
                float t = (float) i * (1.0f / (float) granularity);
                float tt = t * t;
                float ttt = tt * t;

                Point pi; // intermediate point
                double x = 0.5 * (2 * p1.x + (p2.x - p0.x) * t + (2 * p0.x - 5 * p1.x + 4 * p2.x - p3.x) * tt + (3 * p1.x - p0.x - 3 * p2.x + p3.x) * ttt);
                double y = 0.5 * (2 * p1.y + (p2.y - p0.y) * t + (2 * p0.y - 5 * p1.y + 4 * p2.y - p3.y) * tt + (3 * p1.y - p0.y - 3 * p2.y + p3.y) * ttt);
//                if (y > big) y = big;
//                if (y < small) y = small;
                resultList.add(new Point((float)x , (float)y));

            }

            // Now add p2
            resultList.add(p2);
        }

        // finish by adding the last point
        resultList.add(copy.get(copy.size() - 1));
        return resultList;
    }

    private List<Point> generateNewPath2(List<Point> points, int granularity) {
        return null;
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
            currentIndex++;
            if (currentIndex >= smoothList.size() - 1) {
                currentIndex = 0;
            }
            invalidate();
            postDelayed(this, 25);
        }
    };

    class Point {
        float x;
        float y;
        Point(float x, float y) {this.x = x; this.y = y;}
    }


}
