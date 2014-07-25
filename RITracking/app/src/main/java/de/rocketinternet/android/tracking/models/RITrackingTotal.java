package de.rocketinternet.android.tracking.models;

/**
 * @author alessandro.balocco
 */
public class RITrackingTotal {

    /**
     * Net of the order
     */
    private int mNet;
    /**
     * Tax of the order
     */
    private float mTax;
    /**
     * Shipping price of the order
     */
    private int mShipping;
    /**
     * Currency of the order
     */
    private String mCurrency;

    public RITrackingTotal(int net, float tax, int shipping, String currency) {
        mNet = net;
        mTax = tax;
        mShipping = shipping;
        mCurrency = currency;
    }

    public int getNet() {
        return mNet;
    }

    public void setNet(int net) {
        mNet = net;
    }

    public float getTax() {
        return mTax;
    }

    public void setTax(int tax) {
        mTax = tax;
    }

    public int getShipping() {
        return mShipping;
    }

    public void setShipping(int shipping) {
        mShipping = shipping;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }
}
