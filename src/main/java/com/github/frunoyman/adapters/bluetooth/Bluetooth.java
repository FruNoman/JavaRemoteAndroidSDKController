package com.github.frunoyman.adapters.bluetooth;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;

import java.util.List;

public class Bluetooth extends BaseAdapter {
    public Bluetooth(Shell shell) {
        super(shell);
    }

    public void enable(){

    }

    public void disable(){

    }

    public int getState() {
        return 0;
    }

    public int getScanMode() {
        return 0;
    }

    public String getName(){
        return "";
    }

    public void setName(){

    }

    public String getAddress(){
        return "";
    }

    public void startDiscovery(){

    }

    public void cancelDiscovery(){

    }

    public void pair(String address){

    }

    public List<BluetoothDevice> getPairedDevices(){
        return null;
    }
}
