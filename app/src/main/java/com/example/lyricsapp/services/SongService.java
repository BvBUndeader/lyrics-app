package com.example.lyricsapp.services;

import com.example.lyricsapp.entities.Song;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SongService {
    @GET("songs/search")
    Call<Song> fetchSong(@Query("title") String title, @Query("artist") String artist);
}
