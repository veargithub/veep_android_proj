package com.example.ant_test.showpicture;

import java.io.File;
import java.util.UUID;

import com.example.ant_test.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class ShowAllPictureInDeviceActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_all_picture_in_device);
		getImages();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		Log.d(">>>>>>>", "uuid:" + uuid);
	}

	private void getImages() {
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = ShowAllPictureInDeviceActivity.this.getContentResolver();
                //只查询jpeg和png的图片 need permission <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
                if(mCursor == null) return;
                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String imagePath = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    //获取该图片的父路径名
                    String imgParentDir = new File(imagePath).getParentFile().getName();
                    Log.d(">>>>>>>>", imagePath + ", " + imgParentDir);
                }
                mCursor.close();
            }
        }).start();  
        
    }
	
}
