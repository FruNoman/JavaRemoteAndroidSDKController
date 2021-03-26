package com.github.frunoyman.waiter;

import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.adapters.wifi.Wifi;
import com.github.frunoyman.adapters.wifi.WifiConfiguration;
import com.github.frunoyman.controllers.BaseSdk;

public class WifiExpectedConditions {

    public static RemoteExpectedCondition<Boolean> enabled() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {
                    return remoteSdk.getWifi().isEnabled();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Wifi enable");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> disabled() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {
                    return !remoteSdk.getWifi().isEnabled();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Wifi disabled");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> hotspotEnabled() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {
                    return remoteSdk.getWifi().isWifiHotspotEnabled();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Wifi hotspot enabled");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> hotspotDisabled() {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {
                    return !remoteSdk.getWifi().isWifiHotspotEnabled();
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Wifi hotspot disabled");
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> state(Wifi.State state) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getWifi().getState() == state;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Wifi state ('%s')", state);
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> hotspotState(Wifi.State state) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {

                    return remoteSdk.getWifi().getWifiHotspotState() == state;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Wifi hotspot state ('%s')", state);
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> savedNetwork(String ssid) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {
                    boolean found = false;
                    for (WifiConfiguration configuration:remoteSdk.getWifi().getConfiguredNetworks()){
                        if (configuration.getSSID().equals(ssid)){
                            found =  true;
                            break;
                        }
                    }
                    return found;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Wifi saved network ('%s')", ssid);
            }
        };
    }

    public static RemoteExpectedCondition<Boolean> networkStatus(String ssid, WifiConfiguration.Status status) {
        return new RemoteExpectedCondition<Boolean>() {
            public Boolean apply(BaseSdk remoteSdk) {
                try {
                    boolean found = false;
                    for (WifiConfiguration configuration:remoteSdk.getWifi().getConfiguredNetworks()){
                        if (configuration.getSSID().equals(ssid)){
                            if (configuration.getStatus()==status){
                                found = true;
                                break;
                            }else {
                                break;
                            }
                        }
                    }
                    return found;
                } catch (Exception var3) {
                    return null;
                }
            }

            public String toString() {
                return String.format("Wifi saved network ('%s')", ssid);
            }
        };
    }


}
