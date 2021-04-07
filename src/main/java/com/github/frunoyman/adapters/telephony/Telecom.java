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

    private final String CALL = AM_COMMAND
            + "callNumber,";
    private final String END_CALL = AM_COMMAND
            + "endCallProgrammatically";
    private final String ANSWER_RINGING_CALL = AM_COMMAND
            + "answerRingingCall";
    private final String GET_INCOMING_CALL_NUMBER = AM_COMMAND
            + "getIncomingCallNumber";
    private final String SET_DATA_ENABLED = AM_COMMAND
            + "setDataEnabled";
    private final String IS_NETWORK_ROAMING = AM_COMMAND
            + "isNetworkRoaming";
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
    private final String GET_USSD_RESPONSE = AM_COMMAND + "getUssdResponse";


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

    private void call(String number) throws Exception {
        shell.executeBroadcast(CALL+number);
        logger.debug("call number [" + number + "]");
    }

    private void endCall() throws Exception {
        shell.executeBroadcast(END_CALL);
        logger.debug("end call");
    }

    private void answerIncomingCall() throws Exception {
        shell.executeBroadcast(ANSWER_RINGING_CALL);
        logger.debug("answer incoming call");
    }

    private String getIncomingCallNumber() throws Exception {
        String result = shell.executeBroadcast(GET_INCOMING_CALL_NUMBER);
        logger.debug("get incoming call number ["+result+"]");
        return result;
    }


}
