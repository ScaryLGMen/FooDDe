package com.gmail.foodde.model;

public class Category {
    private int ID;
    private String  title, link;

    public Category(int ID, String title, String link) {
        this.ID = ID;
        this.title = title;
        this.link = link;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}
