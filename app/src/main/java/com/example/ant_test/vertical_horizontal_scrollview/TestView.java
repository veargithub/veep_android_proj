package com.example.ant_test.vertical_horizontal_scrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Title: TestView.java
 * @Package com.example.ant_test.vertical_horizontal_scrollview
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-8-6 下午3:59:45
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class TestView extends View{
	Paint paint = new Paint();
	private static final int WIDTH = 6400;
	private static final int HEIGHT = 6400;

	public TestView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TestView(Context context) {
		super(context);
		//this.setLayoutParams(new ViewGroup.LayoutParams(3200, 3200));
	}

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(WIDTH, HEIGHT);
    }


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);  
        int borderLength = 200;  
        for (int i = 0; i <= WIDTH; i += borderLength) {  
            for (int j = borderLength; j <= HEIGHT; j += borderLength) {  
                float textSize = i / borderLength * 5 + j / borderLength + 10;  
                float squareLength = textSize * 3 / 4;  
                paint.setColor(Color.BLACK);  
                int left = i;  
                float top = j - squareLength;  
                float right = i + squareLength;  
                int bottom = j;  
                //canvas.drawRect(left, top, right, bottom, paint);  
                //Log.d(">>>>>>>", left + " " + top +" " + right + " " + bottom);  
                paint.setTextSize(textSize);  
                paint.setColor(Color.RED);  
                canvas.drawText("" + (int)textSize, i, j, paint);  
            }  
        }  
	}
}
