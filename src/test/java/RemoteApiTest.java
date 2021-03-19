import com.android.ddmlib.*;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import com.github.frunoyman.shell.DDMLibShell;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RemoteApiTest {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;

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
    }



    @Test
    public void starting2() throws Exception {
        DDMLibRemoteSdk DDMLibRemoteSDK = new DDMLibRemoteSdk(device);
        Bluetooth bluetooth = DDMLibRemoteSDK.getBluetooth();
        bluetooth.enable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_ON);
        bluetooth.disable();
        Thread.sleep(2000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_OFF);
        System.out.println(bluetooth.getAddress());
    }

    @Test()
    public void starting1() throws Exception {
        DDMLibRemoteSdk DDMLibRemoteSDK = new DDMLibRemoteSdk(device);
        Bluetooth bluetooth = DDMLibRemoteSDK.getBluetooth();
        bluetooth.enable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_ON);
        bluetooth.disable();
        Thread.sleep(2000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_OFF);
        device.reboot("");
        Thread.sleep(50000);
        bluetooth.enable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_ON);
    }

    @Test()
    public void dualTest() throws Exception {
        DDMLibRemoteSdk DDMLibRemoteSDK = new DDMLibRemoteSdk(device);
        Bluetooth bluetooth = DDMLibRemoteSDK.getBluetooth();
        bluetooth.enable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_ON);
        System.out.println(bluetooth.getAddress());
        bluetooth.disable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_OFF);

        IDevice device2 = devices.get(1);
        DDMLibRemoteSdk ddmLibRemoteSdksec = new DDMLibRemoteSdk(device2);
        Bluetooth bluetoothSec = ddmLibRemoteSdksec.getBluetooth();
        bluetoothSec.enable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetoothSec.getState(), Bluetooth.State.STATE_ON);
        System.out.println(bluetoothSec.getAddress());
        bluetoothSec.disable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetoothSec.getState(), Bluetooth.State.STATE_OFF);


    }

    @Test
    public void discoverableTest() throws Exception {
        DDMLibRemoteSdk DDMLibRemoteSDK = new DDMLibRemoteSdk(device);
        Bluetooth bluetooth = DDMLibRemoteSDK.getBluetooth();
        bluetooth.discoverable(200);
        System.out.println(bluetooth.getAddress());
    }

}
