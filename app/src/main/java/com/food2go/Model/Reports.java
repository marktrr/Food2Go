package com.food2go.Model;

import java.util.List;

/**
 * Created by Hy Minh Tran (Mark) on 12/07/2019
 */
public class Reports {
    private String phoneNumber;
    private String customer;
    private String address;
    private String total;
    private String status;

    private List<Order> food;

    public Reports() {

    }

    public Reports(String phoneNumber, String customer, String address, String total, String status, List<Order> food) {
        this.phoneNumber = phoneNumber;
        this.customer = customer;
        this.address = address;
        this.total = total;
        this.status = status;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getFood() {
        return food;
    }

    public void setFood(List<Order> food) {
        this.food = food;
    }
}
