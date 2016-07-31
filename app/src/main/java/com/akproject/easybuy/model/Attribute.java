package com.akproject.easybuy.model;

/**
 * Created by Allan on 17/1/2016.
 */
public class Attribute {

    private int attributeId;
    private int attributeNameTextId;
    private String attributeName;
    private boolean enable;
    private int order;

    public Attribute(int attributeId, int attributeNameTextId, String attributeName, boolean enable, int order) {
        this.attributeId = attributeId;
        this.attributeNameTextId = attributeNameTextId;
        this.attributeName = attributeName;
        this.enable = enable;
        this.order = order;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public int getAttributeNameTextId() {
        return attributeNameTextId;
    }

    public void setAttributeNameTextId(int attributeNameTextId) {
        this.attributeNameTextId = attributeNameTextId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
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
