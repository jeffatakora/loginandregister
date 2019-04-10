package com.example.jeff.loginandregister;

import java.util.ArrayList;

public class User {
    public String id;
    public String userName;
    public String email;
    public String password;

    static ArrayList<String> al;

    public static void createlist(){
        al=new ArrayList<String>();
    }

    public User(String id, String userName, String email, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    // properties
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    public void setName(String userName) {
        this.userName = userName;
    }
    public String getName() {
        return this.userName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return this.password;
    }
}
