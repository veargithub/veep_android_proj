package com.example.ant_test.image_loader.cache.memory;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * @Title: BaseMemoryCache.java
 * @Package com.example.ant_test.image_loader.cache.memory
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-22 下午5:59:30
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class WeakReferenceMemoryCache implements IMemoryCache<String, Bitmap>{
	
	private Map<String, Reference<Bitmap>> map = Collections.synchronizedMap(new HashMap<String, Reference<Bitmap>>()) ;

	@Override
	public boolean put(String key, Bitmap value) {
		map.put(key, new WeakReference<Bitmap>(value));
		return true;
	}

	@Override
	public Bitmap get(String key) {
		Bitmap result = null;
		Reference<Bitmap> reference = map.get(key);
		if (reference != null) {
			result = reference.get();
		}
		return result;
	}

	@Override
	public Bitmap remove(String key) {
		Reference<Bitmap> bmpRef = map.remove(key);
		return bmpRef == null ? null : bmpRef.get();
	}

	@Override
	public Collection<String> keys() {
		synchronized (map) {
			return new HashSet<String>(map.keySet());
		}
	}

	@Override
	public void clear() {
		map.clear();
	}

}
