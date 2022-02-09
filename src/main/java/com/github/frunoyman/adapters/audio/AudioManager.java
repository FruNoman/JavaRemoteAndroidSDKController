package com.github.frunoyman.adapters.audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioManager extends BaseAdapter {
    private final String AUDIO_REMOTE = "com.github.remotesdk.AUDIO_REMOTE";
    private final String AUDIO_BROADCAST = BROADCAST + AUDIO_REMOTE;
    private Logger logger;

    private final String START_BLUETOOTH_SCO = "startBluetoothSco";
    private final String STOP_BLUETOOTH_SCO = "stopBluetoothSco";
    private final String IS_BLUETOOTH_SCO_ON = "isBluetoothScoOn";
    private final String SET_BLUETOOTH_A2DP_ON = "setBluetoothA2dpOn";
    private final String SET_SPEAKER_PHONE_ON = "setSpeakerphoneOn";
    private final String SET_MICROPHONE_MUTE = "setMicrophoneMute";
    private final String IS_SPEAKER_PHONE_ON = "isSpeakerphoneOn";
    private final String IS_BLUETOOTH_SCO_AVAILABLE_OFF_CALL = "isBluetoothScoAvailableOffCall";
    private final String IS_MICROPHONE_MUTE = "isMicrophoneMute";
    private final String IS_MUSIC_ACTIVE = "isMusicActive";
    private final String IS_STREAM_MUTE = "isStreamMute";
    private final String IS_BLUETOOTH_A2DP_ON = "isBluetoothA2dpOn";
    private final String IS_WIRED_HEADSET_ON = "isWiredHeadsetOn";
    private final String GET_MODE = "getMode";
    private final String GET_RINGER_MODE = "getRingerMode";
    private final String GET_PROPERTY = "getProperty";
    private final String GET_MICROPHONES = "getMicrophones";
    private final String SET_WIRED_HEADSET_ON = "setWiredHeadsetOn";
    private final String SET_BLUETOOTH_SCO_ON = "setBluetoothScoOn";
    private final String SET_MODE = "setMode";
    private final String SET_RINGER_MODE = "setRingerMode";
    private final String SET_STREAM_VOLUME = "setStreamVolume";
    private final String GET_AUDIO_DEVICES = "getAudioDevicesInfo";
    private final String GET_STREAM_VOLUME = "getAudioStreamVolume";
    private final String GET_STREAM_MAX_VOLUME = "getAudioStreamMaxVolume";
    private final String ADJUST_VOLUME = "adjustVolume";

    public enum AdjustDirection {
        ADJUST_RAISE(1),
        ADJUST_LOWER(-1),
        ADJUST_SAME(0),
        ADJUST_MUTE(-100),
        ADJUST_UNMUTE(100),
        ADJUST_TOGGLE_MUTE(101);

        private int direction;

        AdjustDirection(int direction) {
            this.direction = direction;
        }

        public int getDirection() {
            return direction;
        }

        public static AdjustDirection getDirection(int direction) {
            for (AdjustDirection adjustDirection : values()) {
                if (adjustDirection.getDirection() == direction) {
                    return adjustDirection;
                }
            }
            return null;
        }
    }

    public enum AudioMode {
        MODE_INVALID(-2),
        MODE_CURRENT(-1),
        MODE_NORMAL(0),
        MODE_RINGTONE(1),
        MODE_IN_CALL(2),
        MODE_IN_COMMUNICATION(3),
        NUM_MODES(4);

        private int mode;

        AudioMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }

        public static AudioMode getMode(int mode) {
            for (AudioMode audioMode : values()) {
                if (audioMode.getMode() == mode) {
                    return audioMode;
                }
            }
            return MODE_INVALID;
        }
    }

    public enum RingerMode {
        RINGER_MODE_SILENT(0),
        RINGER_MODE_VIBRATE(1),
        RINGER_MODE_NORMAL(2);
        private int mode;

        RingerMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }

        public static RingerMode getMode(int mode) {
            for (RingerMode ringerMode : values()) {
                if (ringerMode.getMode() == mode) {
                    return ringerMode;
                }
            }
            return null;
        }
    }

    public enum AudioProperty {
        PROPERTY_OUTPUT_FRAMES_PER_BUFFER("android.media.property.OUTPUT_FRAMES_PER_BUFFER"),
        PROPERTY_SUPPORT_MIC_NEAR_ULTRASOUND(
                "android.media.property.SUPPORT_MIC_NEAR_ULTRASOUND"),
        PROPERTY_SUPPORT_SPEAKER_NEAR_ULTRASOUND(
                "android.media.property.SUPPORT_SPEAKER_NEAR_ULTRASOUND"),
        PROPERTY_SUPPORT_AUDIO_SOURCE_UNPROCESSED(
                "android.media.property.SUPPORT_AUDIO_SOURCE_UNPROCESSED");

        private String property;

        AudioProperty(String property) {
            this.property = property;
        }

        public String getProperty() {
            return property;
        }

        public static AudioProperty getProperty(String property) {
            for (AudioProperty audioProperty : values()) {
                if (audioProperty.getProperty().equals(property.trim())) {
                    return audioProperty;
                }
            }
            return null;
        }
    }

    public enum AudioFlag {
        GET_DEVICES_INPUTS(0x0001),
        GET_DEVICES_OUTPUTS(0x0002),
        GET_DEVICES_ALL(GET_DEVICES_OUTPUTS.getFlag() | GET_DEVICES_INPUTS.getFlag());

        private int flag;

        AudioFlag(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return flag;
        }

        public static AudioFlag getFlag(int flag) {
            for (AudioFlag audioFlag : values()) {
                if (audioFlag.getFlag() == flag) {
                    return audioFlag;
                }
            }
            return null;
        }
    }

    public enum Stream {
        STREAM_DEFAULT(1),
        STREAM_VOICE_CALL(0),
        STREAM_SYSTEM(1),
        STREAM_RING(2),
        STREAM_MUSIC(3),
        STREAM_ALARM(4),
        STREAM_NOTIFICATION(5),
        STREAM_BLUETOOTH_SCO(6),
        STREAM_SYSTEM_ENFORCED(7),
        STREAM_DTMF(8),
        STREAM_TTS(9),
        STREAM_ACCESSIBILITY(10),
        STREAM_ASSISTANT(11);

        private int stream;

        Stream(int stream) {
            this.stream = stream;
        }

        public int getStream() {
            return stream;
        }

        public static Stream getStream(int stream) {
            for (Stream streamNames : values()) {
                if (streamNames.getStream() == stream) {
                    return streamNames;
                }
            }
            return null;
        }
    }

    public AudioManager(Shell shell) {
        super(shell);
        logger = Logger.getLogger(AudioManager.class.getName() + "] [" + shell.getSerial());
    }

    public void startBluetoothSco() {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, START_BLUETOOTH_SCO);
        logger.debug("start bluetooth SCO");
    }

    public void stopBluetoothSco() {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, STOP_BLUETOOTH_SCO);
        logger.debug("stop bluetooth SCO");
    }

    public boolean isBluetoothScoOn() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(AUDIO_BROADCAST, IS_BLUETOOTH_SCO_ON));
        logger.debug("is bluetooth SCO on return [" + result + "]");
        return result;
    }

    public void setBluetoothA2dpOn(boolean state) {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, SET_BLUETOOTH_A2DP_ON, state);
        logger.debug("set bluetooth A2DP on [" + state + "]");
    }

    public void setSpeakerphoneOn(boolean state) {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, SET_SPEAKER_PHONE_ON, state);
        logger.debug("set speaker phone on [" + state + "]");
    }

    public void setMicrophoneMute(boolean state) {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, SET_MICROPHONE_MUTE, state);
        logger.debug("set speaker phone on [" + state + "]");
    }

    public boolean isSpeakerphoneOn() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(AUDIO_BROADCAST, IS_SPEAKER_PHONE_ON));
        logger.debug("is speaker phone on return [" + result + "]");
        return result;
    }

    public boolean isBluetoothScoAvailableOffCall() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(AUDIO_BROADCAST, IS_BLUETOOTH_SCO_AVAILABLE_OFF_CALL));
        logger.debug("is bluetooth SCO available off call return [" + result + "]");
        return result;
    }

    public boolean isMicrophoneMute() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(AUDIO_BROADCAST, IS_MICROPHONE_MUTE));
        logger.debug("is microphone mute return [" + result + "]");
        return result;
    }

    public boolean isMusicActive() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(AUDIO_BROADCAST, IS_MUSIC_ACTIVE));
        logger.debug("is music active return [" + result + "]");
        return result;
    }

    public boolean isStreamMute() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(AUDIO_BROADCAST, IS_STREAM_MUTE));
        logger.debug("is stream mute return [" + result + "]");
        return result;
    }

    public boolean isBluetoothA2dpOn() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(AUDIO_BROADCAST, IS_BLUETOOTH_A2DP_ON));
        logger.debug("is bluetooth A2DP on return [" + result + "]");
        return result;
    }

    public boolean isWiredHeadsetOn() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(AUDIO_BROADCAST, IS_WIRED_HEADSET_ON));
        logger.debug("is wired head set on return [" + result + "]");
        return result;
    }

    public AudioMode getMode() {
        AudioMode result = AudioMode.getMode(Integer.parseInt(shell.executeBroadcastExtended(AUDIO_BROADCAST, GET_MODE)));
        logger.debug("get mode return [" + result + "]");
        return result;
    }

    public RingerMode getRingerMode() {
        RingerMode result = RingerMode.getMode(Integer.parseInt(shell.executeBroadcastExtended(AUDIO_BROADCAST, GET_RINGER_MODE)));
        logger.debug("get ringer mode return [" + result + "]");
        return result;
    }

    public String getProperty(AudioProperty property) {
        String result = shell.executeBroadcastExtended(AUDIO_BROADCAST, GET_PROPERTY, property.getProperty());
        logger.debug("get property [" + property + "] return [" + result + "]");
        return result;
    }

    public List<MicrophoneInfo> getMicrophones() {
        List<MicrophoneInfo> microphoneInfos = new ArrayList<>();
        String result = shell.executeBroadcastExtended(AUDIO_BROADCAST, GET_MICROPHONES);
        ObjectMapper mapper = new ObjectMapper();
        try {
            microphoneInfos = Arrays.asList(mapper.readValue(result, MicrophoneInfo[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("get microphones");
        return microphoneInfos;
    }

    public void setWiredHeadsetOn(boolean state) {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, SET_WIRED_HEADSET_ON, state);
        logger.debug("set wired head set on [" + state + "]");
    }

    public void setBluetoothScoOn(boolean state) {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, SET_BLUETOOTH_SCO_ON, state);
        logger.debug("set wired head set on [" + state + "]");
    }

    public void setMode(AudioMode audioMode) {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, SET_MODE, audioMode.getMode());
        logger.debug("set mode [" + audioMode + "]");
    }

    public void setRingerMode(RingerMode ringerMode) {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, SET_RINGER_MODE, ringerMode.getMode());
        logger.debug("set ringer mode [" + ringerMode + "]");
    }

    public void setStreamVolume(Stream stream, int index, AudioFlag flag) {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, SET_STREAM_VOLUME, stream.getStream(), index, flag.getFlag());
        logger.debug("set stream volume audio mode [" + stream + "] value [" + index + "] audio flag [" + flag + "]");
    }

    public List<AudioDeviceInfo> getAudioDevicesInfo(AudioFlag flag) {
        List<AudioDeviceInfo> audioDeviceInfos = new ArrayList<>();
        String result = shell.executeBroadcastExtended(AUDIO_BROADCAST, GET_AUDIO_DEVICES, flag.getFlag());
        ObjectMapper mapper = new ObjectMapper();
        try {
            audioDeviceInfos = Arrays.asList(mapper.readValue(result, AudioDeviceInfo[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("get audio device info");
        return audioDeviceInfos;
    }

    public int getAudioStreamVolume(Stream stream) {
        int result = Integer.parseInt(shell.executeBroadcastExtended(AUDIO_BROADCAST, GET_STREAM_VOLUME, stream.getStream()));
        logger.debug("get audio stream  [" + stream + "] volume return [" + result + "]");
        return result;
    }

    public int getAudioStreamMaxVolume(Stream stream) {
        int result = Integer.parseInt(shell.executeBroadcastExtended(AUDIO_BROADCAST, GET_STREAM_MAX_VOLUME, stream.getStream()));
        logger.debug("get audio stream [" + stream + "] max volume return [" + result + "]");
        return result;
    }

    public void adjustVolume(AdjustDirection direction, AudioFlag flag) {
        shell.executeBroadcastExtended(AUDIO_BROADCAST, ADJUST_VOLUME, direction.getDirection(), flag.getFlag());
        logger.debug("adjust volume  [" + direction + "] flag [" + flag + "]");
    }


}