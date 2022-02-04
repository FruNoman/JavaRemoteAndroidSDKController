package com.github.frunoyman.controllers;

import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.environment.EnvironmentAdapter;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.adapters.location.LocationAdapter;
import com.github.frunoyman.adapters.player.PlayerAdapter;
import com.github.frunoyman.adapters.telephony.TelecomAdapter;
import com.github.frunoyman.adapters.usb.Usb;
import com.github.frunoyman.adapters.wifi.Wifi;
import com.github.frunoyman.shell.Shell;

public abstract class BaseSdk {

    public abstract BluetoothAdapter getBluetoothAdapter();

    public abstract Wifi getWifiAdapter();

    public abstract TelecomAdapter getTelecomAdapter();

    public abstract RemoteFile getRemoteFile(String path);

    public abstract EnvironmentAdapter getEnvironmentAdapter();

    public abstract Usb getUsbAdapter();

    public abstract LocationAdapter getLocationAdapter();

    public abstract PlayerAdapter getPlayerAdapter();

    public abstract Shell getShell();
}
