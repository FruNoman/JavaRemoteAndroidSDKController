package com.github.frunoyman.adapters.telephony;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Telecom extends BaseAdapter {
    public static final String TELEPHONY_COMMAND = "telephony_remote ";
    public static final String TELEPHONY_REMOTE = "com.github.remotesdk.TELEPHONY_REMOTE";
    private Logger logger;

    private final String AM_COMMAND = BROADCAST
            + TELEPHONY_REMOTE
            + ES
            + TELEPHONY_COMMAND;

    private final String GET_CALL_STATE = AM_COMMAND
            + "getCallState";
    private final String GET_CALL_HISTORY = AM_COMMAND
            + "getCallHistory";
    private final String GET_CONTACTS = AM_COMMAND
            + "getContacts";
    private final String CALL = "am start -a android.intent.action.CALL -d tel:";
    private final String END_CALL = AM_COMMAND
            + "endCallProgrammatically";
    private final String ANSWER_RINGING_CALL = AM_COMMAND
            + "answerRingingCall";
    private final String GET_INCOMING_CALL_NUMBER = AM_COMMAND
            + "getIncomingCallNumber";
    private final String SET_DATA_ENABLED = AM_COMMAND
            + "setDataEnabled,";
    private final String IS_DATA_ENABLED = AM_COMMAND
            + "isDataEnabled";
    private final String GET_DATA_STATE = AM_COMMAND
            + "getDataState";
    private final String GET_DATA_NETWORK_TYPE = AM_COMMAND
            + "getDataNetworkType";
    private final String GET_PHONE_TYPE = AM_COMMAND
            + "getPhoneType";
    private final String GET_SIM_STATE = AM_COMMAND
            + "getSimState";
    private final String GET_NETWORK_OPERATOR_NAME = AM_COMMAND
            + "getNetworkOperatorName";
    private final String SEND_USSD_REQUEST = AM_COMMAND
            + "sendUssdRequest,";
    private final String GET_USSD_RESPONSE = AM_COMMAND
            + "getUssdResponse";


    public Telecom(Shell shell) {
        super(shell);
        logger = Logger.getLogger(Telecom.class.getName() + "] [" + shell.getSerial());
    }

    public enum CallState {
        NONE(0),
        INCOMING_CALL(1),
        IN_CALL(2);

        private int callState;

        CallState(int callState) {
            this.callState = callState;
        }

        public int getCallState() {
            return callState;
        }

        public static CallState getCallState(int state) {
            for (CallState states : values()) {
                if (states.getCallState() == state) {
                    return states;
                }
            }
            return CallState.NONE;
        }
    }

    public enum DataState {
        DATA_UNKNOWN(-1),
        DATA_DISCONNECTED(0),
        DATA_CONNECTING(1),
        DATA_CONNECTED(2),
        DATA_SUSPENDED(3),
        DATA_DISCONNECTING(4);

        private int dataState;

        DataState(int dataState) {
            this.dataState = dataState;
        }

        public int getDataState() {
            return dataState;
        }

        public static DataState getDataState(int state) {
            for (DataState states : values()) {
                if (states.getDataState() == state) {
                    return states;
                }
            }
            return DataState.DATA_UNKNOWN;
        }
    }

    public enum DataNetworkType {
        NETWORK_TYPE_UNKNOWN(0),
        NETWORK_TYPE_GPRS(1),
        NETWORK_TYPE_EDGE(2),
        NETWORK_TYPE_UMTS(3),
        NETWORK_TYPE_CDMA(4),
        NETWORK_TYPE_EVDO_0(5),
        NETWORK_TYPE_EVDO_A(6),
        NETWORK_TYPE_1xRTT(7),
        NETWORK_TYPE_HSDPA(8),
        NETWORK_TYPE_HSUPA(9),
        NETWORK_TYPE_HSPA(10),
        NETWORK_TYPE_IDEN(11),
        NETWORK_TYPE_EVDO_B(12),
        NETWORK_TYPE_LTE(13),
        NETWORK_TYPE_EHRPD(14),
        NETWORK_TYPE_HSPAP(15),
        NETWORK_TYPE_GSM(16),
        NETWORK_TYPE_TD_SCDMA(17),
        NETWORK_TYPE_IWLAN(18),
        NETWORK_TYPE_LTE_CA(19),
        NETWORK_TYPE_NR(20);

        private int networkType;

        DataNetworkType(int networkType) {
            this.networkType = networkType;
        }

        public int getNetworkType() {
            return networkType;
        }

        public static DataNetworkType getNetworkType(int state) {
            for (DataNetworkType states : values()) {
                if (states.getNetworkType() == state) {
                    return states;
                }
            }
            return DataNetworkType.NETWORK_TYPE_UNKNOWN;
        }
    }

    public enum PhoneType {
        PHONE_TYPE_NONE(0),
        PHONE_TYPE_GSM(1),
        PHONE_TYPE_CDMA(2),
        PHONE_TYPE_SIP(3),
        PHONE_TYPE_THIRD_PARTY(4),
        PHONE_TYPE_IMS(5),
        PHONE_CDMA_LTE(6);

        private int phoneType;

        PhoneType(int phoneType) {
            this.phoneType = phoneType;
        }

        public int getPhoneType() {
            return phoneType;
        }

        public static PhoneType getPhoneType(int state) {
            for (PhoneType states : values()) {
                if (states.getPhoneType() == state) {
                    return states;
                }
            }
            return PhoneType.PHONE_TYPE_NONE;
        }
    }

    public enum SimState {
        SIM_STATE_UNKNOWN(0),
        SIM_STATE_ABSENT(1),
        SIM_STATE_PIN_REQUIRED(2),
        SIM_STATE_PUK_REQUIRED(3),
        SIM_STATE_NETWORK_LOCKED(4),
        SIM_STATE_READY(5),
        SIM_STATE_NOT_READY(6),
        SIM_STATE_PERM_DISABLED(7),
        SIM_STATE_CARD_IO_ERROR(8),
        SIM_STATE_CARD_RESTRICTED(9),
        SIM_STATE_LOADED(10),
        SIM_STATE_PRESENT(11);


        private int simState;

        SimState(int simState) {
            this.simState = simState;
        }

        public int getSimState() {
            return simState;
        }

        public static SimState getSimState(int state) {
            for (SimState states : values()) {
                if (states.getSimState() == state) {
                    return states;
                }
            }
            return SimState.SIM_STATE_UNKNOWN;
        }
    }

    public List<CallHistory> getCallHistory() throws Exception {
        List<CallHistory> callHistories = new ArrayList<>();
        String result = shell.executeBroadcast(GET_CALL_HISTORY);
        ObjectMapper mapper = new ObjectMapper();
        try {
            callHistories = Arrays.asList(mapper.readValue(result, CallHistory[].class));
        } catch (Exception e) {
            return new ArrayList<>();
        }
        logger.debug("get call history");
        return callHistories;
    }

    public List<Contact> getContacts() throws Exception {
        List<Contact> contacts = new ArrayList<>();
        String result = shell.executeBroadcast(GET_CONTACTS);
        ObjectMapper mapper = new ObjectMapper();
        try {
            contacts = Arrays.asList(mapper.readValue(result, Contact[].class));
        } catch (Exception e) {
            return new ArrayList<>();
        }
        logger.debug("get contacts");
        return contacts;
    }

    public String sendUssdRequest(String ussd) throws Exception {
        shell.executeBroadcast(SEND_USSD_REQUEST + ussd);
        Thread.sleep(2000);
        String result = getUssdResponce();
        logger.debug("send USSD request [" + ussd + "] [" + result + "]");
        return result;
    }

    private String getUssdResponce() throws Exception {
        String result = shell.executeBroadcast(GET_USSD_RESPONSE);
        return result;
    }

    public void call(String number) throws Exception {
        shell.execute(CALL + number);
        logger.debug("call number [" + number + "]");
    }

    public void endCall() throws Exception {
        shell.executeBroadcast(END_CALL);
        logger.debug("end call");
    }

    public void answerIncomingCall() throws Exception {
        shell.executeBroadcast(ANSWER_RINGING_CALL);
        logger.debug("answer incoming call");
    }

    public String getIncomingCallNumber() throws Exception {
        String result = shell.executeBroadcast(GET_INCOMING_CALL_NUMBER);
        logger.debug("get incoming call number [" + result + "]");
        return result;
    }

    public String getNetworkOperatorName() throws Exception {
        String result = shell.executeBroadcast(GET_NETWORK_OPERATOR_NAME);
        logger.debug("get network operator name [" + result + "]");
        return result;
    }

    public boolean isDataEnabled() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_DATA_ENABLED));
        logger.debug("is data enabled [" + result + "]");
        return result;
    }

    public void setDataEnabled(boolean state) throws Exception {
        shell.executeBroadcast(SET_DATA_ENABLED + state);
        logger.debug("set data enabled [" + state + "]");
    }

    public DataState getDataState() throws Exception {
        DataState result = DataState.getDataState(Integer.parseInt(shell.executeBroadcast(GET_DATA_STATE)));
        logger.debug("get data state [" + result.name() + "]");
        return result;
    }

    public DataNetworkType getDataNetworkType() throws Exception {
        DataNetworkType result = DataNetworkType.getNetworkType(Integer.parseInt(shell.executeBroadcast(GET_DATA_NETWORK_TYPE)));
        logger.debug("get data network type [" + result.name() + "]");
        return result;
    }

    public PhoneType getPhoneType() throws Exception {
        PhoneType result = PhoneType.getPhoneType(Integer.parseInt(shell.executeBroadcast(GET_PHONE_TYPE)));
        logger.debug("get phone type [" + result.name() + "]");
        return result;
    }

    public SimState getSimState() throws Exception {
        SimState result = SimState.getSimState(Integer.parseInt(shell.executeBroadcast(GET_SIM_STATE)));
        logger.debug("get sim state [" + result.name() + "]");
        return result;
    }

    public CallState getCallState() throws Exception {
        CallState result = CallState.getCallState(Integer.parseInt(shell.executeBroadcast(GET_CALL_STATE)));
        logger.debug("get call state [" + result.name() + "]");
        return result;
    }


}
