package com.github.frunoyman.adapters.usb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Usb extends BaseAdapter {
    public static final String USB_REMOTE = "com.github.remotesdk.USB_REMOTE";
    private Logger logger;


    private final String USB_BROADCAST = BROADCAST + USB_REMOTE;


    private final String GET_DEVICE_LIST = "getDeviceList";
    private final String GET_INPUT_DEVICE_LIST = "getInputDeviceList";

    public Usb(Shell shell) {
        super(shell);
        logger = Logger.getLogger(Usb.class.getName() + "] [" + shell.getSerial());
    }

    public List<UsbDevice> getDeviceList(){
        List<UsbDevice> devices = new ArrayList<>();
        String result = shell.executeBroadcastExtended(USB_BROADCAST,GET_DEVICE_LIST);
        ObjectMapper mapper = new ObjectMapper();
        try {
            devices = Arrays.asList(mapper.readValue(result, UsbDevice[].class));
        } catch (Exception e) {

        }
        return devices;
    }

    public List<InputDevice> getInputDeviceList(){
        List<InputDevice> devices = new ArrayList<>();
        String result = shell.executeBroadcastExtended(USB_BROADCAST,GET_INPUT_DEVICE_LIST);
        ObjectMapper mapper = new ObjectMapper();
        try {
            devices = Arrays.asList(mapper.readValue(result, InputDevice[].class));
        } catch (Exception e) {

        }
        return devices;
    }
}
