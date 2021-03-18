package com.github.frunoyman.controllers;

import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.shell.DDMLibShell;
import com.github.frunoyman.shell.Shell;

public class DDMLibRemoteSdk extends BaseSdk{
    private IDevice device;
    private Shell shell;

    public DDMLibRemoteSdk(IDevice device) {
        this.device = device;
        this.shell = new DDMLibShell(device);
    }

    public Bluetooth getBluetooth(){
        return new Bluetooth(shell);
    }
}
