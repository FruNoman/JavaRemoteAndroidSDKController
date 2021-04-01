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
    private final String DISCONNECT = AM_COMMAND
            + "disconnect";
    private final String GET_WIFI_AP_CONFIGURATION = AM_COMMAND
            + "getWifiApConfiguration";
    private final String GET_WIFI_AP_STATE = AM_COMMAND
            + "getWifiApState";
    private final String IS_WIFI_AP_ENABLED = AM_COMMAND
            + "isWifiApEnabled";
    private final String START_TETHERING = AM_COMMAND
            + "startTethering";
    private final String STOP_TETHERING = AM_COMMAND
            + "stopTethering";
    private final String SET_WIFI_AP_CONFIGURATION = AM_COMMAND
            +"setWifiApConfiguration,";


    private final String RECONNECT = AM_COMMAND
            +"reconnect";
    private final String REASSOCIATE = AM_COMMAND
            +"reassociate";
    private final String START_SCAN = AM_COMMAND
            +"startScan";
    private final String GET_SCAN_RESULT = AM_COMMAND
            +"getScanResults";
    private final String IS_CONNECTED = AM_COMMAND
            +"isWifiNetworkConnected";
    private final String IS_SCAN_ALWAYS_AVAILABLE = AM_COMMAND
            +"isScanAlwaysAvailable";
    private final String SET_SCAN_ALWAYS_AVAILABLE = AM_COMMAND
            +"setScanAlwaysAvailable,";
    private final String GET_SCAN_ALWAYS_AVAILABLE = AM_COMMAND
            +"getScanAlwaysAvailable";


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

    public boolean disconnect() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(DISCONNECT));
        logger.debug("disconnect");
        return result;
    }

    public boolean removeNetwork(int netId) throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(REMOVE_NETWORK + netId));
        logger.debug("remove network id [" + netId + "] [" + result + "]");
        return result;
    }

    public void removeAllNetworks() throws Exception {
        for (WifiConfiguration configuration : getConfiguredNetworks()) {
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
        List<WifiConfiguration> wifiConfigurations = Arrays.asList(mapper.readValue(result, WifiConfiguration[].class));
        logger.debug("get configured networks");
        return wifiConfigurations;
    }

    public WifiConfiguration getWifiHotspotConfiguration() throws Exception {
        String result = shell.executeBroadcast(GET_WIFI_AP_CONFIGURATION);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        WifiConfiguration wifiConfiguration = mapper.readValue(result, WifiConfiguration.class);
        logger.debug("get hotspot configured network");
        return wifiConfiguration;
    }

    public boolean setWifiHotspotConfiguration(String ssid, String pass, WifiConfiguration.SecurityType securityType) throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_WIFI_AP_CONFIGURATION + ssid + "," + pass + "," + securityType.getConfig()));
        logger.debug("set hotspot configuration [" + ssid + "] pass [" + pass + "] config [" + securityType + "] [" + result + "]");
        return result;
    }

    public boolean setWifiHotspotConfiguration(WifiConfiguration wifiConfiguration) throws Exception {
        return setWifiHotspotConfiguration(wifiConfiguration.getSSID(), wifiConfiguration.getPreSharedKey(), wifiConfiguration.getSecurityType());
    }

    public int addNetwork(WifiConfiguration wifiConfiguration) throws Exception {
        return addNetwork(wifiConfiguration.getSSID(), wifiConfiguration.getPreSharedKey(), wifiConfiguration.getSecurityType());
    }

    public State getWifiHotspotState() throws Exception {
        State state = State.getState(Integer.parseInt(shell.executeBroadcast(GET_WIFI_AP_STATE)));
        logger.debug("get hotspot state [" + state + "]");
        return state;
    }

    public boolean isWifiHotspotEnabled() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_WIFI_AP_ENABLED));
        logger.debug("is hotspot enabled [" + result + "]");
        return result;
    }

    public boolean startHotspotTethering() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(START_TETHERING));
        logger.debug("start hotspot [" + result + "]");
        return result;
    }

    public void stopHotspotTethering() throws Exception {
        shell.executeBroadcast(STOP_TETHERING);
        logger.debug("stop hotspot");
    }

    public int addNetwork(String ssid, String pass, WifiConfiguration.SecurityType securityType) throws Exception {
        int result = Integer.parseInt(shell.executeBroadcast(ADD_NETWORK + ssid + "," + pass + "," + securityType.getConfig()));
        logger.debug("add network ssid [" + ssid + "] pass [" + pass + "] config [" + securityType + "] [" + result + "]");
        return result;
    }

    public boolean reconnect() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(RECONNECT));
        logger.debug("reconnect [" + result + "]");
        return result;
    }

    public boolean reassociate() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(REASSOCIATE));
        logger.debug("reassociate [" + result + "]");
        return result;
    }

    public boolean startScan() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(START_SCAN));
        logger.debug("start scan [" + result + "]");
        return result;
    }

    public List<ScanResult> getScanResults() throws Exception {
        String result = shell.executeBroadcast(GET_SCAN_RESULT);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<ScanResult> scanResults = Arrays.asList(mapper.readValue(result, ScanResult[].class));
        logger.debug("get scan results");
        return scanResults;
    }

    public boolean isNetworkConnected() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_CONNECTED));
        logger.debug("is network connected [" + result + "]");
        return result;
    }

    public boolean isScanAlwaysAvailable() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_SCAN_ALWAYS_AVAILABLE));
        logger.debug("is scan always available [" + result + "]");
        return result;
    }

    public boolean setScanAlwaysAvailable(boolean state) throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_SCAN_ALWAYS_AVAILABLE + state));
        logger.debug("set scan always available [" + state + "] [" + result + "]");
        return result;
    }

    public boolean getScanAlwaysAvailable() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(GET_SCAN_ALWAYS_AVAILABLE));
        logger.debug("get scan always available  [" + result + "]");
        return result;
    }
}
