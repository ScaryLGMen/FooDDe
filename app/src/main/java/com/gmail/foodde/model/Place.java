package com.gmail.foodde.model;

public class Place {
    private int ID;
    private String text;
    Boolean check, isFound;
    Double latitude, longitude;
    public Place(int ID, String text, boolean check, boolean isFound, Double latitude , Double longitude) {
        this.ID = ID;
        this.text = text;
        this.check = check;
        this.isFound = isFound;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Boolean getCheck() { return check; }
    public void setCheck(Boolean check) { this.check = check; }
    public Boolean getFound() { return isFound; }
    public void setFound(Boolean found) { isFound = found; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
