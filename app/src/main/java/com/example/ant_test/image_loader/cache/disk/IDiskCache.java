package com.example.ant_test.image_loader.cache.disk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.example.ant_test.image_loader.util.IoUtils;

import android.graphics.Bitmap;

/**
 * @Title: IDiskCache.java
 * @Package com.example.ant_test.image_loader.cache
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-19 下午5:42:12
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface IDiskCache {
	File getCacheDirectory();//获取缓存的根目录
	
	File get(String imageUri);//通过图片名从缓存中获取图片
	
	boolean save(String imageUri, Bitmap bitmap) throws IOException;//把本已存在手机里的图片重新压缩格式化，压缩完了后会同名覆盖掉老的图片
	
	boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener) throws IOException;//把网络上下载下来的图片保存到本地磁盘
	
	boolean remove(String imageUri);//从缓存中物理删除这张图片
	
	void clearCache();//物理删除缓存中所有图片
	
	void close();//释放资源，如果需要
}
