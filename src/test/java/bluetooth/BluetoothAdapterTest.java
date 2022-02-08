package bluetooth;

import com.android.ddmlib.*;
import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.bluetooth.BluetoothDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothProfile;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BluetoothAdapterTest {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private BluetoothAdapter bluetoothAdapter;

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
        bluetoothAdapter = DDMLibRemoteSDK.getBluetoothAdapter();
    }


    @Test
    public void bluetoothEnableDisable() throws InterruptedException {
        bluetoothAdapter.enable();

        Thread.sleep(2000);


        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_ON
        );

        Assert.assertTrue(
                bluetoothAdapter.isEnabled()
        );

        bluetoothAdapter.disable();

        Thread.sleep(2000);

        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_OFF
        );

        Assert.assertFalse(
                bluetoothAdapter.isEnabled()
        );
    }

    @Test
    public void bluetoothDiscoverable() throws InterruptedException {
        bluetoothAdapter.enable();

        Thread.sleep(2000);

        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_ON
        );

        bluetoothAdapter.startDiscoverable(120);
        Thread.sleep(2000);

        Assert.assertTrue(
                bluetoothAdapter.isDiscoverable()
        );

        Assert.assertEquals(
                bluetoothAdapter.getScanMode(),
                BluetoothAdapter.ScanMode.SCAN_MODE_CONNECTABLE_DISCOVERABLE
        );

        bluetoothAdapter.stopDiscoverable();

        Thread.sleep(2000);

        Assert.assertFalse(
                bluetoothAdapter.isDiscoverable()
        );

        Assert.assertEquals(
                bluetoothAdapter.getScanMode(),
                BluetoothAdapter.ScanMode.SCAN_MODE_NONE
        );

    }

    @Test
    public void bluetoothChangeNameTest() throws InterruptedException {
        bluetoothAdapter.enable();

        Thread.sleep(2000);

        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_ON
        );

        String prevName = bluetoothAdapter.getName();

        bluetoothAdapter.setName("TestDevice mama");

        Thread.sleep(2000);

        Assert.assertEquals(
                bluetoothAdapter.getName(),
                "TestDevice mama"
        );

        bluetoothAdapter.setName(prevName);

        Thread.sleep(2000);

        Assert.assertEquals(
                bluetoothAdapter.getName(),
                prevName
        );
    }

    @Test
    public void discoveryBluetoothDevices() throws InterruptedException {
        bluetoothAdapter.enable();

        Thread.sleep(2000);

        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_ON
        );

        bluetoothAdapter.startDiscovery();

        Thread.sleep(2000);

        try {
            Thread.sleep(16000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        bluetoothAdapter.cancelDiscovery();

        Thread.sleep(2000);


        List<BluetoothDevice> devices = bluetoothAdapter.getDiscoveredBluetoothDevices();

        for (BluetoothDevice device:devices){
            device.getPairState();
            device.getName();
            device.getBluetoothClass();
            device.getType();
            System.out.println("-----------------------");
        }
    }

    @Test
    public void bluetoothProfileTest() throws InterruptedException {
        bluetoothAdapter.enable();

        Thread.sleep(2000);

        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_ON
        );

        for (BluetoothProfile.Type type : bluetoothAdapter.getConnectedProfiles()){
            System.out.println(type);
        }
    }
}
