package com.example.ant_test.image_loader.cache.memory;

import java.util.Collection;

/**
 * @Title: IMemoryCache.java
 * @Package com.example.ant_test.image_loader.cache.memery
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-22 下午4:41:20
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface IMemoryCache<K, V> {
	
	boolean put(K key, V value);

	V get(K key);

	V remove(K key);

	Collection<K> keys();

	void clear();
}
