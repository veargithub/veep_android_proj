package com.example.ant_test.crash_handler;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/3/4.
 */
public class VCrashHandler implements Thread.UncaughtExceptionHandler{
    private static VCrashHandler ourInstance = new VCrashHandler();

    public static VCrashHandler getInstance() {
        return ourInstance;
    }

    private VCrashHandler() {
    }

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public void init(Context context) {
        Log.d("VCrashHandler", "VCrashHandler init");
        this.mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
        }
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        final String msg = ex.getLocalizedMessage();
        //if(ServerIPManager.isShowLogIP()) saveExceptionToSdcard(ex);

        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "加载失败,请重试", Toast.LENGTH_LONG)
                        .show();
                // showErrorDialog();

                // Error_msg = Error_msg + msg + "\n";
                ex.printStackTrace();

                Looper.loop();
            }

        }.start();
        // Sleep一会后结束程序
        try {

            Thread.sleep(3000);

        } catch (InterruptedException e) {
//			Log.e(TAG, "Error : ", e);
        }

        // 收集设备信息
        //collectCrashDeviceInfo(mContext);
        // 保存错误报告文件
        //String crashFileName = saveCrashInfoToFile(ex);
        // 发送错误报告到服务器
        // sendCrashReportsToServer(mContext);
        //killProcess();
        android.os.Process.killProcess(android.os.Process.myPid());
        return true;
    }
}
