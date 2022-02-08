package location;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.bluetooth.BluetoothDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothProfile;
import com.github.frunoyman.adapters.location.Location;
import com.github.frunoyman.adapters.location.LocationAdapter;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
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
    }


    @Test
    public void locationEnableDisable() throws InterruptedException {
        locationAdapter.setLocationState(true);

        Thread.sleep(3000);

        Assert.assertTrue(locationAdapter.isLocationEnabled());

        locationAdapter.setLocationState(false);

        Thread.sleep(3000);

        Assert.assertFalse(locationAdapter.isLocationEnabled());
    }

    @Test
    public void getLocationUpdatesTest() throws InterruptedException {
        locationAdapter.setLocationState(true);

        Thread.sleep(3000);

        Assert.assertTrue(locationAdapter.isLocationEnabled());


        locationAdapter.requestLocationUpdates(LocationAdapter.Provider.NETWORK_PROVIDER);

        Thread.sleep(60000);

        List<Location> locations = locationAdapter.getUpdatedLocations();

        System.out.println(locations);

        locationAdapter.removeUpdates();

        locationAdapter.setLocationState(false);

        Thread.sleep(2000);

        Assert.assertFalse(locationAdapter.isLocationEnabled());
    }

    @Test
    public void otherMethodsTest() throws InterruptedException {
        locationAdapter.setLocationState(true);

        Thread.sleep(2000);


        locationAdapter.requestLocationUpdates(LocationAdapter.Provider.NETWORK_PROVIDER);

        Thread.sleep(60000);

//        int satellites = locationAdapter.getSatelliteCount();

//        Assert.assertTrue(satellites > 0);

        Location location = locationAdapter.getLastKnownLocation(LocationAdapter.Provider.NETWORK_PROVIDER);

        Assert.assertTrue(location!=null);

        for (LocationAdapter.Provider provider: locationAdapter.getAllProviders()){
            System.out.println("Provider "+provider);
        }

//        for (int x=0; x<satellites;x++){
//            boolean inFix = locationAdapter.usedInFix(x);
//            LocationAdapter.ConstellationType type = locationAdapter.getConstellationType(x);
//            System.out.println("Satellite number "+ x+ " in fix "+inFix+" type "+type);
//        }

        locationAdapter.setLocationState(false);

    }


}
