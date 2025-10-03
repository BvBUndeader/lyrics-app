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
import com.example.lyricsapp.entities.SongResult;
import com.example.lyricsapp.entities.UserData;
import com.example.lyricsapp.services.AlbumService;
import com.example.lyricsapp.services.SongService;
import com.example.lyricsapp.utility.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AlbumFragment extends Fragment {

    TextView albumTitleTV;
    TextView albumArtistTV;
    TextView albumReleaseTV;
    TextView albumGenreTV;

    RecyclerView albumSongsRecycler;

    private UserData userData;
    private SongResult songResult;
    private Album album;
    private Album openedAlbum;

    SongAdapter adapter;

    private Bundle bundle;
    private String title;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album, container, false);

        albumTitleTV = view.findViewById(R.id.albumTitleTV);
        albumReleaseTV = view.findViewById(R.id.albumReleaseTV);
        albumGenreTV = view.findViewById(R.id.albumGenreTV);
        albumArtistTV = view.findViewById(R.id.albumArtistTV);

        albumSongsRecycler = view.findViewById(R.id.albumSongsRecycler);

        bundle = getArguments();
        if(bundle == null){
            bundle = new Bundle();
        }

        if(getArguments() != null){
            userData = getArguments().getParcelable("userData", UserData.class);
            songResult = getArguments().getParcelable("songResult", SongResult.class);
            openedAlbum = getArguments().getParcelable("openedAlbum", Album.class);
            bundle.putParcelable("userData", userData);
        }



        albumSongsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SongAdapter(new ArrayList<>(), new SongAdapter.SongClickListener() {
            @Override
            public void onItemClick(SongResult item, View v) {
                bundle.putParcelable("songResult", item);
                Fragment songFragment = new SongFragment();
                songFragment.setArguments(bundle);
                setCurrentFragment(songFragment);
            }
        });
        albumSongsRecycler.setAdapter(adapter);



        if(openedAlbum != null){
             title = openedAlbum.getTitle();
        }
        if(songResult != null){
            title = songResult.getAlbum();
        }
        loadAlbum(title);

        albumArtistTV.setOnClickListener(v -> {
            Fragment artistFragment = new ArtistFragment();
            artistFragment.setArguments(bundle);
            setCurrentFragment(artistFragment);
        });


        return view;
    }

    public void loadAlbum(String title){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AlbumService albumService = retrofit.create(AlbumService.class);
        SongService songService = retrofit.create(SongService.class);

        Call<Album> call = albumService.fetchSingleAlbum(title);
        call.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if(response.isSuccessful() && response.body() != null){
                    album = response.body();
                    albumTitleTV.setText(album.getTitle());
                    albumArtistTV.setText(album.getArtist());
                    albumReleaseTV.setText("Release date: " + album.getReleaseDate());
                    albumGenreTV.setText(album.getGenre());

                    Call<List<SongResult>> songCall = songService.fetchSongByAlbum(album.getTitle());
                    songCall.enqueue(new Callback<List<SongResult>>() {
                        @Override
                        public void onResponse(Call<List<SongResult>> call, Response<List<SongResult>> response) {
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
                        public void onFailure(Call<List<SongResult>> call, Throwable t) {
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
            public void onFailure(Call<Album> call, Throwable t) {
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