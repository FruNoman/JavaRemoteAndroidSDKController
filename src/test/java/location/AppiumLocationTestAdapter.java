package location;

import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.bluetooth.BluetoothDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothProfile;
import com.github.frunoyman.adapters.location.Location;
import com.github.frunoyman.adapters.location.LocationAdapter;
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

public class AppiumLocationTestAdapter {
    private AndroidDriver driver;
    private LocationAdapter locationAdapter;

    @Before
    public void beforeAppiumTest() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 900);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, 8229);
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
        AppiumRemoteSdk remoteSdk = new AppiumRemoteSdk(driver);
        locationAdapter = remoteSdk.getLocationAdapter();
    }

    @Test
    public void locationEnableDisable() throws InterruptedException {
        locationAdapter.setLocationState(true);

        Thread.sleep(2000);

        Assert.assertTrue(locationAdapter.isLocationEnabled());

        locationAdapter.setLocationState(false);

        Thread.sleep(2000);

        Assert.assertFalse(locationAdapter.isLocationEnabled());
    }

    @Test
    public void getLocationUpdatesTest() throws InterruptedException {
        locationAdapter.setLocationState(true);

        Thread.sleep(2000);

        Assert.assertTrue(locationAdapter.isLocationEnabled());


        locationAdapter.requestLocationUpdates(LocationAdapter.Provider.GPS_PROVIDER);

        Thread.sleep(120000);

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

        int satellites = locationAdapter.getSatelliteCount();

        Assert.assertTrue(satellites>0);

        Location location = locationAdapter.getLastKnownLocation(LocationAdapter.Provider.NETWORK_PROVIDER);

//        Assert.assertTrue(location!=null);

        for (LocationAdapter.Provider provider: locationAdapter.getAllProviders()){
            System.out.println("Provider "+provider);
        }

        for (int x=0; x<satellites;x++){
            boolean inFix = locationAdapter.usedInFix(x);
            LocationAdapter.ConstellationType type = locationAdapter.getConstellationType(x);
            System.out.println("Satellite number "+ x+ " in fix "+inFix+" type "+type);
        }

        locationAdapter.setLocationState(false);

    }



    @After
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
    }
}
