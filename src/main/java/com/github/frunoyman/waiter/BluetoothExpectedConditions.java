package com.github.frunoyman.waiter;


import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.adapters.bluetooth.BluetoothDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothProfile;
import com.github.frunoyman.controllers.BaseSdk;

public class BluetoothExpectedConditions {

    public static RemoteExpectedCondition<Boolean> enabled() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getBluetooth().isEnabled();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth enable");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> disabled() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return !remoteSdk.getBluetooth().isEnabled();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth disabled");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> state(Bluetooth.State state) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getBluetooth().getState() == state;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth state ('%s')", state);
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> discoverable() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getBluetooth().isDiscoverable();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth discoverable");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> stopDiscoverable() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return !remoteSdk.getBluetooth().isDiscoverable();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth stop discoverable");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> discovering() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getBluetooth().isDiscovering();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth discovering");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> stopDiscovering() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return !remoteSdk.getBluetooth().isDiscovering();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth stop discovering");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> discoveredDevice(String mac) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {
                    for(BluetoothDevice device:remoteSdk.getBluetooth().getDiscoveredBluetoothDevices()){
                        if (device.getAddress().equals(mac)){
                            return true;
                        }
                    }
                    return false;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth discovered device ('%s')",mac);
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> connectionState(Bluetooth.ConnectedState state) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getBluetooth().getConnectionState() == state;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth connection state ('%s')", state);
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> profileConnectionState(BluetoothProfile.Type type, BluetoothProfile.State state) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {
                    return remoteSdk.getBluetooth().getProfileConnectionState(type) == state;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth profile connection state ('%s')", state);
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> scanMode(Bluetooth.ScanMode scanMode) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getBluetooth().getScanMode() == scanMode;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Bluetooth scanMode ('%s')", scanMode);
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> name(String name) {
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
