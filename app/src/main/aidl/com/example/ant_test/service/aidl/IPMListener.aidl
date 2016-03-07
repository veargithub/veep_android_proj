package com.example.ant_test.service.aidl;

import com.example.ant_test.service.bean.EmPushMessage;

oneway interface IPMListener {
	void onReceiveMessage(in EmPushMessage message);
}