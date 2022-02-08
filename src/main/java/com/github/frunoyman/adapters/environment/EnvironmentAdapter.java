package com.github.frunoyman.adapters.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnvironmentAdapter extends BaseAdapter {
    public static final String ENVIRONMENT_REMOTE = "com.github.remotesdk.ENVIRONMENT_REMOTE";
    private Logger logger;

    private final String ENVIRONMENT_BROADCAST = BROADCAST + ENVIRONMENT_REMOTE;

    private final String GET_EXTERNAL_STORAGE_DIRECTORY = "getExternalStorageDirectory";
    private final String GET_ROOT_DIRECTORY = "getRootDirectory";
    private final String GET_DATA_DIRECTORY = "getDataDirectory";
    private final String GET_DOWNLOAD_CACHE_DIRECTORY = "getDownloadCacheDirectory";
    private final String GET_STORAGE_VOLUMES = "getStorageVolumes";

    public EnvironmentAdapter(Shell shell) {
        super(shell);
        logger = Logger.getLogger(EnvironmentAdapter.class.getName() + "] [" + shell.getSerial());
    }

    public RemoteFile getExternalStorageDirectory() {
        String result = shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, GET_EXTERNAL_STORAGE_DIRECTORY);
        logger.debug("get external storage directory [" + result + "]");
        return new RemoteFile(shell, result);
    }

    public RemoteFile getRootDirectory() {
        String result = shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, GET_ROOT_DIRECTORY);
        logger.debug("get root directory [" + result + "]");
        return new RemoteFile(shell, result);
    }

    public RemoteFile getDataDirectory() {
        String result = shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, GET_DATA_DIRECTORY);
        logger.debug("get data directory [" + result + "]");
        return new RemoteFile(shell, result);
    }

    public RemoteFile getDownloadCacheDirectory() {
        String result = shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, GET_DOWNLOAD_CACHE_DIRECTORY);
        logger.debug("get download cache directory [" + result + "]");
        return new RemoteFile(shell, result);
    }

    public List<StorageVolume> getStorageVolumes() {
        String result = shell.executeBroadcastExtended(ENVIRONMENT_BROADCAST, GET_STORAGE_VOLUMES);
        List<StorageVolume> volumes = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            volumes = Arrays.asList(mapper.readValue(result, StorageVolume[].class));
        } catch (Exception e) {

        }
        logger.debug("get storage volumes");
        return volumes;
    }
}
