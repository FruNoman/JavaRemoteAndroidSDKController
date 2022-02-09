package com.github.frunoyman.adapters.audio;

public class AudioSystem {
    public enum DeviceType {

        DEVICE_NONE(0x0),
        // reserved bits
        DEVICE_BIT_IN(0x80000000),
        DEVICE_BIT_DEFAULT(0x40000000),
        // output devices, be sure to update AudioManager.java also
        DEVICE_OUT_EARPIECE(0x1),
        DEVICE_OUT_SPEAKER(0x2),
        DEVICE_OUT_WIRED_HEADSET(0x4),
        DEVICE_OUT_WIRED_HEADPHONE(0x8),
        DEVICE_OUT_BLUETOOTH_SCO(0x10),
        DEVICE_OUT_BLUETOOTH_SCO_HEADSET(0x20),
        DEVICE_OUT_BLUETOOTH_SCO_CARKIT(0x40),
        DEVICE_OUT_BLUETOOTH_A2DP(0x80),
        DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES(0x100),
        DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER(0x200),
        DEVICE_OUT_AUX_DIGITAL(0x400),
        DEVICE_OUT_HDMI(DEVICE_OUT_AUX_DIGITAL.getType()),
        DEVICE_OUT_ANLG_DOCK_HEADSET(0x800),
        DEVICE_OUT_DGTL_DOCK_HEADSET(0x1000),
        DEVICE_OUT_USB_ACCESSORY(0x2000),
        DEVICE_OUT_USB_DEVICE(0x4000),
        DEVICE_OUT_REMOTE_SUBMIX(0x8000),
        DEVICE_OUT_TELEPHONY_TX(0x10000),
        DEVICE_OUT_LINE(0x20000),
        DEVICE_OUT_HDMI_ARC(0x40000),
        DEVICE_OUT_SPDIF(0x80000),
        DEVICE_OUT_FM(0x100000),
        DEVICE_OUT_AUX_LINE(0x200000),
        DEVICE_OUT_SPEAKER_SAFE(0x400000),
        DEVICE_OUT_IP(0x800000),
        DEVICE_OUT_BUS(0x1000000),
        DEVICE_OUT_PROXY(0x2000000),
        DEVICE_OUT_USB_HEADSET(0x4000000),
        DEVICE_OUT_HEARING_AID(0x8000000),

        DEVICE_OUT_DEFAULT(DEVICE_BIT_DEFAULT.getType()),

        DEVICE_OUT_ALL(
                DEVICE_OUT_EARPIECE.getType() |
                        DEVICE_OUT_SPEAKER.getType() |
                        DEVICE_OUT_WIRED_HEADSET.getType() |
                        DEVICE_OUT_WIRED_HEADPHONE.getType() |
                        DEVICE_OUT_BLUETOOTH_SCO.getType() |
                        DEVICE_OUT_BLUETOOTH_SCO_HEADSET.getType() |
                        DEVICE_OUT_BLUETOOTH_SCO_CARKIT.getType() |
                        DEVICE_OUT_BLUETOOTH_A2DP.getType() |
                        DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES.getType() |
                        DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER.getType() |
                        DEVICE_OUT_HDMI.getType() |
                        DEVICE_OUT_ANLG_DOCK_HEADSET.getType() |
                        DEVICE_OUT_DGTL_DOCK_HEADSET.getType() |
                        DEVICE_OUT_USB_ACCESSORY.getType() |
                        DEVICE_OUT_USB_DEVICE.getType() |
                        DEVICE_OUT_REMOTE_SUBMIX.getType() |
                        DEVICE_OUT_TELEPHONY_TX.getType() |
                        DEVICE_OUT_LINE.getType() |
                        DEVICE_OUT_HDMI_ARC.getType() |
                        DEVICE_OUT_SPDIF.getType() |
                        DEVICE_OUT_FM.getType() |
                        DEVICE_OUT_AUX_LINE.getType() |
                        DEVICE_OUT_SPEAKER_SAFE.getType() |
                        DEVICE_OUT_IP.getType() |
                        DEVICE_OUT_BUS.getType() |
                        DEVICE_OUT_PROXY.getType() |
                        DEVICE_OUT_USB_HEADSET.getType() |
                        DEVICE_OUT_HEARING_AID.getType() |
                        DEVICE_OUT_DEFAULT.getType()),
        DEVICE_OUT_ALL_A2DP((DEVICE_OUT_BLUETOOTH_A2DP.getType() |
                DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES.getType() |
                DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER.getType())),

        DEVICE_OUT_ALL_SCO((DEVICE_OUT_BLUETOOTH_SCO.getType() |
                DEVICE_OUT_BLUETOOTH_SCO_HEADSET.getType() |
                DEVICE_OUT_BLUETOOTH_SCO_CARKIT.getType())),

        DEVICE_OUT_ALL_USB((DEVICE_OUT_USB_ACCESSORY.getType() |
                DEVICE_OUT_USB_DEVICE.getType() |
                DEVICE_OUT_USB_HEADSET.getType())),

        DEVICE_OUT_ALL_HDMI_SYSTEM_AUDIO((DEVICE_OUT_AUX_LINE.getType() |
                DEVICE_OUT_HDMI_ARC.getType() |
                DEVICE_OUT_SPDIF.getType())),

