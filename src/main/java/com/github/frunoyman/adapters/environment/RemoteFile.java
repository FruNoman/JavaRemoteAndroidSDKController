package com.github.frunoyman.adapters.environment;

import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RemoteFile {
    public static final String ENVIRONMENT_REMOTE = "com.github.remotesdk.ENVIRONMENT_REMOTE";
    private Logger logger;
    private Shell shell;

    protected final String BROADCAST = "am broadcast -a ";

    private final String ENVIRONMENT_BROADCAST = BROADCAST + ENVIRONMENT_REMOTE;

    private final String IS_FILE_EXIST = "isFileExist";
    private final String LIST_FILES = "listFiles";
    private final String IS_DIRECTORY = "isDirectory";
    private final String IS_FILE = "isFile";
    private final String GET_NAME = "getName";
    private final String GET_PARENT = "getParent";
    private final String CAN_EXECUTE = "canExecute";
    private final String CAN_READ = "canRead";
    private final String CAN_WRITE = "canWrite";
    private final String IS_ABSOLUTE = "isAbsolute";
    private final String IS_HIDDEN = "isHidden";
    private final String DELETE = "deleteFile";
    private final String CREATE_NEW_FILE = "createNewFile";
    private final String MAKE_DIR = "makeDir";
    private final String MAKE_DIRS = "createDirs";
    private final String RENAME_TO = "renameTo";
    private final String GET_TOTAL_SPACE = "getTotalSpace";
    private final String LAST_MODIFIED = "lastModified";
    private final String SET_READABLE = "setReadable";
    private final String SET_WRITABLE = "setWritable";
    private final String SET_EXECUTABLE = "setExecutable";

    private String absolutePath;

    public RemoteFile(Shell shell, String absolutePath) {
        this.shell = shell;
        this.absolutePath = absolutePath;
        logger = Logger.getLogger(RemoteFile.class.getName() + "] [" + shell.getSerial());
    }

    public String getAbsolutePath() {
        logger.debug("get absolute path return [" + absolutePath + "]");
        return absolutePath;
    }

    public boolean exist() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, IS_FILE_EXIST, absolutePath));
        logger.debug("is file [" + absolutePath + "] exist return [" + result + "]");
        return result;
    }

    public boolean isFile() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, IS_FILE, absolutePath));
        logger.debug("is [" + absolutePath + "] file  return [" + result + "]");
        return result;
    }

    public boolean isDirectory() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, IS_DIRECTORY, absolutePath));
        logger.debug("is [" + absolutePath + "] directory return [" + result + "]");
        return result;
    }

    public String getName() {
        String result = shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, GET_NAME, absolutePath);
        logger.debug("get [" + absolutePath + "] name return [" + result + "]");
        return result;
    }

    public RemoteFile getParent() {
        String result = shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, GET_PARENT, absolutePath);
        logger.debug("get [" + absolutePath + "] parent return [" + result + "]");
        return new RemoteFile(shell, result);
    }

    public List<RemoteFile> listFiles() {
        List<RemoteFile> remoteFiles = new ArrayList<>();
        if (!this.exist()) {
            throw new RuntimeException("Remote file not exist");
        }
        String result = shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, LIST_FILES, absolutePath);
        try {
            for (String path : result.replaceAll("\\[", "").replaceAll("\\]", "").split(",")) {
                remoteFiles.add(new RemoteFile(shell, path.trim()));
            }
        } catch (Exception e) {

        }
        logger.debug("[" + absolutePath + "] list files");
        return remoteFiles;
    }

    public boolean canExecute() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, CAN_EXECUTE, absolutePath));
        logger.debug("[" + absolutePath + "] can execute return [" + result + "]");
        return result;
    }

    public boolean canRead() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, CAN_READ, absolutePath));
        logger.debug("[" + absolutePath + "] can read return [" + result + "]");
        return result;
    }

    public boolean canWrite() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, CAN_WRITE, absolutePath));
        logger.debug("[" + absolutePath + "] can write return [" + result + "]");
        return result;
    }

    public boolean isAbsolute() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, IS_ABSOLUTE, absolutePath));
        logger.debug("is [" + absolutePath + "] absolute return [" + result + "]");
        return result;
    }

    public boolean isHidden() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, IS_HIDDEN, absolutePath));
        logger.debug("is [" + absolutePath + "] hidden return [" + result + "]");
        return result;
    }

    public boolean delete() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, DELETE, absolutePath));
        logger.debug("[" + absolutePath + "] delete return [" + result + "]");
        return result;
    }

    public boolean createNewFile() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, CREATE_NEW_FILE, absolutePath));
        logger.debug("create new file [" + absolutePath + "] return [" + result + "]");
        return result;
    }

    public boolean makeDir() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, MAKE_DIR, absolutePath));
        logger.debug("make dir [" + absolutePath + "] return [" + result + "]");
        return result;
    }

    public boolean makeDirs() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, MAKE_DIRS, absolutePath));
        logger.debug("make dirs [" + absolutePath + "] return [" + result + "]");
        return result;
    }

    public boolean renameTo(String name) {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, RENAME_TO, absolutePath, name));
        logger.debug("rename [" + absolutePath + "] to [" + name + "] return [" + result + "]");
        if (result) {
            absolutePath = name;
        }
        return result;
    }

    public long getTotalSpace() {
        long result = Long.parseLong(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, GET_TOTAL_SPACE, absolutePath));
        logger.debug("get total space [" + absolutePath + "] return [" + result + "]");
        return result;
    }

    public long lastModified() {
        long result = Long.parseLong(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, LAST_MODIFIED, absolutePath));
        logger.debug("last modified [" + absolutePath + "] return [" + result + "]");
        return result;
    }

    //    public boolean setReadable(boolean state) {
//        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, SET_READABLE + absolutePath + "," + state));
//        logger.debug("set readable [" + absolutePath + "] [" + result + "]");
//        return result;
//    }

//    public boolean setWritable(boolean state) {
//        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, SET_WRITABLE + absolutePath + "," + state));
//        logger.debug("set writable [" + absolutePath + "] [" + result + "]");
//        return result;
//    }

//    public boolean setExecutable(boolean state) {
//        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, SET_EXECUTABLE + absolutePath + "," + state));
//        logger.debug("set executable [" + absolutePath + "] [" + result + "]");
//        return result;
//    }


    @Override
    public String toString() {
        return absolutePath;
    }
}
