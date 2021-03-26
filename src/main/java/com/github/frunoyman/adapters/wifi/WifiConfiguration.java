package com.github.frunoyman.adapters.wifi;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;

import java.util.HashMap;
import java.util.Map;

public class WifiConfiguration {
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

    private String SSID="";
    private String preSharedKey="";
    private int networkId;
    private int status;
    private int authType;
    private SecurityType securityType = SecurityType.PASS;

    public WifiConfiguration() {
    }

    public WifiConfiguration(String SSID, String preSharedKey, SecurityType securityType) {
        this.SSID = SSID;
        this.preSharedKey = preSharedKey;
        this.securityType = securityType;
    }

    public String getSSID() {
        if (SSID!=null){
            return SSID.replaceAll("\"", "");
        }else {
            return SSID;
        }
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getPreSharedKey() {
        if (preSharedKey!=null){
            return preSharedKey.replaceAll("\"", "");
        }else {
            return preSharedKey;
        }

    }

    public void setPreSharedKey(String preSharedKey) {
        this.preSharedKey = preSharedKey;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public void setConfigType(SecurityType securityType) {
        this.securityType = securityType;
    }

    public int getNetworkId() {
        return networkId;
    }

    public Status getStatus() {
        return Status.getStatus(status);
    }

    public AuthType getAuthType() {
        return AuthType.getAutType(authType);
    }

    public enum Status {
        CURRENT(0),
        DISABLED(1),
        ENABLED(2);

        private int status;

        Status(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public static Status getStatus(int status) {
            for (Status stat : values()) {
                if (stat.getStatus() == status) {
                    return stat;
                }
            }
            return DISABLED;
        }
    }

    public enum SecurityType {
        WEP("wep"),
        PASS("pass"),
        OPEN("open");

        private String config;

        SecurityType(String config) {
            this.config = config;
        }

        public String getConfig() {
            return config;
        }

        public static SecurityType getSecurityType(String type) {
            switch (type) {
                case "WPA_PSK":
                    return PASS;
                case "WEP":
                    return WEP;
                case "NONE":
                    return OPEN;
                default:
                    return PASS;
            }
        }
    }

    public enum AuthType {
        NONE(0),
        WPA_PSK(1),
        WPA_EAP(2),
        IEEE8021X(3),
        WPA2_PSK(4),
        OSEN(5),
        FT_PSK(6),
        FT_EAP(7),
        SAE(8),
        OWE(9),
        SUITE_B_192(10),
        WPA_PSK_SHA256(11),
        WPA_EAP_SHA256(12),
        WAPI_PSK(13),
        WAPI_CERT(14),
        FILS_SHA256(15),
        FILS_SHA384(16);

        private int state;

        public int getState() {
            return state;
        }

        public static AuthType getAutType(int state) {
            for (AuthType authType : values()) {
                if (authType.getState() == state) {
                    return authType;
                }
            }
            return NONE;
        }

        AuthType(int state) {
            this.state = state;
        }
    }

    @Override
    public String toString() {
        return "WifiConfiguration{" +
                "SSID='" + getSSID() + '\'' +
                ", preSharedKey='" + preSharedKey + '\'' +
                ", networkId=" + networkId +
                ", status=" + getStatus() +
                ", securityType=" + securityType +
                ", authType=" + getAuthType() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return ((WifiConfiguration) o).getSSID().equals(getSSID());
    }
}
