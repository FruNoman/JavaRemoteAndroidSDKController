package audio;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.github.frunoyman.adapters.audio.AudioDeviceInfo;
import com.github.frunoyman.adapters.audio.AudioManager;
import com.github.frunoyman.adapters.audio.MicrophoneInfo;
import com.github.frunoyman.controllers.DDMLibRemoteSdk;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AudioManagerTest {
    private static List<IDevice> devices = new ArrayList<>();
    private static Object monitor = new Object();
    private IDevice device;
    private AudioManager audioManager;

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
        audioManager = DDMLibRemoteSDK.getAudioManager();
    }


    @Test
    public void bluetoothEnableDisable() throws InterruptedException {
        for(MicrophoneInfo microphoneInfo:audioManager.getMicrophones()){
            System.out.println(microphoneInfo.getInternalDeviceType());
            System.out.println(microphoneInfo.getType());
            System.out.println(microphoneInfo);
        }

       for(AudioManager.AudioProperty property: AudioManager.AudioProperty.values()){
           System.out.println(property+"  "+audioManager.getProperty(property));
       }

        System.out.println(audioManager.getMode());
        System.out.println(audioManager.getRingerMode());

        for (AudioDeviceInfo deviceInfo:audioManager.getAudioDevicesInfo(AudioManager.AudioFlag.GET_DEVICES_ALL)){
            System.out.println(deviceInfo);
        }

        for(AudioManager.Stream stream : AudioManager.Stream.values()){
            audioManager.setStreamVolume(stream,7, AudioManager.AudioFlag.GET_DEVICES_ALL);
            System.out.println(stream +" volume "+audioManager.getAudioStreamVolume(stream));
//            System.out.println(streamName +" max volume "+audioManager.getAudioStreamMaxVolume(streamName));

        }



    }

}
