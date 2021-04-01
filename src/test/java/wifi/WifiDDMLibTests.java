package wifi;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.wifi.Wifi;
import com.github.frunoyman.adapters.wifi.WifiConfiguration;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import com.github.frunoyman.waiter.RemoteWaiter;
import com.github.frunoyman.waiter.WifiExpectedConditions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WifiDDMLibTests {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private Wifi wifi;
    private RemoteWaiter waiter;

    @Before
    public void beforeTest() throws InterruptedException {
        AndroidDebugBridge.initIfNeeded(true);
        AndroidDebugBridge.addDeviceChangeListener(new AndroidDebugBridge.IDeviceChangeListener() {

            public void deviceConnected(IDevice iDevice) {
                synchronized (monitor) {
                    System.out.println("device connected " + iDevice.getSerialNumber());
                    devices.add(iDevice);
                    monitor.notify();
                }
            }

            public void deviceDisconnected(IDevice iDevice) {
                System.out.println("device disconnected " + iDevice.getSerialNumber());
                devices.remove(iDevice);
            }

            public void deviceChanged(IDevice iDevice, int i) {
            }
        });

        AndroidDebugBridge adb = AndroidDebugBridge.createBridge("adb", false);
        adb.getDevices();
        synchronized (monitor) {
            monitor.wait(15000);
        }

        device = devices.get(0);
        DDMLibRemoteSdk DDMLibRemoteSDK = new DDMLibRemoteSdk(device);
        wifi = DDMLibRemoteSDK.getWifi();
        waiter = new RemoteWaiter(DDMLibRemoteSDK, 15);
    }

    @Test
    public void wifiEnableDisable() throws Exception {
        wifi.enable();

        waiter.until(WifiExpectedConditions.enabled());

        waiter.until(
                WifiExpectedConditions.state(Wifi.State.WIFI_STATE_ENABLED
                )
        );

        wifi.disable();

        waiter.until(WifiExpectedConditions.disabled());

        waiter.until(
                WifiExpectedConditions.state(
                        Wifi.State.WIFI_STATE_DISABLED
                )
        );
    }

    @Test
    public void addNetworkTest() throws Exception {
        wifi.enable();

        waiter.until(WifiExpectedConditions.enabled());

        int netId = wifi.addNetwork("RNS_AES", "12345678", WifiConfiguration.SecurityType.PASS);

        waiter.until(WifiExpectedConditions.savedNetwork("RNS_AES"));

        wifi.enableNetwork(netId);

        waiter.until(WifiExpectedConditions.networkStatus("RNS_AES", WifiConfiguration.Status.ENABLED));


        Thread.sleep(5000);

        for (WifiConfiguration configuration : wifi.getConfiguredNetworks()) {
            System.out.println(configuration);
        }


        wifi.disableNetwork(netId);

        waiter.until(WifiExpectedConditions.networkStatus("RNS_AES", WifiConfiguration.Status.DISABLED));


        for (WifiConfiguration configuration : wifi.getConfiguredNetworks()) {
            System.out.println(configuration);
        }

        wifi.removeNetwork(netId);

        wifi.removeAllNetworks();

        wifi.disable();

        waiter.until(WifiExpectedConditions.disabled());
    }

    @Test
    public void getApConfig() throws Exception {
        wifi.startHotspotTethering();

        waiter.until(WifiExpectedConditions.hotspotEnabled());

        waiter.until(WifiExpectedConditions.hotspotState(Wifi.State.WIFI_AP_STATE_ENABLED));

        System.out.println(wifi.getWifiHotspotConfiguration());

        wifi.stopHotspotTethering();

        waiter.until(WifiExpectedConditions.hotspotDisabled());

        waiter.until(WifiExpectedConditions.hotspotState(Wifi.State.WIFI_AP_STATE_DISABLED));
    }

    @Test
    public void setHotSpotConfig() throws Exception {
        WifiConfiguration pass = new WifiConfiguration("Mama", "sukaSomeBliat", WifiConfiguration.SecurityType.PASS);

        wifi.setWifiHotspotConfiguration(pass);

        Thread.sleep(4000);

        Assert.assertEquals(pass, wifi.getWifiHotspotConfiguration());

        wifi.startHotspotTethering();

        waiter.until(WifiExpectedConditions.hotspotEnabled());

        waiter.until(WifiExpectedConditions.hotspotState(Wifi.State.WIFI_AP_STATE_ENABLED));

        System.out.println(wifi.getWifiHotspotConfiguration());

        Thread.sleep(4000);

        wifi.stopHotspotTethering();

        waiter.until(WifiExpectedConditions.hotspotDisabled());

        waiter.until(WifiExpectedConditions.hotspotState(Wifi.State.WIFI_AP_STATE_DISABLED));

        System.out.println(wifi.getWifiHotspotConfiguration());


        WifiConfiguration open = new WifiConfiguration("KAKA", "", WifiConfiguration.SecurityType.OPEN);

        wifi.setWifiHotspotConfiguration(open);

        Thread.sleep(4000);

        Assert.assertEquals(open, wifi.getWifiHotspotConfiguration());

        System.out.println(wifi.getWifiHotspotConfiguration());

    }

    @Test
    public void scanResult() throws Exception {
        wifi.enable();

        waiter.until(WifiExpectedConditions.enabled());

//        wifi.startScan();

        wifi.getScanResults();
    }
}
