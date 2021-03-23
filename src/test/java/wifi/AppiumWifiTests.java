package wifi;

import com.github.frunoyman.adapters.wifi.Wifi;
import com.github.frunoyman.controllers.AppiumRemoteSdk;
import com.github.frunoyman.waiter.RemoteWaiter;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class AppiumWifiTests {
    private AndroidDriver driver;
    private Wifi wifi;
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
        wifi = remoteSdk.getWifi();
        waiter = new RemoteWaiter(remoteSdk, 15);
    }

    @After
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
    }
}
