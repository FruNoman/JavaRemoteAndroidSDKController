package com.github.frunoyman.adapters.environment;

import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

public class RemoteFile {
    public static final String ENVIRONMENT_COMMAND = "environment_remote ";
    public static final String ENVIRONMENT_REMOTE = "com.github.remotesdk.ENVIRONMENT_REMOTE";
    private Logger logger;
    private Shell shell;

    protected final String BROADCAST = "am broadcast -a ";
    protected final String ES = " --es ";

    private final String AM_COMMAND = BROADCAST
            + ENVIRONMENT_REMOTE
            + ES
            + ENVIRONMENT_COMMAND;


    private final String IS_FILE_EXIST = AM_COMMAND
            + "isFileExist,";
    private final String LIST_FILES = AM_COMMAND
            + "listFiles,";
    private final String IS_DIRECTORY = AM_COMMAND
            + "isDirectory,";
    private final String IS_FILE = AM_COMMAND
            + "isFile,";
    private final String GET_NAME = AM_COMMAND
            + "getName,";
    private final String GET_PARENT = AM_COMMAND
            + "getParent,";

    private String absolutePath;

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public RemoteFile(Shell shell, String absolutePath) {
        this.shell = shell;
        this.absolutePath = absolutePath;
        logger = Logger.getLogger(RemoteFile.class.getName() + "] [" + shell.getSerial());
    }

    public boolean exist() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_FILE_EXIST + absolutePath));
        logger.debug("is exist [" + result + "]");
        return result;
    }
}
