package telecom;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.telephony.CallHistory;
import com.github.frunoyman.adapters.telephony.Contact;
import com.github.frunoyman.adapters.telephony.TelecomAdapter;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TelephonyDDMLIBTests {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private TelecomAdapter telecomAdapter;

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
        telecomAdapter = DDMLibRemoteSDK.getTelecomAdapter();
    }

    @Test
    public void telephonyCallHistoryTests() throws Exception {
        for (CallHistory callHistory : telecomAdapter.getCallHistory()) {
            System.out.println(callHistory);
        }
    }

    @Test
    public void telephonyContactsTests() throws Exception {
        for (Contact contact : telecomAdapter.getContacts()) {
            System.out.println(contact);
        }
    }

    @Test
    public void getMobilePhone() throws Exception {
        Assert.assertFalse(telecomAdapter.getMobilePhone().isEmpty());
    }

    @Test
    public void sendUssdTest() throws Exception {
//        telecomAdapter.sendSMS("+380672244346","test test");
//        while (!telecomAdapter.isSMSReceived()){
//            Thread.sleep(1000);
//        }
//        System.out.println(telecomAdapter.getLastSMSNumber());
//        System.out.println(telecomAdapter.getLastSMSText());

        String result = telecomAdapter.sendUssdRequest("*161#");
        System.out.println(result);
        telecomAdapter.getNetworkOperatorName();
        telecomAdapter.getDataState();
        telecomAdapter.getDataNetworkType();
        telecomAdapter.getPhoneType();
        telecomAdapter.getSimState();
        Assert.assertTrue(telecomAdapter.getCallState() == TelecomAdapter.CallState.NONE);
        telecomAdapter.call("466");
        Thread.sleep(10000);
        Assert.assertTrue(telecomAdapter.getCallState() == TelecomAdapter.CallState.IN_CALL);
        Thread.sleep(3000);
        telecomAdapter.endCall();
        Thread.sleep(3000);
        Assert.assertTrue(telecomAdapter.getCallState() == TelecomAdapter.CallState.NONE);

        Assert.assertTrue(telecomAdapter.getContactsSize()>0);
        Assert.assertTrue(telecomAdapter.getCallHistorySize()>0);

    }
}
