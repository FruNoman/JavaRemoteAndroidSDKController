package com.github.frunoyman.controllers;

import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.adapters.environment.Environment;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.adapters.telephony.Telecom;
import com.github.frunoyman.adapters.usb.Usb;
import com.github.frunoyman.adapters.wifi.Wifi;
import com.github.frunoyman.shell.Shell;

public abstract class BaseSdk {

    public abstract Bluetooth getBluetooth();

    public abstract Wifi getWifi();

    public abstract Telecom getTelecom();

    public abstract RemoteFile getRemoteFile(String path);

    public abstract Environment getEnvironment();

    public abstract Usb getUsb();

    public abstract Shell getShell();
}
