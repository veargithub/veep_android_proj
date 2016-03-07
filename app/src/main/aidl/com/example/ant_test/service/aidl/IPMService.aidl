package com.example.ant_test.service.aidl;

import com.example.ant_test.service.aidl.IPMListener;

interface IPMService {
	void setListener(in IPMListener listener);
}