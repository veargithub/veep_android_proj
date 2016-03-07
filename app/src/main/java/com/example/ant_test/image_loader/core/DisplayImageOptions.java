package com.example.ant_test.image_loader.core;

import com.example.ant_test.image_loader.displayer.IBitmapDisplayer;
import com.example.ant_test.image_loader.helper.SampleType;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Handler;

/**
 * 包含了所有显示图片的options
 * 当图片正在加载，但还没加载完的时候是否要显示一张默认的图片
 * 如果图片的uri为空，是否要显示一张默认的图片
 * 如果图片加载失败（下载或从本地读），是否要显示一张默认的图片
 * 加载图片前，如果view里已经有一张图片了，是否清除之
 * 是否将图片缓存到内存里
 * 是否将图片缓存到外部存储里
 * 解码图片用到的options
 * 图片加载好了之后怎么显示（渐变or圆角）
 */
public class DisplayImageOptions {
	private final int imageResOnLoading;//未加载完时显示的默认图片
	private final int imageResForEmptyUri;//空uri时显示的默认图片
	private final int imageResOnFail;//加载失败时显示的默认图片
	private final Drawable imageOnLoading;//未加载完时显示的默认图片
	private final Drawable imageForEmptyUri;//空uri时显示的默认图片
	private final Drawable imageOnFail;//加载失败时显示的默认图片
	
	private final boolean resetViewBeforeLoading;//加载之前是否清除前一张
	private final boolean cacheInMemory;//是否在内存里缓存
	private final boolean cacheOnDisk;//是否在disk里缓存
	
	private final SampleType sampleType;//图片的采样方式
	private final Options decodingOptions;
	private final IBitmapDisplayer displayer;
	private final Handler handler;
	private final boolean isSyncLoading;//是否异步加载，如果为false，则会另开一个线程处理task
	
	private DisplayImageOptions(Builder builder) {
		imageResOnLoading = builder.imageResOnLoading;
		imageResForEmptyUri = builder.imageResForEmptyUri;
		imageResOnFail = builder.imageResOnFail;
		imageOnLoading = builder.imageOnLoading;
		imageForEmptyUri = builder.imageForEmptyUri;
		imageOnFail = builder.imageOnFail;
		resetViewBeforeLoading = builder.resetViewBeforeLoading;
		cacheInMemory = builder.cacheInMemory;
		cacheOnDisk = builder.cacheOnDisk;

		decodingOptions = builder.decodingOptions;

		sampleType = builder.sampleType;
		displayer = builder.displayer;
		handler = builder.handler;
		isSyncLoading = builder.isSyncLoading;
	}
	
	public boolean shouldShowImageOnLoading() {
		return imageOnLoading != null || imageResOnLoading != 0;
	}

	public boolean shouldShowImageForEmptyUri() {
		return imageForEmptyUri != null || imageResForEmptyUri != 0;
	}

	public boolean shouldShowImageOnFail() {
		return imageOnFail != null || imageResOnFail != 0;
	}
	
	public Drawable getImageOnLoading(Resources res) {
		return imageResOnLoading != 0 ? res.getDrawable(imageResOnLoading) : imageOnLoading;
	}

	public Drawable getImageForEmptyUri(Resources res) {
		return imageResForEmptyUri != 0 ? res.getDrawable(imageResForEmptyUri) : imageForEmptyUri;
	}

	public Drawable getImageOnFail(Resources res) {
		return imageResOnFail != 0 ? res.getDrawable(imageResOnFail) : imageOnFail;
	}
	
	public SampleType getSampleType() {
		return sampleType;
	}

	public IBitmapDisplayer getDisplayer() {
		return displayer;
	}
	
	boolean isSyncLoading() {
		return isSyncLoading;
	}

	public boolean isResetViewBeforeLoading() {
		return resetViewBeforeLoading;
	}
	
	public Handler getHandler() {
		return handler;
	}

	public static class Builder {
		private int imageResOnLoading = 0;
		private int imageResForEmptyUri = 0;
		private int imageResOnFail = 0;
		private Drawable imageOnLoading = null;
		private Drawable imageForEmptyUri = null;
		private Drawable imageOnFail = null;
		private boolean resetViewBeforeLoading = false;
		private boolean cacheInMemory = false;
		private boolean cacheOnDisk = false;
		private SampleType sampleType = SampleType.SAMPLE_TYPE_POWER_OF_TWO;
		private Options decodingOptions = new Options();
	
		
		private IBitmapDisplayer displayer = DefaultConfigurationFactory.createBitmapDisplayer();
		private Handler handler = null;
		private boolean isSyncLoading = false;

