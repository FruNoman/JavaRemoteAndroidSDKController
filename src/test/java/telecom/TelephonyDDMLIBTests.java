package telecom;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.telephony.CallHistory;
import com.github.frunoyman.adapters.telephony.Contact;
import com.github.frunoyman.adapters.telephony.Telecom;
import com.github.frunoyman.adapters.wifi.Wifi;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import com.github.frunoyman.waiter.RemoteWaiter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TelephonyDDMLIBTests {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private RemoteWaiter waiter;
    private Telecom telecom;

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
        waiter = new RemoteWaiter(DDMLibRemoteSDK, 15);
        telecom = DDMLibRemoteSDK.getTelecom();
    }

    @Test
    public void telephonyCallHistoryTests() throws Exception {
        for (CallHistory callHistory :telecom.getCallHistory()){
            System.out.println(callHistory);
        }
    }

    @Test
    public void telephonyContactsTests() throws Exception {
        for (Contact contact :telecom.getContacts()){
            System.out.println(contact);
        }
    }

    @Test
    public void getMobilePhone() throws Exception {
        Assert.assertFalse(telecom.getMobilePhone().isEmpty());
    }

    @Test
    public void sendUssdTest() throws Exception {
        String result = telecom.sendUssdRequest("*161#");
        System.out.println(result);
        telecom.getNetworkOperatorName();
        telecom.getDataState();
        telecom.getDataNetworkType();
        telecom.getPhoneType();
        telecom.getSimState();
        telecom.getCallState();
        telecom.call("466");
        Thread.sleep(2000);
        telecom.getCallState();
        Thread.sleep(3000);
        telecom.endCall();
    }
}
