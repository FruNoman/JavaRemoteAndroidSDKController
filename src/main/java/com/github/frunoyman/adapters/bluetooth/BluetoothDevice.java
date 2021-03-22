package com.github.frunoyman.adapters.bluetooth;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;

public class BluetoothDevice extends BaseAdapter {
    private String address;

    public BluetoothDevice(Shell shell,String address) {
        super(shell);
        this.address = address;
    }
}
