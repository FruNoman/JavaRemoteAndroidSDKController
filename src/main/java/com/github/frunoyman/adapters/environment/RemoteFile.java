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

    private final String CAN_EXECUTE = AM_COMMAND
            +"canExecute,";
    private final String CAN_READ = AM_COMMAND
            +"canRead,";
    private final String CAN_WRITE = AM_COMMAND
            +"canWrite,";
    private final String IS_ABSOLUTE = AM_COMMAND
            +"isAbsolute,";
    private final String IS_HIDDEN =AM_COMMAND
            + "isHidden,";
    private final String DELETE = AM_COMMAND
            +"deleteFile,";
    private final String CREATE_NEW_FILE = AM_COMMAND
            +"createNewFile,";
    private final String MAKE_DIR = AM_COMMAND
            +"makeDir,";
    private final String MAKE_DIRS = AM_COMMAND
            +"createDirs,";
    private final String RENAME_TO = AM_COMMAND
            +"renameTo,";
    private final String SET_READABLE =AM_COMMAND
            + "setReadable,";
    private final String SET_WRITABLE = AM_COMMAND
            +"setWritable,";
    private final String SET_EXECUTABLE = AM_COMMAND
            +"setExecutable,";
    private final String GET_TOTAL_SPACE = AM_COMMAND
            +"getTotalSpace,";
    private final String LAST_MODIFIED =AM_COMMAND
            + "lastModified,";


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

    public boolean canExecute() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(CAN_EXECUTE + absolutePath));
        logger.debug("can execute [" + result + "]");
        return result;
    }

    public boolean canRead() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(CAN_READ + absolutePath));
        logger.debug("can read [" + result + "]");
        return result;
    }

    public boolean canWrite() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(CAN_WRITE + absolutePath));
        logger.debug("can write [" + result + "]");
        return result;
    }

    public boolean isAbsolute() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_ABSOLUTE + absolutePath));
        logger.debug("is absolute [" + result + "]");
        return result;
    }

    public boolean isHidden() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(IS_HIDDEN + absolutePath));
        logger.debug("is hidden [" + result + "]");
        return result;
    }

    public boolean delete() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(DELETE + absolutePath));
        logger.debug("delete [" + result + "]");
        return result;
    }

    public boolean createNewFile() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(CREATE_NEW_FILE + absolutePath));
        logger.debug("create new file [" + absolutePath + "] ["+result+"]");
        return result;
    }

    public boolean makeDir() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(MAKE_DIR + absolutePath));
        logger.debug("make dir [" + absolutePath + "] ["+result+"]");
        return result;
    }

    public boolean makeDirs() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(MAKE_DIRS + absolutePath));
        logger.debug("make dirs [" + absolutePath + "] ["+result+"]");
        return result;
    }

    public boolean renameTo(String name) throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(RENAME_TO + absolutePath+","+name));
        logger.debug("rename [" + absolutePath + "] to ["+name+"] ["+result+"]");
        return result;
    }

    public boolean setReadable() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_READABLE + absolutePath));
        logger.debug("set readable [" + absolutePath + "] ["+result+"]");
        return result;
    }

    public boolean setWritable() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_WRITABLE + absolutePath));
        logger.debug("set writable [" + absolutePath + "] ["+result+"]");
        return result;
    }

    public boolean setExecutable() throws Exception {
        boolean result = Boolean.parseBoolean(shell.executeBroadcast(SET_EXECUTABLE + absolutePath));
        logger.debug("set executable [" + absolutePath + "] ["+result+"]");
        return result;
    }

    public long getTotalSpace() throws Exception {
        long result = Long.parseLong(shell.executeBroadcast(GET_TOTAL_SPACE + absolutePath));
        logger.debug("get total space [" + absolutePath + "] ["+result+"]");
        return result;
    }

    public long lastModified() throws Exception {
        long result = Long.parseLong(shell.executeBroadcast(LAST_MODIFIED + absolutePath));
        logger.debug("last modified [" + absolutePath + "] ["+result+"]");
        return result;
    }



    @Override
    public String toString() {
        return  "absolutePath='" + absolutePath + '\'';
    }
}
