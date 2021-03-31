package com.github.frunoyman.adapters.wifi;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class ScanResult {

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

    /**
     * The network name.
     */
    public String SSID;

    /**
     * Ascii encoded SSID. This will replace SSID when we deprecate it. @hide
     */

    /**
     * The address of the access point.
     */
    public String BSSID;

    /**
     * The HESSID from the beacon.
     */
    public long hessid;

    /**
     * The ANQP Domain ID from the Hotspot 2.0 Indication element, if present.
     */
    public int anqpDomainId;

    /*
     * This field is equivalent to the |flags|, rather than the |capabilities| field
     * of the per-BSS scan results returned by WPA supplicant. See the definition of
     * |struct wpa_bss| in wpa_supplicant/bss.h for more details.
     */
    /**
     * Describes the authentication, key management, and encryption schemes
     * supported by the access point.
     */
    public String capabilities;

    /**
     * No security protocol.
     */
    public static final int PROTOCOL_NONE = 0;
    /**
     * Security protocol type: WPA version 1.
     */
    public static final int PROTOCOL_WPA = 1;
    /**
     * Security protocol type: WPA version 2, also called RSN.
     */
    public static final int PROTOCOL_WPA2 = 2;
    /**
     * Security protocol type:
     * OSU Server-only authenticated layer 2 Encryption Network.
     * Used for Hotspot 2.0.
     */
    public static final int PROTOCOL_OSEN = 3;

    /**
     * No security key management scheme.
     */
    public static final int KEY_MGMT_NONE = 0;
    /**
     * Security key management scheme: PSK.
     */
    public static final int KEY_MGMT_PSK = 1;
    /**
     * Security key management scheme: EAP.
     */
    public static final int KEY_MGMT_EAP = 2;
    /**
     * @hide Security key management scheme: FT_PSK.
     */
    public static final int KEY_MGMT_FT_PSK = 3;
    /**
     * @hide Security key management scheme: FT_EAP.
     */
    public static final int KEY_MGMT_FT_EAP = 4;
    /**
     * @hide Security key management scheme: PSK_SHA256
     */
    public static final int KEY_MGMT_PSK_SHA256 = 5;
    /**
     * @hide Security key management scheme: EAP_SHA256.
     */
    public static final int KEY_MGMT_EAP_SHA256 = 6;
    /**
     * Security key management scheme: OSEN.
     * Used for Hotspot 2.0.
     */
    public static final int KEY_MGMT_OSEN = 7;

    /**
     * No cipher suite.
     */
    public static final int CIPHER_NONE = 0;
    /**
     * No group addressed, only used for group data cipher.
     */
    public static final int CIPHER_NO_GROUP_ADDRESSED = 1;
    /**
     * Cipher suite: TKIP
     */
    public static final int CIPHER_TKIP = 2;
    /**
     * Cipher suite: CCMP
     */
    public static final int CIPHER_CCMP = 3;

    /**
     * The detected signal level in dBm, also known as the RSSI.
     * <p>
     * an absolute signal level which can be displayed to a user.
     */
    public int level;
    /**
     * The primary 20 MHz frequency (in MHz) of the channel over which the client is communicating
     * with the access point.
     */
    public int frequency;

    /**
     * AP Channel bandwidth is 20 MHZ
     */
    public static final int CHANNEL_WIDTH_20MHZ = 0;
    /**
     * AP Channel bandwidth is 40 MHZ
     */
    public static final int CHANNEL_WIDTH_40MHZ = 1;
    /**
     * AP Channel bandwidth is 80 MHZ
     */
    public static final int CHANNEL_WIDTH_80MHZ = 2;
    /**
     * AP Channel bandwidth is 160 MHZ
     */
    public static final int CHANNEL_WIDTH_160MHZ = 3;
    /**
     * AP Channel bandwidth is 160 MHZ, but 80MHZ + 80MHZ
     */
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;

    /**
     * AP Channel bandwidth; one of {@link #CHANNEL_WIDTH_20MHZ}, {@link #CHANNEL_WIDTH_40MHZ},
     * {@link #CHANNEL_WIDTH_80MHZ}, {@link #CHANNEL_WIDTH_160MHZ}
     * or {@link #CHANNEL_WIDTH_80MHZ_PLUS_MHZ}.
     */
    public int channelWidth;

    /**
     * Not used if the AP bandwidth is 20 MHz
     * If the AP use 40, 80 or 160 MHz, this is the center frequency (in MHz)
     * if the AP use 80 + 80 MHz, this is the center frequency of the first segment (in MHz)
     */
    public int centerFreq0;

    /**
     * Only used if the AP bandwidth is 80 + 80 MHz
     * if the AP use 80 + 80 MHz, this is the center frequency of the second segment (in MHz)
     */
    public int centerFreq1;

    /**
     *
     */
    public boolean is80211McRTTResponder;

    /**
     * timestamp in microseconds (since boot) when
     * this result was last seen.
     */
    public long timestamp;

    /**
     * Timestamp representing date when this result was last seen, in milliseconds from 1970
     */
    public long seen;

    /**
     * On devices with multiple hardware radio chains, this class provides metadata about
     * each radio chain that was used to receive this scan result (probe response or beacon).
     */
    public static class RadioChainInfo {
        /**
         * Vendor defined id for a radio chain.
         */
        public int id;
        /**
         * Detected signal level in dBm (also known as the RSSI) on this radio chain.
         */
        public int level;

        @Override
        public String toString() {
            return "RadioChainInfo: id=" + id + ", level=" + level;
        }

        @Override
        public boolean equals(Object otherObj) {
            if (this == otherObj) {
                return true;
            }
            if (!(otherObj instanceof RadioChainInfo)) {
                return false;
            }
            RadioChainInfo other = (RadioChainInfo) otherObj;
            return id == other.id && level == other.level;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, level);
        }
    }

    ;

    /**
     * Information about the list of the radio chains used to receive this scan result
     * (probe response or beacon).
     * <p>
     * For Example: On devices with 2 hardware radio chains, this list could hold 1 or 2
     * entries based on whether this scan result was received using one or both the chains.
     * {@hide}
     */
    public RadioChainInfo[] radioChainInfos;

    /**
     * Status indicating the scan result does not correspond to a user's saved configuration
     */
    public boolean untrusted;

    /**
     * Number of time autojoin used it
     */
    public int numUsage;

    /**
     * The approximate distance to the AP in centimeter, if available.  Else
     */
    public int distanceCm;

    /**
     * The standard deviation of the distance to the access point, if available.
     */
    public int distanceSdCm;

    public static final long FLAG_PASSPOINT_NETWORK = 0x0000000000000001;

    public static final long FLAG_80211mc_RESPONDER = 0x0000000000000002;

    /*
     * These flags are specific to the ScanResult class, and are not related to the |flags|
     * field of the per-BSS scan results from WPA supplicant.
     */
    /**
     * Defines flags; such as {@link #FLAG_PASSPOINT_NETWORK}.
     */
    public long flags;

    /**
     * sets a flag in {@link #flags} field
     *
     * @param flag flag to set
     */
    public void setFlag(long flag) {
        flags |= flag;
    }

    /**
     * clears a flag in {@link #flags} field
     *
     * @param flag flag to set
     */
    public void clearFlag(long flag) {
        flags &= ~flag;
    }

    public boolean is80211mcResponder() {
        return (flags & FLAG_80211mc_RESPONDER) != 0;
    }

    public boolean isPasspointNetwork() {
        return (flags & FLAG_PASSPOINT_NETWORK) != 0;
    }

    /**
     * Indicates venue name (such as 'San Francisco Airport') published by access point; only
     * available on Passpoint network and if published by access point.
     */
    public CharSequence venueName;

    /**
     * Indicates Passpoint operator name published by access point.
     */
    public CharSequence operatorFriendlyName;

    /**
     *
     */
    public final static int UNSPECIFIED = -1;

    /**
     *
     */
    public boolean is24GHz() {
        return ScanResult.is24GHz(frequency);
    }

    /**
     *
     */
    public static boolean is24GHz(int freq) {
        return freq > 2400 && freq < 2500;
    }

    /**
     *
     */
    public boolean is5GHz() {
        return ScanResult.is5GHz(frequency);
    }

    /**
     * @hide TODO: makes real freq boundaries
     */
    public static boolean is5GHz(int freq) {
        return freq > 4900 && freq < 5900;
    }

    /**
     * @hide anqp lines from supplicant BSS response
     */
    public List<String> anqpLines;

    /**
     * information elements from beacon
     *
     * @hide
     */
    public static class InformationElement {
        public static final int EID_SSID = 0;
        public static final int EID_SUPPORTED_RATES = 1;
        public static final int EID_TIM = 5;
        public static final int EID_BSS_LOAD = 11;
        public static final int EID_ERP = 42;
        public static final int EID_HT_CAPABILITIES = 45;
        public static final int EID_RSN = 48;
        public static final int EID_EXTENDED_SUPPORTED_RATES = 50;
        public static final int EID_HT_OPERATION = 61;
        public static final int EID_INTERWORKING = 107;
        public static final int EID_ROAMING_CONSORTIUM = 111;
        public static final int EID_EXTENDED_CAPS = 127;
        public static final int EID_VHT_CAPABILITIES = 191;
        public static final int EID_VHT_OPERATION = 192;
        public static final int EID_VSA = 221;

        public int id;
        public byte[] bytes;

        public InformationElement() {
        }

        public InformationElement(InformationElement rhs) {
            this.id = rhs.id;
            this.bytes = rhs.bytes.clone();
        }
    }

    /**
     * information elements found in the beacon
     */
    public InformationElement[] informationElements;

    /** ANQP response elements.
     */
