package com.example.ant_test.image_loader.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * @Title: IImageDownloader.java
 * @Package com.example.ant_test.image_loader.downloader
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-18 下午5:32:20
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface IImageDownloader {
	InputStream getStream(String imageUri) throws IOException ;
	
	public enum Scheme {
		HTTP("http"), HTTPS("https"), FILE("file"), UNKNOWN("");
		
		private String scheme;
		private String uriPrefix;//uri前缀

		Scheme(String scheme) {
			this.scheme = scheme;
			uriPrefix = scheme + "://";
		}
		
		public static Scheme ofUri(String uri) {
			if (uri != null) {
				for (Scheme s : values()) {
					if (s.belongsTo(uri)) {
						return s;
					}
				}
			}
			return UNKNOWN;
		}
		
		private boolean belongsTo(String uri) {
			return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
		}
		
		public String wrap(String path) {
			return uriPrefix + path;
		}
		
		public String crop(String uri) {
			if (!belongsTo(uri)) {
				throw new IllegalArgumentException(String.format("URI [%1$s] 无法识别 [%2$s]", uri, scheme));
			}
			return uri.substring(uriPrefix.length());
		}
	}
}
