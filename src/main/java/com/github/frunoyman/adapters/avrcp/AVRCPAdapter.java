package com.github.frunoyman.adapters.avrcp;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;


public class AVRCPAdapter extends BaseAdapter {
    private final String MEDIA_SESSION_REMOTE = "com.github.remotesdk.MEDIA_SESSION_REMOTE";
    private Logger logger;


    private final String MEDIA_SESSION_BROADCAST = BROADCAST + MEDIA_SESSION_REMOTE;


    private final String TRANSPORT_CONTROL_PLAY = "transportControlPlay";
    private final String TRANSPORT_CONTROL_PAUSE = "transportControlPause";
    private final String TRANSPORT_CONTROL_NEXT = "transportControlNext";
    private final String TRANSPORT_CONTROL_PREV = "transportControlPrev";
    private final String GET_PLAYBACK_STATE_CURRENT_POSITION = "getPlaybackStateCurrentPosition";
    private final String GET_META_DATA = "getMetaData";
    private final String TRANSPORT_CONTROL_IS_PLAYING = "transportControlIsPlaying";

    public enum MediaMetaData {
        METADATA_KEY_TITLE("android.media.metadata.TITLE"),
        METADATA_KEY_ARTIST("android.media.metadata.ARTIST"),
        METADATA_KEY_DURATION("android.media.metadata.DURATION"),
        METADATA_KEY_ALBUM("android.media.metadata.ALBUM"),
        METADATA_KEY_AUTHOR("android.media.metadata.AUTHOR"),
        METADATA_KEY_WRITER("android.media.metadata.WRITER"),
        METADATA_KEY_COMPOSER("android.media.metadata.COMPOSER"),
        METADATA_KEY_COMPILATION("android.media.metadata.COMPILATION"),
        METADATA_KEY_DATE("android.media.metadata.DATE"),
        METADATA_KEY_YEAR("android.media.metadata.YEAR"),
        METADATA_KEY_GENRE("android.media.metadata.GENRE"),
        METADATA_KEY_TRACK_NUMBER("android.media.metadata.TRACK_NUMBER"),
        METADATA_KEY_NUM_TRACKS("android.media.metadata.NUM_TRACKS"),
        METADATA_KEY_DISC_NUMBER("android.media.metadata.DISC_NUMBER"),
        METADATA_KEY_ALBUM_ARTIST("android.media.metadata.ALBUM_ARTIST"),
        METADATA_KEY_ART("android.media.metadata.ART"),
        METADATA_KEY_ART_URI("android.media.metadata.ART_URI"),
        METADATA_KEY_ALBUM_ART("android.media.metadata.ALBUM_ART"),
        METADATA_KEY_ALBUM_ART_URI("android.media.metadata.ALBUM_ART_URI"),
        METADATA_KEY_USER_RATING("android.media.metadata.USER_RATING"),
        METADATA_KEY_RATING("android.media.metadata.RATING"),
        METADATA_KEY_DISPLAY_TITLE("android.media.metadata.DISPLAY_TITLE"),
        METADATA_KEY_DISPLAY_SUBTITLE("android.media.metadata.DISPLAY_SUBTITLE"),
        METADATA_KEY_DISPLAY_DESCRIPTION("android.media.metadata.DISPLAY_DESCRIPTION"),
        METADATA_KEY_DISPLAY_ICON("android.media.metadata.DISPLAY_ICON"),
        METADATA_KEY_DISPLAY_ICON_URI("android.media.metadata.DISPLAY_ICON_URI"),
        METADATA_KEY_MEDIA_ID("android.media.metadata.MEDIA_ID"),
        METADATA_KEY_MEDIA_URI("android.media.metadata.MEDIA_URI"),
        METADATA_KEY_BT_FOLDER_TYPE("android.media.metadata.BT_FOLDER_TYPE"),
        METADATA_KEY_ADVERTISEMENT("android.media.metadata.ADVERTISEMENT"),
        METADATA_KEY_DOWNLOAD_STATUS("android.media.metadata.DOWNLOAD_STATUS");

        private String metadata;

        public String getMetadata() {
            return metadata;
        }

        public static MediaMetaData getMetadata(String metadata) {
            for (MediaMetaData meta : values()) {
                if (meta.getMetadata().equals(metadata)) {
                    return meta;
                }
            }
            return null;
        }

        MediaMetaData(String metadata) {
            this.metadata = metadata;
        }
    }

    public AVRCPAdapter(Shell shell) {
        super(shell);
        logger = Logger.getLogger(AVRCPAdapter.class.getName() + "] [" + shell.getSerial());
    }


    public void play() {
        shell.executeBroadcastExtended(MEDIA_SESSION_BROADCAST, TRANSPORT_CONTROL_PLAY);
        logger.debug("play");
    }

    public void pause() {
        shell.executeBroadcastExtended(MEDIA_SESSION_BROADCAST, TRANSPORT_CONTROL_PAUSE);
        logger.debug("pause");
    }

    public void next() {
        shell.executeBroadcastExtended(MEDIA_SESSION_BROADCAST, TRANSPORT_CONTROL_NEXT);
        logger.debug("next");
    }

    public void prev() {
        shell.executeBroadcastExtended(MEDIA_SESSION_BROADCAST, TRANSPORT_CONTROL_PREV);
        logger.debug("next");
    }

    public int getCurrentPosition() {
        int result = Integer.parseInt(shell.executeBroadcastExtended(MEDIA_SESSION_BROADCAST, GET_PLAYBACK_STATE_CURRENT_POSITION));
        logger.debug("get current position return [" + result + "]");
        return result;
    }

    public boolean isPlaying() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(MEDIA_SESSION_BROADCAST, TRANSPORT_CONTROL_IS_PLAYING));
        logger.debug("is playing return [" + result + "]");
        return result;
    }

    public String getCurrentPlayMetaData(MediaMetaData mediaMetaData) {
        String type = "string";
        if (mediaMetaData == MediaMetaData.METADATA_KEY_DURATION) {
            type = "long";
        }
        String result = shell.executeBroadcastExtended(MEDIA_SESSION_BROADCAST, GET_META_DATA, type, mediaMetaData.getMetadata());
        logger.debug("is playing return [" + result + "]");
        return result;
    }

    public int getDuration() {
        return Integer.parseInt(getCurrentPlayMetaData(MediaMetaData.METADATA_KEY_DURATION));
    }

}
