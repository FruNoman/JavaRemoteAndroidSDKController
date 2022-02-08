package com.github.frunoyman.controllers;

import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.environment.EnvironmentAdapter;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.adapters.location.LocationAdapter;
import com.github.frunoyman.adapters.player.PlayerAdapter;
import com.github.frunoyman.adapters.telephony.TelecomAdapter;
import com.github.frunoyman.adapters.usb.Usb;
import com.github.frunoyman.adapters.wifi.WifiAdapter;
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

    public BluetoothAdapter getBluetoothAdapter() {
        return new BluetoothAdapter(shell);
    }

    @Override
    public WifiAdapter getWifiAdapter() {
        return new WifiAdapter(shell);
    }

    @Override
    public TelecomAdapter getTelecomAdapter() {
        return new TelecomAdapter(shell);
    }

    @Override
    public RemoteFile getRemoteFile(String path) {
        return new RemoteFile(shell, path);
    }

    @Override
    public EnvironmentAdapter getEnvironmentAdapter() {
        return new EnvironmentAdapter(shell);
    }

    @Override
    public Usb getUsbAdapter() {
        return new Usb(shell);
    }

    @Override
    public LocationAdapter getLocationAdapter() {
        return new LocationAdapter(shell);
    }

    @Override
    public PlayerAdapter getPlayerAdapter() {
        return new PlayerAdapter(shell);
    }

    @Override
    public Shell getShell() {
        return shell;
    }
}
