import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.controllers.AppiumRemoteSdk;
import com.github.frunoyman.waiter.RemoteExpectedConditions;
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
                RemoteExpectedConditions.bluetoothState(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        bluetooth.disable();

        waiter.until(
                RemoteExpectedConditions.bluetoothState(Bluetooth.State.STATE_OFF)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_OFF
        );
    }

    @Test()
    public void bluetoothReboot() throws Exception {
        bluetooth.enable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_ON);
        bluetooth.disable();
        Thread.sleep(2000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_OFF);
        Runtime.getRuntime().exec("adb reboot");
        Thread.sleep(50000);
        bluetooth.enable();
        Thread.sleep(1000);
        Assert.assertEquals(bluetooth.getState(), Bluetooth.State.STATE_ON);
    }

    @Test
    public void bluetoothDiscoverable() throws Exception {
        bluetooth.discoverable(200);
        System.out.println(bluetooth.getAddress());
    }

    @Test
    public void bluetoothChangeNameTest() throws Exception {
        bluetooth.enable();

        waiter.until(
                RemoteExpectedConditions.bluetoothState(Bluetooth.State.STATE_ON)
        );

        Assert.assertEquals(
                bluetooth.getState(),
                Bluetooth.State.STATE_ON
        );

        String prevName = bluetooth.getName();

        bluetooth.setName("TestDevice");

        waiter.until(
                RemoteExpectedConditions.bluetoothName("TestDevice")
        );

        Assert.assertEquals(
                bluetooth.getName(),
                "TestDevice"
        );

        bluetooth.setName(prevName);

        waiter.until(
                RemoteExpectedConditions.bluetoothName(prevName)
        );

        Assert.assertEquals(
                bluetooth.getName(),
                prevName
        );
    }

    @After
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
    }
}
