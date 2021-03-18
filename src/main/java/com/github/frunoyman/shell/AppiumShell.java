package com.github.frunoyman.shell;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppiumShell extends Shell {
    private AndroidDriver driver;

    public AppiumShell(AndroidDriver driver) {
        this.driver = driver;
    }

    @Override
    public String execute(String... command) throws Exception {
        StringBuilder commandBuilder = new StringBuilder();
        for (String var : command) {
            commandBuilder.append(var);
            commandBuilder.append(" ");
        }
        Map<String, Object> args = new HashMap<>();
        args.put("command", commandBuilder.toString());
        String response = driver.executeScript("mobile: shell", args).toString();
        String output = response.split("\n")[1];
        Pattern r = Pattern.compile(ADAPTER_PATTERN);
        Matcher m = r.matcher(output);
        if (m.matches()) {
            if (output.contains("result=" + ERROR_CODE)) {
                ObjectMapper objectMapper = new ObjectMapper();
                Exception exception = objectMapper.readValue(m.group(2), Exception.class);
                throw exception;
            } else {
                return m.group(2);
            }
        }else if (output.contains("result="+EMPTY_BROADCAST_CODE)){
            throw new Exception("Empty broadcast");
        }
        return response;
    }

    @Override
    public String getSerial() {
        return (String) driver.getCapabilities().getCapability(MobileCapabilityType.UDID);
    }
}
