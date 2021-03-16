package com.github.frunoyman.controllers;

import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.Bluetooth;

public class RemoteSDK {
    private IDevice device;

    public RemoteSDK(IDevice device) {
        this.device = device;
    }

    public Bluetooth getBluetooth(){
        return new Bluetooth();
    }
}
