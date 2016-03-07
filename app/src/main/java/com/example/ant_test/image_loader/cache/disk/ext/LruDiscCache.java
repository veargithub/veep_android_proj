package com.example.ant_test.image_loader.cache.disk.ext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.ant_test.image_loader.cache.disk.IDiskCache;
import com.example.ant_test.image_loader.cache.disk.ext.DiskLruCache.Snapshot;
import com.example.ant_test.image_loader.cache.disk.naming.IFileNameGenerator;
import com.example.ant_test.image_loader.util.IoUtils;
import android.graphics.Bitmap;

/**
 * @Title: LruDiscCache.java
 * @Package com.example.ant_test.image_loader.cache.disk
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-23 下午5:13:36
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class LruDiscCache implements IDiskCache{
	
	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
	
	public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
	
	public static final int DEFAULT_COMPRESS_QUALITY = 100;
	
	public static final int DEFAULT_APP_VERSION = 1;//LruDiscCache的版本号，如果这个号有改变，则默认会删除所有原缓存
	
	public static final int DEFAULT_VALUE_COUNT = 1;//LruDiscCache中一个key对应的图片数量，一般写死1就行

	protected DiskLruCache cache;

	protected final IFileNameGenerator fileNameGenerator;

	protected int bufferSize = DEFAULT_BUFFER_SIZE;

	protected Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
	protected int compressQuality = DEFAULT_COMPRESS_QUALITY;
	protected int appVersion;
	protected int valueCount;
	
	public LruDiscCache(File cacheDir,long cacheMaxSize, int cacheMaxFileCount, 
			IFileNameGenerator fileNameGenerator) throws IOException {
		this(cacheDir, DEFAULT_APP_VERSION, DEFAULT_VALUE_COUNT, cacheMaxSize, cacheMaxFileCount, fileNameGenerator);
	}
	
	public LruDiscCache(File cacheDir, int appVersion, int valueCount, long cacheMaxSize, int cacheMaxFileCount,
			IFileNameGenerator fileNameGenerator) throws IOException {
		if (cacheDir == null) {
			throw new IllegalArgumentException("cacheDir can not be null");
		}
		if (cacheMaxSize < 0) {
			throw new IllegalArgumentException("cacheMaxSize must be positive");
		}
		if (cacheMaxFileCount < 0) {
			throw new IllegalArgumentException("cacheMaxFileCount must be positive");
		}
		if (fileNameGenerator == null) {
			throw new IllegalArgumentException("fileNameGenerator can not be null");
		}

		if (cacheMaxSize == 0) {
			cacheMaxSize = Long.MAX_VALUE;
		}
		if (cacheMaxFileCount == 0) {
			cacheMaxFileCount = Integer.MAX_VALUE;
		}
		this.appVersion = appVersion;
		this.valueCount = valueCount;
		this.fileNameGenerator = fileNameGenerator;
		initCache(cacheDir, cacheMaxSize, cacheMaxFileCount);
	}
	
	private void initCache(File dir, long maxSize, int maxFileCount) throws IOException{
		cache = DiskLruCache.open(dir, appVersion, valueCount, maxSize, maxFileCount);
	}
	
	@Override
	public File getCacheDirectory() {
		return cache.getDirectory();
	}

	@Override
	public File get(String imageUri) {
		try {
			Snapshot snapshot = cache.get(getKey(imageUri));
			return snapshot == null ? null : snapshot.getFile(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean save(String imageUri, Bitmap bitmap) throws IOException {
		DiskLruCache.Editor editor = cache.edit(getKey(imageUri));
		if (editor == null) {
			return false;
		}
		OutputStream os = new BufferedOutputStream(editor.newOutputStream(0), bufferSize);
		boolean saved = false;
		try {
			saved = bitmap.compress(compressFormat, compressQuality, os);
		} finally {
			IoUtils.closeStream(os);
		}
		if (saved) {
			editor.commit();
		} else {
			editor.abort();
		}
		return saved;
	}

	@Override
	public boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener)
			throws IOException {
		DiskLruCache.Editor editor = cache.edit(getKey(imageUri));
		if (editor == null) {
			return false;
		}
		OutputStream os = new BufferedOutputStream(editor.newOutputStream(0), bufferSize);
		boolean saved = false;
		try {
			saved = IoUtils.copyStream(imageStream, os, listener, bufferSize);
		} finally {
			IoUtils.closeStream(os);
		}
		if (saved) {
			editor.commit();
		} else {
			editor.abort();
		}
		return saved;
	}

	@Override
	public boolean remove(String imageUri) {
		try {
			return cache.remove(getKey(imageUri));
		} catch (IOException e) {}
		return false;
	}

	@Override
	public void clearCache() {
		try {
			cache.delete();
			initCache(cache.getDirectory(), cache.getMaxSize(), cache.getMaxFileCount());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		try {
			cache.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getKey(String imageUri) {
		return fileNameGenerator.generate(imageUri);
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

	public void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
	}

	public void setValueCount(int valueCount) {
		this.valueCount = valueCount;
	}
	
	
}
