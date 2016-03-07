package com.example.ant_test.plugin.core;

import android.content.Intent;

/**
 * Created by chx7078 on 2015/9/28.
 */
public class PluginIntent extends Intent{

    public String pluginPackageName, pluginClassName;

    public PluginIntent(String pluginPackageName, String pluginClassName) {
        this.pluginPackageName = pluginPackageName;
        this.pluginClassName = pluginClassName;
    }


}
