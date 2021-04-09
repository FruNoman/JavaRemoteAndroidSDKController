package usb;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.usb.InputDevice;
import com.github.frunoyman.adapters.usb.Usb;
import com.github.frunoyman.adapters.usb.UsbDevice;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import com.github.frunoyman.waiter.RemoteWaiter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UsbTests {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private RemoteWaiter waiter;
    private DDMLibRemoteSdk DDMLibRemoteSDK;

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
        DDMLibRemoteSDK = new DDMLibRemoteSdk(device);
        waiter = new RemoteWaiter(DDMLibRemoteSDK, 15);
    }

    @Test
    public void getUsbDeviceTest() throws Exception {
        Usb usb = DDMLibRemoteSDK.getUsb();
        for (UsbDevice device:usb.getDeviceList()){
            System.out.println(device);
        }
    }

    @Test
    public void getInputDeviceTest() throws Exception {
        Usb usb = DDMLibRemoteSDK.getUsb();
        for (InputDevice device:usb.getInputDeviceList()){
            System.out.println(device);
        }
    }
}
