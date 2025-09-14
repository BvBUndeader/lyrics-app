package com.example.lyricsapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class SongResult implements Parcelable {
    @SerializedName("title")
    private String title;
    @SerializedName("album")
    private String album;
    @SerializedName("artist")
    private String artist;

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

    public SongResult(String title, String album, String artist) {
        this.title = title;
        this.album = album;
        this.artist = artist;
    }

    public SongResult(Parcel in){
        title = in.readString();
        album = in.readString();
        artist = in.readString();
    }

    public static final Creator<SongResult> CREATOR = new Creator<SongResult>() {
        @Override
        public SongResult createFromParcel(Parcel source) {
            return new SongResult(source);
        }

        @Override
        public SongResult[] newArray(int size) {
            return new SongResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
    }
}
