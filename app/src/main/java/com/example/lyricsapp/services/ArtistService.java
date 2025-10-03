package com.example.lyricsapp.services;

import com.example.lyricsapp.entities.Artist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArtistService {

    @GET("artists/search")
    Call<Artist> fetchArtist(@Query("name") String name, @Query("lang") String lang);

    @GET("artists/multisearch")
    Call<List<Artist>> fetchMultipleArtists(@Query("name") String name);
}
