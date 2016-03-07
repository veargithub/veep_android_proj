package com.example.ant_test.show_image_in_sdcard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.ant_test.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

public class ShowImageInSdcardActivity extends Activity{
	private GridView gv;
	private List<ImageFolder> list;
	private ImageFolderAdapter adapter;
	private Map<String, ImageFolder> folderMap;
	private String[] curImages;
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			//TODO end dialog
			list.clear();
			Map<String, ImageFolder> fm = folderMap;
			if (fm.isEmpty()) return;
			Iterator<Map.Entry<String, ImageFolder>> iterator = fm.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, ImageFolder> entry = iterator.next();
				ImageFolder folder = entry.getValue();
				list.add(folder);
			}
			adapter.notifyDataSetChanged();
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image_in_sdcard);
		curImages = this.getIntent().getStringArrayExtra("images");
		gv = (GridView) findViewById(R.id.gv1);
		list= new ArrayList<ImageFolder>();
		folderMap = new HashMap<String, ImageFolder>();
		adapter = new ImageFolderAdapter(this, gv, list);
		gv.setAdapter(adapter);
		loadImageUri();
	}
	
	private static final String[] IMG_TYPES = new String[] {"image/jpeg", "image/png"};
	
	private void loadImageUri() {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(this, "找不到设备中的照片", Toast.LENGTH_SHORT).show();
			return;
		}
		//TODO start dialog
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, ImageFolder> fm = new HashMap<String, ImageFolder>();
				Uri ecUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver contentResolver = ShowImageInSdcardActivity.this.getContentResolver();
				Cursor cursor = contentResolver.query(ecUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
						IMG_TYPES, MediaStore.Images.Media.DATE_MODIFIED);
				while (cursor.moveToNext()) {
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.d(">>>>", path);
					File file = new File(path);
					String parentName = file.getParentFile().getName();
					if (parentName != null && !parentName.equals("")) {
						ImageFolder folder;
						if (fm.containsKey(parentName)) {
							folder = fm.get(parentName);
						} else {
							Log.d(">>>>", "parentName:" + parentName);
							folder = new ImageFolder();
							folder.firstImagePath = path;
							folder.folderName = parentName;
							fm.put(parentName, folder);
						}
						folder.totalCount++;
						if (curImages != null) {
							for (String imgPath : curImages) {
								if (imgPath != null && imgPath.equals(path)) {
									folder.selectedCount++;
									break;
								}
							}
						}
						folder.images.add(path);
					}
				}
				cursor.close();
				folderMap = fm;
				handler.sendEmptyMessage(0);
			}
		}).start();
	}
}
