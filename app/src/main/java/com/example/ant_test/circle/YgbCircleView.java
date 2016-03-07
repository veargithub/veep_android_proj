package com.example.ant_test.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by chx7078 on 2015/7/21.
 */
public class YgbCircleView extends View {

    protected Paint paint;
    protected RectF rectF;
    protected List<CircleViewData> data;
    protected float start;

    public YgbCircleView(Context context) {
        super(context);
        init();
    }

    public YgbCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YgbCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setStart(float start) {
        this.start = start;
    }

    protected void init() {
        paint = new Paint();
        paint.setAntiAlias(true);

        //paint.setColor(Color.RED);
    }

    protected int getTotalSweep() {
        return 360;
    }

    public void setData(List<CircleViewData> data) {
        this.data = data;
        double total = 0d;
        for (CircleViewData cvData : data) {
            total += cvData.data;
        }
        for (CircleViewData cvData : data) {
            cvData.sweep = (float)(cvData.data / total * getTotalSweep());
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int diameter = width > height ? height : width;
        setMeasuredDimension(diameter, diameter);
        rectF = new RectF(0, 0, diameter, diameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null) return;
        float start = this.start;
        for (CircleViewData cvData : data) {
            paint.setColor(getResources().getColor(cvData.color));
            canvas.drawArc(rectF, start, cvData.sweep, true, paint);
            start += cvData.sweep;
        }
    }
}
