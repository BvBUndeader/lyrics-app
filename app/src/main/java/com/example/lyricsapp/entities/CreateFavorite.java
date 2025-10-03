package com.example.lyricsapp.entities;

public class CreateFavorite {
    private String username;

    private long songId;

    public CreateFavorite(String username, long songId) {
        this.username = username;
        this.songId = songId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }
}
