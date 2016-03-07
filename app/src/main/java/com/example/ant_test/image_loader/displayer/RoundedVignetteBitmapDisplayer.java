package com.example.ant_test.image_loader.displayer;

import android.graphics.Bitmap;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;

import com.example.ant_test.image_loader.helper.LoadedFrom;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;
import com.example.ant_test.image_loader.image_wrapper.ImageViewWrapper;


/**
 * @Title: RoundedVignetteBitmapDisplayer.java
 * @Package com.example.ant_test.image_loader.displayer
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-10-8 下午3:17:33
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class RoundedVignetteBitmapDisplayer extends RoundedBitmapDisplayer{

	public RoundedVignetteBitmapDisplayer(int cornerRadiusPixels, int marginPixels) {
		super(cornerRadiusPixels, marginPixels);
	}
	
	@Override
	public void display(Bitmap bitmap, IImageWrapper imageWrapper, LoadedFrom loadedFrom) {
		if (!(imageWrapper instanceof ImageViewWrapper)) {
			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		imageWrapper.setImageDrawable(new RoundedVignetteDrawable(bitmap, cornerRadius, margin));
	}

	protected static class RoundedVignetteDrawable extends RoundedDrawable {

		RoundedVignetteDrawable(Bitmap bitmap, int cornerRadius, int margin) {
			super(bitmap, cornerRadius, margin);
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			RadialGradient vignette = new RadialGradient(
					mRect.centerX(), mRect.centerY() * 1.0f / 0.7f, mRect.centerX() * 1.3f,
					new int[]{0, 0, 0x7f000000}, new float[]{0.0f, 0.7f, 1.0f},
					Shader.TileMode.CLAMP);

			Matrix oval = new Matrix();
			oval.setScale(1.0f, 0.7f);
			vignette.setLocalMatrix(oval);

			paint.setShader(new ComposeShader(bitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
		}
	}

}
