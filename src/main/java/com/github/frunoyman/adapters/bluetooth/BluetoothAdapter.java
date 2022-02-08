package com.github.frunoyman.adapters.bluetooth;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BluetoothAdapter extends BaseAdapter {
    private final String BLUETOOTH_REMOTE = "com.github.remotesdk.BLUETOOTH_REMOTE";
    private final String BLUETOOTH_ADDRESS_PATTERN = "[\\s\\S]*address:[\\s\\S](.*)[\\s\\S]*";
    private Logger logger;


    private final String BLUETOOTH_BROADCAST = BROADCAST + BLUETOOTH_REMOTE;

    private final String ERROR_TEST = "ERROR_TEST";
    private final String GET_ADDRESS = "dumpsys bluetooth_manager";
    private final String ENABLE = "enable";
    private final String DISABLE = "disable";
    private final String GET_STATE = "getState";
    private final String DISCOVERABLE = "discoverable";
    private final String SET_NAME = "setName";
    private final String GET_NAME = "getName";
    private final String START_DISCOVERY = "startDiscovery";
    private final String CANCEL_DISCOVERY = "cancelDiscovery";
    private final String PAIR = "pairDevice";
    private final String GET_DISCOVERED_DEVICES = "getDiscoveredDevices";
    private final String SET_PAIRING_CONFIRMATION = "setPairingConfirmation";
    private final String GET_SCAN_MODE = "getScanMode";
    private final String GET_BONDED_DEVICES = "getBondedDevices";
    private final String GET_REMOTE_DEVICE = "getRemoteDevice";
    private final String IS_ENABLED = "isEnabled";
    private final String FACTORY_RESET = "factoryReset";
    private final String GET_BLUETOOTH_CLASS = "getBluetoothClass";
    private final String SET_SCAN_MODE = "setScanMode";
    private final String GET_DISCOVERABLE_TIMEOUT = "getDiscoverableTimeout";
    private final String SET_DISCOVERABLE_TIMEOUT = "setDiscoverableTimeout";
    private final String IS_DISCOVERING = "isDiscovering";
    private final String GET_CONNECTION_STATE = "getConnectionState";
    private final String GET_PROFILE_CONNECTION_STATE = "getProfileConnectionState";
    private final String REMOVE_PAIRED_DEVICE = "removeBond";
    private final String GET_PAIR_STATE = "getPairState";
    private final String GET_DEVICE_NAME = "getDeviceName";
    private final String GET_DEVICE_TYPE = "getDeviceType";
    private final String GET_CONNECTED_PROFILES = "getConnectedProfiles";
    private final String DEVICE_CANCEL_PAIRING = "deviceCancelPairing";

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

    public void error_test() {
        shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, ERROR_TEST);
        logger.debug("error test");
    }

    public void enable() {
        shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, ENABLE);
        logger.debug("enable");
    }

    public void disable() {
        shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, DISABLE);
        logger.debug("disable");
    }

    public State getState() {
        State state = State.getState(Integer.parseInt(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_STATE)));
        logger.debug("get state return [" + state + "]");
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
        logger.debug("get address return [" + address + "]");
        return address;
    }

    public void startDiscoverable(int seconds) {
        shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, DISCOVERABLE, seconds);
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
        String result = shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_NAME);
        logger.debug("get name return [" + result + "]");
        return result;
    }

    public boolean setName(String name) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, SET_NAME, name));
        logger.debug("set name [" + name + "] return [" + result + "]");
        return result;
    }

    public boolean startDiscovery() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, START_DISCOVERY));
        logger.debug("start discovery return [" + result + "]");
        return result;
    }


    public boolean cancelDiscovery() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, CANCEL_DISCOVERY));
        logger.debug("cancel discovery return [" + result + "]");
        return result;
    }

    public boolean pair(String address) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, PAIR, address));
        logger.debug("pair device [" + address + "] return [" + result + "]");
        return result;
    }

    public boolean pair(BluetoothDevice device) {
        return pair(device.getAddress());
    }


    public List<BluetoothDevice> getDiscoveredBluetoothDevices() {
        List<BluetoothDevice> devices = new ArrayList<>();
        String result = shell.executeBroadcastExtended(BLUETOOTH_BROADCAST,
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
        ScanMode scanMode = ScanMode.getScanMode(Integer.parseInt(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_SCAN_MODE)));
        logger.debug("get scan mode return [" + scanMode + "]");
        return scanMode;
    }


    public List<BluetoothDevice> getPairedDevices() {
        List<BluetoothDevice> devices = new ArrayList<>();
        String result = shell.executeBroadcastExtended(BLUETOOTH_BROADCAST,
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
        BluetoothDevice device = new BluetoothDevice(shell, shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_REMOTE_DEVICE, address));
        logger.debug("get device [" + address + "]");
        return device;
    }

    public boolean isEnabled() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, IS_ENABLED));
        logger.debug("is enabled return [" + result + "]");
        return result;
    }

    public boolean factoryReset() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, FACTORY_RESET));
        logger.debug("factory reset return [" + result + "]");
        return result;
    }

    public BluetoothClass getBluetoothClass() {
        int result = Integer.parseInt(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_BLUETOOTH_CLASS));
        BluetoothClass bluetoothClass = new BluetoothClass(result);
        logger.debug("get class return [" + bluetoothClass + "]");
        return bluetoothClass;
    }

    public boolean setScanMode(ScanMode scanMode, long duration) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, SET_SCAN_MODE, scanMode.getScanMode(), duration));
        logger.debug("set scan mode [" + scanMode + "] duration [" + duration + "] return [" + result + "]");
        return result;
    }

    public int getDiscoverableTimeout() {
        int result = Integer.parseInt(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_DISCOVERABLE_TIMEOUT));
        logger.debug("get discoverable timeout return [" + result + "]");
        return result;
    }

    public boolean setDiscoverableTimeout(int seconds) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, SET_DISCOVERABLE_TIMEOUT, seconds));
        logger.debug("set discoverable timeout [" + seconds + "] return [" + result + "]");
        return result;
    }

    public boolean isDiscovering() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, IS_DISCOVERING));
        logger.debug("is discovering return [" + result + "]");
        return result;
    }


    public ConnectedState getConnectionState() {
        ConnectedState connectedState = ConnectedState.getState(Integer.parseInt(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_CONNECTION_STATE)));
        logger.debug("get connection state return [" + connectedState + "]");
        return connectedState;
    }

    public BluetoothProfile.State getProfileConnectionState(BluetoothProfile.Type type) {
        BluetoothProfile.State state = BluetoothProfile.State.getState(
                Integer.parseInt(
                        shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_PROFILE_CONNECTION_STATE, type.getComstant()
                        )
                )
        );
        logger.debug("get profile [" + type + "] connection state return [" + state + "}");
        return state;
    }

    public boolean removePairedDevice(String address) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, REMOVE_PAIRED_DEVICE, address));
        logger.debug("remove paired device [" + address + "] return [" + result + "]");
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
        String result = shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_CONNECTED_PROFILES);
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
