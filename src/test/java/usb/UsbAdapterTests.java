package usb;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.usb.InputDevice;
import com.github.frunoyman.adapters.usb.UsbAdapter;
import com.github.frunoyman.adapters.usb.UsbDevice;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UsbAdapterTests {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
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
    }

    @Test
    public void getUsbDeviceTest() throws Exception {
        UsbAdapter usbAdapter = DDMLibRemoteSDK.getUsbAdapter();
        for (UsbDevice device: usbAdapter.getDeviceList()){
            System.out.println(device);
        }
    }

    @Test
    public void getInputDeviceTest() throws Exception {
        UsbAdapter usbAdapter = DDMLibRemoteSDK.getUsbAdapter();
        for (InputDevice device: usbAdapter.getInputDeviceList()){
            System.out.println(device);
        }
    }
}
