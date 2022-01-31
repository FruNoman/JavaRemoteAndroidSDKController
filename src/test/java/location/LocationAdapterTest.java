package location;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.bluetooth.BluetoothDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothProfile;
import com.github.frunoyman.adapters.location.LocationAdapter;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import com.github.frunoyman.waiter.BluetoothExpectedConditions;
import com.github.frunoyman.waiter.RemoteWaiter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapterTest {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private LocationAdapter locationAdapter;
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
        locationAdapter = DDMLibRemoteSDK.getLocationAdapter();
        waiter = new RemoteWaiter(DDMLibRemoteSDK, 15);
    }


    @Test
    public void bluetoothEnableDisable(){
        locationAdapter.setLocationState(true);

        waiter.until(
                BluetoothExpectedConditions.state(BluetoothAdapter.State.STATE_ON)
        );


    }


}