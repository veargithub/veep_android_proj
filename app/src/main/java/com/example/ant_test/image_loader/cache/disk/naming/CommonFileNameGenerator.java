package com.example.ant_test.image_loader.cache.disk.naming;
/**
 * @Title: CommonFileNameGenerator.java
 * @Package com.example.ant_test.image_loader.cache.disk.naming
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-23 下午1:15:46
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class CommonFileNameGenerator implements IFileNameGenerator{

	@Override
	public String generate(String imageUri) {
		return imageUri;
	}

}
