package com.example.ant_test.disk_lru_cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.ant_test.R;
import com.example.ant_test.disk_lru_cache.libcore.io.DiskLruCache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

/**
 * @Title: DiskLruCacheTestActivity.java
 * @Package com.example.ant_test.disk_lru_cache
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-24 下午4:49:31
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class DiskLruCacheTestActivity extends Activity{

	private ImageView iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_with_one_imageview);
		iv = (ImageView) findViewById(R.id.iv1);
		test();
	}
	
	private void test() {
		open();
	    new Thread(new Runnable() {  
	        @Override  
	        public void run() {  
	            try {  
	                 
	                String key = hashKeyForDisk(imageUrl);  
	                DiskLruCache.Editor editor = mDiskLruCache.edit(key);  
	                if (editor != null) {  
	                    OutputStream outputStream = editor.newOutputStream(0);  
	                    if (downloadUrlToStream(imageUrl, outputStream)) {  
	                        editor.commit();  
	                    } else {  
	                        editor.abort();  
	                    }  
	                }  
	                mDiskLruCache.flush();
	                show();
	                
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }).start();  
	}

	public File getDiskCacheDir(Context context, String uniqueName) {  
	    String cachePath;  
	    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
	            || !Environment.isExternalStorageRemovable()) {  
	        cachePath = context.getExternalCacheDir().getPath();  
	    } else {  
	        cachePath = context.getCacheDir().getPath();  
	    }  
	    return new File(cachePath + File.separator + uniqueName);  
	} 
	
	DiskLruCache mDiskLruCache = null;
	
	private void open() {
		try {  
		    File cacheDir = getDiskCacheDir(this, "bitmap");  
		    if (!cacheDir.exists()) {  
		        cacheDir.mkdirs();  
		    }  
		    mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, 10 * 1024 * 1024);  
		} catch (IOException e) {  
		    e.printStackTrace();  
		}  
	}
	
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {  
        HttpURLConnection urlConnection = null;  
        BufferedOutputStream out = null;  
        BufferedInputStream in = null;  
        try {  
            final URL url = new URL(urlString);  
            urlConnection = (HttpURLConnection) url.openConnection();  
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);  
            out = new BufferedOutputStream(outputStream, 8 * 1024);  
            int b;  
            while ((b = in.read()) != -1) {  
                out.write(b);  
            }  
            return true;  
        } catch (final IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (urlConnection != null) {  
                urlConnection.disconnect();  
            }  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (final IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return false;  
    }
    
    public String hashKeyForDisk(String key) {  
        String cacheKey;  
        try {  
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");  
            mDigest.update(key.getBytes());  
            cacheKey = bytesToHexString(mDigest.digest());  
        } catch (NoSuchAlgorithmException e) {  
            cacheKey = String.valueOf(key.hashCode());  
        }  
        return cacheKey;  
    }  
      
    private String bytesToHexString(byte[] bytes) {  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < bytes.length; i++) {  
            String hex = Integer.toHexString(0xFF & bytes[i]);  
            if (hex.length() == 1) {  
                sb.append('0');  
            }  
            sb.append(hex);  
        }  
        return sb.toString();  
    }
    
    String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
    Bitmap bitmap;
    private void show() {
        try {  
             
            String key = hashKeyForDisk(imageUrl);  
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);  
            if (snapShot != null) {  
                InputStream is = snapShot.getInputStream(0);  
                bitmap = BitmapFactory.decodeStream(is);  
                iv.post(new Runnable() {

					@Override
					public void run() {
						if (bitmap != null) {
							iv.setImageBitmap(bitmap);
						}
						
					}
                	
                });
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
}
