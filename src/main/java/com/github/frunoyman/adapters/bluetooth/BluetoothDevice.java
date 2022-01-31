package com.github.frunoyman.adapters.bluetooth;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.controllers.BaseSdk;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

public class BluetoothDevice extends BaseAdapter {
    private Logger logger;

    private final String BLUETOOTH_COMMAND = "bluetooth_command ";
    private final String BLUETOOTH_REMOTE = "com.github.remotesdk.BLUETOOTH_REMOTE";
    private final String AM_COMMAND = BROADCAST
            + BLUETOOTH_REMOTE
            + ES
            + BLUETOOTH_COMMAND;

    private final String GET_PAIR_STATE = AM_COMMAND
            + "getPairState,";
    private final String GET_DEVICE_NAME = AM_COMMAND
            + "getDeviceName,";
    private final String GET_DEVICE_TYPE = AM_COMMAND
            + "getDeviceType,";
    private final String GET_DEVICE_CLASS = AM_COMMAND
            + "getDeviceClass,";
    private final String SET_PAIRING_CONFIRMATION = AM_COMMAND
            + "setPairingConfirmation,";
    private final String SET_DEVICE_PIN = AM_COMMAND
            + "setDevicePin,";
    private final String DEVICE_CANCEL_PAIRING = AM_COMMAND
            + "deviceCancelPairing,";
    private final String GET_MESSAGE_ACCESS_PERMISSION = AM_COMMAND
            + "getMessageAccessPermission,";
    private final String GET_SIM_ACCESS_PERMISSION = AM_COMMAND
            + "getSimAccessPermission,";
    private final String GET_PHONE_BOOK_ACCESS_PERMISSION = AM_COMMAND
            + "getPhonebookAccessPermission,";

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
        PairState pairState = PairState.getPairState(Integer.parseInt(shell.executeBroadcast(GET_PAIR_STATE + address)));
        logger.debug("get device [" + address + "] pair state [" + pairState + "]");
        return pairState;
    }

    public String getName() {
        String result = shell.executeBroadcast(GET_DEVICE_NAME + address);
        logger.debug("get device [" + address + "] name [" + result + "]");
        return result;
    }

    public Type getType() {
        Type type = Type.getType(Integer.parseInt(shell.executeBroadcast(GET_DEVICE_TYPE + address)));
        logger.debug("get device [" + address + "] type [" + type + "]");
        return type;
    }

    public BluetoothClass getBluetoothClass() {
        BluetoothClass bluetoothClass = new BluetoothClass(Integer.parseInt(shell.executeBroadcast(GET_DEVICE_CLASS + address)));
        logger.debug("get device [" + address + "] class [" + bluetoothClass + "]");
        return bluetoothClass;
    }

    public boolean setPairingConfirmation(String address, boolean confirmation) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_PAIRING_CONFIRMATION + address + "," + confirmation));
        logger.debug("set pairing confirmation [" + address + "] confirmation [" + confirmation + "]");
        return result;
    }

    public boolean setPin(String pin) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_DEVICE_PIN + address + "," + pin));
        logger.debug("set device [" + address + "] pin [" + pin + "]");
        return result;
    }

    public boolean cancelPairing() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(DEVICE_CANCEL_PAIRING + address));
        logger.debug("device [" + address + "] cancel pairing ["+result+"]");
        return result;
    }

    public Access getMessageAccessPermission() {
        Access access =Access.getAccess(Integer.valueOf(shell.executeBroadcast(GET_MESSAGE_ACCESS_PERMISSION + address)));
        logger.debug("device [" + address + "] get message access permission ["+access+"]");
        return access;
    }

    public Access getSimAccessPermission() {
        Access access =Access.getAccess(Integer.valueOf(shell.executeBroadcast(GET_SIM_ACCESS_PERMISSION + address)));
        logger.debug("device [" + address + "] get sim access permission ["+access+"]");
        return access;
    }

    public Access getPhoneBookAccessPermission() {
        Access access =Access.getAccess(Integer.valueOf(shell.executeBroadcast(GET_PHONE_BOOK_ACCESS_PERMISSION + address)));
        logger.debug("device [" + address + "] get phone book access permission ["+access+"]");
        return access;
    }

    @Override
    public String toString() {
        return address;
    }
}
