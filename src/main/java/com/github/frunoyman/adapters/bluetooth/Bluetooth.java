package com.github.frunoyman.adapters.bluetooth;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
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
            + "discoverable,";
    private final String SET_NAME = AM_COMMAND
            + "setName,";
    private final String GET_NAME = AM_COMMAND
            + "getName";
    private final String START_DISCOVERY = AM_COMMAND
            + "startDiscovery";
    private final String CANCEL_DISCOVERY = AM_COMMAND
            + "cancelDiscovery";
    private final String PAIR = AM_COMMAND
            + "pairDevice,";
    private final String GET_DISCOVERED_DEVICES = AM_COMMAND
            + "getDiscoveredDevices";
    private final String SET_PAIRING_CONFIRMATION = AM_COMMAND
            + "setPairingConfirmation,";

    private final String GET_SCAN_MODE = AM_COMMAND
            + "getScanMode";
    private final String GET_BONDED_DEVICES = AM_COMMAND
            + "getBondedDevices";
    private final String GET_REMOTE_DEVICE = AM_COMMAND
            + "getRemoteDevice,";
    private final String IS_ENABLED = AM_COMMAND
            + "isEnabled";
    private final String FACTORY_RESET = AM_COMMAND
            + "factoryReset";
    private final String GET_BLUETOOTH_CLASS = AM_COMMAND
            + "getBluetoothClass";
    private final String SET_SCAN_MODE = AM_COMMAND
            + "setScanMode,";
    private final String GET_DISCOVERABLE_TIMEOUT = AM_COMMAND
            + "getDiscoverableTimeout";
    private final String SET_DISCOVERABLE_TIMEOUT = AM_COMMAND
            + "setDiscoverableTimeout,";
    private final String IS_DISCOVERING = AM_COMMAND
            + "isDiscovering";
    private final String GET_SUPPORTED_PROFILES = AM_COMMAND
            + "getSupportedProfiles";
    private final String GET_CONNECTION_STATE = AM_COMMAND
            + "getConnectionState";
    private final String GET_PROFILE_CONNECTION_STATE = AM_COMMAND
            + "getProfileConnectionState";


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

    public enum ScanMode {
        SCAN_MODE_NONE(20),

        SCAN_MODE_CONNECTABLE(21),

        SCAN_MODE_CONNECTABLE_DISCOVERABLE(23);

        private int scanMode;

        ScanMode(int scanMode) {
            this.scanMode = scanMode;
        }

        public int getScanMode() {
            return scanMode;
        }

        public static ScanMode getScanMode(int scanMode) {
            for (ScanMode mode : values()) {
                if (mode.getScanMode() == scanMode) {
                    return mode;
                }
            }
            return ScanMode.SCAN_MODE_NONE;
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
        logger.debug("bluetooth state [" + state.name() + "]");
        return state;
    }

    public String getAddress() throws Exception {
        String address = "";
        String output = shell.execute(GET_ADDRESS);
        Pattern r = Pattern.compile(BLUETOOTH_ADDRESS_PATTERN);
        Matcher m = r.matcher(output);
        if (m.matches()) {
            address = m.group(1);
        }
        logger.debug("bluetooth address [" + address + "]");
        return address;
    }

    public void discoverable(int seconds) throws Exception {
        shell.executeBroadcast(DISCOVERABLE + seconds);
        logger.debug("bluetooth discoverable time [" + seconds + "]");
    }

    public void discoverable() throws Exception {
        discoverable(120);
    }

    public String getName() throws Exception {
        String name = shell.executeBroadcast(GET_NAME);
        logger.debug("bluetooth get name [" + name + "]");
        return name;
    }

    public boolean setName(String name) throws Exception {
        boolean success = Boolean.parseBoolean(shell.executeBroadcast(SET_NAME + "'" + name + "'"));
        logger.debug("bluetooth set name [" + name + "] return [" + success + "]");
        return success;
    }

    public boolean startDiscovery() throws Exception {
        boolean success = Boolean.parseBoolean(shell.executeBroadcast(START_DISCOVERY));
        logger.debug("bluetooth start discovery");
        return success;
    }

    public boolean cancelDiscovery() throws Exception {
        boolean success = Boolean.parseBoolean(shell.executeBroadcast(CANCEL_DISCOVERY));
        logger.debug("bluetooth cancel discovery");
        return success;
    }

    public boolean pair(String address) throws Exception {
        boolean success = Boolean.parseBoolean(shell.executeBroadcast(PAIR + address));
        logger.debug("bluetooth pair device [" + address + "] return [" + success + "]");
        return success;
    }

    public boolean setPairingConfirmation(String address, boolean confirmation) throws Exception {
        boolean success = Boolean.parseBoolean(shell.executeBroadcast(SET_PAIRING_CONFIRMATION + address + "," + confirmation));
        logger.debug("bluetooth set pairing confirmation for device [" + address + "] return [" + success + "]");
        return success;
    }

    public List<BluetoothDevice> getDiscoveredBluetoothDevices() throws Exception {
        List<BluetoothDevice> devices = new ArrayList<>();
        String result = shell.executeBroadcast(
                GET_DISCOVERED_DEVICES
        );
        if (!result.isEmpty()) {
            for (String address : result.split(",")) {
                devices.add(new BluetoothDevice(shell, address));
            }
        }
        return devices;
    }

    public ScanMode getScanMode() throws Exception {
        ScanMode scanMode = ScanMode.getScanMode(Integer.parseInt(shell.executeBroadcast(GET_SCAN_MODE)));
        logger.debug("bluetooth scan mode [" + scanMode.name() + "]");
        return scanMode;
    }


    public List<BluetoothDevice> getPairedDevices() throws Exception {
        List<BluetoothDevice> devices = new ArrayList<>();
        String result = shell.executeBroadcast(
                GET_BONDED_DEVICES
        );
        if (!result.isEmpty()) {
            for (String address : result.split(",")) {
                devices.add(new BluetoothDevice(shell, address));
            }
        }
        return devices;
    }

    public BluetoothDevice getRemoteDevice(String address) throws Exception {
        BluetoothDevice device = new BluetoothDevice(shell, shell.executeBroadcast(GET_REMOTE_DEVICE + address));
        logger.debug("bluetooth get device [" + address + "]");
        return device;
    }

    public boolean isEnabled() throws Exception {
        boolean success = Boolean.parseBoolean(shell.executeBroadcast(IS_ENABLED));
        logger.debug("bluetooth is enabled");
        return success;
    }

    public boolean factoryReset() throws Exception {
        boolean success = Boolean.parseBoolean(shell.executeBroadcast(FACTORY_RESET));
        logger.debug("bluetooth factory reset");
        return success;
    }

    public BluetoothClass getBluetoothClass() throws Exception {
        int result = Integer.parseInt(shell.executeBroadcast(GET_BLUETOOTH_CLASS));
        logger.debug("bluetooth get class");
        return new BluetoothClass(result);
    }

    public boolean setScanMode(ScanMode scanMode, long duration) throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_SCAN_MODE + scanMode.getScanMode() + "," + duration));
        logger.debug("bluetooth set scan mode");
        return result;
    }

    public int getDiscoverableTimeout() throws Exception {
        int result = Integer.parseInt(shell.executeBroadcast(GET_DISCOVERABLE_TIMEOUT));
        logger.debug("bluetooth get discoverable timeout");
        return result;
    }

    public boolean setDiscoverableTimeout(int seconds) throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_DISCOVERABLE_TIMEOUT+seconds));
        logger.debug("bluetooth set discoverable timeout ["+seconds+"]");
        return result;
    }

    public boolean isDiscovering() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_DISCOVERING));
        logger.debug("bluetooth is discovering");
        return result;
    }

    public List<BluetoothProfile.Type> getSupportedProfiles() throws Exception {
        List<BluetoothProfile.Type> profiles = new ArrayList<>();
        String result = shell.executeBroadcast(
                GET_SUPPORTED_PROFILES
        );
        if (!result.isEmpty()) {
            for (String type : result.split(",")) {
                profiles.add(BluetoothProfile.Type.getConstant(Integer.parseInt(type)));
            }
        }
        return profiles;
    }
}
