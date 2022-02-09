package com.github.frunoyman.controllers;

import com.github.frunoyman.adapters.audio.AudioManager;
import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.environment.EnvironmentAdapter;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.adapters.location.LocationAdapter;
import com.github.frunoyman.adapters.player.PlayerAdapter;
import com.github.frunoyman.adapters.telephony.TelecomAdapter;
import com.github.frunoyman.adapters.usb.UsbAdapter;
import com.github.frunoyman.adapters.wifi.WifiAdapter;
import com.github.frunoyman.shell.Shell;

public abstract class BaseSdk {

    public abstract BluetoothAdapter getBluetoothAdapter();

    public abstract WifiAdapter getWifiAdapter();

    public abstract TelecomAdapter getTelecomAdapter();

    public abstract RemoteFile getRemoteFile(String path);

    public abstract EnvironmentAdapter getEnvironmentAdapter();

    public abstract UsbAdapter getUsbAdapter();

    public abstract LocationAdapter getLocationAdapter();

    public abstract PlayerAdapter getPlayerAdapter();

    public abstract AudioManager getAudioManager();

    public abstract Shell getShell();
}
