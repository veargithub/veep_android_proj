package com.example.ant_test.image_loader.cache.disk;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.ant_test.image_loader.cache.disk.naming.IFileNameGenerator;
import com.example.ant_test.image_loader.core.DefaultConfigurationFactory;
import com.example.ant_test.image_loader.util.IoUtils;

import android.graphics.Bitmap;

/**
 * @Title: BaseDiscCache.java
 * @Package com.example.ant_test.image_loader.cache.disk
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-23 下午1:22:35
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class BaseDiscCache implements IDiskCache{
	
	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
	/** {@value */
	public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
	/** {@value */
	public static final int DEFAULT_COMPRESS_QUALITY = 100;
	
	private static final String TEMP_IMAGE_POSTFIX = ".tmp";

	protected final File cacheDir;//默认存放图片的文件夹名称
	//protected final File reserveCacheDir;//备胎，先去了，一般用不到
	
	protected final IFileNameGenerator fileNameGenerator;//图片名称生成器

	protected int bufferSize = DEFAULT_BUFFER_SIZE;

	protected Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;//压缩格式，貌似只有png和jpg
	protected int compressQuality = DEFAULT_COMPRESS_QUALITY;//压缩质量
	
	public BaseDiscCache(File cacheDir) {
		this(cacheDir, null);
	}
	
	public BaseDiscCache(File cacheDir, File reserveCacheDir) {
		this(cacheDir, reserveCacheDir, DefaultConfigurationFactory.createFileNameGenerator());
	}
	
	public BaseDiscCache(File cacheDir, File reserveCacheDir, IFileNameGenerator fileNameGenerator) {
		if (cacheDir == null) {
			throw new IllegalArgumentException("cacheDir must not be null");
		}
		if (fileNameGenerator == null) {
			throw new IllegalArgumentException("fileNameGenerator must not be null");
		}

		this.cacheDir = cacheDir;
		//this.reserveCacheDir = reserveCacheDir;
		this.fileNameGenerator = fileNameGenerator;
	}
	
	

	@Override
	public File getCacheDirectory() {
		return cacheDir;
	}

	@Override
	public File get(String imageUri) {
		return getFile(imageUri);//路径+文件名
	}

	/**
	 * 这个方法是把网络上下载下来的图片保存到本地磁盘
	 * @return 保存成功 - true 保存失败 - false
	 */
	@Override
	public boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener) throws IOException {
		File imageFile = getFile(imageUri);
		File tmpFile = new File(imageFile.getAbsolutePath() + TEMP_IMAGE_POSTFIX);
		boolean loaded = false;
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile), bufferSize);
			try {
				loaded = IoUtils.copyStream(imageStream, os, listener, bufferSize);
			} finally {
				IoUtils.closeStream(os);
			}
		} finally {
			IoUtils.closeStream(imageStream);
			if (loaded && !tmpFile.renameTo(imageFile)) {
				loaded = false;
			}
			if (!loaded) {
				tmpFile.delete();
			}
		}
		return loaded;
	}

	/**
	 * 这个方法是把本已存在手机里的图片重新压缩格式化，压缩完了后会同名覆盖掉老的图片
	 */
	@Override
	public boolean save(String imageUri, Bitmap bitmap) throws IOException {
		File imageFile = getFile(imageUri);
		File tmpFile = new File(imageFile.getAbsolutePath() + TEMP_IMAGE_POSTFIX);
		OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile), bufferSize);
		boolean savedSuccessfully = false;
		try {
			savedSuccessfully = bitmap.compress(compressFormat, compressQuality, os);
		} finally {
			IoUtils.closeStream(os);
			if (savedSuccessfully && !tmpFile.renameTo(imageFile)) {
				savedSuccessfully = false;
			}
			if (!savedSuccessfully) {
				tmpFile.delete();
			}
		}
		bitmap.recycle();
		return savedSuccessfully;
	}

	@Override
	public boolean remove(String imageUri) {
		return getFile(imageUri).delete();
	}

	@Override
	public void clearCache() {
		File[] files = cacheDir.listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}
		}
	}

	protected File getFile(String imageUri) {
		String fileName = fileNameGenerator.generate(imageUri);
		File dir = cacheDir;
//		if (!cacheDir.exists() && !cacheDir.mkdirs()) {
//			if (reserveCacheDir != null && (reserveCacheDir.exists() || reserveCacheDir.mkdirs())) {
//				dir = reserveCacheDir;
//			}
//		}
		return new File(dir, fileName);
	}
	
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
		this.compressFormat = compressFormat;
	}

	public void setCompressQuality(int compressQuality) {
		this.compressQuality = compressQuality;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
