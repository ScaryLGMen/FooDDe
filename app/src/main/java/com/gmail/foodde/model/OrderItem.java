package com.gmail.foodde.model;

public class OrderItem {

    private String user_mail, user_phone, address, address_add_info,
            delivery_start_time, dishes, dishes_comment, status, token;
    private Double address_latitude, address_longitude;
    private float price;
    private int delivery_id, id;



    public OrderItem(int id, String user_mail, String user_phone, String address,
                     Double address_latitude, Double address_longitude,
                     String address_add_info, String delivery_start_time,
                     int delivery_id, String dishes, String dishes_comment, float price,
                     String status, String token) {
        this.user_mail = user_mail;
        this.user_phone = user_phone;
        this.address = address;
        this.address_add_info = address_add_info;
        this.delivery_start_time = delivery_start_time;
        this.dishes = dishes;
        this.dishes_comment = dishes_comment;
        this.status = status;
        this.token = token;
        this.address_latitude = address_latitude;
        this.address_longitude = address_longitude;
        this.price = price;
        this.delivery_id = delivery_id;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_add_info() {
        return address_add_info;
    }

    public void setAddress_add_info(String address_add_info) {
        this.address_add_info = address_add_info;
    }

    public String getDelivery_start_time() {
        return delivery_start_time;
    }

    public void setDelivery_start_time(String delivery_start_time) {
        this.delivery_start_time = delivery_start_time;
    }

    public String getDishes() {
        return dishes;
    }

    public void setDishes(String dishes) {
        this.dishes = dishes;
    }

    public String getDishes_comment() {
        return dishes_comment;
    }

    public void setDishes_comment(String dishes_comment) {
        this.dishes_comment = dishes_comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getAddress_latitude() {
        return address_latitude;
    }

    public void setAddress_latitude(Double address_latitude) {
        this.address_latitude = address_latitude;
    }

    public Double getAddress_longitude() {
        return address_longitude;
    }

    public void setAddress_longitude(Double address_longitude) {
        this.address_longitude = address_longitude;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        this.delivery_id = delivery_id;
    }
}
