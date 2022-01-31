package com.github.frunoyman.adapters.location;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.adapters.bluetooth.BluetoothAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationAdapter extends BaseAdapter {
    private final String BLUETOOTH_COMMAND = "location_command ";
    private final String BLUETOOTH_REMOTE = "com.github.remotesdk.LOCATION_REMOTE";
    private Logger logger;


    private final String AM_COMMAND = BROADCAST
            + BLUETOOTH_REMOTE
            + ES
            + BLUETOOTH_COMMAND;

    private final String IS_ENABLE = "isLocationEnabled";
    private final String SET_LOCATION_STATE = "setLocationState,";
    private final String GET_ALL_PROVIDERS = "getAllProviders";
    private final String GET_LAST_KNOWN_LOCATION = "getLastKnownLocation";
    private final String REQUEST_LOCATION_UPDATES = "requestLocationUpdates";
    private final String REMOVE_LOCATION_UPDATES = "removeUpdates";

    private final String GET_UPDATED_LOCATION_LIST = "getUpdatedLocations";

    private final String GET_SATELLITE_COUNT = "getSatelliteCount";
    private final String GET_CONSTELLATION_TYPE = "getConstellationType";
    private final String USED_IN_FIX = "usedInFix";

    public enum Provider {
        GPS_PROVIDER("gps"),
        NETWORK_PROVIDER("network"),
        PASSIVE_PROVIDER("passive"),
        FUSED_PROVIDER("fused");

        private String providerName;

        Provider(String providerName) {
            this.providerName = providerName;
        }

        public String getProviderName() {
            return providerName;
        }

        public static LocationAdapter.Provider getProviderName(String providerName) {
            for (LocationAdapter.Provider provider : values()) {
                if (provider.getProviderName() == providerName) {
                    return provider;
                }
            }
            return null;
        }
    }

    public LocationAdapter(Shell shell) {
        super(shell);
        logger = Logger.getLogger(BluetoothAdapter.class.getName() + "] [" + shell.getSerial());
    }

    public boolean isLocationEnabled() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_ENABLE));
        logger.debug("is location enabled [" + result + "]");
        return result;
    }

    public boolean setLocationState(boolean state) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_LOCATION_STATE + state));
        logger.debug("set location state [" + state + "] [" + result + "]");
        return result;
    }

    public List<Provider> getAllProviders() {
        List<Provider> providers = new ArrayList<>();
        String result = shell.executeBroadcast(GET_ALL_PROVIDERS);
        if (!result.isEmpty()) {
            for (String prov : result.split(",")) {
                providers.add(Provider.getProviderName(prov));
            }
        }
        logger.debug("get all providers");
        return providers;
    }

    public Location getLastKnownLocation(Provider provider) {
        String result = shell.executeBroadcast(GET_LAST_KNOWN_LOCATION + provider.getProviderName());
        Location location = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
        try {
            location = mapper.readValue(result, Location.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("get provider [" + provider.getProviderName() + "] last known location [" + location + "]");
        return location;
    }
}
