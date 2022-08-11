package com.gmail.foodde.model;

public class MultiViewItem {

    private int ID, viewType;

    /*private int itemID, itemGrams, itemCategory;
    private String itemName, itemDescription, itemLink;
    private Float itemPrice;*/
    private Item item;

    public MultiViewItem(int ID, int viewType) {
        this.ID = ID;
        this.viewType = viewType;
    }
    public MultiViewItem(int ID, int viewType, Item item) {
        this.ID = ID;
        this.viewType = viewType;
        this.item = item;
    }

    /*public MultiViewItem(int ID, int viewType, int itemID, int itemGrams,
                         int itemCategory, String itemName, String itemDescription,
                         String itemLink, Float itemPrice) {
        this.ID = ID;
        this.viewType = viewType;
        this.itemID = itemID;
        this.itemGrams = itemGrams;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemLink = itemLink;
        this.itemPrice = itemPrice;
    }*/

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }
    public int getViewType() { return viewType; }
    public void setViewType(int viewType) { this.viewType = viewType; }
    public int getItemID() { return item.getID(); }
    public void setItemID(int itemID) { this.item.setID(itemID);}
    public int getItemGrams() { return item.getGrams(); }
    public void setItemGrams(int itemGrams) { this.item.setGrams(itemGrams); }
    public int getItemCategory() { return item.getCategory(); }
    public void setItemCategory(int itemCategory) { this.item.setCategory(itemCategory); }
    public String getItemName() { return item.getName(); }
    public void setItemName(String itemName) { this.item.setName(itemName); }
    public String getItemDescription() { return item.getDescription(); }
    public void setItemDescription(String itemDescription) { this.item.setDescription(itemDescription);}
    public String getItemLink() { return item.getLink(); }
    public void setItemLink(String itemLink) { this.item.setLink(itemLink); }
    public Float getItemPrice() { return item.getPrice(); }
    public void setItemPrice(Float itemPrice) { this.item.setPrice(itemPrice); }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
}
