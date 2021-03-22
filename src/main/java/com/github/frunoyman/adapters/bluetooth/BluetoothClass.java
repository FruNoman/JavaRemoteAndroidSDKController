package com.github.frunoyman.adapters.bluetooth;

public class BluetoothClass {
    private int bluetoothClass;
    private int DEVICE_BITMASK = 0x1FFC;
    private int MAJOR_BITMASK = 0x1F00;

    public BluetoothClass(int bluetoothClass) {
        this.bluetoothClass = bluetoothClass;
    }


    public int hashCode() {
        return bluetoothClass;
    }

    public Device getDeviceClass() {
        int constant = (bluetoothClass & DEVICE_BITMASK);
        return Device.getClass(constant);
    }

    public Device.Major getMajorDeviceClass() {
        int constant = (bluetoothClass & MAJOR_BITMASK);
        return Device.Major.getMajorClass(constant);
    }

    public enum Device {
        // Devices in the COMPUTER major class
        COMPUTER_UNCATEGORIZED(0x0100),
        COMPUTER_DESKTOP(0x0104),
        COMPUTER_SERVER(0x0108),
        COMPUTER_LAPTOP(0x010C),
        COMPUTER_HANDHELD_PC_PDA(0x0110),
        COMPUTER_PALM_SIZE_PC_PDA(0x0114),
        COMPUTER_WEARABLE(0x0118),

        // Devices in the PHONE major class
        PHONE_UNCATEGORIZED(0x0200),
        PHONE_CELLULAR(0x0204),
        PHONE_CORDLESS(0x0208),
        PHONE_SMART(0x020C),
        PHONE_MODEM_OR_GATEWAY(0x0210),
        PHONE_ISDN(0x0214),

        // Minor classes for the AUDIO_VIDEO major class
        AUDIO_VIDEO_UNCATEGORIZED(0x0400),
        AUDIO_VIDEO_WEARABLE_HEADSET(0x0404),
        AUDIO_VIDEO_HANDSFREE(0x0408),
        AUDIO_VIDEO_MICROPHONE(0x0410),
        AUDIO_VIDEO_LOUDSPEAKER(0x0414),
        AUDIO_VIDEO_HEADPHONES(0x0418),
        AUDIO_VIDEO_PORTABLE_AUDIO(0x041C),
        AUDIO_VIDEO_CAR_AUDIO(0x0420),
        AUDIO_VIDEO_SET_TOP_BOX(0x0424),
        AUDIO_VIDEO_HIFI_AUDIO(0x0428),
        AUDIO_VIDEO_VCR(0x042C),
        AUDIO_VIDEO_VIDEO_CAMERA(0x0430),
        AUDIO_VIDEO_CAMCORDER(0x0434),
        AUDIO_VIDEO_VIDEO_MONITOR(0x0438),
        AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER(0x043C),
        AUDIO_VIDEO_VIDEO_CONFERENCING(0x0440),
        AUDIO_VIDEO_VIDEO_GAMING_TOY(0x0448),

        // Devices in the WEARABLE major class
        WEARABLE_UNCATEGORIZED(0x0700),
        WEARABLE_WRIST_WATCH(0x0704),
        WEARABLE_PAGER(0x0708),
        WEARABLE_JACKET(0x070C),
        WEARABLE_HELMET(0x0710),
        WEARABLE_GLASSES(0x0714),

        // Devices in the TOY major class
        TOY_UNCATEGORIZED(0x0800),
        TOY_ROBOT(0x0804),
        TOY_VEHICLE(0x0808),
        TOY_DOLL_ACTION_FIGURE(0x080C),
        TOY_CONTROLLER(0x0810),
        TOY_GAME(0x0814),

        // Devices in the HEALTH major class
        HEALTH_UNCATEGORIZED(0x0900),
        HEALTH_BLOOD_PRESSURE(0x0904),
        HEALTH_THERMOMETER(0x0908),
        HEALTH_WEIGHING(0x090C),
        HEALTH_GLUCOSE(0x0910),
        HEALTH_PULSE_OXIMETER(0x0914),
        HEALTH_PULSE_RATE(0x0918),
        HEALTH_DATA_DISPLAY(0x091C),

        // Devices in PERIPHERAL major class
        PERIPHERAL_NON_KEYBOARD_NON_POINTING(0x0500),
        PERIPHERAL_KEYBOARD(0x0540),
        PERIPHERAL_POINTING(0x0580),
        PERIPHERAL_KEYBOARD_POINTING(0x05C0);

        Device(int constant) {
            this.constant = constant;
        }

        private int constant;

        public int getConstant() {
            return constant;
        }

        public static Device getClass(int constant) {
            for (Device device : Device.values()) {
                if (device.getConstant() == constant) {
                    return device;
                }
            }
            return null;
        }

        public enum Major {
            MISC(0x0000),
            COMPUTER(0x0100),
            PHONE(0x0200),
            NETWORKING(0x0300),
            AUDIO_VIDEO(0x0400),
            PERIPHERAL(0x0500),
            IMAGING(0x0600),
            WEARABLE(0x0700),
            TOY(0x0800),
            HEALTH(0x0900),
            UNCATEGORIZED(0x1F00);

            private int constant;

            Major(int constant) {
                this.constant = constant;
            }

            public int getConstant() {
                return constant;
            }

            public static Major getMajorClass(int constant) {
                for (Major major : Major.values()) {
                    if (major.getConstant() == constant) {
                        return major;
                    }
                }
                return null;
            }
        }

    }

    @Override
    public String toString() {
        return "BluetoothClass{" +
                "deviceClass=" + getDeviceClass() +
                " majorDeviceClass=" + getMajorDeviceClass() +
                '}';
    }
}
