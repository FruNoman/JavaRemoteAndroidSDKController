package com.github.frunoyman.shell;

import com.android.ddmlib.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DDMLibShell extends Shell {


    private IDevice iDevice;

    public DDMLibShell(IDevice iDevice) {
        this.iDevice = iDevice;
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
            File apk = getApk();
            iDevice.installPackage(apk.getAbsolutePath(), false);
            execute("pm grant com.github.remotesdk android.permission.BLUETOOTH");
            execute("pm grant com.github.remotesdk android.permission.BLUETOOTH_ADMIN");
            execute("pm grant com.github.remotesdk android.permission.WRITE_EXTERNAL_STORAGE");
            execute("pm grant com.github.remotesdk android.permission.READ_EXTERNAL_STORAGE");
        }
        if (!execute("ps -A").contains(REMOTE_PACKAGE)) {
            execute("am", "start", "-n", REMOTE_PACKAGE + "/.MainActivity");
            Thread.sleep(3000);
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
        String response = receiver.getOutput();
        String output = response;
        Pattern r = Pattern.compile(ADAPTER_PATTERN);
        Matcher m = r.matcher(output);
        if (m.matches()) {
            if (output.contains("result=" + ERROR_CODE)) {
                ObjectMapper objectMapper = new ObjectMapper();
                Exception exception = objectMapper.readValue(m.group(3), Exception.class);
                throw exception;
            } else if (output.contains("result=" + SUCCESS_CODE)) {
                return m.group(3);
            }
        } else if (output.contains("result=" + EMPTY_BROADCAST_CODE)) {
            throw new Exception("Empty broadcast");
        }
        return response;
    }

    @Override
    public String getSerial() {
        return iDevice.getSerialNumber();
    }
}
