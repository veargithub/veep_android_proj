package com.example.ant_test.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

public class NumberProgressBar extends ProgressBar {
	private static final String TAG = "NumberProgressBar";
	private String numberText;
	private String percentsText;
    private Paint mPaint;
    private int maxNumber;
    private int curNumber;
    private int paddingLeft;
    private int paddingRight;
     
    public NumberProgressBar(Context context) {
        super(context);
        init();
    }
     
    public NumberProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
 
    public NumberProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public void setMaxNumber(int number) {
    	this.maxNumber = number;
    }
    @Override
    public void setProgress(int progress) {
    	this.curNumber = progress / this.getMax() * this.maxNumber;
        setText(progress, curNumber);
        super.setProgress(progress);  
    }
    
    public void setNumber(int number) {
    	if (number > this.maxNumber) {
    		number = maxNumber;
    	}
    	int progress = number  * this.getMax() / maxNumber;
    	Log.d(TAG, number + " " + progress);
    	setText(progress, curNumber);
    	super.setProgress(progress);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawNumber(canvas);
        drawPercents(canvas);
    }
    
    private void drawNumber(Canvas canvas) {
    	Rect textRect = new Rect();
        mPaint.getTextBounds(this.numberText, 0, this.percentsText.length(), textRect);
        int x = this.paddingLeft;
        int y = (getHeight() / 2) - textRect.centerY();
        canvas.drawText(this.numberText, x, y, this.mPaint);
    }
    
    private void drawPercents(Canvas canvas) {
    	Rect rect = new Rect();
    	mPaint.getTextBounds(this.percentsText, 0, this.percentsText.length(), rect);
    	int x = getWidth() - rect.width() - this.paddingRight;
    	int y = (getHeight() / 2) - rect.centerY();
    	canvas.drawText(this.percentsText, x, y, mPaint);
    }
     
    private void init(){
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setTextSize(18);
    }
    
    private void setText(int progress, int number) {
    	 int i = (progress * 100) / this.getMax();
         this.percentsText = String.valueOf(i) + "%";
         this.numberText = number + "/" + this.maxNumber;
    }
}
