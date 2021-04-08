package com.github.frunoyman.adapters.environment;

import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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
        logger.debug("get absolute path [" + absolutePath + "]");
        return absolutePath;
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

    public boolean isFile() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_FILE + absolutePath));
        logger.debug("is file [" + result + "]");
        return result;
    }

    public boolean isDirectory() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_DIRECTORY + absolutePath));
        logger.debug("is directory [" + result + "]");
        return result;
    }

    public String getName() throws Exception {
        String result = shell.executeBroadcast(GET_NAME + absolutePath);
        logger.debug("get name [" + result + "]");
        return result;
    }

    public RemoteFile getParent() throws Exception {
        String result = shell.executeBroadcast(GET_PARENT + absolutePath);
        logger.debug("get parent [" + result + "]");
        return new RemoteFile(shell,result);
    }

    public List<RemoteFile> listFiles() throws Exception {
        List<RemoteFile> remoteFiles = new ArrayList<>();
        String result = shell.executeBroadcast(LIST_FILES + absolutePath);
        try {
            for (String path : result.replaceAll("\\[", "").replaceAll("\\]", "").split(",")) {
                remoteFiles.add(new RemoteFile(shell,path.trim()));
            }
        }catch (Exception e){

        }
        logger.debug("list files");
        return remoteFiles;
    }


    @Override
    public String toString() {
        return  "absolutePath='" + absolutePath + '\'';
    }
}
