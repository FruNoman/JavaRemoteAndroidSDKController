package player;

import com.github.frunoyman.adapters.location.Location;
import com.github.frunoyman.adapters.location.LocationAdapter;
import com.github.frunoyman.adapters.player.PlayerAdapter;
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

public class AppiumPlayerAdapterTest {
    private AndroidDriver driver;
    private PlayerAdapter playerAdapter;

    @Before
    public void beforeAppiumTest() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 900);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, 8229);
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
        AppiumRemoteSdk remoteSdk = new AppiumRemoteSdk(driver);
        playerAdapter = remoteSdk.getPlayerAdapter();
    }

    @Test
    public void playerSingleSongTest() throws InterruptedException {
        playerAdapter.displayView(PlayerAdapter.PlayerView.EQUALIZER);

        playerAdapter.play("/storage/emulated/0/A_000354_PCM_16bit_48Khz_1536kbps_stereo.wav");

        Thread.sleep(5000);

        Assert.assertTrue(playerAdapter.isPlaying());

        Assert.assertTrue(playerAdapter.getDuration() > 0);

        long firstPosition = playerAdapter.getCurrentPosition();

        Assert.assertTrue(firstPosition > 0);

        playerAdapter.seekTo((firstPosition + 10000));

        Thread.sleep(1000);

        long updatePosition = playerAdapter.getCurrentPosition();

        Assert.assertTrue(updatePosition > firstPosition);

        playerAdapter.pause();

        Assert.assertFalse(playerAdapter.isPlaying());

        Assert.assertFalse(playerAdapter.isLooping());

        playerAdapter.setLooping(true);

        Assert.assertTrue(playerAdapter.isLooping());

        playerAdapter.setLooping(false);

        playerAdapter.stop();
    }

    @Test
    public void playFolderTest() throws InterruptedException {
        playerAdapter.displayView(PlayerAdapter.PlayerView.EQUALIZER);

        playerAdapter.playFolder("/storage/emulated/0/audio");

        Thread.sleep(1000);

        Assert.assertTrue(playerAdapter.isPlaying());

        Thread.sleep(5000);

        String currentFile = playerAdapter.getCurrentPlayingFile().getAbsolutePath();

        playerAdapter.next();

        Thread.sleep(5000);


        String updateFile = playerAdapter.getCurrentPlayingFile().getAbsolutePath();

        Assert.assertFalse(currentFile.equals(updateFile));

        playerAdapter.stop();



    }



    @After
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
    }
}
