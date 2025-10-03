package com.example.lyricsapp.services;

import com.example.lyricsapp.entities.Album;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AlbumService {
    @GET("albums/search")
    Call<Album> fetchSingleAlbum(@Query("title") String title);

    @GET("albums/multisearch")
    Call<List<Album>> fetchAlbumsByArtist(@Query("artist") String artist);
}
