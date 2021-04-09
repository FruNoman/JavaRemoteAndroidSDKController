package com.github.frunoyman.adapters.usb;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class InputDevice {
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

    private int generation;
    private boolean virtual;
    private int productId;
    private int sources;
    private int vendorId;
    private String descriptor;
    private boolean enabled;
    private boolean external;
    private int keyboardType;
    private int controllerNumber;
    private String name;
    private int id;
    private boolean fullKeyboard;



    public InputDevice() {
    }

    public int getGeneration() {
        return generation;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public int getProductId() {
        return productId;
    }

    public int getSources() {
        return sources;
    }

    public int getVendorId() {
        return vendorId;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isExternal() {
        return external;
    }

    public int getKeyboardType() {
        return keyboardType;
    }

    public int getControllerNumber() {
        return controllerNumber;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isFullKeyboard() {
        return fullKeyboard;
    }

    @Override
    public String toString() {
        return "InputDevice{" +
                "generation=" + generation +
                ", virtual=" + virtual +
                ", productId=" + productId +
                ", sources=" + sources +
                ", vendorId=" + vendorId +
                ", descriptor='" + descriptor + '\'' +
                ", enabled=" + enabled +
                ", external=" + external +
                ", keyboardType=" + keyboardType +
                ", controllerNumber=" + controllerNumber +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", fullKeyboard=" + fullKeyboard +
                '}';
    }
}
