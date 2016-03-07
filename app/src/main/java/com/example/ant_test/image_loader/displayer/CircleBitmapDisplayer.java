package com.example.ant_test.image_loader.displayer;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.ant_test.image_loader.helper.LoadedFrom;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;

/**
 * Created by chx7078 on 2015/5/12.
 */
public class CircleBitmapDisplayer implements IBitmapDisplayer{

    @Override
    public void display(Bitmap bitmap, IImageWrapper wrapper, LoadedFrom from) {
        CirCleDrawable cirCleDrawable = new CirCleDrawable(bitmap, wrapper.getWidth(), wrapper.getHeight());
        wrapper.setImageDrawable(cirCleDrawable);
    }

    public static class CirCleDrawable extends Drawable {

        private Bitmap bitmap;
        protected final Paint paint;
        private int width;
        private int height;

        public CirCleDrawable(Bitmap bitmap, int width, int height) {
            this.bitmap = bitmap;
            this.width = width;
            this.height = height;
            paint = new Paint();
            paint.setAntiAlias(true);
        }

        @Override
        public void draw(Canvas canvas) {
            // paint.setColor(Color.CYAN);
            Bitmap bitmap1 = getCircularBitmap(bitmap);
            //Log.d(">>>>", "draw:" + bitmap1.getWidth() + " " + bitmap1.getHeight());
            canvas.drawBitmap(bitmap1, 0, 0, paint);
        }

        public Bitmap getCircularBitmap(Bitmap bitmap) {
            Bitmap output;
            int radius, radius2, radius3, x, y;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                radius = bitmap.getHeight();
                x = (bitmap.getWidth() - bitmap.getHeight()) / 2;
                y = 2;
            } else {
                radius = bitmap.getWidth();
                x = 2;
                y = (bitmap.getHeight() - bitmap.getWidth()) / 2;
            }
            radius2 = width > height ? height : width;
            radius -= 4;
            radius3 = radius > radius2 ? radius : radius2;
            Bitmap square = Bitmap.createBitmap(bitmap, x, y, radius, radius);

            output = Bitmap.createBitmap(radius3, radius3, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xffffffff;
            final Paint paint = new Paint();
            final RectF rect = new RectF(0, 0, radius3, radius3);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            //canvas.drawCircle(radius / 2, radius / 2 , radius / 2, paint);
            canvas.drawRoundRect(rect, radius3 / 2, radius3 / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(square, null, rect, paint);
            return output;
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }
    }
}
