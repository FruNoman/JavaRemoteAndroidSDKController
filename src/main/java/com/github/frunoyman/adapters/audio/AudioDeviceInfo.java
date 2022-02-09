package com.github.frunoyman.adapters.audio;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class AudioDeviceInfo {

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    private String address;
    private int [] channelCounts;
    private int [] channelIndexMasks;
    private int [] channelMasks;
    private int [] encodings;
    private int id;
    private String productName;
    private int [] sampleRates;
    private int type;
    private boolean sink;
    private boolean source;


    public String getAddress() {
        return address;
    }

    public int[] getChannelCounts() {
        return channelCounts;
    }

    public AudioFormat.ChanelMask[] getChannelIndexMasks() {
        List<AudioFormat.ChanelMask> masks = new ArrayList<>();
        for (Integer msk:channelMasks){
            masks.add(AudioFormat.ChanelMask.getMask(msk));
        }

        return masks.toArray(new AudioFormat.ChanelMask[0]);
    }

    public int[] getChannelMasks() {
        return channelMasks;
    }

    public AudioFormat.Encodings [] getEncodings() {
        List<AudioFormat.Encodings> audioEncodings = new ArrayList<>();
        for (Integer enc:encodings){
            audioEncodings.add(AudioFormat.Encodings.getType(enc));
        }

        return audioEncodings.toArray(new AudioFormat.Encodings[0]);
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int[] getSampleRates() {
        return sampleRates;
    }

    public Type getType() {
        return Type.getType(type);
    }

    public boolean isSink() {
        return sink;
    }

    public boolean isSource() {
        return source;
    }

    public enum Type {
        /**
         * A device type associated with an unknown or uninitialized device.
         */
        TYPE_UNKNOWN(0),
        /**
         * A device type describing the attached earphone speaker.
         */
        TYPE_BUILTIN_EARPIECE(1),
        /**
         * A device type describing the speaker system (i.e. a mono speaker or stereo speakers) built
         * in a device.
         */
        TYPE_BUILTIN_SPEAKER(2),
        /**
         * A device type describing a headset, which is the combination of a headphones and microphone.
         */
        TYPE_WIRED_HEADSET(3),
        /**
         * A device type describing a pair of wired headphones.
         */
         TYPE_WIRED_HEADPHONES(4),
        /**
         * A device type describing an analog line-level connection.
         */
        TYPE_LINE_ANALOG(5),
        /**
         * A device type describing a digital line connection (e.g. SPDIF).
         */
        TYPE_LINE_DIGITAL(6),
        /**
         * A device type describing a Bluetooth device typically used for telephony.
         */
        TYPE_BLUETOOTH_SCO(7),
        /**
         * A device type describing a Bluetooth device supporting the A2DP profile.
         */
         TYPE_BLUETOOTH_A2DP (8),
        /**
         * A device type describing an HDMI connection .
         */
        TYPE_HDMI(9),
        /**
         * A device type describing the Audio Return Channel of an HDMI connection.
         */
         TYPE_HDMI_ARC(10),
        /**
         * A device type describing a USB audio device.
         */
        TYPE_USB_DEVICE (11),
        /**
         * A device type describing a USB audio device in accessory mode.
         */
        TYPE_USB_ACCESSORY(12),
        /**
         * A device type describing the audio device associated with a dock.
         */
        TYPE_DOCK (13),
        /**
         * A device type associated with the transmission of audio signals over FM.
         */
        TYPE_FM (14),
        /**
         * A device type describing the microphone(s) built in a device.
         */
        TYPE_BUILTIN_MIC(15),
        /**
         * A device type for accessing the audio content transmitted over FM.
         */
        TYPE_FM_TUNER (16),
        /**
         * A device type for accessing the audio content transmitted over the TV tuner system.
         */
        TYPE_TV_TUNER(17),
        /**
         * A device type describing the transmission of audio signals over the telephony network.
         */
        TYPE_TELEPHONY(18),
        /**
         * A device type describing the auxiliary line-level connectors.
         */
        TYPE_AUX_LINE(19),
        /**
         * A device type connected over IP.
         */
        TYPE_IP(20),
        /**
         * A type-agnostic device used for communication with external audio systems
         */
         TYPE_BUS (21),
        /**
         * A device type describing a USB audio headset.
         */
        TYPE_USB_HEADSET(22),
        /**
         * A device type describing a Hearing Aid.
         */
        TYPE_HEARING_AID(23);

        private int type;

        Type(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
        public static Type getType(int type) {
            for (Type deviceType:values()){
                if (deviceType.getType() == type){
                    return deviceType;
                }
            }
            return TYPE_UNKNOWN;
        }



    }

    @Override
    public String toString() {
        return "AudioDeviceInfo{" +
                "additionalProperties=" + additionalProperties +
                ", address='" + address + '\'' +
                ", channelCounts=" + Arrays.toString(channelCounts) +
                ", channelIndexMasks=" + Arrays.toString(channelIndexMasks) +
                ", channelMasks=" + Arrays.toString(getChannelIndexMasks()) +
                ", encodings=" + Arrays.toString(getEncodings()) +
                ", id=" + id +
                ", productName='" + productName + '\'' +
                ", sampleRates=" + Arrays.toString(sampleRates) +
                ", type=" + getType() +
                ", sink=" + sink +
                ", source=" + source +
                '}';
    }
}
