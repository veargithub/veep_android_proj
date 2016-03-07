package com.example.ant_test.slient_install;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by chx7078 on 2015/9/1.
 * 这个例子演示了如果从commend line安装一个apk，但是失败了，因为install package 这个权限需要app在/system/app下
 */
public class SilentInstallActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //execShellStr("ls -al /mnt/sdcard/");
        execShellStr("pm install -r /sdcard/zoomrefresh.apk");
    }

    public static String execShellStr(String cmd) {
        String[] cmdStrings = new String[] {"sh", "-c", cmd};
        String retString = "";
        try {
            Process process = Runtime.getRuntime().exec(cmdStrings);
            BufferedReader stdout =
                    new BufferedReader(new InputStreamReader(
                            process.getInputStream()), 1024);
            BufferedReader stderr =
                    new BufferedReader(new InputStreamReader(
                            process.getErrorStream()), 1024);

            String line = null;

            while ((null != (line = stdout.readLine())) || (null != (line = stderr.readLine()))) {
                if (!line.equals("")) {
                    retString += line + "\n";
                }
            }
            Log.d(">>>>", retString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return retString;
    }
}
