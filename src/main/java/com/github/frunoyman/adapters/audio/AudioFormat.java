package com.github.frunoyman.adapters.audio;

public class AudioFormat {

    public enum Encodings {
        /**
         * Invalid audio data format
         */
        ENCODING_INVALID(0),
        /**
         * Default audio data format
         */
        ENCODING_DEFAULT(1),

        // These values must be kept in sync with core/jni/android_media_AudioFormat.h
        // Also sync av/services/audiopolicy/managerdefault/ConfigParsingUtils.h
        /**
         * Audio data format: PCM 16 bit per sample. Guaranteed to be supported by devices.
         */
        ENCODING_PCM_16BIT(2),
        /**
         * Audio data format: PCM 8 bit per sample. Not guaranteed to be supported by devices.
         */
        ENCODING_PCM_8BIT(3),
        /**
         * Audio data format: single-precision floating-point per sample
         */
        ENCODING_PCM_FLOAT(4),
        /**
         * Audio data format: AC-3 compressed
         */
        ENCODING_AC3(5),
        /**
         * Audio data format: E-AC-3 compressed
         */
        ENCODING_E_AC3(6),
        /**
         * Audio data format: DTS compressed
         */
        ENCODING_DTS(7),
        /**
         * Audio data format: DTS HD compressed
         */
        ENCODING_DTS_HD(8),
        /**
         * Audio data format: MP3 compressed
         */
        ENCODING_MP3(9),
        /**
         * Audio data format: AAC LC compressed
         */
        ENCODING_AAC_LC(10),
        /**
         * Audio data format: AAC HE V1 compressed
         */
        ENCODING_AAC_HE_V1(11),
        /**
         * Audio data format: AAC HE V2 compressed
         */
        ENCODING_AAC_HE_V2(12),

        /**
         * Audio data format: compressed audio wrapped in PCM for HDMI
         * or S/PDIF passthrough.
         * IEC61937 uses a stereo stream of 16-bit samples as the wrapper.
         * So the channel mask for the track must be .
         * Data should be written to the stream in a short[] array.
         * If the data is written in a byte[] array then there may be endian problems
         * on some platforms when converting to short internally.
         */
        ENCODING_IEC61937(13),
        /**
         * Audio data format: DOLBY TRUEHD compressed
         **/
        ENCODING_DOLBY_TRUEHD(14),
        /**
         * Audio data format: AAC ELD compressed
         */
        ENCODING_AAC_ELD(15),
        /**
         * Audio data format: AAC xHE compressed
         */
        ENCODING_AAC_XHE(16),
        /**
         * Audio data format: AC-4 sync frame transport format
         */
        ENCODING_AC4(17),
        /**
         * Audio data format: E-AC-3-JOC compressed
         * E-AC-3-JOC streams can be decoded by downstream devices supporting {@link #ENCODING_E_AC3}.
         * Use {@link #ENCODING_E_AC3} as the AudioTrack encoding when the downstream device
         * supports {@link #ENCODING_E_AC3} but not {@link #ENCODING_E_AC3_JOC}.
         **/
        ENCODING_E_AC3_JOC(18);

        private int type;

        Encodings(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static Encodings getType(int type) {
            for (Encodings encodings : values()) {
                if (encodings.getType() == type) {
                    return encodings;
                }
            }
            return ENCODING_INVALID;
        }
    }

    public enum ChanelMask {
        CHANNEL_IN_DEFAULT(1),
        // These directly match native
        CHANNEL_IN_LEFT(0x4),
        CHANNEL_IN_RIGHT(0x8),
        CHANNEL_IN_FRONT(0x10),
        CHANNEL_IN_BACK(0x20),
        CHANNEL_IN_LEFT_PROCESSED(0x40),
        CHANNEL_IN_RIGHT_PROCESSED(0x80),
        CHANNEL_IN_FRONT_PROCESSED(0x100),
        CHANNEL_IN_BACK_PROCESSED(0x200),
        CHANNEL_IN_PRESSURE(0x400),
        CHANNEL_IN_X_AXIS(0x800),
        CHANNEL_IN_Y_AXIS(0x1000),
        CHANNEL_IN_Z_AXIS(0x2000),
        CHANNEL_IN_VOICE_UPLINK(0x4000),
        CHANNEL_IN_VOICE_DNLINK(0x8000),
        CHANNEL_IN_MONO(CHANNEL_IN_FRONT.getMask()),
        CHANNEL_IN_STEREO((CHANNEL_IN_LEFT.getMask() | CHANNEL_IN_RIGHT.getMask())),
        CHANNEL_IN_FRONT_BACK(CHANNEL_IN_FRONT.getMask() | CHANNEL_IN_BACK.getMask());

        private int mask;

        public int getMask() {
            return mask;
        }

        ChanelMask(int mask) {
            this.mask = mask;
        }

        public static ChanelMask getMask(int mask) {
            for (ChanelMask chanelMask : values()) {
                if (chanelMask.getMask() == mask) {
                    return chanelMask;
                }
            }
            return CHANNEL_IN_DEFAULT;
        }
    }
}
