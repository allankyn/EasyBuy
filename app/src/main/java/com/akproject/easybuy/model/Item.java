package com.akproject.easybuy.model;

/**
 * Created by Allan on 30/1/2016.
 */
public class Item {

    private int itemId;
    private int itemNameTextId;
    private String itemName;
    private boolean enable;
    private int order;

    public Item(int itemId, int itemNameTextId, String itemName, boolean enable, int order) {
        this.itemId = itemId;
        this.itemNameTextId = itemNameTextId;
        this.itemName = itemName;
        this.enable = enable;
        this.order = order;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemNameTextId() {
        return itemNameTextId;
    }

    public void setItemNameTextId(int itemNameTextId) {
        this.itemNameTextId = itemNameTextId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
