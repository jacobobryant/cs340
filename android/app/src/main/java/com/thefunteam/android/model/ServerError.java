package com.thefunteam.android.model;

public class ServerError {
    String code;
    String message;

    public ServerError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
