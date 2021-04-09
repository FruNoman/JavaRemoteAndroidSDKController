package com.github.frunoyman.adapters.environment;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class StorageVolume {
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

    private long fatVolumeId;
    private String id;
    private long maxFileSize;
    private String path;
    private String pathFile;
    private String state;
    private String userLabel;
    private boolean emulated;
    private boolean primary;
    private boolean removable;

    public StorageVolume() {
    }

    public long getFatVolumeId() {
        return fatVolumeId;
    }

    public String getId() {
        return id;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public String getPath() {
        return path;
    }

    public String getPathFile() {
        return pathFile;
    }

    public String getState() {
        return state;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public boolean isEmulated() {
        return emulated;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isRemovable() {
        return removable;
    }

    @Override
    public String toString() {
        return path;
    }
}
