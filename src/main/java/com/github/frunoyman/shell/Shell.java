package com.github.frunoyman.shell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Shell {
    protected final String ADAPTER_PATTERN = "(.*data=\")(.*)(\".*)";
    protected final String REMOTE_PACKAGE = "com.github.remotesdk";
    protected final int ERROR_CODE = 123;
    protected final int SUCCESS_CODE = 373;
    protected final int EMPTY_BROADCAST_CODE = 0;

    public abstract String execute(String... command) throws Exception;

    public abstract String executeBroadcast(String... command) throws Exception;

    public abstract String getSerial();

    public static File getApk() {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("app-debug.apk");
            OutputStream resStreamOut = null;
            int readBytes;
            byte[] buffer = new byte[4096];
            File outputFile = File.createTempFile("app-debug", ".apk");
            resStreamOut = new FileOutputStream(outputFile);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
