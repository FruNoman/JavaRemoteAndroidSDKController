package com.github.frunoyman.adapters.bluetooth;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.controllers.BaseSdk;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

public class BluetoothDevice extends BaseAdapter {
    private final String BLUETOOTH_REMOTE = "com.github.remotesdk.BLUETOOTH_REMOTE";
    private Logger logger;

    private final String BLUETOOTH_BROADCAST = BROADCAST + BLUETOOTH_REMOTE;

    private final String GET_PAIR_STATE = "getPairState";
    private final String GET_DEVICE_NAME = "getDeviceName";
    private final String GET_DEVICE_TYPE = "getDeviceType";
    private final String GET_DEVICE_CLASS = "getDeviceClass";
    private final String SET_PAIRING_CONFIRMATION = "setPairingConfirmation";
    private final String SET_DEVICE_PIN = "setDevicePin";
    private final String DEVICE_CANCEL_PAIRING = "deviceCancelPairing";
    private final String GET_MESSAGE_ACCESS_PERMISSION = "getMessageAccessPermission";
    private final String GET_SIM_ACCESS_PERMISSION = "getSimAccessPermission";
    private final String GET_PHONE_BOOK_ACCESS_PERMISSION = "getPhonebookAccessPermission";

    private String address;

    public BluetoothDevice(Shell shell, String address) {
        super(shell);
        this.address = address;
        logger = Logger.getLogger(BluetoothDevice.class.getName() + "] [" + shell.getSerial());
    }

    public enum Type {
        DEVICE_TYPE_UNKNOWN(0),
        DEVICE_TYPE_CLASSIC(1),
        DEVICE_TYPE_LE(2),
        DEVICE_TYPE_DUAL(3);

        private int type;

        Type(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static Type getType(int id) {
            for (Type type : values()) {
                if (type.getType() == id) {
                    return type;
                }
            }
            return Type.DEVICE_TYPE_UNKNOWN;
        }
    }

    public enum Access {
        ACCESS_UNKNOWN(0),
        ACCESS_ALLOWED(1),
        ACCESS_REJECTED(2);

        Access(int state) {
            this.state = state;
        }

        private int state;

        public static Access getAccess(Integer id) {
            if (id == null) {
                return Access.ACCESS_UNKNOWN;
            }
            for (Access bondState : values()) {
                if (bondState.state == id) {
                    return bondState;
                }
            }
            return Access.ACCESS_UNKNOWN;
        }
    }

    public enum PairState {
        NONE(10),
        PAIRING(11),
        PAIRED(12);

        PairState(int state) {
            this.state = state;
        }

        private int state;

        public int getState() {
            return state;
        }

        public static PairState getPairState(Integer id) {
            if (id == null) {
                return PairState.NONE;
            }
            for (PairState bondState : values()) {
                if (bondState.state == id) {
                    return bondState;
                }
            }
            return PairState.NONE;
        }
    }

    public String getAddress() {
        return address;
    }

    public PairState getPairState() {
        PairState pairState = PairState.getPairState(Integer.parseInt(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_PAIR_STATE, address)));
        logger.debug("get device [" + address + "] pair state return [" + pairState + "]");
        return pairState;
    }

    public String getName() {
        String result = shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_DEVICE_NAME, address);
        logger.debug("get device [" + address + "] name return [" + result + "]");
        return result;
    }

    public Type getType() {
        Type type = Type.getType(Integer.parseInt(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_DEVICE_TYPE, address)));
        logger.debug("get device [" + address + "] type return [" + type + "]");
        return type;
    }

    public BluetoothClass getBluetoothClass() {
        BluetoothClass bluetoothClass = new BluetoothClass(Integer.parseInt(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_DEVICE_CLASS, address)));
        logger.debug("get device [" + address + "] class return [" + bluetoothClass + "]");
        return bluetoothClass;
    }

    public boolean setPairingConfirmation(String address, boolean confirmation) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, SET_PAIRING_CONFIRMATION, address, confirmation));
        logger.debug("set pairing confirmation [" + address + "] confirmation [" + confirmation + "] return [" + result + "]");
        return result;
    }

    public boolean setPin(String pin) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, SET_DEVICE_PIN, address, pin));
        logger.debug("set device [" + address + "] pin [" + pin + "] return [" + result + "]");
        return result;
    }

    public boolean cancelPairing() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, DEVICE_CANCEL_PAIRING, address));
        logger.debug("device [" + address + "] cancel pairing return [" + result + "]");
        return result;
    }

    public Access getMessageAccessPermission() {
        Access access = Access.getAccess(Integer.valueOf(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_MESSAGE_ACCESS_PERMISSION, address)));
        logger.debug("device [" + address + "] get message access permission return [" + access + "]");
        return access;
    }

    public Access getSimAccessPermission() {
        Access access = Access.getAccess(Integer.valueOf(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_SIM_ACCESS_PERMISSION, address)));
        logger.debug("device [" + address + "] get sim access permission return [" + access + "]");
        return access;
    }

    public Access getPhoneBookAccessPermission() {
        Access access = Access.getAccess(Integer.valueOf(shell.executeBroadcastExtended(BLUETOOTH_BROADCAST, GET_PHONE_BOOK_ACCESS_PERMISSION, address)));
        logger.debug("device [" + address + "] get phone book access permission return [" + access + "]");
        return access;
    }

    @Override
    public String toString() {
        return address;
    }
}
