package de.rocketinternet.android.tracking.models.enums;

/**
 * @author alessandro.balocco
 *         <p/>
 *         Enum that defines the type of payments for a checkout transaction
 */
public enum RITrackingPaymentMethod {
    COD("COD"),
    CREDIT_CARD("Credit card"),
    GIF_CARD("Gift card");

    private String mValue;

    private RITrackingPaymentMethod(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
