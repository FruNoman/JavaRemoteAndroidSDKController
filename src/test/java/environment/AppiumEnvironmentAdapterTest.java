package environment;

import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.environment.EnvironmentAdapter;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.adapters.environment.StorageVolume;
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

public class AppiumEnvironmentAdapterTest {
    private AndroidDriver driver;
    private BluetoothAdapter bluetoothAdapter;
    private AppiumRemoteSdk remoteSdk;
    @Before
    public void beforeAppiumTest() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 900);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, 8229);
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
        remoteSdk = new AppiumRemoteSdk(driver);
        bluetoothAdapter = remoteSdk.getBluetoothAdapter();
    }

    @Test
    public void remoteFileExistTetst(){
        RemoteFile remoteFile = remoteSdk.getRemoteFile("/storage/emulated/0");
        for (RemoteFile file:remoteFile.listFiles()){
            file.isFile();
            file.isDirectory();
            file.getName();
            file.exist();
            file.getAbsolutePath();
            file.getTotalSpace();
            file.isAbsolute();
            file.isHidden();
            file.canExecute();
            file.canRead();
            file.canWrite();
            file.lastModified();
            System.out.println("-------------------------------------");
        }
    }

    @Test
    public void environmentTest(){
        EnvironmentAdapter environmentAdapter = remoteSdk.getEnvironmentAdapter();
        RemoteFile externalStorage = environmentAdapter.getExternalStorageDirectory();
        externalStorage.exist();
        for (RemoteFile file:externalStorage.listFiles()){
            file.isFile();
            file.isDirectory();
            file.getName();
            file.exist();
            file.getAbsolutePath();
            System.out.println("---------------------");
        }

        RemoteFile dataDirectory = environmentAdapter.getDataDirectory();
        for (RemoteFile file:dataDirectory.listFiles()){
            file.isFile();
            file.isDirectory();
            file.getName();
            file.exist();
            file.getAbsolutePath();
            System.out.println("---------------------");
        }

        RemoteFile downloadCacheDirectory = environmentAdapter.getDownloadCacheDirectory();
        for (RemoteFile file:downloadCacheDirectory.listFiles()){
            file.isFile();
            file.isDirectory();
            file.getName();
            file.exist();
            file.getAbsolutePath();
            System.out.println("---------------------");
        }

        RemoteFile rootDirectory = environmentAdapter.getRootDirectory();
        for (RemoteFile file:rootDirectory.listFiles()){
            file.isFile();
            file.isDirectory();
            file.getName();
            file.exist();
            file.getAbsolutePath();
            System.out.println("---------------------");
        }
    }

    @Test
    public void getStorageVolumesTest(){
        EnvironmentAdapter environmentAdapter = remoteSdk.getEnvironmentAdapter();
        for(StorageVolume volume: environmentAdapter.getStorageVolumes()){
            System.out.println(volume);
        }
    }

    @Test
    public void createFileTest(){
        RemoteFile logcat = remoteSdk.getRemoteFile("/storage/emulated/0/logcat.txt");
        if (!logcat.exist()){
            logcat.createNewFile();
        }

        Assert.assertTrue(logcat.exist());
    }


    @Test
    public void createDeleteDirTest(){
        RemoteFile myDir = remoteSdk.getRemoteFile("/storage/emulated/0/myDir");
        if (!myDir.exist()){
            myDir.makeDir();
        }

        Assert.assertTrue(myDir.exist());
        Assert.assertTrue(myDir.isDirectory());

        myDir.delete();

        Assert.assertFalse(myDir.exist());
    }

    @Test
    public void createDeleteDirsTest(){
        RemoteFile myDir = remoteSdk.getRemoteFile("/storage/emulated/0/myDir/innerDir");
        if (!myDir.exist()){
            myDir.makeDirs();
        }

        Assert.assertTrue(myDir.exist());
        Assert.assertTrue(myDir.isDirectory());

        myDir.delete();

        Assert.assertFalse(myDir.exist());
    }

    @Test
    public void renameFileTest(){
        RemoteFile file = remoteSdk.getRemoteFile("/storage/emulated/0/beforeRename.txt");
        if (!file.exist()){
            file.createNewFile();
        }

        Assert.assertTrue(file.exist());

        file.renameTo("/storage/emulated/0/afterRename.txt");

        Assert.assertEquals(file.getName(),"afterRename.txt");

        file.delete();
    }


    @After
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
    }
}
