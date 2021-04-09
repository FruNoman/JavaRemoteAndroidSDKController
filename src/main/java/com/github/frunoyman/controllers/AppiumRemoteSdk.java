package com.github.frunoyman.controllers;

import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.adapters.environment.Environment;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.adapters.telephony.Telecom;
import com.github.frunoyman.adapters.usb.Usb;
import com.github.frunoyman.adapters.wifi.Wifi;
import com.github.frunoyman.shell.AppiumShell;
import com.github.frunoyman.shell.Shell;
import io.appium.java_client.android.AndroidDriver;

public class AppiumRemoteSdk extends BaseSdk {
    private AndroidDriver driver;
    private Shell shell;

    public AppiumRemoteSdk(AndroidDriver driver) {
        this.driver = driver;
        this.shell = new AppiumShell(driver);
    }

    public Bluetooth getBluetooth() {
        return new Bluetooth(shell);
    }

    @Override
    public Wifi getWifi() {
        return new Wifi(shell);
    }

    @Override
    public Telecom getTelecom() {
        return new Telecom(shell);
    }

    @Override
    public RemoteFile getRemoteFile(String path) {
        return new RemoteFile(shell, path);
    }

    @Override
    public Environment getEnvironment() {
        return new Environment(shell);
    }

    @Override
    public Usb getUsb() {
        return new Usb(shell);
    }

    @Override
    public Shell getShell() {
        return shell;
    }
}
