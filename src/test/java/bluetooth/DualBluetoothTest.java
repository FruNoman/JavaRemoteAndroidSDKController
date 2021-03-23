package bluetooth;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import com.github.frunoyman.waiter.RemoteWaiter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DualBluetoothTest {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private IDevice device2;

    private Bluetooth bluetooth;
    private RemoteWaiter waiter;
    private Bluetooth bluetooth2;
    private RemoteWaiter waiter2;

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
        device2 = devices.get(1);
        DDMLibRemoteSdk DDMLibRemoteSDK2 = new DDMLibRemoteSdk(device2);
        bluetooth = DDMLibRemoteSDK.getBluetooth();
        waiter = new RemoteWaiter(DDMLibRemoteSDK, 15);

        bluetooth2 = DDMLibRemoteSDK2.getBluetooth();
        waiter2 = new RemoteWaiter(DDMLibRemoteSDK2, 15);
    }

    @Test
    public void dualBluetoothTest() throws Exception {
        bluetooth.enable();
        bluetooth2.enable();
        System.out.println(bluetooth.getAddress());
        System.out.println(bluetooth2.getAddress());
        bluetooth.setPairingConfirmation(bluetooth2.getAddress(),true);
        bluetooth2.setPairingConfirmation(bluetooth.getAddress(),true);

    }
}
