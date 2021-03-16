package com.github.frunoyman.controllers;

import com.github.frunoyman.adapters.Bluetooth;
import io.appium.java_client.android.AndroidDriver;

public class AppiumRemoteSdk {
    private AndroidDriver driver;

    public AppiumRemoteSdk(AndroidDriver driver) {
        this.driver = driver;
    }

    public Bluetooth getBluetooth(){
        return new Bluetooth();
    }
}
