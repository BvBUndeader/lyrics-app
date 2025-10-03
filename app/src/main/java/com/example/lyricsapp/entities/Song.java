package com.example.lyricsapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Song implements Parcelable {

    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("album")
    private String album;
    @SerializedName("artist")
    private String artist;
    @SerializedName("lyrics")
    private String lyrics;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Song() {
    }
    public Song(String title, String album, String artist, String lyrics) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.lyrics = lyrics;
    }

    protected Song(Parcel in) {
        id = in.readLong();
        title = in.readString();
        album = in.readString();
        artist = in.readString();
        lyrics = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeString(lyrics);
    }
}