		public Builder() {
			decodingOptions.inPurgeable = true;
			decodingOptions.inInputShareable = true;
		}

		/**
		 * Stub image will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
		 * image aware view} during image loading
		 *
		 * @param imageRes Stub image resource
		 * @deprecated Use {@link #showImageOnLoading(int)} instead
		 */
		@Deprecated
		public Builder showStubImage(int imageRes) {
			imageResOnLoading = imageRes;
			return this;
		}

		/**
		 * Incoming image will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
		 * image aware view} during image loading
		 *
		 * @param imageRes Image resource
		 */
		public Builder showImageOnLoading(int imageRes) {
			imageResOnLoading = imageRes;
			return this;
		}

		/**
		 * Incoming drawable will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
		 * image aware view} during image loading.
		 * This option will be ignored if {@link DisplayImageOptions.Builder#showImageOnLoading(int)} is set.
		 */
		public Builder showImageOnLoading(Drawable drawable) {
			imageOnLoading = drawable;
			return this;
		}

		/**
		 * Incoming image will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
		 * image aware view} if empty URI (null or empty
		 * string) will be passed to <b>ImageLoader.displayImage(...)</b> method.
		 *
		 * @param imageRes Image resource
		 */
		public Builder showImageForEmptyUri(int imageRes) {
			imageResForEmptyUri = imageRes;
			return this;
		}

		/**
		 * Incoming drawable will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
		 * image aware view} if empty URI (null or empty
		 * string) will be passed to <b>ImageLoader.displayImage(...)</b> method.
		 * This option will be ignored if {@link DisplayImageOptions.Builder#showImageForEmptyUri(int)} is set.
		 */
		public Builder showImageForEmptyUri(Drawable drawable) {
			imageForEmptyUri = drawable;
			return this;
		}

		/**
		 * Incoming image will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
		 * image aware view} if some error occurs during
		 * requested image loading/decoding.
		 *
		 * @param imageRes Image resource
		 */
		public Builder showImageOnFail(int imageRes) {
			imageResOnFail = imageRes;
			return this;
		}

		/**
		 * Incoming drawable will be displayed in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
		 * image aware view} if some error occurs during
		 * requested image loading/decoding.
		 * This option will be ignored if {@link DisplayImageOptions.Builder#showImageOnFail(int)} is set.
		 */
		public Builder showImageOnFail(Drawable drawable) {
			imageOnFail = drawable;
			return this;
		}

		/**
		 * {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
		 * image aware view} will be reset (set <b>null</b>) before image loading start
		 *
		 * @deprecated Use {@link #resetViewBeforeLoading(boolean) resetViewBeforeLoading(true)} instead
		 */
		public Builder resetViewBeforeLoading() {
			resetViewBeforeLoading = true;
			return this;
		}

		/**
		 * Sets whether {@link com.nostra13.universalimageloader.core.imageaware.ImageAware
		 * image aware view} will be reset (set <b>null</b>) before image loading start
		 */
		public Builder resetViewBeforeLoading(boolean resetViewBeforeLoading) {
			this.resetViewBeforeLoading = resetViewBeforeLoading;
			return this;
		}

		/**
		 * Loaded image will be cached in memory
		 *
		 * @deprecated Use {@link #cacheInMemory(boolean) cacheInMemory(true)} instead
		 */
		@Deprecated
		public Builder cacheInMemory() {
			cacheInMemory = true;
			return this;
		}

		/** Sets whether loaded image will be cached in memory */
		public Builder cacheInMemory(boolean cacheInMemory) {
			this.cacheInMemory = cacheInMemory;
			return this;
		}

		/**
		 * Loaded image will be cached on disk
		 *
		 * @deprecated Use {@link #cacheOnDisk(boolean) cacheOnDisk(true)} instead
		 */
		@Deprecated
		public Builder cacheOnDisc() {
			return cacheOnDisk(true);
		}

		/**
		 * Sets whether loaded image will be cached on disk
		 *
		 * @deprecated Use {@link #cacheOnDisk(boolean)} instead
		 */
		@Deprecated
		public Builder cacheOnDisc(boolean cacheOnDisk) {
			return cacheOnDisk(cacheOnDisk);
		}

