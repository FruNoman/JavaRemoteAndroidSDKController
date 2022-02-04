package com.github.frunoyman.adapters.location;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class Location {
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

    private double altitude;
    private double latitude;
    private double longitude;
    private double accuracy;
    private String provider;

    public Location() {
    }

    public Location(double altitude, double latitude, double longitude, double accuracy, String provider) {
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "Location{" +
                "altitude=" + altitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", accuracy=" + accuracy +
                ", provider='" + provider + '\'' +
                '}';
    }
}
