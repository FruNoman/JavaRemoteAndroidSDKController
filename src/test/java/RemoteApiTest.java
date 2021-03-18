import com.android.ddmlib.*;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.controllers.RemoteSdk;
import com.github.frunoyman.shell.DDMLIBShell;
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
    public void starting() {
        RemoteSdk remoteSDK = new RemoteSdk(device);
        Bluetooth bluetooth = remoteSDK.getBluetooth();
        bluetooth.enable();
        Assert.assertEquals(bluetooth.getState(), 1);
        bluetooth.disable();
        Assert.assertEquals(bluetooth.getState(), 0);
    }

    @Test
    public void shellTest() throws Exception {
        DDMLIBShell shell = new DDMLIBShell(device);
        String output = shell.execute("am broadcast -a com.github.remotesdk.BLUETOOTH_REMOTE  --es bluetooth_remote getState");
        System.out.println(output);
    }

    @Test
    public void testExceptions() {
        try {
            throw new ArithmeticException();
        } catch (Exception e) {
            System.out.println(e.getClass());
        }
    }
}
