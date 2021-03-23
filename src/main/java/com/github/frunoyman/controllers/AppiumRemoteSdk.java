package com.github.frunoyman.controllers;

import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.adapters.wifi.Wifi;
import com.github.frunoyman.shell.AppiumShell;
import com.github.frunoyman.shell.Shell;
import io.appium.java_client.android.AndroidDriver;

public class AppiumRemoteSdk extends BaseSdk{
    private AndroidDriver driver;
    private Shell shell;

    public AppiumRemoteSdk(AndroidDriver driver) {
        this.driver = driver;
        this.shell = new AppiumShell(driver);
    }

    public Bluetooth getBluetooth(){
        return new Bluetooth(shell);
    }

    @Override
    public Wifi getWifi() {
        return new Wifi(shell);
    }
}
