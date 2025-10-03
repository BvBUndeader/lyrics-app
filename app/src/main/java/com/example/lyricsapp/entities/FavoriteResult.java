package com.example.lyricsapp.entities;

import com.google.gson.annotations.SerializedName;

public class FavoriteResult {
    @SerializedName("songId")
    private long songId;
    @SerializedName("songTitle")
    private String songTitle;
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("albumName")
    private String albumName;

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
