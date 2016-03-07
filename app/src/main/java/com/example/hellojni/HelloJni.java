package com.example.hellojni;

/**
 * Created by Administrator on 2015/2/17.
 */
public class HelloJni {

    public native String  stringFromJNI();


    static {
        System.loadLibrary("hello-jni");
    }
}
