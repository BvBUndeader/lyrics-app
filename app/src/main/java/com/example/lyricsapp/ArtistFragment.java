package com.example.lyricsapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lyricsapp.adapters.AlbumAdapter;
import com.example.lyricsapp.adapters.SongAdapter;
import com.example.lyricsapp.entities.Album;
import com.example.lyricsapp.entities.Artist;
import com.example.lyricsapp.entities.SongResult;
import com.example.lyricsapp.entities.UserData;
import com.example.lyricsapp.services.AlbumService;
import com.example.lyricsapp.services.ArtistService;
import com.example.lyricsapp.utility.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtistFragment extends Fragment {

    TextView artistNameTV;
    TextView artistBioTV;
    RecyclerView artistAlbumsRecycler;

    private UserData userData;
    private SongResult songResult;
    private Artist artist;

    private Album openedFromAlbum;
    private String artistName;

    AlbumAdapter adapter;

    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        artistNameTV = view.findViewById(R.id.artistNameTV);
        artistBioTV = view.findViewById(R.id.artistBioTV);
        artistAlbumsRecycler = view.findViewById(R.id.artistAlbumsRecycler);

        if(bundle == null){
            bundle = new Bundle();
        }

        if(getArguments() != null){
            userData = getArguments().getParcelable("userData", UserData.class);
            songResult = getArguments().getParcelable("songResult", SongResult.class);
            openedFromAlbum = getArguments().getParcelable("openedAlbum", Album.class);
            bundle.putParcelable("userData", userData);
        }

        artistAlbumsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AlbumAdapter(new ArrayList<>(), new AlbumAdapter.AlbumClickListener() {
            @Override
            public void onItemClick(Album item, View v) {
                bundle.putParcelable("openedAlbum", item);
                Fragment albumFragment = new AlbumFragment();
                albumFragment.setArguments(bundle);
                setCurrentFragment(albumFragment);
            }
        });

        artistAlbumsRecycler.setAdapter(adapter);

        String lang = "en"; // hardcoded for now - localization support to be added in future

        if(openedFromAlbum != null){
            artistName = openedFromAlbum.getArtist();
        }
        if(songResult != null){
            artistName = songResult.getArtist();
        }
        loadArtist(lang,artistName);

        return view;
    }

    public void loadArtist(String lang, String artistName){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArtistService artistService = retrofit.create(ArtistService.class);
        AlbumService albumService = retrofit.create(AlbumService.class);

        Call<Artist> call = artistService.fetchArtist(artistName, lang);
        call.enqueue(new Callback<Artist>() {
            @Override
            public void onResponse(Call<Artist> call, Response<Artist> response) {
                if(response.isSuccessful() && response.body() != null){
                    artist = response.body();
                    artistNameTV.setText(artist.getName());
                    artistBioTV.setText(artist.getBio());

                    Call<List<Album>> albumCall = albumService.fetchAlbumsByArtist(artist.getName());
                    albumCall.enqueue(new Callback<List<Album>>() {
                        @Override
                        public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                            if(response.isSuccessful() && response.body() != null){
                                adapter.updateResults(response.body());
                            }
                            int statusCode = response.code();
                            if(statusCode == 404){
                                adapter.updateResults(new ArrayList<>());
                            }
                            if(statusCode >= 500){
                                Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Album>> call, Throwable t) {
                            Log.e("API_FAILURE", t.getMessage(), t);
                        }
                    });

                }
                int statusCode = response.code();
                if(statusCode >= 500){
                    Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Artist> call, Throwable t) {
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}