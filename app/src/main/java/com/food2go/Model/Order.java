package com.food2go.Model;

import java.util.HashMap;

/**
 * Created by Hy Minh Tran (Mark) on 12/05/2019
 */
public class Order extends HashMap<String, String> {
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private String userPhone;


    public Order(String productId, String productName, int quantity, double price, String userPhone) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.userPhone = userPhone;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }
}
