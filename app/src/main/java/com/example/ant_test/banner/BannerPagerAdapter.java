package com.example.ant_test.banner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ant_test.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;


public class BannerPagerAdapter extends PagerAdapter implements View.OnClickListener {
	private List<ImageView> imageList;
    private boolean onlyOne = false;//是否只有1张图，如果是就不用滑了
    private PointTips pointTips;
    private Context mContext;
    private OnBannerClickListener onBannerClickListener;

	public BannerPagerAdapter(Context context, int[] res) {
        this.mContext = context;
        initImageLoader(context);
		if (context != null && res != null) {
			imageList = new ArrayList<ImageView>();
			for (int i = 0; i < res.length; i++) {
				ImageView iv = new ImageView(context);
				iv.setImageDrawable(context.getResources().getDrawable(res[i]));
				imageList.add(iv);
			}
		}
	}

    public BannerPagerAdapter(Context context, PointTips pointTips) {
        this.mContext = context;
        this.pointTips = pointTips;
        initImageLoader(context);
        setImages(null);
    }
	
	@Override
	public int getCount() {
        if (onlyOne) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//        final List<ImageView> list = imageList;//如果图片太少会有BUG,所以要注视掉这段代码
//		if (list != null && list.size() > 0) {
//			container.removeView(list.get(position % list.size()));
//		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
        final List<ImageView> list = imageList;
		if (list != null && list.size() > 0) {
			int size = list.size();
			View view = list.get(position % size);
            final ViewGroup parent = (ViewGroup)view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
			container.addView(view);
			return view;
		}
		return null;
	}
	
	public int getImageSize() {
		if (imageList != null) {
			return imageList.size();
		} 
		return 0;
	}

    public void setImages(List<IBannerEntity> bannerList) {
        final Context context = mContext;
        if (context == null) return;
        final List<ImageView> list = new ArrayList<ImageView>();
//        final String[] imgs = BannerUtil.getImages(context);
//        final String[] urls = BannerUtil.getUrls(context);
        //final List<IBannerEntity> bannerList = BannerUtil.loadBannerList(context);

        if (bannerList == null || bannerList.size() == 0) {
            final ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageResource(R.drawable.ic_launcher);//TODO
            //iv.setId(-1);
            iv.setOnClickListener(this);
            list.add(iv);
        } else {
            for (int i = 0; i < bannerList.size(); i++) {
                final ImageView iv = new ImageView(context);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(bannerList.get(i).getImageUrl(), iv, options);
                iv.setOnClickListener(new MyOnClickListener(bannerList.get(i)));
                list.add(iv);
            }

        }
        if (list.size() <= 1) {
            onlyOne = true;
        } else {
            onlyOne = false;
        }
        pointTips.setPointNumber(list.size());
        if (list.size() == 2) {
            for (int i = 0; i < bannerList.size(); i++) {
                final ImageView iv = new ImageView(context);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(bannerList.get(i).getImageUrl(), iv, options);
                iv.setOnClickListener(new MyOnClickListener(bannerList.get(i)));
                list.add(iv);
            }
        }
        imageList = list;
        this.notifyDataSetChanged();

    }

    ImageLoaderConfiguration config;
    DisplayImageOptions options;

    private void initImageLoader(Context context) {
        config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.ic_launcher)

                .build();
    }

    @Override
    public void onClick(View v) {
        String url = (String)v.getTag();
        if (url != null) {
            Log.d(">>>>", "onClick:" + url);
            final Context context = mContext;
            if (context instanceof Activity) {
                Activity act = (Activity)context;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                act.startActivity(browserIntent);
            }
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        private IBannerEntity banner;

        public MyOnClickListener(IBannerEntity banner) {
            this.banner = banner;
        }

        @Override
        public void onClick(View v) {
            if (onBannerClickListener != null) {
                onBannerClickListener.onBannerClick(banner);
            }
        }
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    public interface OnBannerClickListener {
        public void onBannerClick(IBannerEntity bannerEntity);
    }
}