        DEVICE_ALL_HDMI_SYSTEM_AUDIO_AND_SPEAKER(
                (DEVICE_OUT_ALL_HDMI_SYSTEM_AUDIO.getType() |
                        DEVICE_OUT_SPEAKER.getType())),

        // input devices
        DEVICE_IN_COMMUNICATION(DEVICE_BIT_IN.getType() | 0x1),
        DEVICE_IN_AMBIENT(DEVICE_BIT_IN.getType() | 0x2),
        DEVICE_IN_BUILTIN_MIC(DEVICE_BIT_IN.getType() | 0x4),
        DEVICE_IN_BLUETOOTH_SCO_HEADSET(DEVICE_BIT_IN.getType() | 0x8),
        DEVICE_IN_WIRED_HEADSET(DEVICE_BIT_IN.getType() | 0x10),
        DEVICE_IN_AUX_DIGITAL(DEVICE_BIT_IN.getType() | 0x20),
        DEVICE_IN_HDMI(DEVICE_IN_AUX_DIGITAL.getType()),
        DEVICE_IN_VOICE_CALL(DEVICE_BIT_IN.getType() | 0x40),
        DEVICE_IN_TELEPHONY_RX(DEVICE_IN_VOICE_CALL.getType()),
        DEVICE_IN_BACK_MIC(DEVICE_BIT_IN.getType() | 0x80),
        DEVICE_IN_REMOTE_SUBMIX(DEVICE_BIT_IN.getType() | 0x100),
        DEVICE_IN_ANLG_DOCK_HEADSET(DEVICE_BIT_IN.getType() | 0x200),
        DEVICE_IN_DGTL_DOCK_HEADSET(DEVICE_BIT_IN.getType() | 0x400),
        DEVICE_IN_USB_ACCESSORY(DEVICE_BIT_IN.getType() | 0x800),
        DEVICE_IN_USB_DEVICE(DEVICE_BIT_IN.getType() | 0x1000),
        DEVICE_IN_FM_TUNER(DEVICE_BIT_IN.getType() | 0x2000),
        DEVICE_IN_TV_TUNER(DEVICE_BIT_IN.getType() | 0x4000),
        DEVICE_IN_LINE(DEVICE_BIT_IN.getType() | 0x8000),
        DEVICE_IN_SPDIF(DEVICE_BIT_IN.getType() | 0x10000),
        DEVICE_IN_BLUETOOTH_A2DP(DEVICE_BIT_IN.getType() | 0x20000),
        DEVICE_IN_LOOPBACK(DEVICE_BIT_IN.getType() | 0x40000),
        DEVICE_IN_IP(DEVICE_BIT_IN.getType() | 0x80000),
        DEVICE_IN_BUS(DEVICE_BIT_IN.getType() | 0x100000),
        DEVICE_IN_PROXY(DEVICE_BIT_IN.getType() | 0x1000000),
        DEVICE_IN_USB_HEADSET(DEVICE_BIT_IN.getType() | 0x2000000),
        DEVICE_IN_DEFAULT(DEVICE_BIT_IN.getType() | DEVICE_BIT_DEFAULT.getType()),

        DEVICE_IN_ALL((
                DEVICE_IN_COMMUNICATION.getType() |
                        DEVICE_IN_AMBIENT.getType() |
                        DEVICE_IN_BUILTIN_MIC.getType() |
                        DEVICE_IN_BLUETOOTH_SCO_HEADSET.getType() |
                        DEVICE_IN_WIRED_HEADSET.getType() |
                        DEVICE_IN_HDMI.getType() |
                        DEVICE_IN_TELEPHONY_RX.getType() |
                        DEVICE_IN_BACK_MIC.getType() |
                        DEVICE_IN_REMOTE_SUBMIX.getType() |
                        DEVICE_IN_ANLG_DOCK_HEADSET.getType() |
                        DEVICE_IN_DGTL_DOCK_HEADSET.getType() |
                        DEVICE_IN_USB_ACCESSORY.getType() |
                        DEVICE_IN_USB_DEVICE.getType() |
                        DEVICE_IN_FM_TUNER.getType() |
                        DEVICE_IN_TV_TUNER.getType() |
                        DEVICE_IN_LINE.getType() |
                        DEVICE_IN_SPDIF.getType() |
                        DEVICE_IN_BLUETOOTH_A2DP.getType() |
                        DEVICE_IN_LOOPBACK.getType() |
                        DEVICE_IN_IP.getType() |
                        DEVICE_IN_BUS.getType() |
                        DEVICE_IN_PROXY.getType() |
                        DEVICE_IN_USB_HEADSET.getType() |
                        DEVICE_IN_DEFAULT.getType())),

        DEVICE_IN_ALL_SCO(DEVICE_IN_BLUETOOTH_SCO_HEADSET.getType()),
        DEVICE_IN_ALL_USB((DEVICE_IN_USB_ACCESSORY.getType() |
                DEVICE_IN_USB_DEVICE.getType() |
                DEVICE_IN_USB_HEADSET.getType()));

        private int type;

        DeviceType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static DeviceType getType(int type) {
            for (DeviceType deviceType:values()){
                if(deviceType.getType()==type){
                    return deviceType;
                }
            }
            return DEVICE_NONE;
        }
    }
}
