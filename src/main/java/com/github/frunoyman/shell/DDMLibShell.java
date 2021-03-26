package com.github.frunoyman.shell;

import com.android.ddmlib.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DDMLibShell extends Shell {
    private Logger logger;
    private IDevice iDevice;


    public DDMLibShell(IDevice iDevice) {
        this.iDevice = iDevice;
        logger = Logger.getLogger(DDMLibShell.class.getName() + "] [" + getSerial());
    }


    public String execute(String... command) throws Exception {
        StringBuilder commandBuilder = new StringBuilder();
        for (String var : command) {
            commandBuilder.append(var);
            commandBuilder.append(" ");
        }
        CollectingOutputReceiver receiver = new CollectingOutputReceiver();
        try {
            iDevice.executeShellCommand(commandBuilder.toString(), receiver, 60, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (AdbCommandRejectedException e) {
            e.printStackTrace();
        } catch (ShellCommandUnresponsiveException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiver.getOutput();
    }

    @Override
    public String executeBroadcast(String... command) throws Exception {
        if (!execute("pm list packages -3").contains(REMOTE_PACKAGE)) {
            logger.debug("Remote controller apk was not found, installing ...");
            File apk = getApk();
            iDevice.installPackage(apk.getAbsolutePath(), false,"-g");
            for (String permission:permissions){
                execute("pm grant com.github.remotesdk "+permission);
            }
        }
        if (!execute("ps -A").contains(REMOTE_PACKAGE)) {
            logger.debug("Remote controller was not running, starting ...");
            execute("am", "start", "-n", REMOTE_PACKAGE + "/.MainActivity");
            Thread.sleep(3000);
            execute("input keyevent KEYCODE_HOME");
        }

        StringBuilder commandBuilder = new StringBuilder();
        for (String var : command) {
            commandBuilder.append(var);
            commandBuilder.append(" ");
        }
        CollectingOutputReceiver receiver = new CollectingOutputReceiver();
        try {
            iDevice.executeShellCommand(commandBuilder.toString(), receiver, 60, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (AdbCommandRejectedException e) {
            e.printStackTrace();
        } catch (ShellCommandUnresponsiveException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output = receiver.getOutput();
        Pattern r = Pattern.compile(ADAPTER_PATTERN);
        Matcher m = r.matcher(output);
        if (m.matches()) {
            if (output.contains("result=" + ERROR_CODE)) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
                objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                Exception exception = objectMapper.readValue(m.group(3), Exception.class);
                throw exception;
            } else if (output.contains("result=" + SUCCESS_CODE)) {
                return m.group(3);
            }
        } else if (output.contains("result=" + EMPTY_BROADCAST_CODE)) {
            throw new Exception("Empty broadcast");
        }
        return output;
    }

    @Override
    public String getSerial() {
        return iDevice.getSerialNumber();
    }
}
