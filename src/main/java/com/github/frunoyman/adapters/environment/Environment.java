package com.github.frunoyman.adapters.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.adapters.bluetooth.Bluetooth;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Environment extends BaseAdapter {
    public static final String ENVIRONMENT_COMMAND = "environment_remote ";
    public static final String ENVIRONMENT_REMOTE = "com.github.remotesdk.ENVIRONMENT_REMOTE";
    private Logger logger;

    private final String AM_COMMAND = BROADCAST
            + ENVIRONMENT_REMOTE
            + ES
            + ENVIRONMENT_COMMAND;

    private final String GET_EXTERNAL_STORAGE_DIRECTORY = AM_COMMAND
            + "getExternalStorageDirectory";
    private final String GET_ROOT_DIRECTORY = AM_COMMAND
            + "getRootDirectory";
    private final String GET_DATA_DIRECTORY = AM_COMMAND
            + "getDataDirectory";
    private final String GET_DOWNLOAD_CACHE_DIRECTORY = AM_COMMAND
            + "getDownloadCacheDirectory";
    private final String GET_STORAGE_VOLUMES =AM_COMMAND
            + "getStorageVolumes";

    public Environment(Shell shell) {
        super(shell);
        logger = Logger.getLogger(Environment.class.getName() + "] [" + shell.getSerial());
    }

    public RemoteFile getExternalStorageDirectory() throws Exception {
        String  result = shell.executeBroadcast(GET_EXTERNAL_STORAGE_DIRECTORY);
        logger.debug("get external storage directory [" + result + "]");
        return new RemoteFile(shell,result);
    }

    public RemoteFile getRootDirectory() throws Exception {
        String  result = shell.executeBroadcast(GET_ROOT_DIRECTORY);
        logger.debug("get root directory [" + result + "]");
        return new RemoteFile(shell,result);
    }

    public RemoteFile getDataDirectory() throws Exception {
        String  result = shell.executeBroadcast(GET_DATA_DIRECTORY);
        logger.debug("get data directory [" + result + "]");
        return new RemoteFile(shell,result);
    }

    public RemoteFile getDownloadCacheDirectory() throws Exception {
        String  result = shell.executeBroadcast(GET_DOWNLOAD_CACHE_DIRECTORY);
        logger.debug("get download cache directory [" + result + "]");
        return new RemoteFile(shell,result);
    }

    public List<StorageVolume>  getStorageVolumes() throws Exception {
        String  result = shell.executeBroadcast(GET_STORAGE_VOLUMES);
        List<StorageVolume> volumes = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            volumes = Arrays.asList(mapper.readValue(result,StorageVolume[].class));
        }catch (Exception e){

        }
        logger.debug("get storage volumes");
        return volumes;
    }
}
