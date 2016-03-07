package com.example.ant_test.rounded_bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Title: RoundedImageView.java
 * @Package com.example.ant_test.rounded_bitmap
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-30 下午1:08:43
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class RoundedImageView extends ImageView{
	private Bitmap bitmap;
	private BitmapShader shader;
	private Paint paint = new Paint();
	private RectF rect;

	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RoundedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RoundedImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if (bitmap == null) {
			bitmap = ((BitmapDrawable)this.getDrawable()).getBitmap();
			shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			rect = new RectF(0.0f, 0.0f, width, height);
		}
		
		paint.setAntiAlias(true);
		paint.setShader(shader);
		 
		canvas.drawRoundRect(rect, 25, 25, paint);

	}
}
