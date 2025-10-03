package com.example.lyricsapp.services;

import com.example.lyricsapp.entities.PassChangeRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;

public interface UserService {
    @PATCH("users/passchange")
    Call<String> changePassword(@Body PassChangeRequest request);
}
