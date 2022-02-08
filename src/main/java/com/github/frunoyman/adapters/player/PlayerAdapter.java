package com.github.frunoyman.adapters.player;

import com.github.frunoyman.adapters.BaseAdapter;
import com.github.frunoyman.adapters.environment.RemoteFile;
import com.github.frunoyman.shell.Shell;
import org.apache.log4j.Logger;

public class PlayerAdapter extends BaseAdapter {
    private final String PLAYER_REMOTE = "com.github.remotesdk.PLAYER_REMOTE";
    private Logger logger;


    private final String PLAYER_BROADCAST = BROADCAST + PLAYER_REMOTE;


    private final String PLAY_SONG = "playSong";
    private final String STOP_SONG = "stopSong";
    private final String SEEK_TO = "seekToSong";
    private final String GET_DURATION = "getSongDuration";
    private final String GET_CURRENT_POSITION = "getSongCurrentPosition";
    private final String IS_SONG_PLAYING = "isSongPlaying";
    private final String IS_SONG_LOOPING = "isSongLooping";
    private final String SET_LOOPING = "setSongLooping";
    private final String DISPLAY_VIDEO = "displayPlayerView";
    private final String PLAY_FOLDER = "playFolder";
    private final String NEXT_SONG = "nextSong";
    private final String PREV_SONG = "prevSong";
    private final String PAUSE_SONG = "pauseSong";
    private final String REV_SONG = "revSong";
    private final String GET_CURRENT_PLAYING_FILE = "getCurrentPlayingFile";
    private final String GET_MEDIA_METADATA = "getMediaMetadata";

    public PlayerAdapter(Shell shell) {
        super(shell);
        logger = Logger.getLogger(PlayerAdapter.class.getName() + "] [" + shell.getSerial());
    }

    public enum PlayerView {
        VIDEO("video"),
        EQUALIZER("equalizer"),
        DEFAULT("default");

        private String view;

        public String getView() {
            return view;
        }

        PlayerView(String view) {
            this.view = view;
        }
    }

    public enum MetaData {
        METADATA_KEY_CD_TRACK_NUMBER(0),
        METADATA_KEY_ALBUM(1),
        METADATA_KEY_ARTIST(2),
        METADATA_KEY_AUTHOR(3),
        METADATA_KEY_COMPOSER(4),
        METADATA_KEY_DATE(5),
        METADATA_KEY_GENRE(6),
        METADATA_KEY_TITLE(7),
        METADATA_KEY_YEAR(8),
        METADATA_KEY_DURATION(9),
        METADATA_KEY_NUM_TRACKS(10),
        METADATA_KEY_WRITER(11),
        METADATA_KEY_MIMETYPE(12),
        METADATA_KEY_ALBUMARTIST(13),
        METADATA_KEY_DISC_NUMBER(14),
        METADATA_KEY_COMPILATION(15),
        METADATA_KEY_HAS_AUDIO(16),
        METADATA_KEY_HAS_VIDEO(17),
        METADATA_KEY_VIDEO_WIDTH(18),
        METADATA_KEY_VIDEO_HEIGHT(19),
        METADATA_KEY_BITRATE(20),
        METADATA_KEY_TIMED_TEXT_LANGUAGES(21),
        METADATA_KEY_IS_DRM(22),
        METADATA_KEY_LOCATION(23),
        METADATA_KEY_VIDEO_ROTATION(24),
        METADATA_KEY_CAPTURE_FRAMERATE(25),
        METADATA_KEY_HAS_IMAGE(26),
        METADATA_KEY_IMAGE_COUNT(27),
        METADATA_KEY_IMAGE_PRIMARY(28),
        METADATA_KEY_IMAGE_WIDTH(29),
        METADATA_KEY_IMAGE_HEIGHT(30),
        METADATA_KEY_IMAGE_ROTATION(31),
        METADATA_KEY_VIDEO_FRAME_COUNT(32),
        METADATA_KEY_EXIF_OFFSET(33),
        METADATA_KEY_EXIF_LENGTH(34),
        METADATA_KEY_COLOR_STANDARD(35),
        METADATA_KEY_COLOR_TRANSFER(36),
        METADATA_KEY_COLOR_RANGE(37),
        METADATA_KEY_SAMPLERATE(38),
        METADATA_KEY_BITS_PER_SAMPLE(39);

