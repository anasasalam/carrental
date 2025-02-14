package com.aosp.vendor.carrental.service;

public class User {
    private String userId;
    private String customerId;

    public User(String userId, String customerId) {
        this.userId = userId;
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
