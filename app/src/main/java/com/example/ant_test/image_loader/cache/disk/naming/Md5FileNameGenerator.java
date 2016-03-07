package com.example.ant_test.image_loader.cache.disk.naming;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Title: Md5FileNameGenerator.java
 * @Package com.example.ant_test.image_loader.cache.disk.naming
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-26 上午10:41:35
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Md5FileNameGenerator implements IFileNameGenerator{

	private static final String HASH_ALGORITHM = "MD5";
	private static final int RADIX = 10 + 26; // 10 digits + 26 letters

	@Override
	public String generate(String imageUri) {
		byte[] md5 = getMD5(imageUri.getBytes());
		BigInteger bi = new BigInteger(md5).abs();
		return bi.toString(RADIX);
	}

	private byte[] getMD5(byte[] data) {
		byte[] hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(data);
			hash = digest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}

}
