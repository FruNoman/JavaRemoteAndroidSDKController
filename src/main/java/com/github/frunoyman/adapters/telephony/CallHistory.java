package com.github.frunoyman.adapters.telephony;

public class CallHistory {
    private String name;
    private String number;
    private int type;
    private long callTime;

    public CallHistory() {
    }


    public enum CallHistoryType {
        INCOMING_TYPE(1),
        OUTGOING_TYPE(2),
        MISSED_TYPE(3),
        VOICEMAIL_TYPE(4),
        REJECTED_TYPE(5),
        BLOCKED_TYPE(6),
        ANSWERED_EXTERNALLY_TYPE(7),
        UNKNOWN_TYPE(-1);

        int type;

        CallHistoryType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static CallHistoryType getType(int type) {
            for (CallHistoryType callHistoryType : values()) {
                if (callHistoryType.getType() == type) {
                    return callHistoryType;
                }
            }
            return CallHistoryType.UNKNOWN_TYPE;
        }

    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public CallHistoryType getType() {
        return CallHistoryType.getType(type);
    }

    public long getCallTime() {
        return callTime;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", type=" + getType() +
                ", callTime=" + callTime +
                '}';
    }
}
