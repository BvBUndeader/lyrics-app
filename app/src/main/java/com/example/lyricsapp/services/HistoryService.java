package com.example.lyricsapp.services;

import com.example.lyricsapp.entities.CreateHistoryRecord;
import com.example.lyricsapp.entities.HistoryData;
import com.example.lyricsapp.entities.SongResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HistoryService {

    @POST("/songhistory")
    Call<HistoryData> addToHistory(@Body CreateHistoryRecord createHistoryRecord);

    @GET("/songhistory/recent")
    Call<List<SongResult>> listHistoryRecords(@Query("userId") long userId);
}
