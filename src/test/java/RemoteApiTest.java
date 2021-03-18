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
    public void starting() throws Exception {
        DDMLibRemoteSdk DDMLibRemoteSDK = new DDMLibRemoteSdk(device);
        Bluetooth bluetooth = DDMLibRemoteSDK.getBluetooth();
        bluetooth.enable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_ON);
        bluetooth.disable();
        Thread.sleep(2000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_OFF);
    }

    @Test
    public void shellTest() throws Exception {
        DDMLibShell shell = new DDMLibShell(device);
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
