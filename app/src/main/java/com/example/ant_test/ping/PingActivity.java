package com.example.ant_test.ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

public class PingActivity extends Activity{
	public static final String DEFAULT_JJ_SERVER_LIST = "[wt_f]\n" +
			"jjlvs1=140.207.218.114:80,140.207.218.115:80,140.207.218.116:80\n" +
			"[dx_f]\n" +
			"jjlvs1=61.129.84.226:80,61.129.84.227:80,61.129.84.228:80\n" +
			"[yd_f]\n" +
			"jjlvs1=221.181.67.194:80,221.181.67.195:80,221.181.67.196:80";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] strs = {"140.207.218.114", "61.129.84.226", "221.181.67.194"};
		for (int i = 0; i < strs.length; i++) {
			System.out.println(">>>>" + strs[i] + ":" + getNetSpeed(strs[i]));
		}
		int sProductVersion = 0;
		try {
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			sProductVersion = appInfo.metaData.getInt("com.eastmoney.android.berlin.productVersion");
    	} catch (NameNotFoundException e) {
    		sProductVersion = 0;
			e.printStackTrace();
		}
		System.out.println("sProductVersion:" + sProductVersion);
	}
	
	public int getNetSpeed(String addr) {
		String s = "\n";
		float avgTime = 0;
		String cmd = "ping -w 5 -c 1 " + addr;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				s += line + "\n";
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			avgTime = Float.MAX_VALUE;
			e.printStackTrace();
		} catch (SecurityException e) {
			avgTime = Float.MAX_VALUE;
			e.printStackTrace();
		}
		if (s.contains("min/avg/max/mdev")) {
			String timeStr = s.split("min/avg/max/mdev")[1].split("/")[1];
			avgTime = Float.parseFloat(timeStr);
		} else {
			avgTime = Float.MAX_VALUE;
		}

		return (int)avgTime;
	}

}