		/** Sets whether loaded image will be cached on disk */
		public Builder cacheOnDisk(boolean cacheOnDisk) {
			this.cacheOnDisk = cacheOnDisk;
			return this;
		}

		/**
		 * Sets {@linkplain ImageScaleType scale type} for decoding image. This parameter is used while define scale
		 * size for decoding image to Bitmap. Default value - {@link ImageScaleType#IN_SAMPLE_POWER_OF_2}
		 */
		public Builder imageScaleType(SampleType sampleType) {
			this.sampleType = sampleType;
			return this;
		}

		/** Sets {@link Bitmap.Config bitmap config} for image decoding. Default value - {@link Bitmap.Config#ARGB_8888} */
		public Builder bitmapConfig(Bitmap.Config bitmapConfig) {
			if (bitmapConfig == null) throw new IllegalArgumentException("bitmapConfig can't be null");
			decodingOptions.inPreferredConfig = bitmapConfig;
			return this;
		}

		/**
		 * Sets options for image decoding.<br />
		 * <b>NOTE:</b> {@link Options#inSampleSize} of incoming options will <b>NOT</b> be considered. Library
		 * calculate the most appropriate sample size itself according yo {@link #imageScaleType(ImageScaleType)}
		 * options.<br />
		 * <b>NOTE:</b> This option overlaps {@link #bitmapConfig(android.graphics.Bitmap.Config) bitmapConfig()}
		 * option.
		 */
		public Builder decodingOptions(Options decodingOptions) {
			if (decodingOptions == null) throw new IllegalArgumentException("decodingOptions can't be null");
			this.decodingOptions = decodingOptions;
			return this;
		}

		

		

		/**
		 * Sets custom {@link BitmapDisplayer displayer} for image loading task. Default value -
		 * {@link DefaultConfigurationFactory#createBitmapDisplayer()}
		 */
		public Builder displayer(IBitmapDisplayer displayer) {
			if (displayer == null) throw new IllegalArgumentException("displayer can't be null");
			this.displayer = displayer;
			return this;
		}

		Builder syncLoading(boolean isSyncLoading) {
			this.isSyncLoading = isSyncLoading;
			return this;
		}

		/**
		 * Sets custom {@linkplain Handler handler} for displaying images and firing {@linkplain ImageLoadingListener
		 * listener} events.
		 */
		public Builder handler(Handler handler) {
			this.handler = handler;
			return this;
		}

		/** Sets all options equal to incoming options */
		public Builder cloneFrom(DisplayImageOptions options) {
			imageResOnLoading = options.imageResOnLoading;
			imageResForEmptyUri = options.imageResForEmptyUri;
			imageResOnFail = options.imageResOnFail;
			imageOnLoading = options.imageOnLoading;
			imageForEmptyUri = options.imageForEmptyUri;
			imageOnFail = options.imageOnFail;
			resetViewBeforeLoading = options.resetViewBeforeLoading;
			cacheInMemory = options.cacheInMemory;
			cacheOnDisk = options.cacheOnDisk;
			sampleType = options.sampleType;
			decodingOptions = options.decodingOptions;
		
			displayer = options.displayer;
			handler = options.handler;
			isSyncLoading = options.isSyncLoading;
			return this;
		}

		/** Builds configured {@link DisplayImageOptions} object */
		public DisplayImageOptions build() {
			return new DisplayImageOptions(this);
		}
	}
	
	/**
	 * 创建一个简单的options
	 * <ul>
	 * <li>原图片<b>不</b>会被清空，直到新的图片被加载出来之前</li>
	 * <li>图片 <b>不</b>会被缓存到内存里</li>
	 * <li>图片<b>不</b>会被缓存到磁盘中</li>
	 * <li>{@link ImageScaleType#IN_SAMPLE_POWER_OF_2} 压缩模式会被使用</li>
	 * <li>{@link Bitmap.Config#ARGB_8888} bitmap 配置会被使用</li>
	 * <li>{@link SimpleBitmapDisplayer} 会被使用</li>
	 * </ul>
	 * <p/>
	 * These option are appropriate for simple single-use image (from drawables or from Internet) displaying.
	 */
	public static DisplayImageOptions createSimple() {
		return new Builder().build();
	}
}
