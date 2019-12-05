package com.food2go.Model;
/**
 * Created by Hy Minh Tran (Mark) on 12/03/2019
 */

public class Users {
    private String id;
    private String password;

    public Users() {

    }

    public Users(String id, String password) {
        this.id = id;
        this.password = password;
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
}
