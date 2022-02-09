package com.github.frunoyman.adapters.audio;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class MicrophoneInfo {
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
    private String description;
    private int directionality;
    private int group;
    private int id;
    private int indexInTheGroup;
    private int internalDeviceType;
    private int location;
    private double maxSpl;
    private double minSpl;
    private double sensitivity;
    private int type;

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public int getDirectionality() {
        return directionality;
    }

    public int getGroup() {
        return group;
    }

    public int getId() {
        return id;
    }

    public int getIndexInTheGroup() {
        return indexInTheGroup;
    }

    public AudioSystem.DeviceType getInternalDeviceType() {
        return AudioSystem.DeviceType.getType(internalDeviceType);
    }

    public int getLocation() {
        return location;
    }

    public double getMaxSpl() {
        return maxSpl;
    }

    public double getMinSpl() {
        return minSpl;
    }

    public double getSensitivity() {
        return sensitivity;
    }

    public AudioDeviceInfo.Type getType() {
        return AudioDeviceInfo.Type.getType(type);
    }

    public MicrophoneInfo() {
    }

    @Override
    public String toString() {
        return "MicrophoneInfo{" +
                "address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", directionality=" + directionality +
                ", group=" + group +
                ", id=" + id +
                ", indexInTheGroup=" + indexInTheGroup +
                ", internalDeviceType=" + getInternalDeviceType() +
                ", location=" + location +
                ", maxSpl=" + maxSpl +
                ", minSpl=" + minSpl +
                ", sensitivity=" + sensitivity +
                ", type=" + getType() +
                '}';
    }
}
