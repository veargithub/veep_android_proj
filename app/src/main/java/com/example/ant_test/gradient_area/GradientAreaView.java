package com.example.ant_test.gradient_area;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.ant_test.R;

/**
 * Created by chx7078 on 2015/7/28.
 */
public class GradientAreaView extends View {

    private Paint mPaint;

    public GradientAreaView(Context context) {
        super(context);
        init();
    }

    public GradientAreaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientAreaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        float gradientX0 = 0, gradientY0 = 0, gradientX1 = getWidth(), gradientY1 = getHeight();
        mPaint.setStrokeWidth(0);
        Shader shader = new LinearGradient(gradientX0, gradientY0, gradientX1, gradientY1,
                getResources().getColor(R.color.asset4), getResources().getColor(R.color.asset1), Shader.TileMode.CLAMP);//参数顺序好像错了
        mPaint.setShader(shader);

        Path path = new Path();//DO NOT NEW

        path.moveTo(100, 100);
        path.lineTo(200, 100);
        path.lineTo(200, 200);
        path.lineTo(100, 200);
        path.lineTo(100, 100);

        canvas.drawPath(path, mPaint);
        mPaint.setShader(null);
    }
}
