package com.github.frunoyman.adapters.usb;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class UsbDevice {
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

    private int deviceProtocol;
    private boolean hasVideoPlayback;
    private String serialNumber;
    private int productId;
    private String manufacturerName;
    private int vendorId;
    private int deviceSubclass;
    private int deviceId;
    private String deviceName;
    private String version;
    private boolean hasAudioPlayback;
    private String productName;
    private int configurationCount;
    private int deviceClass;
    private int interfaceCount;
    private boolean hasVideoCapture;
    private boolean hasMidi;
    private boolean hasAudioCapture;

    public int getDeviceProtocol() {
        return deviceProtocol;
    }

    public boolean isHasVideoPlayback() {
        return hasVideoPlayback;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public int getProductId() {
        return productId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getDeviceSubclass() {
        return deviceSubclass;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getVersion() {
        return version;
    }

    public boolean isHasAudioPlayback() {
        return hasAudioPlayback;
    }

    public String getProductName() {
        return productName;
    }

    public int getConfigurationCount() {
        return configurationCount;
    }

    public int getDeviceClass() {
        return deviceClass;
    }

    public int getInterfaceCount() {
        return interfaceCount;
    }

    public boolean isHasVideoCapture() {
        return hasVideoCapture;
    }

    public boolean isHasMidi() {
        return hasMidi;
    }

    public boolean isHasAudioCapture() {
        return hasAudioCapture;
    }

    public UsbDevice() {
    }

    @Override
    public String toString() {
        return "UsbDevice{" +
                "deviceProtocol=" + deviceProtocol +
                ", hasVideoPlayback=" + hasVideoPlayback +
                ", serialNumber='" + serialNumber + '\'' +
                ", productId=" + productId +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", vendorId=" + vendorId +
                ", deviceSubclass=" + deviceSubclass +
                ", deviceId=" + deviceId +
                ", deviceName='" + deviceName + '\'' +
                ", version='" + version + '\'' +
                ", hasAudioPlayback=" + hasAudioPlayback +
                ", productName='" + productName + '\'' +
                ", configurationCount=" + configurationCount +
                ", deviceClass=" + deviceClass +
                ", interfaceCount=" + interfaceCount +
                ", hasVideoCapture=" + hasVideoCapture +
                ", hasMidi=" + hasMidi +
                ", hasAudioCapture=" + hasAudioCapture +
                '}';
    }
}
