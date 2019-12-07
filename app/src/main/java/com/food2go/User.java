package com.food2go;

public class User {

    String username;
    String email;
    int phone;
    String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, int phone, String password) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

}