package de.rocketinternet.android.tracking.models;

/**
 * @author alessandro.balocco
 *
 * Model class that defines the total of a checkout
 */
public class RITrackingTotal {
    /**
     * Net of the order
     */
    private double mNet;
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

    public RITrackingTotal() {
    }

    public RITrackingTotal(double net, float tax, int shipping,
                           String currency) {
        mNet = net;
        mTax = tax;
        mShipping = shipping;
        mCurrency = currency;
    }

    public double getNet() {
        return mNet;
    }

    public void setNet(double net) {
        mNet = net;
    }

    public float getTax() {
        return mTax;
    }

    public void setTax(float tax) {
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
