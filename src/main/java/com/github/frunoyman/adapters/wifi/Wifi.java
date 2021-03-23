package com.github.frunoyman.adapters.wifi;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

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
        logger.debug("get state ["+state+"]");
        return state;
    }
}
