package de.rocketinternet.android.tracking.models;

import java.util.List;

import de.rocketinternet.android.tracking.models.enums.RITrackingPaymentMethod;

/**
 * @author alessandro.balocco
 *
 * Model class that defines the transaction describing a checkout operation
 */
public class RITrackingTransaction {
    /**
     * The id of the transaction
     */
    private String mTransactionId;
    /**
     * The transaction affiliation
     */
    private String mAffiliation;
    /**
     * The payment method used for this transaction
     */
    private RITrackingPaymentMethod mPaymentMethod;
    /**
     * The voucher amount
     */
    private float mVoucherAmount;
    /**
     * The number of previous purchases by the user or 0 if this is a new user
     */
    private int mNumberOfPreviousPurchases;
    /**
     * The total information of the transaction
     */
    private RITrackingTotal mTotal;
    /**
     * The list of the products that are ready to be purchased
     */
    private List<RITrackingProduct> mProductsList;

    public RITrackingTransaction() {
    }

    public RITrackingTransaction(String transactionId, String affiliation, RITrackingPaymentMethod paymentMethod,
                                 float voucherAmount, int numberOfPreviousPurchases, RITrackingTotal total,
                                 List<RITrackingProduct> products) {
        mTransactionId = transactionId;
        mAffiliation = affiliation;
        mPaymentMethod = paymentMethod;
        mVoucherAmount = voucherAmount;
        mNumberOfPreviousPurchases = numberOfPreviousPurchases;
        mTotal = total;
        mProductsList = products;
    }

    public String getTransactionId() {
        return mTransactionId;
    }

    public void setTransactionId(String transactionId) {
        mTransactionId = transactionId;
    }

    public String getAffiliation() {
        return mAffiliation;
    }

    public void setAffiliation(String affiliation) {
        mAffiliation = affiliation;
    }

    public RITrackingPaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(RITrackingPaymentMethod paymentMethod) {
        mPaymentMethod = paymentMethod;
    }

    public float getVoucherAmount() {
        return mVoucherAmount;
    }

    public void setVoucherAmount(float voucherAmount) {
        mVoucherAmount = voucherAmount;
    }

    public int getNumberOfPreviousPurchases() {
        return mNumberOfPreviousPurchases;
    }

    public void setNumberOfPreviousPurchases(int numberOfPreviousPurchases) {
        mNumberOfPreviousPurchases = numberOfPreviousPurchases;
    }

    public RITrackingTotal getTotal() {
        return mTotal;
    }

    public void setTrackingTotal(RITrackingTotal total) {
        mTotal = total;
    }

    public List<RITrackingProduct> getProductsList() {
        return mProductsList;
    }

    public void setProductsList(List<RITrackingProduct> products) {
        mProductsList = products;
    }
}
