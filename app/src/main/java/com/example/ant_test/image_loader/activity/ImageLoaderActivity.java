package com.example.ant_test.image_loader.activity;


import com.example.ant_test.R;


//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import com.example.ant_test.image_loader.UrlConstant;


import com.example.ant_test.image_loader.cache.disk.naming.Md5FileNameGenerator;
import com.example.ant_test.image_loader.core.DisplayImageOptions;
import com.example.ant_test.image_loader.core.ImageLoader;
import com.example.ant_test.image_loader.core.ImageLoaderMainConfig;
import com.example.ant_test.image_loader.displayer.CircleBitmapDisplayer;
import com.example.ant_test.image_loader.displayer.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * @Title: ImageLoaderActivity.java
 * @Package com.example.ant_test.image_loader.activity
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-3 下午2:21:03
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ImageLoaderActivity extends Activity{
	private ListView listview;
	DisplayImageOptions options, options3;
    com.nostra13.universalimageloader.core.DisplayImageOptions options2, options4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_image_loader);

		ImageLoaderMainConfig config = new ImageLoaderMainConfig.Builder(this)
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.diskCacheSize(50 * 1024 * 1024) // 50 Mb
		.build();
        ImageLoader.getInstance().init(config);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.displayer(new RoundedBitmapDisplayer(20))
        //        .displayer((new CircleBitmapDisplayer()))
		.build();

        options3 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                //.displayer(new RoundedBitmapDisplayer(20))
                .displayer((new CircleBitmapDisplayer()))
                .build();

        ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config2);
        options2 = new com.nostra13.universalimageloader.core.DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer(20))
                //.displayer(new com.example.ant_test.image_loader.extras.CircleBitmapDisplayer())
                .build();
        options4 = new com.nostra13.universalimageloader.core.DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                //.displayer(new com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer(20))
                        .displayer(new com.example.ant_test.image_loader.extras.CircleBitmapDisplayer())
                .build();

		listview = (ListView) findViewById(android.R.id.list);
		listview.setAdapter(new ImageAdapter());
	}

    class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		ImageAdapter() {
			inflater = LayoutInflater.from(ImageLoaderActivity.this);
		}

		@Override
		public int getCount() {
			return UrlConstant.URLS.length * 2;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = inflater.inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.text);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text.setText("Item " + (position + 1));
            Log.d(">>>>", "holder.image:" + holder.image.getWidth() + " " + holder.image.getHeight());

            if (position % 2 == 0) {
                //com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(UrlConstant.URLS[position / 2], holder.image, options2, null, null);
                ImageLoader.getInstance().displayImage(UrlConstant.URLS[position / 2], holder.image, options, null, null);
            } else {
                //com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(UrlConstant.URLS[position / 2], holder.image, options4, null, null);
                ImageLoader.getInstance().displayImage(UrlConstant.URLS[position / 2], holder.image, options3, null, null);
            }

			return view;
		}
	}
	
	private static class ViewHolder {
		TextView text;
		ImageView image;
	}
}
