package com.example.ant_test.image_loader.helper;
/**
 * @Title: SampleType.java
 * @Package com.example.ant_test.image_loader.helper
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-28 下午2:05:50
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public enum SampleType {
	
	SAMPLE_TYPE_NONE,//不缩小
	
	SAMPLE_TYPE_WITH_RATIO,//按比例缩小
	
	SAMPLE_TYPE_POWER_OF_TWO;//按比例缩小，且缩小倍数只能是2的n次方
	
}