//    public AnqpInformationElement[] anqpElements;

    /**
     * Flag indicating if this AP is a carrier AP. The determination is based
     * on the AP's SSID and if AP is using EAP security.
     */
    public boolean isCarrierAp;

    /**
     *
     */
    public int carrierApEapType;

    /**
     * The name of the carrier that's associated with this AP if it is a carrier AP.
     */
    public String carrierName;



    public ScanResult(String Ssid, String BSSID, long hessid, int anqpDomainId, String caps,
                      int level, int frequency,
                      long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1,
                      boolean is80211McRTTResponder) {
        this.SSID = Ssid;
        this.BSSID = BSSID;
        this.hessid = hessid;
        this.anqpDomainId = anqpDomainId;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = distCm;
        this.distanceSdCm = distSdCm;
        this.channelWidth = channelWidth;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        if (is80211McRTTResponder) {
            this.flags = FLAG_80211mc_RESPONDER;
        } else {
            this.flags = 0;
        }
        this.isCarrierAp = false;
        this.carrierApEapType = UNSPECIFIED;
        this.carrierName = null;
        this.radioChainInfos = null;
    }


    public ScanResult() {
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String none = "<none>";

        sb.append("SSID: ").
                append(SSID == null ? none : SSID).
                append(", BSSID: ").
                append(BSSID == null ? none : BSSID).
                append(", capabilities: ").
                append(capabilities == null ? none : capabilities).
                append(", level: ").
                append(level).
                append(", frequency: ").
                append(frequency).
                append(", timestamp: ").
                append(timestamp);

        sb.append(", distance: ").append((distanceCm != UNSPECIFIED ? distanceCm : "?")).
                append("(cm)");
        sb.append(", distanceSd: ").append((distanceSdCm != UNSPECIFIED ? distanceSdCm : "?")).
                append("(cm)");

        sb.append(", passpoint: ");
        sb.append(((flags & FLAG_PASSPOINT_NETWORK) != 0) ? "yes" : "no");
        sb.append(", ChannelBandwidth: ").append(channelWidth);
        sb.append(", centerFreq0: ").append(centerFreq0);
        sb.append(", centerFreq1: ").append(centerFreq1);
        sb.append(", 80211mcResponder: ");
        sb.append(((flags & FLAG_80211mc_RESPONDER) != 0) ? "is supported" : "is not supported");
        sb.append(", Carrier AP: ").append(isCarrierAp ? "yes" : "no");
        sb.append(", Carrier AP EAP Type: ").append(carrierApEapType);
        sb.append(", Carrier name: ").append(carrierName);
        sb.append(", Radio Chain Infos: ").append(Arrays.toString(radioChainInfos));
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

}
