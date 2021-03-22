package com.github.frunoyman.waiter;


import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.controllers.BaseSdk;

public class RemoteExpectedConditions {

    public static RemoteExpectedCondition<Boolean> bluetoothState(Bluetooth.State state) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getBluetooth().getState() == state;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth adapter state ('%s')", state);
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> bluetoothName(String name) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getBluetooth().getName().equals(name);
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth adapter name ('%s')", name);
            }
        };
    }

}
