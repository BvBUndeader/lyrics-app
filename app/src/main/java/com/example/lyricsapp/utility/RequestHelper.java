package com.example.lyricsapp.utility;

public class RequestHelper {

    public static final String ADDRESS = "http://10.0.2.2:5294";
//    public static final String PORT = "7131";

    public static final String LOGIN_ENDPOINT = "/login?username=%s&password=%s";
    public static final String REGISTER_ENDPOINT = "/users";
    public static final String MULTI_SONG_ENDPOINT = "/songs/multisearch?title=%s";
    public static final String SINGLE_SONG_ENDPOINT = "/songs/search?title=%s";
}
