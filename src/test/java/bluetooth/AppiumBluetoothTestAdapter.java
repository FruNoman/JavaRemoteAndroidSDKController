package bluetooth;

import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.bluetooth.BluetoothDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothProfile;
import com.github.frunoyman.controllers.AppiumRemoteSdk;
import com.github.frunoyman.waiter.BluetoothExpectedConditions;
import com.github.frunoyman.waiter.RemoteWaiter;
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
    private RemoteWaiter waiter;

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
        waiter = new RemoteWaiter(remoteSdk, 15);
    }

    @Test
    public void bluetoothEnableDisable() {
        bluetoothAdapter.enable();

        waiter.until(
                BluetoothExpectedConditions.state(BluetoothAdapter.State.STATE_ON)
        );


        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_ON
        );

        Assert.assertTrue(
                bluetoothAdapter.isEnabled()
        );

        bluetoothAdapter.disable();

        waiter.until(
                BluetoothExpectedConditions.state(BluetoothAdapter.State.STATE_OFF)
        );

        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_OFF
        );

        Assert.assertFalse(
                bluetoothAdapter.isEnabled()
        );
    }

    @Test
    public void bluetoothDiscoverable() {
        bluetoothAdapter.enable();

        waiter.until(
                BluetoothExpectedConditions.state(BluetoothAdapter.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_ON
        );

        bluetoothAdapter.startDiscoverable(120);
        waiter.until(
                BluetoothExpectedConditions.discoverable()
        );

        Assert.assertTrue(
                bluetoothAdapter.isDiscoverable()
        );

        Assert.assertEquals(
                bluetoothAdapter.getScanMode(),
                BluetoothAdapter.ScanMode.SCAN_MODE_CONNECTABLE_DISCOVERABLE
        );

        bluetoothAdapter.stopDiscoverable();

        waiter.until(
                BluetoothExpectedConditions.stopDiscoverable()
        );

        Assert.assertFalse(
                bluetoothAdapter.isDiscoverable()
        );

        Assert.assertEquals(
                bluetoothAdapter.getScanMode(),
                BluetoothAdapter.ScanMode.SCAN_MODE_NONE
        );

    }

    @Test
    public void bluetoothChangeNameTest() {
        bluetoothAdapter.enable();

        waiter.until(
                BluetoothExpectedConditions.state(BluetoothAdapter.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_ON
        );

        String prevName = bluetoothAdapter.getName();

        bluetoothAdapter.setName("TestDevice mama");

        waiter.until(
                BluetoothExpectedConditions.name("TestDevice mama")
        );

        Assert.assertEquals(
                bluetoothAdapter.getName(),
                "TestDevice mama"
        );

        bluetoothAdapter.setName(prevName);

        waiter.until(
                BluetoothExpectedConditions.name(prevName)
        );

        Assert.assertEquals(
                bluetoothAdapter.getName(),
                prevName
        );
    }

    @Test
    public void discoveryBluetoothDevices() {
        bluetoothAdapter.enable();

        waiter.until(
                BluetoothExpectedConditions.state(BluetoothAdapter.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetoothAdapter.getState(),
                BluetoothAdapter.State.STATE_ON
        );

        bluetoothAdapter.startDiscovery();

        waiter.until(
                BluetoothExpectedConditions.discovering()
        );

        try {
            Thread.sleep(16000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        bluetoothAdapter.cancelDiscovery();

        waiter.until(
                BluetoothExpectedConditions.stopDiscovering()
        );


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
    public void bluetoothProfileTest() {
        bluetoothAdapter.enable();

        waiter.until(
                BluetoothExpectedConditions.state(BluetoothAdapter.State.STATE_ON)
        );

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
