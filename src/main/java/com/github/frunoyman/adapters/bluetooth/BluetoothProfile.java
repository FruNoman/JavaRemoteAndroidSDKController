package com.github.frunoyman.adapters.bluetooth;

public interface BluetoothProfile {

    public enum State{
        /** The profile is in disconnected state */
        STATE_DISCONNECTED(0),
        /** The profile is in connecting state */
        STATE_CONNECTING(1),
        /** The profile is in connected state */
        STATE_CONNECTED(2),
        /** The profile is in disconnecting state */
        STATE_DISCONNECTING(3);

        private int state;

        State(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }

        public static State getState(int state){
            for(State states:values()){
                if (states.getState()==state){
                    return states;
                }
            }
            return null;
        }
    }

    public enum Type{

        /**
         * Headset and Handsfree profile
         */
         HEADSET(1),

        /**
         * A2DP profile.
         */
         A2DP(2),

        /**
         * Health Profile
         *
         */
         HEALTH(3),

        /**
         * HID Host
         *
         */
        HID_HOST(4),

        /**
         * PAN Profile
         *
         */
        PAN(5),

        /**
         * PBAP
         *
         */
         PBAP(6),

        /**
         * GATT
         */
        GATT(7),

        /**
         * GATT_SERVER
         */
         GATT_SERVER(8),

        /**
         * MAP Profile
         *
         */
         MAP(9),

        /*
         * SAP Profile
         * @hide
         */
        SAP(10),

        /**
         * A2DP Sink Profile
         *
         */
        A2DP_SINK(11),

        /**
         * AVRCP Controller Profile
         *
         */
        AVRCP_CONTROLLER(12),

        /**
         * AVRCP Target Profile
         *
         */
        AVRCP(13),

        /**
         * Headset Client - HFP HF Role
         *
         */
        HEADSET_CLIENT(16),

        /**
         * PBAP Client
         *
         */
        PBAP_CLIENT(17),

        /**
         * MAP Messaging Client Equipment (MCE)
         *
         */
        MAP_CLIENT(19),

        /**
         * HID Device
         */
        HID_DEVICE(19),

        /**
         * Object Push Profile (OPP)
         *
         */
        OPP(20),

        /**
         * Hearing Aid Device
         *
         */
        HEARING_AID(21),

        /**
         * Max profile ID. This value should be updated whenever a new profile is added to match
         * the largest value assigned to a profile.
         *
         */
        MAX_PROFILE_ID(21);

        private int comstant;

        Type(int comstant) {
            this.comstant = comstant;
        }

        public int getComstant() {
            return comstant;
        }

        public static Type getConstant(int constant){
            for (Type type:values()){
                if(type.getComstant()==constant){
                    return type;
                }
            }
            return null;
        }
    }
}
