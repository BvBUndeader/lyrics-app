package com.example.lyricsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lyricsapp.entities.FavoriteResult;
import com.example.lyricsapp.entities.SongResult;
import com.example.lyricsapp.entities.UserData;
import com.example.lyricsapp.services.FavoriteService;
import com.example.lyricsapp.utility.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritesFragment extends Fragment {

    RecyclerView songFavoriteResultsRecycler;
    FavoriteAdapter adapter;
    private Bundle bundle;
    private UserData userData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState){
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        songFavoriteResultsRecycler = view.findViewById(R.id.songFavoriteResultsRecycler);


        bundle = getArguments();
        if(bundle == null){
            bundle = new Bundle();
        }

        if(getArguments() != null) {
            userData = getArguments().getParcelable("userData", UserData.class);

            if(userData != null){
                bundle.putParcelable("userData",userData);
            }
        }

        songFavoriteResultsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoriteAdapter(new ArrayList<>(), new FavoriteAdapter.OnFavoriteInteractionListener() {
            @Override
            public void onSongResultClick(FavoriteResult favoriteResult) {
                SongResult songData = new SongResult(favoriteResult.getSongTitle(),
                        favoriteResult.getAlbumName(), favoriteResult.getArtistName());
                bundle.putParcelable("songResult", songData);
                Fragment songFragment = new SongFragment();
                songFragment.setArguments(bundle);
                setCurrentFragment(songFragment);
            }

            @Override
            public void onRemoveFavClick(FavoriteResult favoriteResult, UserData userData, int position) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                FavoriteService favoriteService = retrofit.create(FavoriteService.class);

                Call<Void> rmFavCall = favoriteService.removeFromFavorites(userData.getId(), favoriteResult.getSongTitle());
                rmFavCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            adapter.removeItem(position);
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
            }
        }, userData);
        songFavoriteResultsRecycler.setAdapter(adapter);
        initiateFavorites();


        return view;
    }

    private void initiateFavorites(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FavoriteService favoriteService = retrofit.create(FavoriteService.class);

        Call<List<FavoriteResult>> call = favoriteService.listFavorites(userData.getId());

        call.enqueue(new Callback<List<FavoriteResult>>() {
            @Override
            public void onResponse(Call<List<FavoriteResult>> call, Response<List<FavoriteResult>> response) {
                if(response.isSuccessful() && response.body() != null){
                    adapter.updateResutls(response.body());
                }else {
                    int statusCode = response.code();
                    if(statusCode == 404){
                        adapter.updateResutls(new ArrayList<>());
                        Toast.makeText(getContext(), "This user does not have any songs in favorites", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FavoriteResult>> call, Throwable t) {
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