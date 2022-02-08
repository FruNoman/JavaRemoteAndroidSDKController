package com.github.frunoyman.shell;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.appmanagement.AndroidInstallApplicationOptions;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.appmanagement.BaseInstallApplicationOptions;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppiumShell extends Shell {
    private Logger logger;
    private AndroidDriver driver;

    public AppiumShell(AndroidDriver driver) {
        this.driver = driver;
        logger = Logger.getLogger(AppiumShell.class.getName() + "] [" + getSerial());

    }

    @Override
    public String execute(String... command) {
        StringBuilder commandBuilder = new StringBuilder();
        for (String var : command) {
            commandBuilder.append(var);
            commandBuilder.append(" ");
        }
        Map<String, Object> args = new HashMap<>();
        args.put("command", commandBuilder.toString());
        return driver.executeScript("mobile: shell", args).toString();
    }

    @Override
    public String executeBroadcastExtended(String broadcast, String command, Object... params) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append(broadcast);
        commandBuilder.append(" --es command " + command);
        for (int x = 0; x < params.length; x++) {
            commandBuilder.append(" --es param" + x + " '" + params[x] + "'");
        }
        if (!execute("pm list packages -3").contains(REMOTE_PACKAGE)) {
            logger.debug("Remote controller apk was not found, installing ...");
            File apk = getApk();
            AndroidInstallApplicationOptions options = new AndroidInstallApplicationOptions();
            options.withGrantPermissionsEnabled();
            driver.installApp(apk.getAbsolutePath(), options);
            if (!execute("pm list packages -3").contains(REMOTE_PACKAGE)) {
                throw new RuntimeException("Pls install RemoteController apk manually");
            }
        }
        if (!execute("ps -A").contains(REMOTE_PACKAGE)) {
            logger.debug("Remote controller was not running, starting ...");
            execute("am", "start", "-n", REMOTE_PACKAGE + "/.MainActivity");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.pressKey(new KeyEvent(AndroidKey.HOME));
        }
        String output = execute(commandBuilder.toString());
        Pattern r = Pattern.compile(ADAPTER_PATTERN);
        Matcher m = r.matcher(output);
        if (m.matches()) {
            if (output.contains("result=" + ERROR_CODE)) {
                throw new RuntimeException(m.group(1));
            } else if (output.contains("result=" + SUCCESS_CODE)) {
                return m.group(1);

            }
        } else if (output.contains("result=" + EMPTY_BROADCAST_CODE)) {
            throw new RuntimeException("Empty broadcast error");
        } else if (!output.contains("data=\"") && output.contains("result=" + SUCCESS_CODE)) {
            return "";
        }
        return output;
    }

    @Override
    public String getSerial() {
        return (String) execute("getprop", "ro.boot.serialno").trim();
    }
}
