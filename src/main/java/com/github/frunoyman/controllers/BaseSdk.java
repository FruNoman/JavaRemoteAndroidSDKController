package com.github.frunoyman.controllers;

import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.adapters.telephony.Telecom;
import com.github.frunoyman.adapters.wifi.Wifi;

public abstract class BaseSdk {

    public abstract Bluetooth getBluetooth();

    public abstract Wifi getWifi();

    public abstract Telecom getTelecom();
}
