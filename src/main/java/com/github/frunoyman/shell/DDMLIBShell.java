package com.github.frunoyman.shell;

import com.android.ddmlib.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DDMLIBShell extends Shell{


    private IDevice iDevice;

    public DDMLIBShell(IDevice iDevice) {
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
            iDevice.executeShellCommand(commandBuilder.toString(), receiver);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (AdbCommandRejectedException e) {
            e.printStackTrace();
        } catch (ShellCommandUnresponsiveException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output = receiver.getOutput().split("\n")[1];
        Pattern r = Pattern.compile(ADAPTER_PATTERN);
        Matcher m = r.matcher(output);
        if (m.matches()) {
            if (output.contains("result="+ERROR_CODE)) {
                ObjectMapper objectMapper = new ObjectMapper();
                Exception exception = objectMapper.readValue(m.group(2), Exception.class);
                throw exception;
            }else {
                return m.group(2);
            }
        }
        return receiver.getOutput();
    }
}
