package com.example.ant_test.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.example.ant_test.R;

/**
 * Created by chx7078 on 2015/7/21.
 */
public class YgbSemiCircleView extends YgbCircleView{
    private int bgColor;
    int radius;
    public YgbSemiCircleView(Context context) {
        super(context);
    }

    public YgbSemiCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YgbSemiCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();
        bgColor = getResources().getColor(R.color.white);
        //paint.setStyle(Paint.Style.STROKE);
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    protected int getTotalSweep() {
        return 180;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int diameter = width > height * 2 ? height * 2 : width;
        int strokeWidth = diameter / 8;
        radius = (diameter - strokeWidth * 2) / 2;
        //paint.setStrokeWidth(strokeWidth);//todo
        setMeasuredDimension(diameter, diameter / 2);
        //rectF = new RectF(0 + strokeWidth / 2, 0 + strokeWidth, diameter - strokeWidth / 2, diameter - strokeWidth / 2);
        rectF = new RectF(0, 0, diameter, diameter);
        start = 180;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(">>>>", "onDraw");
        if (data == null) return;
        float start = this.start;
        for (CircleViewData cvData : data) {
            paint.setColor(getResources().getColor(cvData.color));
            canvas.drawArc(rectF, start, cvData.sweep, true, paint);
            start += cvData.sweep;
        }
        paint.setColor(bgColor);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, paint);
    }
}
