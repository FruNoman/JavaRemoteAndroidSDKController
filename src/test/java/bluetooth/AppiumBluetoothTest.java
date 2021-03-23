package bluetooth;

import com.github.frunoyman.adapters.bluetooth.Bluetooth;
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

public class AppiumBluetoothTest {
    private AndroidDriver driver;
    private Bluetooth bluetooth;
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
        bluetooth = remoteSdk.getBluetooth();
        waiter = new RemoteWaiter(remoteSdk, 15);
    }

    @Test
    public void bluetoothEnableDisable() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        waiter.until(
                BluetoothExpectedConditions.enabled()
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        Assert.assertTrue(
                bluetooth.isEnabled()
        );

        bluetooth.disable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_OFF)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_OFF
        );

        Assert.assertFalse(
                bluetooth.isEnabled()
        );
    }

    @Test()
    public void bluetoothReboot() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

//        device.reboot("");

        Thread.sleep(50000);

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        bluetooth.disable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_OFF)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_OFF
        );
    }

    @Test
    public void bluetoothDiscoverable() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        bluetooth.startDiscoverable(120);

        waiter.until(
                BluetoothExpectedConditions.discoverable()
        );

        Assert.assertTrue(
                bluetooth.isDiscoverable()
        );

        Assert.assertEquals(
                bluetooth.getScanMode(),
                Bluetooth.ScanMode.SCAN_MODE_CONNECTABLE_DISCOVERABLE
        );

        bluetooth.getDiscoverableTimeout();

        bluetooth.cancelDiscoverable();

        waiter.until(
                BluetoothExpectedConditions.stopDiscoverable()
        );

        Assert.assertFalse(
                bluetooth.isDiscoverable()
        );

        Assert.assertEquals(
                bluetooth.getScanMode(),
                Bluetooth.ScanMode.SCAN_MODE_NONE
        );

    }

    @Test
    public void bluetoothChangeNameTest() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        String prevName = bluetooth.getName();

        bluetooth.setName("TestDevice mama");

        waiter.until(
                BluetoothExpectedConditions.name("TestDevice mama")
        );

        Assert.assertEquals(
                bluetooth.getName(),
                "TestDevice mama"
        );

        bluetooth.setName(prevName);

        waiter.until(
                BluetoothExpectedConditions.name(prevName)
        );

        Assert.assertEquals(
                bluetooth.getName(),
                prevName
        );
    }

    @Test
    public void discoveryBluetoothDevices() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        bluetooth.startDiscovery();

        waiter.until(
                BluetoothExpectedConditions.discovering()
        );

        Thread.sleep(6000);

//        waiter.until(
//                BluetoothExpectedConditions.discoveredDevice("74:65:50:80:AC:97")
//        );

        bluetooth.cancelDiscovery();

        waiter.until(
                BluetoothExpectedConditions.stopDiscovering()
        );


        List<BluetoothDevice> devices = bluetooth.getDiscoveredBluetoothDevices();

        for (BluetoothDevice device:devices){
            device.getPairState();
            device.getName();
            device.getBluetoothClass();
            device.getType();
            System.out.println("-----------------------");
        }
    }

    @Test
    public void bluetoothSupportProfilesTest() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        for (BluetoothProfile.Type type:bluetooth.getSupportedProfiles()){
            System.out.println(type);
        }
    }

    @Test
    public void bluetoothGetProfileConnectionStateTest() throws Exception {
        bluetooth.enable();

        waiter.until(
                BluetoothExpectedConditions.state(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        for (BluetoothProfile.Type type:bluetooth.getSupportedProfiles()){
            if (type!=null) {
                bluetooth.getProfileConnectionState(type);
            }
        }
    }

    @After
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
    }
}
