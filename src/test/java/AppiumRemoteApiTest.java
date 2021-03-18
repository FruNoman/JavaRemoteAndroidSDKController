import com.github.frunoyman.adapters.bluetooth.Bluetooth;
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

public class AppiumRemoteApiTest {
    private AndroidDriver driver;

    @Before
    public void beforeAppiumTest() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 160);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, 8229);
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
    }

    @Test
    public void appiumTest() {
        AppiumRemoteSdk remoteSdk = new AppiumRemoteSdk(driver);
        Bluetooth bluetooth = remoteSdk.getBluetooth();
        bluetooth.enable();
        Assert.assertEquals(bluetooth.getState(), 0);
        bluetooth.disable();
        Assert.assertEquals(bluetooth.getState(), 0);
    }

    @After
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
    }
}
