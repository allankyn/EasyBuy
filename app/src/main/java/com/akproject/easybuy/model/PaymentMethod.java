package com.akproject.easybuy.model;

/**
 * Created by Allan on 17/1/2016.
 */
public class PaymentMethod {

    private int paymentMethodId;
    private int paymentMethodNameTextId;
    private String paymentMethodName;
    private int cutOffDay;
    private boolean enable;
    private int order;

    public PaymentMethod(int paymentMethodId, int paymentMethodNameTextId, String paymentMethodName, int cutOffDay) {
        this.paymentMethodId = paymentMethodId;
        //this.order = order;
        //this.enable = enable;
        this.cutOffDay = cutOffDay;
        this.paymentMethodName = paymentMethodName;
        this.paymentMethodNameTextId = paymentMethodNameTextId;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getCutOffDay() {
        return cutOffDay;
    }

    public void setCutOffDay(int cutOffDay) {
        this.cutOffDay = cutOffDay;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public int getPaymentMethodNameTextId() {
        return paymentMethodNameTextId;
    }

    public void setPaymentMethodNameTextId(int paymentMethodNameTextId) {
        this.paymentMethodNameTextId = paymentMethodNameTextId;
    }
}
