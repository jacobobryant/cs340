package com.thefunteam.android.model.InGameModel;

public class MessageCommand {
    private String sessionId;
    private String message;

    public MessageCommand(String sessionId, String message) {
        this.sessionId = sessionId;
        this.message = message;
    }
}
