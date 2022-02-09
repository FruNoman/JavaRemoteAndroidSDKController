package com.github.frunoyman.adapters.sms;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

public class SmsManager extends BaseAdapter {
    private final String SMS_REMOTE = "com.github.remotesdk.SMS_REMOTE";
    private final String SMS_BROADCAST = BROADCAST + SMS_REMOTE;
    private Logger logger;

    private final String SEND_SMS = "sendSMS";
    private final String IS_SMS_RECEIVED = "isSMSReceived";
    private final String GET_LAST_SMS_NUMBER = "getLastSMSNumber";
    private final String GET_LAST_SMS_TEXT = "getLastSMSText";
    private final String IS_MAP_PROFILE_MESSAGE_RECEIVED = "isMAPProfileMessageReceived";
    private final String GET_LAST_MAP_PROFILE_SMS_NUMBER = "getLastMAPProfileSMSNumber";
    private final String GET_LAST_MAP_PROFILE_SMS_TEXT = "getLastMAPProfileSMSText";
    private final String GET_LAST_MAP_PROFILE_SMS_URI = "getLastMAPProfileSMSURI";

    public SmsManager(Shell shell) {
        super(shell);
        logger = Logger.getLogger(SmsManager.class.getName() + "] [" + shell.getSerial());
    }

    public void sendSMS(String number, String text) {
        shell.executeBroadcastExtended(SMS_BROADCAST, SEND_SMS, number, text);
        logger.debug("send SMS to  [" + number + "] with text [" + text + "]");
    }

    public boolean isSMSReceived() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(SMS_BROADCAST, IS_SMS_RECEIVED));
        logger.debug("is SMS received return [" + result + "]");
        return result;
    }

    public String getLastSMSNumber() {
        String result = shell.executeBroadcastExtended(SMS_BROADCAST, GET_LAST_SMS_NUMBER);
        logger.debug("get last SMS number return [" + result + "]");
        return result;
    }

    public String getLastSMSText() {
        String result = shell.executeBroadcastExtended(SMS_BROADCAST, GET_LAST_SMS_TEXT);
        logger.debug("get last SMS text return [" + result + "]");
        return result;
    }

    public boolean isMAPProfileMessageReceived() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(SMS_BROADCAST, IS_MAP_PROFILE_MESSAGE_RECEIVED));
        logger.debug("is MAP profile message received return [" + result + "]");
        return result;
    }

    public String getLastMAPProfileSMSNumber() {
        String result = shell.executeBroadcastExtended(SMS_BROADCAST, GET_LAST_MAP_PROFILE_SMS_NUMBER);
        logger.debug("get last MAP profile SMS number return [" + result + "]");
        return result;
    }

    public String getLastMAPProfileSMSText() {
        String result = shell.executeBroadcastExtended(SMS_BROADCAST, GET_LAST_MAP_PROFILE_SMS_TEXT);
        logger.debug("get last MAP profile SMS text return [" + result + "]");
        return result;
    }
}
