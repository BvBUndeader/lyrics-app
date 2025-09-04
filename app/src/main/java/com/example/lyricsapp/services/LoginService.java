package com.example.lyricsapp.services;

import com.example.lyricsapp.entities.UserData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginService {
    @GET("/login")
    Call<UserData> login(@Query("username")String username, @Query("password")String password);
}
