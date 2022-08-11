package com.gmail.foodde.model;

public class DiscountItem {
    private boolean isExists;
    private int percent;
    private String promText = "";
    private String[] properties;

    public DiscountItem(String promText, String[] properties) {
        this.promText = promText;
        this.properties = properties;
    }

    public DiscountItem(boolean isExists, int percent) {
        this.isExists = isExists;
        this.percent = percent;
    }

    public DiscountItem() {

    }

    public DiscountItem(boolean isExists, int percent, String promText) {
        this.isExists = isExists;
        this.percent = percent;
        this.promText = promText;
    }

    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getPromText() { return promText; }

    public void setPromText(String promText) { this.promText = promText; }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }
}
