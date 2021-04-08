package com.github.frunoyman.controllers;

import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.adapters.environment.Environment;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.adapters.telephony.Telecom;
import com.github.frunoyman.adapters.wifi.Wifi;
import com.github.frunoyman.shell.DDMLibShell;
import com.github.frunoyman.shell.Shell;

public class DDMLibRemoteSdk extends BaseSdk {
    private IDevice device;
    private Shell shell;

    public DDMLibRemoteSdk(IDevice device) {
        this.device = device;
        this.shell = new DDMLibShell(device);
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
    public Shell getShell() {
        return shell;
    }
}
