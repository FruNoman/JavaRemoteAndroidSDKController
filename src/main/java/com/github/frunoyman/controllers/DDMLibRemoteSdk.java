package com.github.frunoyman.controllers;

import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.audio.AudioManager;
import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.environment.EnvironmentAdapter;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.adapters.location.LocationAdapter;
import com.github.frunoyman.adapters.player.PlayerAdapter;
import com.github.frunoyman.adapters.telephony.TelecomAdapter;
import com.github.frunoyman.adapters.usb.UsbAdapter;
import com.github.frunoyman.adapters.wifi.WifiAdapter;
import com.github.frunoyman.shell.DDMLibShell;
import com.github.frunoyman.shell.Shell;

public class DDMLibRemoteSdk extends BaseSdk {
    private IDevice device;
    private Shell shell;

    public DDMLibRemoteSdk(IDevice device) {
        this.device = device;
        this.shell = new DDMLibShell(device);
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
    public UsbAdapter getUsbAdapter() {
        return new UsbAdapter(shell);
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
    public AudioManager getAudioManager() {
        return new AudioManager(shell);
    }

    @Override
    public Shell getShell() {
        return shell;
    }
}
