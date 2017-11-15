package com.thefunteam.android.model.shared;

public class FaceupTrainCommand {
    public final String sessionId;
    public final int index;

    public FaceupTrainCommand(String sessionId, int index) {
        this.sessionId = sessionId;
        this.index = index;
    }
}

