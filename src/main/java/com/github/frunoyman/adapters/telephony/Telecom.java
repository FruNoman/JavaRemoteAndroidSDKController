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


    public Telecom(Shell shell) {
        super(shell);
        logger = Logger.getLogger(Telecom.class.getName() + "] [" + shell.getSerial());
    }

    public enum CallState {
        CALL_STATE_IDLE(0),
        /**
         * Device call state: Ringing. A new call arrived and is
         * ringing or waiting. In the latter case, another call is
         * already active.
         */
        CALL_STATE_RINGING(1),
        /**
         * Device call state: Off-hook. At least one call exists
         * that is dialing, active, or on hold, and no calls are ringing
         * or waiting.
         */
        CALL_STATE_OFFHOOK(2);

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
            return CallState.CALL_STATE_IDLE;
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
        return contacts;
    }


}
