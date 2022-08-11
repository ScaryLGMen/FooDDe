package com.gmail.foodde.model;

public class BasketItem {
    private int ID, quantity;
    private String name, description, link;
    private  Float price;

    public BasketItem(int ID, int quantity ,String name, String description, String link, Float price) {
        this.ID = ID;
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.link = link;
        this.price = price;

    }

    public int getID() { return ID; }

    public void setID(int ID) { this.ID = ID; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

    public Float getPrice() { return price; }

    public void setPrice(Float price) { this.price = price; }
}
