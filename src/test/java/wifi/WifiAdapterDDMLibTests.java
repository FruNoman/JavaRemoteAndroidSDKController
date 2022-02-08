package wifi;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.wifi.ScanResult;
import com.github.frunoyman.adapters.wifi.WifiAdapter;
import com.github.frunoyman.adapters.wifi.WifiConfiguration;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WifiAdapterDDMLibTests {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private WifiAdapter wifiAdapter;

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
        wifiAdapter = DDMLibRemoteSDK.getWifiAdapter();
    }

    @Test
    public void wifiEnableDisable() throws InterruptedException {
        wifiAdapter.enable();

        Thread.sleep(2000);


        wifiAdapter.disable();

        Thread.sleep(2000);

    }

    @Test
    public void addNetworkTest() throws InterruptedException {
        wifiAdapter.enable();

        Thread.sleep(2000);
        int netId = wifiAdapter.addNetwork("RNS_AES", "12345678", WifiConfiguration.SecurityType.PASS);

        Thread.sleep(2000);
        wifiAdapter.enableNetwork(netId);

        Thread.sleep(2000);

        Thread.sleep(5000);

        for (WifiConfiguration configuration : wifiAdapter.getConfiguredNetworks()) {
            System.out.println(configuration);
        }


        wifiAdapter.disableNetwork(netId);

//        waiter.until(WifiExpectedConditions.networkStatus("RNS_AES", WifiConfiguration.Status.DISABLED));


        for (WifiConfiguration configuration : wifiAdapter.getConfiguredNetworks()) {
            System.out.println(configuration);
        }

        wifiAdapter.removeNetwork(netId);

        wifiAdapter.removeAllNetworks();

        wifiAdapter.disable();

        Thread.sleep(2000);    }

    @Test
    public void getApConfig() throws Exception {
        wifiAdapter.startHotspotTethering();

        Thread.sleep(2000);

        System.out.println(wifiAdapter.getWifiHotspotConfiguration());

        wifiAdapter.stopHotspotTethering();

        Thread.sleep(2000);
    }

    @Test
    public void setHotSpotConfig() throws Exception {
        WifiConfiguration pass = new WifiConfiguration("Mama", "sukaSomeBliat", WifiConfiguration.SecurityType.PASS);

        wifiAdapter.setWifiHotspotConfiguration(pass);

        Thread.sleep(4000);

        Assert.assertEquals(pass, wifiAdapter.getWifiHotspotConfiguration());

        wifiAdapter.startHotspotTethering();

        Thread.sleep(2000);

        System.out.println(wifiAdapter.getWifiHotspotConfiguration());

        Thread.sleep(4000);

        wifiAdapter.stopHotspotTethering();

        Thread.sleep(2000);

        System.out.println(wifiAdapter.getWifiHotspotConfiguration());


        WifiConfiguration open = new WifiConfiguration("KAKA", "", WifiConfiguration.SecurityType.OPEN);

        wifiAdapter.setWifiHotspotConfiguration(open);

        Thread.sleep(4000);

        Assert.assertEquals(open, wifiAdapter.getWifiHotspotConfiguration());

        System.out.println(wifiAdapter.getWifiHotspotConfiguration());

    }

    @Test
    public void scanResult() throws Exception {
        wifiAdapter.enable();

        Thread.sleep(2000);
        wifiAdapter.isScanAlwaysAvailable();

        wifiAdapter.startScan();

        for(ScanResult scanResult: wifiAdapter.getScanResults()){
            System.out.println(scanResult);
        }
    }
}
