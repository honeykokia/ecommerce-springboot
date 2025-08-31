package com.example.demo.enums;

public enum UserStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    BANNED("banned");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
