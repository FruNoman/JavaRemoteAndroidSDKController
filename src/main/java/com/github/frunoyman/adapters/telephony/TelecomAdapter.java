package com.github.frunoyman.adapters.telephony;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelecomAdapter extends BaseAdapter {
    public static final String TELEPHONY_REMOTE = "com.github.remotesdk.TELEPHONY_REMOTE";
    private Logger logger;

    private final String TELEPHONY_BROADCAST = BROADCAST + TELEPHONY_REMOTE;

    private final String GET_CALL_STATE = "getCallState";
    private final String GET_CALL_HISTORY = "getCallHistory";
    private final String GET_CONTACTS = "getContacts";
    private final String CALL = "callNumber";
    private final String END_CALL = "endCallProgrammatically";
    private final String ANSWER_RINGING_CALL = "answerRingingCall";
    private final String GET_INCOMING_CALL_NUMBER = "getIncomingCallNumber";
    private final String SET_DATA_ENABLED = "setDataEnabled";
    private final String IS_DATA_ENABLED = "isDataEnabled";
    private final String GET_DATA_STATE = "getDataState";
    private final String GET_DATA_NETWORK_TYPE = "getDataNetworkType";
    private final String GET_PHONE_TYPE = "getPhoneType";
    private final String GET_SIM_STATE = "getSimState";
    private final String GET_NETWORK_OPERATOR_NAME = "getNetworkOperatorName";
    private final String SEND_USSD_REQUEST = "sendUssdRequest";
    private final String GET_USSD_RESPONSE = "getUssdResponse";
    private final String GET_MOBILE_PHONE = "getMobilePhone";
    private final String GET_CONTACTS_SIZE = "getContactsSize";
    private final String GET_CALL_HISTORY_SIZE = "getCallHistorySize";
    private final String CONTACT_GENERATOR = "contactGeneratorProgram";



    private final String GET_CONTACT_IMAGE = "getContactImage";
    private final String CREATE_CONTACT = "createContactProgrammatically";


    public TelecomAdapter(Shell shell) {
        super(shell);
        logger = Logger.getLogger(TelecomAdapter.class.getName() + "] [" + shell.getSerial());
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

    public List<CallHistory> getCallHistory() {
        List<CallHistory> callHistories = new ArrayList<>();
        String result = shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_CALL_HISTORY);
        ObjectMapper mapper = new ObjectMapper();
        try {
            callHistories = Arrays.asList(mapper.readValue(result, CallHistory[].class));
        } catch (Exception e) {
            return new ArrayList<>();
        }
        logger.debug("get call history");
        return callHistories;
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        String result = shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_CONTACTS);
        ObjectMapper mapper = new ObjectMapper();
        try {
            contacts = Arrays.asList(mapper.readValue(result, Contact[].class));
        } catch (Exception e) {
            return new ArrayList<>();
        }
        logger.debug("get contacts");
        return contacts;
    }

    public String sendUssdRequest(String ussd) {
        shell.executeBroadcastExtended(TELEPHONY_BROADCAST, SEND_USSD_REQUEST, ussd);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = getUssdResponce();
        logger.debug("send USSD request [" + ussd + "] return [" + result + "]");
        return result;
    }

    public String getMobilePhone() {
        String result = shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_MOBILE_PHONE);
        logger.debug("get mobile phone return [" + result + "]");
        return result;
    }

    private String getUssdResponce() {
        String result = shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_USSD_RESPONSE);
        return result;
    }

    public void call(String number) {
        shell.executeBroadcastExtended(TELEPHONY_BROADCAST,CALL , number);
        logger.debug("call number [" + number + "]");
    }

    public void endCall() {
        shell.executeBroadcastExtended(TELEPHONY_BROADCAST, END_CALL);
        logger.debug("end call");
    }

    public void answerIncomingCall() {
        shell.executeBroadcastExtended(TELEPHONY_BROADCAST, ANSWER_RINGING_CALL);
        logger.debug("answer incoming call");
    }

    public String getIncomingCallNumber() {
        String result = shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_INCOMING_CALL_NUMBER);
        logger.debug("get incoming call number return [" + result + "]");
        return result;
    }

    public String getNetworkOperatorName() {
        String result = shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_NETWORK_OPERATOR_NAME);
        logger.debug("get network operator name return [" + result + "]");
        return result;
    }

    public boolean isDataEnabled() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(TELEPHONY_BROADCAST, IS_DATA_ENABLED));
        logger.debug("is data enabled return [" + result + "]");
        return result;
    }

    public void setDataEnabled(boolean state) {
        shell.executeBroadcastExtended(TELEPHONY_BROADCAST, SET_DATA_ENABLED, state);
        logger.debug("set data enabled [" + state + "]");
    }

    public DataState getDataState() {
        DataState result = DataState.getDataState(Integer.parseInt(shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_DATA_STATE)));
        logger.debug("get data state return [" + result.name() + "]");
        return result;
    }

    public DataNetworkType getDataNetworkType() {
        DataNetworkType result = DataNetworkType.getNetworkType(Integer.parseInt(shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_DATA_NETWORK_TYPE)));
        logger.debug("get data network type return [" + result.name() + "]");
        return result;
    }

    public PhoneType getPhoneType() {
        PhoneType result = PhoneType.getPhoneType(Integer.parseInt(shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_PHONE_TYPE)));
        logger.debug("get phone type return [" + result.name() + "]");
        return result;
    }

    public SimState getSimState() {
        SimState result = SimState.getSimState(Integer.parseInt(shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_SIM_STATE)));
        logger.debug("get sim state return [" + result.name() + "]");
        return result;
    }

    public CallState getCallState() {
        CallState result = CallState.getCallState(Integer.parseInt(shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_CALL_STATE)));
        logger.debug("get call state return [" + result.name() + "]");
        return result;
    }



    public int getContactsSize() {
        int result = Integer.parseInt(shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_CONTACTS_SIZE));
        logger.debug("get contacts size return [" + result + "]");
        return result;
    }

    public int getCallHistorySize() {
        int result = Integer.parseInt(shell.executeBroadcastExtended(TELEPHONY_BROADCAST, GET_CALL_HISTORY_SIZE));
        logger.debug("get call history size return [" + result + "]");
        return result;
    }

    public void generateContacts(int number) {
        shell.executeBroadcastExtended(TELEPHONY_BROADCAST, CONTACT_GENERATOR, number);
        logger.debug("generate contacts [" + number + "]");
    }




}
