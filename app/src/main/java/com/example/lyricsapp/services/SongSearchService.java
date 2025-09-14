package com.example.lyricsapp.services;

import com.example.lyricsapp.entities.SongResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SongSearchService {
    @GET("songs/multisearch")
    Call<List<SongResult>> searchSong(@Query("title") String title);
}