        private int metaData;

        public int getMetaData() {
            return metaData;
        }

        public static PlayerAdapter.MetaData getMetaData(int metaData) {
            for (PlayerAdapter.MetaData data : values()) {
                if (data.getMetaData() == metaData) {
                    return data;
                }
            }
            return null;
        }

        MetaData(int metaData) {
            this.metaData = metaData;
        }
    }

    public void play(String path) {
        shell.executeBroadcastExtended(PLAYER_BROADCAST, PLAY_SONG, path);
        logger.debug("play [" + path + "]");
    }

    public void play(RemoteFile file) {
        play(file.getAbsolutePath());
    }

    public void stop() {
        shell.executeBroadcastExtended(PLAYER_BROADCAST, STOP_SONG);
        logger.debug("stop song");
    }

    public void seekTo(long seekTo) {
        shell.executeBroadcastExtended(PLAYER_BROADCAST, SEEK_TO, seekTo);
        logger.debug("seek to [" + seekTo + "]");
    }

    public long getDuration() {
        long result = Long.parseLong(shell.executeBroadcastExtended(PLAYER_BROADCAST, GET_DURATION));
        logger.debug("get duration return [" + result + "]");
        return result;
    }

    public long getCurrentPosition() {
        long result = Long.parseLong(shell.executeBroadcastExtended(PLAYER_BROADCAST, GET_CURRENT_POSITION));
        logger.debug("get current position return [" + result + "]");
        return result;
    }

    public boolean isPlaying() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(PLAYER_BROADCAST, IS_SONG_PLAYING));
        logger.debug("is playing return [" + result + "]");
        return result;
    }

    public boolean isLooping() {
        boolean result = Boolean.parseBoolean(shell.executeBroadcastExtended(PLAYER_BROADCAST, IS_SONG_LOOPING));
        logger.debug("is looping  return [" + result + "]");
        return result;
    }

    public void setLooping(boolean state) {
        shell.executeBroadcastExtended(PLAYER_BROADCAST, SET_LOOPING, state);
        logger.debug("set looping [" + state + "]");
    }

    public void displayView(PlayerView playerView) {
        shell.execute("am start -n com.github.remotesdk/.MainActivity -f 0x20000000" +
                " --activity-single-top --activity-clear-when-task-reset");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shell.executeBroadcastExtended(PLAYER_BROADCAST, DISPLAY_VIDEO, playerView.getView());
        logger.debug("display view [" + playerView.getView() + "]");
    }

    public void playFolder(String path) {
        shell.executeBroadcastExtended(PLAYER_BROADCAST, PLAY_FOLDER, path);
        logger.debug("play folder [" + path + "]");
    }

    public void playFolder(RemoteFile file) {
        playFolder(file.getAbsolutePath());
    }

    public void next() {
        shell.executeBroadcastExtended(PLAYER_BROADCAST, NEXT_SONG);
        logger.debug("play next");
    }

    public void previous() {
        shell.executeBroadcastExtended(PLAYER_BROADCAST, PREV_SONG);
        logger.debug("play previous");
    }

    public void rev() {
        shell.executeBroadcastExtended(PLAYER_BROADCAST, REV_SONG);
        logger.debug("rev play");
    }

    public void pause() {
        shell.executeBroadcastExtended(PLAYER_BROADCAST, PAUSE_SONG);
        logger.debug("pause");
    }

    public RemoteFile getCurrentPlayingFile() {
        String result = shell.executeBroadcastExtended(PLAYER_BROADCAST, GET_CURRENT_PLAYING_FILE);
        logger.debug("get current playing file [" + result + "]");
        return new RemoteFile(shell, result);
    }

    public String getCurrentPlayMediaMetaData(MetaData metaData) {
        String file = getCurrentPlayingFile().getAbsolutePath();
        String result = shell.executeBroadcastExtended(PLAYER_BROADCAST, GET_MEDIA_METADATA, file, metaData.getMetaData());
        logger.debug("get current play media metadata  [" + metaData.name() + "] return [" + result + "]");
        return result;
    }
}
