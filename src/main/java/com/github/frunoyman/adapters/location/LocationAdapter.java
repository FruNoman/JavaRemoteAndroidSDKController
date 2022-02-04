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
    private final String LOCATION_COMMAND = "location_command ";
    private final String LOCATION_REMOTE = "com.github.remotesdk.LOCATION_REMOTE";
    private Logger logger;


    private final String AM_COMMAND = BROADCAST
            + LOCATION_REMOTE
            + ES
            + LOCATION_COMMAND;

    private final String IS_ENABLE = AM_COMMAND + "isLocationEnabled";
    private final String SET_LOCATION_STATE = AM_COMMAND + "setLocationState,";
    private final String GET_ALL_PROVIDERS = AM_COMMAND + "getAllProviders";
    private final String GET_LAST_KNOWN_LOCATION = AM_COMMAND + "getLastKnownLocation,";
    private final String REQUEST_LOCATION_UPDATES = AM_COMMAND + "requestLocationUpdates,";
    private final String GET_UPDATED_LOCATION_LIST = AM_COMMAND + "getUpdatedLocations";
    private final String REMOVE_LOCATION_UPDATES = AM_COMMAND + "removeUpdates";

    private final String GET_SATELLITE_COUNT = AM_COMMAND + "getSatelliteCount";
    private final String GET_CONSTELLATION_TYPE = AM_COMMAND + "getConstellationType,";
    private final String USED_IN_FIX = AM_COMMAND + "usedInFix,";


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
                if (provider.getProviderName().equals(providerName)) {
                    return provider;
                }
            }
            return null;
        }
    }

    public enum ConstellationType {
        CONSTELLATION_UNKNOWN(0),
        CONSTELLATION_GPS(1),
        CONSTELLATION_SBAS(2),
        CONSTELLATION_GLONASS(3),
        CONSTELLATION_QZSS(4),
        CONSTELLATION_BEIDOU(5),
        CONSTELLATION_GALILEO(6),
        CONSTELLATION_IRNSS(7);

        private int constellationType;

        ConstellationType(int constellationType) {
            this.constellationType = constellationType;
        }

        public int getConstellationType() {
            return constellationType;
        }

        public static LocationAdapter.ConstellationType getConstellationType(int constellationType) {
            for (LocationAdapter.ConstellationType type : values()) {
                if (type.getConstellationType() == constellationType) {
                    return type;
                }
            }
            return null;
        }
    }

    public LocationAdapter(Shell shell) {
        super(shell);
        logger = Logger.getLogger(LocationAdapter.class.getName() + "] [" + shell.getSerial());
    }

    public boolean isLocationEnabled() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_ENABLE));
        logger.debug("is location enabled [" + result + "]");
        return result;
    }

    public boolean setLocationState(boolean state) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_LOCATION_STATE + state));
        logger.debug("set location state [" + state + "] return [" + result + "]");
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

    public void requestLocationUpdates(Provider provider) {
        shell.execute("am start -n com.github.remotesdk/.MainActivity --activity-single-top --activity-clear-when-task-reset");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shell.executeBroadcast(REQUEST_LOCATION_UPDATES + provider.getProviderName());
        logger.debug("request location updates");
    }

    public List<Location> getUpdatedLocations() {
        List<Location> locations = new ArrayList<>();
        String result = shell.executeBroadcast(GET_UPDATED_LOCATION_LIST);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
        try {
            locations = Arrays.asList(mapper.readValue(result, Location[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("get updated locations");
        return locations;
    }

    public void removeUpdates() {
        shell.executeBroadcast(REMOVE_LOCATION_UPDATES);
        logger.debug("remove location updates");
    }

    public int getSatelliteCount() {
        int result = Integer.parseInt(shell.executeBroadcast(GET_SATELLITE_COUNT));
        logger.debug("get satellite count return [" + result + "]");
        return result;
    }

    public boolean usedInFix(int satelliteIndex) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(USED_IN_FIX + satelliteIndex));
        logger.debug("used satellite  [" + satelliteIndex + "] in fix return [" + result + "]");
        return result;
    }

    public ConstellationType getConstellationType(int satelliteIndex) {
        ConstellationType result = ConstellationType.getConstellationType(
                Integer.parseInt(shell.executeBroadcast(
                        GET_CONSTELLATION_TYPE + satelliteIndex)));
        logger.debug("get satellite  [" + satelliteIndex + "] constellation type return [" + result.name() + "]");
        return result;
    }
}
