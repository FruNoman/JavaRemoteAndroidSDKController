package com.github.frunoyman.adapters.wifi;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class NetworkInfo {

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public NetworkInfo() {
    }

    public enum Type {
        TYPE_NONE(-1),

        TYPE_MOBILE(0),

        TYPE_WIFI(1),

        TYPE_MOBILE_MMS(2),

        TYPE_MOBILE_SUPL(3),

        TYPE_MOBILE_DUN(4),

        TYPE_MOBILE_HIPRI(5),

        TYPE_WIMAX(6),

        TYPE_BLUETOOTH(7),

        TYPE_DUMMY(8),

        TYPE_ETHERNET(9),

        TYPE_MOBILE_FOTA(10),

        TYPE_MOBILE_IMS(11),

        TYPE_MOBILE_CBS(12),

        TYPE_WIFI_P2P(13),

        TYPE_MOBILE_IA(14),

        TYPE_MOBILE_EMERGENCY(15),

        TYPE_PROXY(16),

        TYPE_VPN(17),
        MAX_RADIO_TYPE(TYPE_VPN.getType()),
        MAX_NETWORK_TYPE(TYPE_VPN.getType()),
        MIN_NETWORK_TYPE(TYPE_MOBILE.getType()),

        DEFAULT_NETWORK_PREFERENCE(TYPE_WIFI.getType());

        private int type;

        public int getType() {
            return type;
        }

        public static Type getType(int type) {
            for (Type currentType:values()){
                if (currentType.getType()==type){
                    return currentType;
                }
            }
            return Type.TYPE_NONE;
        }

        Type(int type) {
            this.type = type;
        }
    }

    public enum State {
        CONNECTING, CONNECTED, SUSPENDED, DISCONNECTING, DISCONNECTED, UNKNOWN
    }

    public enum DetailedState {
        /**
         * Ready to start data connection setup.
         */
        IDLE,
        /**
         * Searching for an available access point.
         */
        SCANNING,
        /**
         * Currently setting up data connection.
         */
        CONNECTING,
        /**
         * Network link established, performing authentication.
         */
        AUTHENTICATING,
        /**
         * Awaiting response from DHCP server in order to assign IP address information.
         */
        OBTAINING_IPADDR,
        /**
         * IP traffic should be available.
         */
        CONNECTED,
        /**
         * IP traffic is suspended
         */
        SUSPENDED,
        /**
         * Currently tearing down data connection.
         */
        DISCONNECTING,
        /**
         * IP traffic not available.
         */
        DISCONNECTED,
        /**
         * Attempt to connect failed.
         */
        FAILED,
        /**
         * Access to this network is blocked.
         */
        BLOCKED,
        /**
         * Link has poor connectivity.
         */
        VERIFYING_POOR_LINK,
        /**
         * Checking if network is a captive portal
         */
        CAPTIVE_PORTAL_CHECK
    }

    /**
     * This is the map described in the Javadoc comment above. The positions
     * of the elements of the array must correspond to the ordinal values
     * of <code>DetailedState</code>.
     */
    private static final EnumMap<DetailedState, State> stateMap =
            new EnumMap<DetailedState, State>(DetailedState.class);

    static {
        stateMap.put(DetailedState.IDLE, State.DISCONNECTED);
        stateMap.put(DetailedState.SCANNING, State.DISCONNECTED);
        stateMap.put(DetailedState.CONNECTING, State.CONNECTING);
        stateMap.put(DetailedState.AUTHENTICATING, State.CONNECTING);
        stateMap.put(DetailedState.OBTAINING_IPADDR, State.CONNECTING);
        stateMap.put(DetailedState.VERIFYING_POOR_LINK, State.CONNECTING);
        stateMap.put(DetailedState.CAPTIVE_PORTAL_CHECK, State.CONNECTING);
        stateMap.put(DetailedState.CONNECTED, State.CONNECTED);
        stateMap.put(DetailedState.SUSPENDED, State.SUSPENDED);
        stateMap.put(DetailedState.DISCONNECTING, State.DISCONNECTING);
        stateMap.put(DetailedState.DISCONNECTED, State.DISCONNECTED);
        stateMap.put(DetailedState.FAILED, State.DISCONNECTED);
        stateMap.put(DetailedState.BLOCKED, State.DISCONNECTED);
    }

    private int type;
    private int subtype;
    private String typeName;
    private String subtypeName;
    private State state;
    private DetailedState detailedState;
    private String reason;
    private String extraInfo;

    private boolean connected;
    private boolean connectedOrConnecting;
    private boolean available;
    private boolean failover;
    private boolean roaming;

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setSubtypeName(String subtypeName) {
        this.subtypeName = subtypeName;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setDetailedState(DetailedState detailedState) {
        this.detailedState = detailedState;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setConnectedOrConnecting(boolean connectedOrConnecting) {
        this.connectedOrConnecting = connectedOrConnecting;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    //    public NetworkInfo(int type, int subtype, String typeName, String subtypeName) {
//        if (!ConnectivityManager.isNetworkTypeValid(type)
//                && type != ConnectivityManager.TYPE_NONE) {
//            throw new IllegalArgumentException("Invalid network type: " + type);
//        }
//        mNetworkType = type;
//        mSubtype = subtype;
//        mTypeName = typeName;
//        mSubtypeName = subtypeName;
//        setDetailedState(DetailedState.IDLE, null, null);
//        mState = State.UNKNOWN;
//    }


    public NetworkInfo(NetworkInfo source) {
        if (source != null) {
            synchronized (source) {
                type = source.type;
                subtype = source.subtype;
                typeName = source.typeName;
                subtypeName = source.subtypeName;
                state = source.state;
                detailedState = source.detailedState;
                reason = source.reason;
                extraInfo = source.extraInfo;
                failover = source.failover;
                available = source.available;
                roaming = source.roaming;
            }
        }
    }

    public Type getType() {
        synchronized (this) {
            return Type.getType(type);
        }
    }

    public void setType(int type) {
        synchronized (this) {
            this.type = type;
        }
    }

    /**
     * Return a network-type-specific integer describing the subtype
     * of the network.
     *
     * @return the network subtype
     */
    public int getSubtype() {
        synchronized (this) {
            return subtype;
        }
    }

    /**
     *
     */
    public void setSubtype(int subtype, String subtypeName) {
        synchronized (this) {
            this.subtype = subtype;
            this.subtypeName = subtypeName;
        }
    }

    /**
     * Return a human-readable name describe the type of the network,
     * for example "WIFI" or "MOBILE".
     *
     * @return the name of the network type
     * instead with one of the NetworkCapabilities#TRANSPORT_* constants :
     * {@link #getType} and {@link #getTypeName} cannot account for networks using
     * multiple transports. Note that generally apps should not care about transport;
     * apps concerned with meteredness or bandwidth should be looking at, as they
     * offer this information with much better accuracy.
     */
    @Deprecated
    public String getTypeName() {
        synchronized (this) {
            return typeName;
        }
    }

    /**
     * Return a human-readable name describing the subtype of the network.
     *
     * @return the name of the network subtype
     */
    public String getSubtypeName() {
        synchronized (this) {
            return subtypeName;
        }
    }

    /**
     * Indicates whether network connectivity exists or is in the process
     * of being established. This is good for applications that need to
     * do anything related to the network other than read or write data.
     * For the latter, call {@link #isConnected()} instead, which guarantees
     * that the network is fully usable.
     *
     * @return {@code true} if network connectivity exists or is in the process
     * of being established, {@code false} otherwise.
     * @deprecated Apps should instead use the
     * learn about connectivity changes.
     * give a more accurate picture of the connectivity state of
     * the device and let apps react more easily and quickly to changes.
     */
    @Deprecated
    public boolean isConnectedOrConnecting() {
        synchronized (this) {
            return state == State.CONNECTED || state == State.CONNECTING;
        }
    }

    /**
     * Indicates whether network connectivity exists and it is possible to establish
     * connections and pass data.
     * <p>Always call this before attempting to perform data transactions.
     *
     * @return {@code true} if network connectivity exists, {@code false} otherwise.
     */
    public boolean isConnected() {
        synchronized (this) {
            return state == State.CONNECTED;
        }
    }

    /**
     * Indicates whether network connectivity is possible. A network is unavailable
     * when a persistent or semi-persistent condition prevents the possibility
     * of connecting to that network. Examples include
     * <ul>
     * <li>The device is out of the coverage area for any network of this type.</li>
     * <li>The device is on a network other than the home network (i.e., roaming), and
     * data roaming has been disabled.</li>
     * <li>The device's radio is turned off, e.g., because airplane mode is enabled.</li>
     * </ul>
     * Since Android L, this always returns {@code true}, because the system only
     * returns info for available networks.
     *
     * @return {@code true} if the network is available, {@code false} otherwise
     * @deprecated Apps should instead use the
     * learn about connectivity changes.
     * give a more accurate picture of the connectivity state of
     * the device and let apps react more easily and quickly to changes.
     */
    @Deprecated
    public boolean isAvailable() {
        synchronized (this) {
            return available;
        }
    }

    /**
     * Sets if the network is available, ie, if the connectivity is possible.
     *
     * @param isAvailable the new availability value.
     */
    @Deprecated
    public void setIsAvailable(boolean isAvailable) {
        synchronized (this) {
            available = isAvailable;
        }
    }

    /**
     * Indicates whether the current attempt to connect to the network
     * resulted from the ConnectivityManager trying to fail over to this
     * network following a disconnect from another network.
     *
     * @return {@code true} if this is a failover attempt, {@code false}
     * otherwise.
     * @deprecated This field is not populated in recent Android releases,
     * and does not make a lot of sense in a multi-network world.
     */
    @Deprecated
    public boolean isFailover() {
        synchronized (this) {
            return failover;
        }
    }

    /**
     * Set the failover boolean.
     *
     * @param isFailover {@code true} to mark the current connection attempt
     *                   as a failover.
     * @hide
     * @deprecated This hasn't been set in any recent Android release.
     */
    @Deprecated
    public void setFailover(boolean isFailover) {
        synchronized (this) {
            failover = isFailover;
        }
    }

    /**
     * Indicates whether the device is currently roaming on this network. When
     * {@code true}, it suggests that use of data on this network may incur
     * extra costs.
     *
     * @return {@code true} if roaming is in effect, {@code false} otherwise.
     * @deprecated Callers should switch to checking
     * instead, since that handles more complex situations, such as
     * VPNs.
     */
    @Deprecated
    public boolean isRoaming() {
        synchronized (this) {
            return roaming;
        }
    }

    /**
     *
     */
    @Deprecated
    public void setRoaming(boolean isRoaming) {
        synchronized (this) {
            roaming = isRoaming;
        }
    }

    /**
     * Reports the current coarse-grained state of the network.
     *
     * @return the coarse-grained state
     * @deprecated Apps should instead use the
     * learn about connectivity changes.
     * give a more accurate picture of the connectivity state of
     * the device and let apps react more easily and quickly to changes.
     */
    @Deprecated
    public State getState() {
        synchronized (this) {
            return state;
        }
    }

    /**
     * Reports the current fine-grained state of the network.
     *
     * @return the fine-grained state
     */
    public DetailedState getDetailedState() {
        synchronized (this) {
            return detailedState;
        }
    }

    /**
     * Sets the fine-grained state of the network.
     *
     * @param detailedState the {@link DetailedState}.
     * @param reason        a {@code String} indicating the reason for the state change,
     *                      if one was supplied. May be {@code null}.
     * @param extraInfo     an optional {@code String} providing addditional network state
     *                      information passed up from the lower networking layers.
     */
    @Deprecated
    public void setDetailedState(DetailedState detailedState, String reason, String extraInfo) {
        synchronized (this) {
            this.detailedState = detailedState;
            this.state = stateMap.get(detailedState);
            this.reason = reason;
            this.extraInfo = extraInfo;
        }
    }

    /**
     * Set the extraInfo field.
     *
     * @param extraInfo an optional {@code String} providing addditional network state
     *                  information passed up from the lower networking layers.
     * @hide
     */
    public void setExtraInfo(String extraInfo) {
        synchronized (this) {
            this.extraInfo = extraInfo;
        }
    }

    /**
     * Report the reason an attempt to establish connectivity failed,
     * if one is available.
     *
     * @return the reason for failure, or null if not available
     * @deprecated This method does not have a consistent contract that could make it useful
     * to callers.
     */
    public String getReason() {
        synchronized (this) {
            return reason;
        }
    }

    /**
     * Report the extra information about the network state, if any was
     * provided by the lower networking layers.
     *
     * @return the extra information, or null if not available
     */
    public String getExtraInfo() {
        synchronized (this) {
            return extraInfo;
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            StringBuilder builder = new StringBuilder("[");
            builder.append("type: ").append(getTypeName()).append("[").append(getSubtypeName()).
                    append("], state: ").append(state).append("/").append(detailedState).
                    append(", reason: ").append(reason == null ? "(unspecified)" : reason).
                    append(", extra: ").append(extraInfo == null ? "(none)" : extraInfo).
                    append(", failover: ").append(failover).
                    append(", available: ").append(available).
                    append(", roaming: ").append(roaming).
                    append("]");
            return builder.toString();
        }
    }

}
