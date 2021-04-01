package com.github.frunoyman.adapters.telephony;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

public class Telecom extends BaseAdapter {
    private Logger logger;

    public Telecom(Shell shell) {
        super(shell);
        logger = Logger.getLogger(Telecom.class.getName() + "] [" + shell.getSerial());
    }
}
