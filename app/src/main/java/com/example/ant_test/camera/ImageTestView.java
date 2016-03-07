package com.example.ant_test.camera;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Title: ImageTestView.java
 * @Package com.example.ant_test.camera
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-25 下午3:41:55
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ImageTestView extends ImageView{
	private Camera camera;
	private Matrix cameraMatrix;

	public ImageTestView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ImageTestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ImageTestView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		camera = new Camera();
		cameraMatrix = new Matrix();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		camera.save();
		camera.rotateX(0);
		camera.rotateY(45);
		camera.rotateZ(45);
		camera.getMatrix(cameraMatrix);
		camera.restore(); 
		canvas.concat( cameraMatrix );
		super.onDraw(canvas);
	}
	
}
