package com.github.frunoyman.adapters.bluetooth;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bluetooth extends BaseAdapter {
    private final String BLUETOOTH_COMMAND = "bluetooth_remote ";
    private final String BLUETOOTH_REMOTE = "com.github.remotesdk.BLUETOOTH_REMOTE";
    private final String BLUETOOTH_ADDRESS_PATTERN = "[\\s\\S]*address:[\\s\\S](.*)[\\s\\S]*";
    private Logger logger;


    private final String AM_COMMAND = BROADCAST
            + BLUETOOTH_REMOTE
            + ES
            + BLUETOOTH_COMMAND;

    private final String ENABLE = AM_COMMAND
            + "enable";
    private final String DISABLE = AM_COMMAND
            + "disable";
    private final String GET_STATE = AM_COMMAND
            + "getState";
    private final String GET_ADDRESS = "dumpsys bluetooth_manager";

    private final String DISCOVERABLE = AM_COMMAND
            +"discoverable,";


    public Bluetooth(Shell shell) {
        super(shell);
        logger = Logger.getLogger(Bluetooth.class.getName() + "] [" + shell.getSerial());
    }

    public enum State {
        STATE_OFF(10),
        STATE_TURNING_ON(11),
        STATE_ON(12),
        STATE_TURNING_OFF(13);

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
            return State.STATE_OFF;
        }
    }

    public void enable() throws Exception {
        shell.executeBroadcast(ENABLE);
        logger.debug("bluetooth enable");
    }

    public void disable() throws Exception {
        shell.executeBroadcast(DISABLE);
        logger.debug("bluetooth disable");
    }

    public State getState() throws Exception {
        State state = State.getState(Integer.parseInt(shell.executeBroadcast(GET_STATE)));
        logger.debug("bluetooth state ["+state.name()+"]");
        return state;
    }

    public String getAddress() throws Exception {
        String address = "";
        String output = shell.execute(GET_ADDRESS);
        Pattern r = Pattern.compile(BLUETOOTH_ADDRESS_PATTERN);
        Matcher m = r.matcher(output);
        if (m.matches()) {
            address= m.group(1);
        }
        logger.debug("bluetooth address ["+address+"]");
        return address;
    }

    public void discoverable(int seconds) throws Exception {
        shell.executeBroadcast(DISCOVERABLE+seconds);
        logger.debug("bluetooth discoverable time ["+seconds+"]");
    }

    public void discoverable() throws Exception {
        discoverable(120);
    }

    public int getScanMode() {
        return 0;
    }

    public String getName() {
        return "";
    }

    public void setName() {

    }



    public void startDiscovery() {

    }

    public void cancelDiscovery() {

    }

    public void pair(String address) {

    }

    public List<BluetoothDevice> getPairedDevices() {
        return null;
    }
}
