package com.gmail.foodde.model;

public class Item {

    private int ID, category, grams;
    private String name, description, link;
    private Float price;

    public Item(int ID, String name, String description, int category, Float price, String link, int grams) {
        this.ID = ID;
        this.category = category;
        this.name = name;
        this.description = description;
        this.link = link;
        this.price = price;
        this.grams = grams;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getGrams() { return grams; }

    public void setGrams(int grams) { this.grams = grams; }
}
