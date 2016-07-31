package com.akproject.easybuy.model;

import java.util.Date;

/**
 * Created by Allan on 30/1/2016.
 */
public class Transaction {

    private int id;
    private Item item;
    private String brand;
    private float quantity;
    private float price;
    private String shop;
    private Date date;
    private int paymentMethodId;
    private String paymentMethod;
    private boolean hasBuy;
    private Attribute[] attributes;

    public Transaction(int id, Item item, String brand, float quantity, float price, String shop,
                       Date date, int paymentMethodId, String paymentMethod, boolean hasBuy) {
        this.id = id;
        this.item = item;
        this.price = price;
        this.shop = shop;
        this.date = date;
        this.paymentMethodId = paymentMethodId;
        this.paymentMethod = paymentMethod;
        this.hasBuy = hasBuy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isHasBuy() {
        return hasBuy;
    }

    public void setHasBuy(boolean hasBuy) {
        this.hasBuy = hasBuy;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }
}
