package com.example.ant_test.image_loader.extras;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * Created by chx7078 on 2015/5/12.
 */
public class CircleBitmapDisplayer implements BitmapDisplayer {

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        CirCleDrawable cirCleDrawable = new CirCleDrawable(bitmap, imageAware.getWidth(), imageAware.getHeight());
        imageAware.setImageDrawable(cirCleDrawable);
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
            //Bitmap bitmap1 = getCircularBitmap(bitmap);
            Bitmap bitmap1 = inciseBitmap(bitmap);
            canvas.drawBitmap(bitmap1, 0, 0, paint);
        }

        public Bitmap getCircularBitmap(Bitmap bitmap) {
            Bitmap output;
            int radius = 0;
            radius = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() : bitmap.getWidth();
//            if (this.width > 0 && this.height > 0) {
//                int tempRadius = width > height ? height : width;
//                if (tempRadius < radius) {
//                    radius = tempRadius;
//                }
//            }
            output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xffffffff;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, radius, radius);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(radius / 2, radius / 2 , radius / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }

        public static Bitmap inciseBitmap(Bitmap bitmap) {
            if (bitmap == null) {
                return Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
            }

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            //正方形的边长
            int r = 0;
            //取最短边做边长
            if(width > height) {
                r = height;
            } else {
                r = width;
            }
            //构建一个bitmap
            Bitmap backgroundBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //new一个Canvas，在backgroundBmp上画图
            Canvas canvas = new Canvas(backgroundBmp);
            Paint paint = new Paint();
            //设置边缘光滑，去掉锯齿
            paint.setAntiAlias(true);
            paint.setStrokeWidth(1);
            paint.setColor(0xffffffff);
            //宽高相等，即正方形
            RectF rect = new RectF(0, 0, r, r);
            //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
            //且都等于r/2时，画出来的圆角矩形就是圆形
            canvas.drawRoundRect(rect, r/2, r/2, paint);
            //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //canvas将bitmap画在backgroundBmp上
            canvas.drawBitmap(bitmap, null, rect, paint);

            return backgroundBmp;
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
