package com.example.lyricsapp.entities;

public class CreateHistoryRecord {
    private long userId;
    private long songId;

    public CreateHistoryRecord(long userId, long songId) {
        this.userId = userId;
        this.songId = songId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }
}
