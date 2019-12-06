package com.food2go.Model;
/**
 * Created by Hy Minh Tran (Mark) on 12/03/2019
 */

public class Users {
    private String id;
    private String password;
    private String phoneNumber;

    public Users() {

    }

    public Users(String id, String password, String phoneNumber) {
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
