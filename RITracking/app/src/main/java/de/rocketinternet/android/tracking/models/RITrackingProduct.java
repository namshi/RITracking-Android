package de.rocketinternet.android.tracking.models;

/**
 * @author alessandro.balocco
 */
public class RITrackingProduct {

    /**
     * Identifier of the product
     */
    private String mIdentifier;
    /**
     * Name of the product
     */
    private String mName;
    /**
     * Quantity of the product
     */
    private long mQuantity;
    /**
     * Price of the product
     */
    private double mPrice;
    /**
     * Currency of the product price
     */
    private String mCurrency;
    /**
     * Category of the product
     */
    private String mCategory;

    public RITrackingProduct(String identifier, String name, long quantity, double price,
                             String currency, String category) {
        mIdentifier = identifier;
        mName = name;
        mQuantity = quantity;
        mPrice = price;
        mCurrency = currency;
        mCategory = category;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public void setIdentifier(String identifier) {
        mIdentifier = identifier;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getQuantity() {
        return mQuantity;
    }

    public void setQuantity(long quantity) {
        mQuantity = quantity;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
}
