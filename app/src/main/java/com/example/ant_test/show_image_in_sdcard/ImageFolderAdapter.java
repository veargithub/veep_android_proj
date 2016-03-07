package com.example.ant_test.show_image_in_sdcard;

import java.util.List;
import com.example.ant_test.R;
import com.example.ant_test.show_image_in_sdcard.NativeImageLoader.NativeImageCallBack;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Title: ImageAdapter.java
 * @Package com.example.ant_test.show_image_in_sdcard
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-6-3 下午7:04:50
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ImageFolderAdapter extends BaseAdapter{
	private List<ImageFolder> list;
	private LayoutInflater inflater;
	private GridView gridView;

	public ImageFolderAdapter(Context context, GridView gridView, List<ImageFolder> list) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.gridView = gridView; 
	} 
	
	@Override
	public int getCount() {
		if (list == null) return 0;
		return list.size();
	}

	@Override
	public ImageFolder getItem(int position) {
		if (list == null) return null;
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gridview_item_folder_show_image_in_sdcard, null);
		}
		ImageView iv = (ImageView)convertView.findViewById(R.id.iv1);
		TextView tv1 = (TextView)convertView.findViewById(R.id.tv1);
		TextView tv2 = (TextView)convertView.findViewById(R.id.tv2);
		ImageFolder folder = getItem(position);
		if (folder != null ) {
			String path = folder.firstImagePath;
			iv.setTag(path);
			tv1.setText(folder.folderName);
			tv2.setText(folder.totalCount + "");
			NativeImageLoader.getInstance().loadImage(path, iv, 100, 100);
//			Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, null, new NativeImageCallBack() {
//				
//				@Override
//				public void onImageLoader(Bitmap bitmap, String path) {
//					ImageView mImageView = (ImageView) gridView.findViewWithTag(path);
//					if(bitmap != null && mImageView != null){
//						mImageView.setImageBitmap(bitmap);
//					}
//				}
//			});
//			if (bitmap != null) {
//				iv.setImageBitmap(bitmap);
//			}
		}
		return convertView;
	}

}
