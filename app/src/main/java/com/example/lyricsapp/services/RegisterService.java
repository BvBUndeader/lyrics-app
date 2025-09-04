package com.example.lyricsapp.services;

import com.example.lyricsapp.entities.RequestRegister;
import com.example.lyricsapp.entities.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterService {
    @POST("/users")
    Call<UserData> register(@Body RequestRegister requestRegister);
}
