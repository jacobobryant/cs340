package com.thefunteam.android.model;

public class LoginCommand {
    String username;
    String password;

    public LoginCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
