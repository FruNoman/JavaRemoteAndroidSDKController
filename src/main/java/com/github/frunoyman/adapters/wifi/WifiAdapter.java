package com.github.frunoyman.adapters.wifi;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WifiAdapter extends BaseAdapter {
    private final String WIFI_REMOTE = "com.github.remotesdk.WIFI_REMOTE";
    private Logger logger;

    private final String WIFI_BROADCAST = BROADCAST + WIFI_REMOTE;

    private final String ENABLE = "enable";
    private final String DISABLE = "disable";
    private final String GET_STATE = "getState";
    private final String IS_ENABLED = "isEnabled";
    private final String ADD_NETWORK = "addNetwork";
    private final String ENABLE_NETWORK = "enableNetwork";
    private final String DISABLE_NETWORK = "disableNetwork";
    private final String REMOVE_NETWORK = "removeNetwork";
    private final String GET_CONFIGURE_NETWORKS = "getConfiguredNetworks";
    private final String DISCONNECT = "disconnect";
    private final String GET_WIFI_AP_CONFIGURATION = "getWifiApConfiguration";
    private final String GET_WIFI_AP_STATE = "getWifiApState";
    private final String IS_WIFI_AP_ENABLED = "isWifiApEnabled";
    private final String START_TETHERING = "startTethering";
    private final String STOP_TETHERING = "stopTethering";
    private final String SET_WIFI_AP_CONFIGURATION = "setWifiApConfiguration";
    private final String RECONNECT = "reconnect";
    private final String REASSOCIATE = "reassociate";
    private final String START_SCAN = "startScan";
    private final String GET_SCAN_RESULT = "getScanResults";
    private final String IS_CONNECTED = "isWifiNetworkConnected";
    private final String IS_SCAN_ALWAYS_AVAILABLE = "isScanAlwaysAvailable";
    private final String SET_SCAN_ALWAYS_AVAILABLE = "setScanAlwaysAvailable";
    private final String GET_IP_ADDRESS = "getIpAddress";


    public WifiAdapter(Shell shell) {
        super(shell);
        logger = Logger.getLogger(WifiAdapter.class.getName() + "] [" + shell.getSerial());
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


    public boolean enable() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, ENABLE));
        logger.debug("enable");
        return result;
    }

    public boolean disable() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, DISABLE));
        logger.debug("disable");
        return result;
    }

    public State getState() {
        State state = State.getState(Integer.parseInt(shell.executeBroadcastExtended(WIFI_BROADCAST, GET_STATE)));
        logger.debug("get state [" + state + "]");
        return state;
    }

    public boolean isEnabled() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, IS_ENABLED));
        logger.debug("is enabled [" + result + "]");
        return result;
    }

    public boolean enableNetwork(int netId) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, ENABLE_NETWORK, netId));
        logger.debug("enable network id [" + netId + "] [" + result + "]");
        return result;
    }

    public boolean disableNetwork(int netId) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, DISABLE_NETWORK, netId));
        logger.debug("disable network id [" + netId + "] [" + result + "]");
        return result;
    }

    public boolean disconnect() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, DISCONNECT));
        logger.debug("disconnect");
        return result;
    }

    public boolean removeNetwork(int netId) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, REMOVE_NETWORK, netId));
        logger.debug("remove network id [" + netId + "] [" + result + "]");
        return result;
    }

    public void removeAllNetworks() {
        for (WifiConfiguration configuration : getConfiguredNetworks()) {
            removeNetwork(configuration.getNetworkId());
        }
    }

    public List<WifiConfiguration> getConfiguredNetworks() {
        String result = shell.executeBroadcastExtended(WIFI_BROADCAST, GET_CONFIGURE_NETWORKS);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
        List<WifiConfiguration> wifiConfigurations = null;
        try {
            wifiConfigurations = Arrays.asList(mapper.readValue(result, WifiConfiguration[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("get configured networks");
        return wifiConfigurations;
    }

    public WifiConfiguration getWifiHotspotConfiguration() {
        String result = shell.executeBroadcastExtended(WIFI_BROADCAST, GET_WIFI_AP_CONFIGURATION);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        WifiConfiguration wifiConfiguration = null;
        try {
            wifiConfiguration = mapper.readValue(result, WifiConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("get hotspot configured network");
        return wifiConfiguration;
    }

    public boolean setWifiHotspotConfiguration(String ssid, String pass, WifiConfiguration.SecurityType securityType) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, SET_WIFI_AP_CONFIGURATION, ssid, pass, securityType.getConfig()));
        logger.debug("set hotspot configuration [" + ssid + "] pass [" + pass + "] config [" + securityType + "] [" + result + "]");
        return result;
    }

    public boolean setWifiHotspotConfiguration(WifiConfiguration wifiConfiguration) {
        return setWifiHotspotConfiguration(wifiConfiguration.getSSID(), wifiConfiguration.getPreSharedKey(), wifiConfiguration.getSecurityType());
    }

    public int addNetwork(WifiConfiguration wifiConfiguration) {
        return addNetwork(wifiConfiguration.getSSID(), wifiConfiguration.getPreSharedKey(), wifiConfiguration.getSecurityType());
    }

    public State getWifiHotspotState() {
        State state = State.getState(Integer.parseInt(shell.executeBroadcastExtended(WIFI_BROADCAST, GET_WIFI_AP_STATE)));
        logger.debug("get hotspot state [" + state + "]");
        return state;
    }

    public boolean isWifiHotspotEnabled() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, IS_WIFI_AP_ENABLED));
        logger.debug("is hotspot enabled [" + result + "]");
        return result;
    }

    public boolean startHotspotTethering() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, START_TETHERING));
        logger.debug("start hotspot [" + result + "]");
        return result;
    }

    public void stopHotspotTethering() {
        shell.executeBroadcastExtended(WIFI_BROADCAST, STOP_TETHERING);
        logger.debug("stop hotspot");
    }

    public int addNetwork(String ssid, String pass, WifiConfiguration.SecurityType securityType) {
        int result = Integer.parseInt(shell.executeBroadcastExtended(WIFI_BROADCAST, ADD_NETWORK , ssid , pass , securityType.getConfig()));
        logger.debug("add network ssid [" + ssid + "] pass [" + pass + "] config [" + securityType + "] [" + result + "]");
        return result;
    }

    public boolean reconnect() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, RECONNECT));
        logger.debug("reconnect [" + result + "]");
        return result;
    }

    public boolean reassociate() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, REASSOCIATE));
        logger.debug("reassociate [" + result + "]");
        return result;
    }

    public boolean startScan() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, START_SCAN));
        logger.debug("start scan [" + result + "]");
        return result;
    }

    public List<ScanResult> getScanResults() {
        String result = shell.executeBroadcastExtended(WIFI_BROADCAST, GET_SCAN_RESULT);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<ScanResult> scanResults = null;
        try {
            scanResults = Arrays.asList(mapper.readValue(result, ScanResult[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("get scan results");
        return scanResults;
    }

    public boolean isNetworkConnected() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, IS_CONNECTED));
        logger.debug("is network connected [" + result + "]");
        return result;
    }

    public boolean isScanAlwaysAvailable() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, IS_SCAN_ALWAYS_AVAILABLE));
        logger.debug("is scan always available [" + result + "]");
        return result;
    }

    public boolean setScanAlwaysAvailable(boolean state) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(WIFI_BROADCAST, SET_SCAN_ALWAYS_AVAILABLE , state));
        logger.debug("set scan always available [" + state + "] [" + result + "]");
        return result;
    }

}
