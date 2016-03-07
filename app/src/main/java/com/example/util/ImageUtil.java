package com.example.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

public class ImageUtil {
	private static final String NAME = "image";
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	private static final String CACHE_DIR = "eastmoney_image_cache";
	/**
	 * 把图片存到sharedpreference里
	 * @param url
	 * @param bitmap
	 */
	public static void saveInSP(Context context, String url, Bitmap bitmap) {
		SharedPreferences sp = context.getSharedPreferences(NAME, 0);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageString = Base64.encodeToString(b, Base64.DEFAULT);
        sp.edit().putString(url, imageString).commit();
	}
	private static String getImageString(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(NAME, 0);
        String imageString = sp.getString(key, "");
        return imageString;
	}
	/**
	 * 从sharedpreference里加载图片
	 * @param key
	 * @return
	 */
	public static Bitmap loadFromSP(Context context, String key) {
		String imageString = getImageString(context, key);
		if (imageString == null || imageString.equals("")) return null;
        byte[] b = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
	}
	
	public static Bitmap loadFromSP(Context context, String key, int width, int height) {
		String imageString = getImageString(context, key);
		if (imageString == null || imageString.equals("")) return null;
		byte[] b = Base64.decode(imageString, Base64.DEFAULT);
		Bitmap bitmap = unzipImageSize(b, width, height);
		return bitmap;
	}
	
	private static Bitmap unzipImageSize(byte[] b, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(b, 0, b.length, options);
		int sampleSize = getSample(options.outWidth, options.outHeight, width, height);
		Log.d(">>>>", "sample size:" + sampleSize);
		options.inSampleSize = sampleSize;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, options);
		return bitmap;
	}
	
	private static Bitmap unzipImageSize(String path, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int sampleSize = getSample(options.outWidth, options.outHeight, width, height);
		Log.d(">>>>", "sample size2:" + sampleSize);
		options.inSampleSize = sampleSize;
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	
	private static int getSample(int imageWidth, int imageHeight, int viewWidth, int viewHeight) {
		float scale = 1f;
		if (viewWidth < 1 && viewHeight < 1) {
			return 1;
		} else if (viewWidth < 1) {
			scale = (float)imageHeight / viewHeight;
		} else if (viewHeight < 1) {
			scale = (float)imageWidth / viewWidth;
		} else {
			final float widthScale = (float)imageWidth / viewWidth;
			final float heightScale = (float)imageHeight / viewHeight;
			scale = Math.min(widthScale, heightScale);
		}
		Log.d(">>>>", "getSample:" + imageWidth + " " + viewWidth + " "
				+ imageHeight + " " + viewHeight + " " + scale);
		return (Math.round(scale) + 1);
	}
	
	/** 将图片存入sd卡里 **/
    public static void saveInExternal(Bitmap bm, String url) {
    	boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (!sdCardExist || bm == null) {
			return;
		}
        //判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            //SD空间不足
        	return;
        }
        String filename = convertUrlToFileName(url);
        String dir = getDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdirs();
        File file = new File(dir +"/" + filename);
        if (file.exists()){
        	file.delete();
        }
        OutputStream outStream = null;
        try {
        	outStream = new FileOutputStream(file);
            file.createNewFile();
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        } finally {
        	if (outStream != null) {
        		try {
        			outStream.close();
        		} catch(Exception e) {};
        	}
        }
    }

    public static Bitmap loadFromExternal(final String url) {
        final String path = getDirectory() + "/" + convertUrlToFileName(url);
        File file = new File(path);
        if (file.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(path);
            if (bmp == null) {
                file.delete();
            } else {
                return bmp;
            }
        }
        return null;
    }
    
    /** 从sd卡中获取图片 **/
    public static Bitmap loadFromExternal(final String url, int width, int height) {
        final String path = getDirectory() + "/" + convertUrlToFileName(url);
        File file = new File(path);
        if (file.exists()) {
            Bitmap bitmap = unzipImageSize(path, width, height);
            if (bitmap == null) {
                file.delete();
            } else {
                return bitmap;
            }
        }
        return null;
    }
    /**
     * 获取图片
     * @param context
     * @param url
     * @return
     */
    public static Bitmap load(final Context context, final String url) {
    	if (url == null || url.equals("")) return null;
    	if (context != null) {
    		Bitmap bitmap = loadFromSP(context, url);
    		if (bitmap != null) return bitmap;
    	}
    	Bitmap bitmap = loadFromExternal(url);
    	if (bitmap != null) return bitmap;
    	return null;
    }
    public static Bitmap load(final Context context, final String url, int width, int height) {
    	if (url == null || url.equals("")) return null;
    	Bitmap bitmap = null;
    	if (context != null) {
    		bitmap = loadFromSP(context, url, width, height);
    		if (bitmap != null) return bitmap;
    		bitmap = loadFromExternal(url, width, height);
        	if (bitmap != null) return bitmap;
    	}
    	return null;
    }
    public static final String LOAD_TYPE_WIDTH = "1";
    public static final String LOAD_TYPE_HEIGHT = "2";
   
    private static final int MB = 1024*1024;
    /** 计算sdcard上的剩余空间 **/
    private static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    } 
    /** 获得缓存目录 **/
    private static String getDirectory() {
        String dir = getSDPath() + "/" + CACHE_DIR;
        return dir;
    }
                                                                
    /** 取SD卡路径 **/
    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    } 
    private static final String WHOLESALE_CONV = ".cach";
    /** 将url转成文件名 **/
    private static String convertUrlToFileName(String url) {
		return url.replaceAll("/", "_") + WHOLESALE_CONV;
    }
}
