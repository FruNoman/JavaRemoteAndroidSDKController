package com.github.frunoyman.controllers;

import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.shell.DDMLIBShell;
import com.github.frunoyman.shell.Shell;

public class RemoteSdk extends BaseSdk{
    private IDevice device;
    private Shell shell;

    public RemoteSdk(IDevice device) {
        this.device = device;
        this.shell = new DDMLIBShell(device);
    }

    public Bluetooth getBluetooth(){
        return new Bluetooth(shell);
    }
}
