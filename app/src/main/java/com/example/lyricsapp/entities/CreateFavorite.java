package com.example.lyricsapp.entities;

public class CreateFavorite {
    private String username;

    private String songTitle;

    public CreateFavorite(String username, String songTitle) {
        this.username = username;
        this.songTitle = songTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
}
