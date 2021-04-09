package com.github.frunoyman.adapters.usb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Usb extends BaseAdapter {
    public static final String USB_COMMAND = "usb_remote ";
    public static final String USB_REMOTE = "com.github.remotesdk.USB_REMOTE";
    private Logger logger;


    private final String AM_COMMAND = BROADCAST
            + USB_REMOTE
            + ES
            + USB_COMMAND;

    private final String GET_DEVICE_LIST = AM_COMMAND
            + "getDeviceList";

    public Usb(Shell shell) {
        super(shell);
        logger = Logger.getLogger(Usb.class.getName() + "] [" + shell.getSerial());
    }

    public List<UsbDevice> getDeviceList() throws Exception {
        List<UsbDevice> devices = new ArrayList<>();
        String result = shell.executeBroadcast(GET_DEVICE_LIST);
        ObjectMapper mapper = new ObjectMapper();
        try {
            devices = Arrays.asList(mapper.readValue(result, UsbDevice[].class));
        }catch (Exception e){

        }
        return devices;
    }
}
