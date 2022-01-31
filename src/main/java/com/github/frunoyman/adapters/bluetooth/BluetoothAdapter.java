package com.github.frunoyman.adapters.bluetooth;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BluetoothAdapter extends BaseAdapter {
    private final String BLUETOOTH_COMMAND = "bluetooth_command ";
    private final String BLUETOOTH_REMOTE = "com.github.remotesdk.BLUETOOTH_REMOTE";
    private final String BLUETOOTH_ADDRESS_PATTERN = "[\\s\\S]*address:[\\s\\S](.*)[\\s\\S]*";
    private Logger logger;


    private final String AM_COMMAND = BROADCAST
            + BLUETOOTH_REMOTE
            + ES
            + BLUETOOTH_COMMAND;

    private final String GET_ADDRESS = "dumpsys bluetooth_manager";
    private final String ENABLE = AM_COMMAND + "enable";
    private final String DISABLE = AM_COMMAND + "disable";
    private final String GET_STATE = AM_COMMAND + "getState";
    private final String DISCOVERABLE = AM_COMMAND + "discoverable,";
    private final String SET_NAME = AM_COMMAND + "setName,";
    private final String GET_NAME = AM_COMMAND + "getName";
    private final String START_DISCOVERY = AM_COMMAND + "startDiscovery";
    private final String CANCEL_DISCOVERY = AM_COMMAND + "cancelDiscovery";
    private final String PAIR = AM_COMMAND + "pairDevice,";
    private final String GET_DISCOVERED_DEVICES = AM_COMMAND + "getDiscoveredDevices";
    private final String SET_PAIRING_CONFIRMATION = AM_COMMAND + "setPairingConfirmation,";
    private final String GET_SCAN_MODE = AM_COMMAND + "getScanMode";
    private final String GET_BONDED_DEVICES = AM_COMMAND + "getBondedDevices";
    private final String GET_REMOTE_DEVICE = AM_COMMAND + "getRemoteDevice,";
    private final String IS_ENABLED = AM_COMMAND + "isEnabled";
    private final String FACTORY_RESET = AM_COMMAND + "factoryReset";
    private final String GET_BLUETOOTH_CLASS = AM_COMMAND + "getBluetoothClass";
    private final String SET_SCAN_MODE = AM_COMMAND + "setScanMode,";
    private final String GET_DISCOVERABLE_TIMEOUT = AM_COMMAND + "getDiscoverableTimeout";
    private final String SET_DISCOVERABLE_TIMEOUT = AM_COMMAND + "setDiscoverableTimeout,";
    private final String IS_DISCOVERING = AM_COMMAND + "isDiscovering";
    private final String GET_CONNECTION_STATE = AM_COMMAND + "getConnectionState";
    private final String GET_PROFILE_CONNECTION_STATE = AM_COMMAND + "getProfileConnectionState,";
    private final String REMOVE_PAIRED_DEVICE = AM_COMMAND + "removeBond,";
    private final String GET_PAIR_STATE = AM_COMMAND + "getPairState";
    private final String GET_DEVICE_NAME = AM_COMMAND + "getDeviceName,";
    private final String GET_DEVICE_TYPE = AM_COMMAND + "getDeviceType,";
    private final String GET_CONNECTED_PROFILES = AM_COMMAND + "getConnectedProfiles";
    private final String DEVICE_CANCEL_PAIRING = AM_COMMAND + "deviceCancelPairing,";

//    private final String GET_DEVICE_CLASS = AM_COMMAND + "getDeviceClass,";
//    private final String SET_DEVICE_PAIRING_CONFIRM = AM_COMMAND + "setDevicePairingConfirmation,";
//    private final String SET_DEVICE_PIN = AM_COMMAND + "setDevicePin,";
//    private final String GET_MESSAGE_ACCESS_PERMISSION = AM_COMMAND + "getMessageAccessPermission";
//    private final String GET_SIM_ACCESS_PERMISSION = AM_COMMAND + "getSimAccessPermission";
//    private final String GET_PHONE_BOOK_ACCESS_PERMISSION = AM_COMMAND + "getPhonebookAccessPermission";
//    private final String IS_SCAN_ALWAYS_AVAILABLE = AM_COMMAND + "isScanAlwaysAvailable";
//    private final String SET_SCAN_ALWAYS_AVAILABLE = AM_COMMAND + "setScanAlwaysAvailable";
//    private final String DISCONNECT_DEVICE_PROFILE = AM_COMMAND + "DisconnectDeviceProfile";
//    private final String CONNECT_DEVICE_PROFILE = AM_COMMAND + "ConnectDeviceProfile";


    public BluetoothAdapter(Shell shell) {
        super(shell);
        logger = Logger.getLogger(BluetoothAdapter.class.getName() + "] [" + shell.getSerial());
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

    public enum ConnectedState {
        STATE_DISCONNECTED(0),
        STATE_CONNECTING(1),
        STATE_CONNECTED(2),
        STATE_DISCONNECTING(3);

        private int state;

        ConnectedState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }

        public static ConnectedState getState(int state) {
            for (ConnectedState states : values()) {
                if (states.getState() == state) {
                    return states;
                }
            }
            return ConnectedState.STATE_DISCONNECTED;
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

    public void enable() {
        shell.executeBroadcast(ENABLE);
        logger.debug("enable");
    }

    public void disable() {
        shell.executeBroadcast(DISABLE);
        logger.debug("disable");
    }

    public State getState() {
        State state = State.getState(Integer.parseInt(shell.executeBroadcast(GET_STATE)));
        logger.debug("get state [" + state + "]");
        return state;
    }

    public String getAddress() {
        String address = "";
        String output = shell.execute(GET_ADDRESS);
        Pattern r = Pattern.compile(BLUETOOTH_ADDRESS_PATTERN);
        Matcher m = r.matcher(output);
        if (m.matches()) {
            address = m.group(1);
        }
        logger.debug("get address [" + address + "]");
        return address;
    }

    public void startDiscoverable(int seconds) {
        shell.executeBroadcast(DISCOVERABLE + seconds);
        logger.debug("start discoverable seconds [" + seconds + "]");
    }

    public void stopDiscoverable() {
        setScanMode(ScanMode.SCAN_MODE_NONE, 0);
        logger.debug("stop discoverable");
    }

    public boolean isDiscoverable() {
        return getScanMode() == ScanMode.SCAN_MODE_CONNECTABLE_DISCOVERABLE;
    }

    public String getName() {
        String name = shell.executeBroadcast(GET_NAME);
        logger.debug("get name [" + name + "]");
        return name;
    }

    public boolean setName(String name) {
        boolean success = Boolean.parseBoolean(shell.executeBroadcast(SET_NAME + "'" + name + "'"));
        logger.debug("set name [" + name + "]");
        return success;
    }

    public boolean startDiscovery() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(START_DISCOVERY));
        logger.debug("start discovery [" + result + "]");
        return result;
    }


    public boolean cancelDiscovery() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(CANCEL_DISCOVERY));
        logger.debug("cancel discovery [" + result + "]");
        return result;
    }

    public boolean pair(String address) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(PAIR + address));
        logger.debug("pair device [" + address + "]");
        return result;
    }

    public boolean pair(BluetoothDevice device) {
        return pair(device.getAddress());
    }


    public List<BluetoothDevice> getDiscoveredBluetoothDevices() {
        List<BluetoothDevice> devices = new ArrayList<>();
        String result = shell.executeBroadcast(
                GET_DISCOVERED_DEVICES
        );
        if (!result.isEmpty()) {
            for (String address : result.split(",")) {
                devices.add(new BluetoothDevice(shell, address));
            }
        }
        logger.debug("get discovered devices");
        return devices;
    }

    public ScanMode getScanMode() {
        ScanMode scanMode = ScanMode.getScanMode(Integer.parseInt(shell.executeBroadcast(GET_SCAN_MODE)));
        logger.debug("get scan mode [" + scanMode + "]");
        return scanMode;
    }


    public List<BluetoothDevice> getPairedDevices() {
        List<BluetoothDevice> devices = new ArrayList<>();
        String result = shell.executeBroadcast(
                GET_BONDED_DEVICES
        );
        if (!result.isEmpty()) {
            for (String address : result.split(",")) {
                devices.add(new BluetoothDevice(shell, address));
            }
        }
        logger.debug("get paired devices");
        return devices;
    }

    public BluetoothDevice getRemoteDevice(String address) {
        BluetoothDevice device = new BluetoothDevice(shell, shell.executeBroadcast(GET_REMOTE_DEVICE + address));
        logger.debug("get device [" + address + "]");
        return device;
    }

    public boolean isEnabled() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_ENABLED));
        logger.debug("is enabled [" + result + "]");
        return result;
    }

    public boolean factoryReset() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(FACTORY_RESET));
        logger.debug("factory reset [" + result + "]");
        return result;
    }

    public BluetoothClass getBluetoothClass() {
        int result = Integer.parseInt(shell.executeBroadcast(GET_BLUETOOTH_CLASS));
        BluetoothClass bluetoothClass = new BluetoothClass(result);
        logger.debug("get class [" + bluetoothClass + "]");
        return bluetoothClass;
    }

    public boolean setScanMode(ScanMode scanMode, long duration) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_SCAN_MODE + scanMode.getScanMode() + "," + duration));
        logger.debug("set scan mode [" + scanMode + "] duration [" + duration + "]");
        return result;
    }

    public int getDiscoverableTimeout() {
        int result = Integer.parseInt(shell.executeBroadcast(GET_DISCOVERABLE_TIMEOUT));
        logger.debug("get discoverable timeout [" + result + "]");
        return result;
    }

    public boolean setDiscoverableTimeout(int seconds) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_DISCOVERABLE_TIMEOUT + seconds));
        logger.debug("set discoverable timeout [" + seconds + "]");
        return result;
    }

    public boolean isDiscovering() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_DISCOVERING));
        logger.debug("is discovering [" + result + "]");
        return result;
    }


    public ConnectedState getConnectionState() {
        ConnectedState connectedState = ConnectedState.getState(Integer.parseInt(shell.executeBroadcast(GET_CONNECTION_STATE)));
        logger.debug("get connection state [" + connectedState + "]");
        return connectedState;
    }

    public BluetoothProfile.State getProfileConnectionState(BluetoothProfile.Type type) {
        BluetoothProfile.State state = BluetoothProfile.State.getState(
                Integer.parseInt(
                        shell.executeBroadcast(GET_PROFILE_CONNECTION_STATE + type.getComstant()
                        )
                )
        );
        logger.debug("get profile [" + type + "] connection state [" + state + "}");
        return state;
    }

    public boolean removePairedDevice(String address) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(REMOVE_PAIRED_DEVICE + address));
        logger.debug("remove paired device [" + address + "]");
        return result;
    }

    public boolean removePairedDevice(BluetoothDevice device) {
        return removePairedDevice(device.getAddress());
    }

    public void removeAllPairedDevices() {
        for (BluetoothDevice device : getPairedDevices()) {
            removePairedDevice(device);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public List<BluetoothProfile.Type> getConnectedProfiles() {
        String result = shell.executeBroadcast(GET_CONNECTED_PROFILES);
        List<BluetoothProfile.Type> profiles = new ArrayList<>();
        if (!result.isEmpty()) {
            for (String profile : result.split(",")) {
                profiles.add(BluetoothProfile.Type.getConstant(Integer.parseInt(profile)));
            }
        }
        logger.debug("get connected profiles");
        return profiles;
    }

    //    public BluetoothDevice.PairState getDevicePairState(String address) {
//        BluetoothDevice.PairState pairState = BluetoothDevice.PairState.getPairState(
//                Integer.parseInt(shell.executeBroadcast(GET_PAIR_STATE + address)));
//        logger.debug("get pair state [" + pairState + "]");
//        return pairState;
//    }

//    public String getDeviceName(String address) {
//        String result = shell.executeBroadcast(GET_DEVICE_NAME + address);
//        logger.debug("get device name [" + result + "]");
//        return result;
//    }

//    public BluetoothDevice.Type getDeviceType(String address) {
//        BluetoothDevice.Type type = BluetoothDevice.Type.getType(
//                Integer.parseInt(shell.executeBroadcast(GET_DEVICE_TYPE + address)));
//        logger.debug("get device type [" + type + "]");
//        return type;
//    }

//    public boolean setDevicePin(String address, String pin) {
//        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_DEVICE_PIN + address + "," + pin));
//        logger.debug("set device [" + address + "] pin [" + pin + "]");
//        return result;
//    }

//    public boolean cancelPairing(String address) {
//        boolean result = Boolean.parseBoolean(shell.executeBroadcast(DEVICE_CANCEL_PAIRING + address));
//        logger.debug("device cancel pairing [" + address + "]");
//        return result;
//    }

//    public boolean setPairingConfirmation(String address, boolean confirmation) {
//        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_PAIRING_CONFIRMATION + address + "," + confirmation));
//        logger.debug("set pairing confirmation [" + address + "] confirmation [" + confirmation + "]");
//        return result;
//    }
}
