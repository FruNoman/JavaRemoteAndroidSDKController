package com.github.frunoyman.adapters.wifi;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class Wifi extends BaseAdapter {
    private final String WIFI_COMMAND = "wifi_remote ";
    private final String WIFI_REMOTE = "com.github.remotesdk.WIFI_REMOTE";
    private Logger logger;

    private final String AM_COMMAND = BROADCAST
            + WIFI_REMOTE
            + ES
            + WIFI_COMMAND;

    private final String ENABLE = AM_COMMAND
            + "enable";
    private final String DISABLE = AM_COMMAND
            + "disable";
    private final String GET_STATE = AM_COMMAND
            + "getState";

    private final String IS_ENABLED = AM_COMMAND
            + "isEnabled";
    private final String ADD_NETWORK = AM_COMMAND
            + "addNetwork,";
    private final String ENABLE_NETWORK = AM_COMMAND
            + "enableNetwork,";
    private final String DISABLE_NETWORK = AM_COMMAND
            + "disableNetwork,";
    private final String REMOVE_NETWORK = AM_COMMAND
            + "removeNetwork,";
    private final String GET_CONFIGURE_NETWORKS = AM_COMMAND
            + "getConfiguredNetworks";


    public Wifi(Shell shell) {
        super(shell);
        logger = Logger.getLogger(Wifi.class.getName() + "] [" + shell.getSerial());
    }



    public enum State {
        WIFI_STATE_DISABLING(0),
        WIFI_STATE_DISABLED(1),
        WIFI_STATE_ENABLING(2),
        WIFI_STATE_ENABLED(3),
        WIFI_STATE_UNKNOWN(4),
        WIFI_AP_STATE_DISABLING(10),
        WIFI_AP_STATE_DISABLED(11),
        WIFI_AP_STATE_ENABLING(12),
        WIFI_AP_STATE_ENABLED(13),
        WIFI_AP_STATE_FAILED(14);

        private int state;

        State(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }

        public static State getState(int state) {
            for (State states : values()) {
                if (states.getState() == state) {
                    return states;
                }
            }
            return State.WIFI_STATE_UNKNOWN;
        }
    }


    public boolean enable() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(ENABLE));
        logger.debug("enable");
        return result;
    }

    public boolean disable() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(DISABLE));
        logger.debug("disable");
        return result;
    }

    public State getState() throws Exception {
        State state = State.getState(Integer.parseInt(shell.executeBroadcast(GET_STATE)));
        logger.debug("get state [" + state + "]");
        return state;
    }

    public boolean isEnabled() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_ENABLED));
        logger.debug("is enabled [" + result + "]");
        return result;
    }

    public int addNetwork(String ssid, String pass, WifiConfiguration.SecurityType securityType) throws Exception {
        int result = Integer.parseInt(shell.executeBroadcast(ADD_NETWORK + ssid + "," + pass + "," + securityType.getConfig()));
        logger.debug("add network ssid [" + ssid + "] pass [" + pass + "] config [" + securityType + "] [" + result + "]");
        return result;
    }

    public int addNetwork(WifiConfiguration wifiConfiguration) throws Exception {
        return addNetwork(wifiConfiguration.getSSID(),wifiConfiguration.getPreSharedKey(),wifiConfiguration.getSecurityType());
    }

    public boolean enableNetwork(int netId) throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(ENABLE_NETWORK + netId));
        logger.debug("enable network id [" + netId + "] [" + result + "]");
        return result;
    }

    public boolean disableNetwork(int netId) throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(DISABLE_NETWORK + netId));
        logger.debug("disable network id [" + netId + "] [" + result + "]");
        return result;
    }

    public boolean removeNetwork(int netId) throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(REMOVE_NETWORK + netId));
        logger.debug("remove network id [" + netId + "] [" + result + "]");
        return result;
    }

    public void removeAllNetworks() throws Exception {
       for (WifiConfiguration configuration:getConfiguredNetworks()){
           removeNetwork(configuration.getNetworkId());
       }
    }

    public List<WifiConfiguration> getConfiguredNetworks() throws Exception {
        String result = shell.executeBroadcast(GET_CONFIGURE_NETWORKS);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
        List<WifiConfiguration> wifiConfigurations =  Arrays.asList(mapper.readValue(result, WifiConfiguration[].class));
        logger.debug("get configured networks");
        return wifiConfigurations;
    }
}
