package de.rocketinternet.android.tracking.trackers.ad4push;

/**
 * @author alessandro.balocco
 */
public enum RIAd4PushUserEnum {
    DEVICE_INFO("device_info"),
    GEOLOCATION("geolocation"),
    MEMBER_INFO("member_info"),
    MESSAGES("messages"),
    USER_PREFERENCES("user_preferences"),
    IGNORE("ignore");

    private final String mValue;

    private RIAd4PushUserEnum(final String value) {
        this.mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
