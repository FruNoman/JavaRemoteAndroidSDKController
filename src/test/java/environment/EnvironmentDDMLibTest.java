package environment;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.environment.Environment;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.adapters.environment.StorageVolume;
import com.github.frunoyman.adapters.telephony.Telecom;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import com.github.frunoyman.waiter.RemoteWaiter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnvironmentDDMLibTest {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private RemoteWaiter waiter;
    private DDMLibRemoteSdk DDMLibRemoteSDK;

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
        DDMLibRemoteSDK = new DDMLibRemoteSdk(device);
        waiter = new RemoteWaiter(DDMLibRemoteSDK, 15);
    }

    @Test
    public void remoteFileExistTetst() throws Exception {
        RemoteFile remoteFile = DDMLibRemoteSDK.getRemoteFile("/storage/emulated/0");
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
    public void environmentTest() throws Exception {
        Environment environment = DDMLibRemoteSDK.getEnvironment();
        RemoteFile externalStorage = environment.getExternalStorageDirectory();
        for (RemoteFile file:externalStorage.listFiles()){
            file.isFile();
            file.isDirectory();
            file.getName();
            file.exist();
            file.getAbsolutePath();
            System.out.println("---------------------");
        }

        RemoteFile dataDirectory = environment.getDataDirectory();
        for (RemoteFile file:dataDirectory.listFiles()){
            file.isFile();
            file.isDirectory();
            file.getName();
            file.exist();
            file.getAbsolutePath();
            System.out.println("---------------------");
        }

        RemoteFile downloadCacheDirectory = environment.getDownloadCacheDirectory();
        for (RemoteFile file:downloadCacheDirectory.listFiles()){
            file.isFile();
            file.isDirectory();
            file.getName();
            file.exist();
            file.getAbsolutePath();
            System.out.println("---------------------");
        }

        RemoteFile rootDirectory = environment.getRootDirectory();
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
    public void getStorageVolumesTest() throws Exception {
        Environment environment = DDMLibRemoteSDK.getEnvironment();
        for(StorageVolume volume:environment.getStorageVolumes()){
            System.out.println(volume);
        }
    }

    @Test
    public void createFileTest() throws Exception {
        Environment environment = DDMLibRemoteSDK.getEnvironment();
        RemoteFile logcat = DDMLibRemoteSDK.getRemoteFile("/storage/emulated/0/logcat.txt");
        if (!logcat.exist()){
            logcat.createNewFile();
        }

        Assert.assertTrue(logcat.exist());
    }


    @Test
    public void createDeleteDirTest() throws Exception {
        RemoteFile myDir = DDMLibRemoteSDK.getRemoteFile("/storage/emulated/0/myDir");
        if (!myDir.exist()){
            myDir.makeDir();
        }

        Assert.assertTrue(myDir.exist());
        Assert.assertTrue(myDir.isDirectory());

        myDir.delete();

        Assert.assertFalse(myDir.exist());
    }

    @Test
    public void createDeleteDirsTest() throws Exception {
        RemoteFile myDir = DDMLibRemoteSDK.getRemoteFile("/storage/emulated/0/myDir/innerDir");
        if (!myDir.exist()){
            myDir.makeDirs();
        }

        Assert.assertTrue(myDir.exist());
        Assert.assertTrue(myDir.isDirectory());

        myDir.delete();

        Assert.assertFalse(myDir.exist());
    }

    @Test
    public void renameFileTest() throws Exception {
        RemoteFile file = DDMLibRemoteSDK.getRemoteFile("/storage/emulated/0/beforeRename.txt");
        if (!file.exist()){
            file.createNewFile();
        }

        Assert.assertTrue(file.exist());

        file.renameTo("/storage/emulated/0/afterRename.txt");

        Assert.assertEquals(file.getName(),"afterRename.txt");

        file.delete();
    }
}
