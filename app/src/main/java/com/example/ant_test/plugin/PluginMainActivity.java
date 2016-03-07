package com.example.ant_test.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.ant_test.plugin.impl.IPlugin;

import dalvik.system.DexClassLoader;

/**
 * Created by chx7078 on 2015/9/2.
 * 这是一个插件的小例子，调用sdcard中的某个jar的某个方法
 * 1.编译class文件，D:\workspace\VeepEmPro\app\src\main\java\com\example\ant_test\plugin>javac impl\
     IPlugin.java PluginImpl.java 这样会在当前目录下生成PluginImpl.class
   2.打成jar包 D:\workspace\VeepEmPro\app\src\main\java\com\example\ant_test\plugin>jar -cvf plugin.jar PluginImpl.class
     已添加清单
     正在添加: PluginImpl.class(输入 = 411) (输出 = 273)(压缩了 33%)
     打包的时候不能把IPlugin.class打进去，否则会出现重复定义的错误。
 */
public class PluginMainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useDexClassLoader();
    }
    public void useDexClassLoader() {
        DexClassLoader cDexClassLoader =
                new DexClassLoader("/mnt/sdcard/myplugin2.jar", "/data/data/com.example.ant_test", null, this.getClass()
                        .getClassLoader());
        try {
            Class<?> class1 = cDexClassLoader.loadClass("com.example.ant_test.plugin.PluginImpl");
            IPlugin interfacePlug = (IPlugin) class1.newInstance();
            int ret = interfacePlug.function_02(12, 13);
            Log.d(">>>>", "ret:" + ret);
        } catch (Exception e) {
        }
    }
}
