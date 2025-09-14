package com.example.lyricsapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyricsapp.entities.CreateFavorite;
import com.example.lyricsapp.entities.FavoriteData;
import com.example.lyricsapp.entities.FavoriteResult;
import com.example.lyricsapp.entities.Song;
import com.example.lyricsapp.entities.SongResult;
import com.example.lyricsapp.entities.UserData;
import com.example.lyricsapp.services.FavoriteService;
import com.example.lyricsapp.services.SongService;
import com.example.lyricsapp.utility.RequestHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongFragment extends Fragment {

    TextView songTitleTV;
    TextView songAlbumTV;
    TextView songArtistTV;
    TextView songLyricsTV;
    ImageView favoriteBtn;

    UserData userData;
    SongResult songResult;
    Song song;
    boolean isFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_song, container, false);

       songTitleTV = view.findViewById(R.id.songTitleTV);
       songAlbumTV = view.findViewById(R.id.songAlbumTV);
       songArtistTV = view.findViewById(R.id.songArtistTV);
       songLyricsTV = view.findViewById(R.id.songLyricsTV);
       favoriteBtn = view.findViewById(R.id.favoriteBtn);



        if(getArguments() != null){
            userData = getArguments().getParcelable("userData", UserData.class);
            songResult = getArguments().getParcelable("songResult", SongResult.class);
        }

        if(songResult == null){
            Log.e("Error: ", "Song data did not parse properly");
            return view;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SongService songService = retrofit.create(SongService.class);
        FavoriteService favoriteService = retrofit.create(FavoriteService.class);

        Call<Song> call = songService.fetchSong(songResult.getTitle(), songResult.getArtist());
        call.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if(response.isSuccessful() && response.body() != null){
                    song = response.body();
                    songTitleTV.setText(song.getTitle());
                    songAlbumTV.setText(song.getAlbum());
                    songArtistTV.setText(song.getArtist());
                    songLyricsTV.setText(song.getLyrics());

                    Call<FavoriteResult> favCheckCall = favoriteService.checkFavorite(userData.getId(), song.getTitle());
                    favCheckCall.enqueue(new Callback<FavoriteResult>() {
                        @Override
                        public void onResponse(Call<FavoriteResult> call, Response<FavoriteResult> response) {
                            if(response.isSuccessful()){
                                favoriteBtn.setImageResource(R.drawable.favorite_star_filled);
                                isFavorite = true;
                            }
                            int responseCode = response.code();
                            if(responseCode >= 500){
                                Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<FavoriteResult> call, Throwable t) {
                            Log.e("API_FAILURE", t.getMessage(), t);
                        }
                    });

                    favoriteBtn.setOnClickListener(v ->{
                        if(isFavorite){
                            Call<Void> removeFavorite = favoriteService.removeFromFavorites(userData.getId(), song.getTitle());
                            removeFavorite.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()){
                                        favoriteBtn.setImageResource(R.drawable.favorite_star_outlined);
                                        isFavorite = false;
                                    }
                                    int responseCode = response.code();
                                    if(responseCode >= 500){
                                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e("API_FAILURE", t.getMessage(), t);
                                }
                            });
                        }else {
                            CreateFavorite createFavorite = new CreateFavorite(userData.getUsername(), song.getTitle());
                            Call<FavoriteData> addFavorite = favoriteService.addToFavorites(createFavorite);
                            addFavorite.enqueue(new Callback<FavoriteData>() {
                                @Override
                                public void onResponse(Call<FavoriteData> call, Response<FavoriteData> response) {
                                    if(response.isSuccessful()){
                                        favoriteBtn.setImageResource(R.drawable.favorite_star_filled);
                                        isFavorite = true;
                                    }else{
                                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<FavoriteData> call, Throwable t) {
                                    Log.e("API_FAILURE", t.getMessage(), t);
                                }
                            });
                        }
                    });

                }else {
                    Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });









        return view;
    }
}