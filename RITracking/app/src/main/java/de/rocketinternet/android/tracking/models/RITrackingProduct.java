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
    private int mQuantity;
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
    /**
     * Sub-category of the product
     */
    private String mSubCategory;
    /**
     * Brand of the product
     */
    private String mBrand;
    /**
     * Percentage of discount of this product
     */
    private int mDiscount;
    /**
     * Average rating total value
     */
    private float mAverageRating;

    public RITrackingProduct() {
    }

    public RITrackingProduct(String identifier, String name, int quantity, double price,
                             String currency, String category, String subCategory, String brand,
                             int discount, float averageRating) {
        mIdentifier = identifier;
        mName = name;
        mQuantity = quantity;
        mPrice = price;
        mCurrency = currency;
        mCategory = category;
        mSubCategory = subCategory;
        mBrand = brand;
        mDiscount = discount;
        mAverageRating = averageRating;
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

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
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

    public String getSubCategory() {
        return mSubCategory;
    }

    public void setSubCategory(String subCategory) {
        mSubCategory = subCategory;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        mBrand = brand;
    }

    public int getDiscount() {
        return mDiscount;
    }

    public void setDiscount(int discount) {
        mDiscount = discount;
    }

    public float getAverageRating() {
        return mAverageRating;
    }

    public void setAverageRating(float averageRating) {
        mAverageRating = averageRating;
    }
}
