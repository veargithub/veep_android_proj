package com.example.ant_test.path_tracing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.view.View;

/**
 * Created by chx7078 on 2015/4/29.
 */
public class TestActivity extends Activity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        // 路径效果的相位
        float phase;
        // 7种不同路径效果的数组
        PathEffect[] effects = new PathEffect[7];
        // 颜色ID数组
        int[] colors;
        // 画笔
        private Paint paint;
        // 声明路径 对象
        Path path;

        float[][] points;

        int index = 0;

        public MyView(Context context) {
            super(context);
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            // 创建、并初始化Path
            path = new Path();
            path.moveTo(0, 0);

            points = new float[100][2];
            for (int i = 0; i < 100; i++) {
                // 生成15个点，随机生成它们的Y坐标，并将它们连成一条Path
                float x = i * 5;
                float y = (float) Math.random() * 60;
                points[i] = new float[] {x, y};

            }

        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (index >= points.length) return;
                    float[] fs = points[index];
                    path.lineTo(fs[0], fs[1]);
                    invalidate();
                    index++;
                    postDelayed(this, 100);
                }
            }, 100);
        }
    }
}
