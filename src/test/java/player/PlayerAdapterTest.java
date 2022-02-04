package player;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.adapters.bluetooth.BluetoothDevice;
import com.github.frunoyman.adapters.bluetooth.BluetoothProfile;
import com.github.frunoyman.adapters.player.PlayerAdapter;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import com.github.frunoyman.waiter.BluetoothExpectedConditions;
import com.github.frunoyman.waiter.RemoteWaiter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapterTest {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private PlayerAdapter playerAdapter;
    private RemoteWaiter waiter;

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
        DDMLibRemoteSdk DDMLibRemoteSDK = new DDMLibRemoteSdk(device);
        playerAdapter = DDMLibRemoteSDK.getPlayerAdapter();
        waiter = new RemoteWaiter(DDMLibRemoteSDK, 15);
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


}
