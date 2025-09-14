package com.example.lyricsapp.services;

import com.example.lyricsapp.entities.CreateFavorite;
import com.example.lyricsapp.entities.FavoriteData;
import com.example.lyricsapp.entities.FavoriteResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteService {
    @GET("/favoritescheck")
    Call<FavoriteResult> checkFavorite(@Query("userId") long userId,@Query("songTitle") String songTitle);

    @GET("/favorites/{userId}")
    Call<List<FavoriteResult>> listFavorites(@Path("userId") long userId);

    @POST("/favorites")
    Call<FavoriteData> addToFavorites(@Body CreateFavorite createFavorite);

    @DELETE("/favorites/delete/{userId}/{songTitle}")
    Call<Void> removeFromFavorites(@Path("userId") long userId,@Path("songTitle") String songTitle);
}
