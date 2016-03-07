package com.example.ant_test.image_loader.cache.disk;

import java.io.File;

import com.example.ant_test.image_loader.cache.disk.naming.IFileNameGenerator;

/**
 * @Title: UnlimitedDiscCache.java
 * @Package com.example.ant_test.image_loader.cache.disk
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-10-9 下午5:25:09
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class UnlimitedDiscCache extends BaseDiscCache{

	public UnlimitedDiscCache(File cacheDir, File reserveCacheDir,
			IFileNameGenerator fileNameGenerator) {
		super(cacheDir, null, fileNameGenerator);
	}

	public UnlimitedDiscCache(File cacheDir, File reserveCacheDir) {
		super(cacheDir, null);
	}

	public UnlimitedDiscCache(File cacheDir) {
		super(cacheDir);
	}

}
