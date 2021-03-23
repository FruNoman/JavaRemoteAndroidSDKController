import com.android.ddmlib.*;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.adapters.bluetooth.BluetoothDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothProfile;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import com.github.frunoyman.waiter.BluetoothExpectedConditions;
import com.github.frunoyman.waiter.RemoteWaiter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BluetoothTest {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private Bluetooth bluetooth;
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
        bluetooth = DDMLibRemoteSDK.getBluetooth();
        waiter = new RemoteWaiter(DDMLibRemoteSDK, 15);
    }


    @Test
    public void bluetoothEnableDisable() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        waiter.until(
                BluetoothExpectedConditions.enabled()
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        Assert.assertTrue(
                bluetooth.isEnabled()
        );

        bluetooth.disable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_OFF)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_OFF
        );

        Assert.assertFalse(
                bluetooth.isEnabled()
        );
    }

    @Test()
    public void bluetoothReboot() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        device.reboot("");

        Thread.sleep(50000);

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        bluetooth.disable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_OFF)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_OFF
        );
    }

    @Test
    public void bluetoothDiscoverable() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        bluetooth.startDiscoverable(200);

        waiter.until(
                BluetoothExpectedConditions.discoverable()
        );

        Assert.assertTrue(
                bluetooth.isDiscoverable()
        );

        Assert.assertEquals(
                bluetooth.getScanMode(),
                Bluetooth.ScanMode.SCAN_MODE_CONNECTABLE_DISCOVERABLE
        );

        bluetooth.cancelDiscoverable();

        Assert.assertFalse(
                bluetooth.isDiscoverable()
        );

        Assert.assertEquals(
                bluetooth.getScanMode(),
                Bluetooth.ScanMode.SCAN_MODE_NONE
        );

    }

    @Test
    public void bluetoothChangeNameTest() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        String prevName = bluetooth.getName();

        bluetooth.setName("TestDevice mama");

        waiter.until(
                BluetoothExpectedConditions.name("TestDevice mama")
        );

        Assert.assertEquals(
                bluetooth.getName(),
                "TestDevice mama"
        );

        bluetooth.setName(prevName);

        waiter.until(
                BluetoothExpectedConditions.name(prevName)
        );

        Assert.assertEquals(
                bluetooth.getName(),
                prevName
        );
    }

    @Test
    public void discoveryBluetoothDevices() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        bluetooth.startDiscovery();

        waiter.until(
                BluetoothExpectedConditions.discovering()
        );

        Thread.sleep(6000);

//        waiter.until(
//                BluetoothExpectedConditions.discoveredDevice("74:65:50:80:AC:97")
//        );

        bluetooth.cancelDiscovery();

        waiter.until(
                BluetoothExpectedConditions.stopDiscovering()
        );


        List<BluetoothDevice> devices = bluetooth.getDiscoveredBluetoothDevices();

        for (BluetoothDevice device:devices){
            System.out.println(device.getAddress());
        }
    }

    @Test
    public void bluetoothSupportProfilesTest() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        for (BluetoothProfile.Type type:bluetooth.getSupportedProfiles()){
            System.out.println(type);
        }
    }

    @Test
    public void bluetoothGetProfileConnectionStateTest() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        for (BluetoothProfile.Type type:bluetooth.getSupportedProfiles()){
            if (type!=null) {
                bluetooth.getProfileConnectionState(type);
            }
        }
    }


}
