package com.example.lyricsapp.entities;

public class PassChangeRequest {
    private long userId;
    private String newPassword;

    public PassChangeRequest(long userId, String newPassword) {
        this.userId = userId;
        this.newPassword = newPassword;
    }
}
