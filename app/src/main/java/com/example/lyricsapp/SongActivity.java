package com.example.lyricsapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lyricsapp.utility.RequestHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SongActivity extends AppCompatActivity {

    TextView songTitleTV;
    TextView songAlbumTV;
    TextView songArtistTV;
    TextView songLyricsTV;

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_song);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        songTitleTV = findViewById(R.id.songTitleTV);
        songAlbumTV = findViewById(R.id.songAlbumTV);
        songArtistTV = findViewById(R.id.songArtistTV);
        songLyricsTV = findViewById(R.id.songLyricsTV);

        String title = "limousine";
        String artist = "bring me the horizon";

        executor.execute(() -> {
            try{
                String endpointString = RequestHelper.ADDRESS + RequestHelper.SINGLE_SONG_ENDPOINT;
                endpointString = String.format(endpointString, title, artist);
                URL url = new URL(endpointString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();

                handler.post(() ->{
                    if (responseCode == 200){
                        InputStream is = null;
                        try {
                            is = (responseCode < 400) ? connection.getInputStream() : connection.getErrorStream();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while (true) {
                            try {
                                if (!((line = reader.readLine()) != null)) break;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            sb.append(line);
                        }


                        try {
                            JSONObject jsonObject = new JSONObject(sb.toString());
                            songTitleTV.setText(jsonObject.getString("title"));
                            songAlbumTV.setText(jsonObject.getString("album"));
                            songArtistTV.setText(jsonObject.getString("artist"));
                            songLyricsTV.setText(jsonObject.getString("lyrics"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

            } catch (IOException e){
                throw new RuntimeException(e);
            }

        });
    }
}