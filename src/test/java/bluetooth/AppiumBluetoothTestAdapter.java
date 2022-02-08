package bluetooth;

import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.bluetooth.BluetoothDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothProfile;
import com.github.frunoyman.controllers.AppiumRemoteSdk;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AppiumBluetoothTestAdapter {
    private AndroidDriver driver;
    private BluetoothAdapter bluetoothAdapter;

    @Before
    public void beforeAppiumTest() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 900);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, 8229);
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
        AppiumRemoteSdk remoteSdk = new AppiumRemoteSdk(driver);
        bluetoothAdapter = remoteSdk.getBluetoothAdapter();
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

        for (BluetoothDevice device : devices) {
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

        for (BluetoothProfile.Type type : bluetoothAdapter.getConnectedProfiles()) {
            System.out.println(type);
        }
    }


    @After
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
    }
}
