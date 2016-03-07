package com.example.ant_test.plugin;

import com.example.ant_test.plugin.impl.IPlugin;

/**
 * Created by chx7078 on 2015/9/2.
 */
public class PluginImpl implements IPlugin{
    @Override
    public String function_01() {
        return null;
    }

    @Override
    public int function_02(int a, int b) {
        return a + b;
    }
}
