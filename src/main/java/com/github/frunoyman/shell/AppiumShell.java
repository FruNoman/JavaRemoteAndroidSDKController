package com.github.frunoyman.shell;

import io.appium.java_client.android.AndroidDriver;

public class AppiumShell extends Shell{
    private AndroidDriver driver;

    public AppiumShell(AndroidDriver driver) {
        this.driver = driver;
    }

    @Override
    public String execute(String... command) throws Exception {
        return null;
    }
}
