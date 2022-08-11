package com.gmail.foodde.model;

public class PromItem {
    private String text, description, link, expiratoin_date, discount;
    private int ID;

    public PromItem(int ID, String text, String description, String link, String expiratoin_date, String discount) {
        this.text = text;
        this.description = description;
        this.link = link;
        this.expiratoin_date = expiratoin_date;
        this.discount = discount;
        this.ID = ID;
    }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

    public String getExpiratoin_date() { return expiratoin_date; }

    public void setExpiratoin_date(String expiratoin_date) { this.expiratoin_date = expiratoin_date; }
    
    public String getDiscount() { return discount; }

    public void setDiscount(String discount) { this.discount = discount; }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
