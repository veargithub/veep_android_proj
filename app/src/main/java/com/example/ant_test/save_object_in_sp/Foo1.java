package com.example.ant_test.save_object_in_sp;

import java.io.Serializable;
import java.util.List;

/**
 * @Title: Foo1.java
 * @Package com.example.ant_test.save_object_in_sp
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-30 上午9:39:14
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Foo1 implements Serializable{
	private static final long serialVersionUID = 1L;

	public String property1;
	
	public int property2;
	
	public List<Foo2> list;
}
