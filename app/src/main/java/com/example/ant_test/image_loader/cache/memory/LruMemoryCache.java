package com.example.ant_test.image_loader.cache.memory;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * @Title: LruMemoryCache.java
 * @Package com.example.ant_test.image_loader.cache.memory
 * @Description: 限制了图片占用总大小的cache
 * @author Chenxiao
 * @date 2014-9-22 下午5:22:45
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class LruMemoryCache implements IMemoryCache<String, Bitmap>{
	private final LinkedHashMap<String, Bitmap> map;

	private final int maxSize;//这个cache最大多少bytes(16mb可能会比较好？)
	/** Size of this cache in bytes */
	private int size;//当前一共占用多少bytes
	
	public LruMemoryCache(int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		this.maxSize = maxSize;
		this.map = new LinkedHashMap<String, Bitmap>(0, 0.75f, true);
	}

	@Override
	public final boolean put(String key, Bitmap value) {
		if (key == null || value == null) {
			throw new NullPointerException("key == null || value == null");
		}

		synchronized (this) {
			size += sizeOf(key, value);
			Bitmap previous = map.put(key, value);//如果map里已经有了这个key，则返回这个key对应的value，并用新的value覆盖老的
			if (previous != null) {//判断是否存在老的图片，如果有，则减去老的图片所占用的空间
				size -= sizeOf(key, previous);
			}
		}

		trimToSize(maxSize);
		return true;
	}

	@Override
	public final Bitmap get(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}

		synchronized (this) {
			return map.get(key);
		}
	}

	@Override
	public final Bitmap remove(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}

		synchronized (this) {
			Bitmap previous = map.remove(key);//如果map里存在这个key，则返回这个key对应的图片
			if (previous != null) {//如果上面删除成功，则总的size减去这张图片所占的size
				size -= sizeOf(key, previous);
			}
			return previous;
		}
	}

	@Override
	public Collection<String> keys() {
		synchronized (this) {
			return new HashSet<String>(map.keySet());
		}
	}

	@Override
	public void clear() {
		trimToSize(-1); // -1 will evict 0-sized elements
	}
	/**
	 * 判断现在的size是否已经超过限制的size，如果是，则按照先进先删的原则，删除掉老的图片直到当前size小于限制size
	 * @param maxSize
	 */
	private void trimToSize(int maxSize) {
		while (true) {
			String key;
			Bitmap value;
			synchronized (this) {
				if (size < 0 || (map.isEmpty() && size != 0)) {
					throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
				}

				if (size <= maxSize || map.isEmpty()) {
					break;
				}

				Map.Entry<String, Bitmap> toEvict = map.entrySet().iterator().next();
				if (toEvict == null) {
					break;
				}
				key = toEvict.getKey();
				value = toEvict.getValue();
				map.remove(key);//删除
				size -= sizeOf(key, value);//刷新当前size
			}
		}
	}
	/**
	 * 获取图片所占的空间，单位byte
	 * @param key
	 * @param value
	 * @return
	 */
	private int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
}
