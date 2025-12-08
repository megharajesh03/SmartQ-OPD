package com.example.demo.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean loginStatus = false;  // Default value is false

    @Column(nullable = false)
    private String accountType;  // Values can be 'A' for Admin, 'U' for User, and 'D' for Doctor

    // Constructors, Getters, and Setters

    public User() {
    }

    public User(String id, String password, boolean loginStatus, String accountType) {
        this.id = id;
        this.password = password;
        this.loginStatus = loginStatus;
        this.accountType = accountType;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        // You can add validation here if you like
        if (!accountType.equals("A") && !accountType.equals("P") && !accountType.equals("D")) {
            throw new IllegalArgumentException("Invalid account type. Must be 'A', 'P', or 'D'.");
        }
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', password='" + password + "', loginStatus=" + loginStatus + ", accountType='" + accountType + "'}";
    }
}
