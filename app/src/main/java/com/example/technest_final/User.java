package com.example.technest_final;

public class User {
    public String username;
    public String email;
    public String password;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.password = "";
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
