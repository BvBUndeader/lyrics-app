package com.example.lyricsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyricsapp.entities.SongResult;
import com.example.lyricsapp.entities.UserData;
import com.example.lyricsapp.services.SongSearchService;
import com.example.lyricsapp.utility.RequestHelper;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    SearchBar songSearchBar;
    SearchView songSearchView;
    RecyclerView songSearchResultRecycler;
    ResultAdapter adapter;

    TextView searchErrorTV;
    private Bundle bundle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        songSearchBar = view.findViewById(R.id.songSearchBar);
        songSearchView = view.findViewById(R.id.songSearchView);
        songSearchResultRecycler = view.findViewById(R.id.songSearchResultsRecycler);
        searchErrorTV = view.findViewById(R.id.searchErrorTV);

        songSearchView.setupWithSearchBar(songSearchBar);

        UserData userData;
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

        songSearchResultRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ResultAdapter(new ArrayList<>(), new ResultAdapter.ClickListener() {
            @Override
            public void onItemClick(SongResult item, View v) {

                bundle.putParcelable("songResult", item);
                Fragment songFragment = new SongFragment();
                songFragment.setArguments(bundle);
                setCurrentFragment(songFragment);


                Log.i("Song data: ", item.getTitle() + " " + item.getAlbum() + " " + item.getArtist());

                Log.i("Message", "Wow, it works");
            }
        });
        songSearchResultRecycler.setAdapter(adapter);

        songSearchView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.showSoftInput(v.findFocus(), 0);
                }
            }
        });

        songSearchView.getEditText().setOnEditorActionListener((v, actionId, event ) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                String query = v.getText().toString();
                songSearchBar.setText(query);
                songSearchView.hide();
                performSearch(query);
                return true;
            }
            return false;
        });

        return view;
    }

    private void performSearch(String query){

        searchErrorTV.setText("");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        SongSearchService songSearchService = retrofit.create(SongSearchService.class);

        Call<List<SongResult>> call = songSearchService.searchSong(query);

        call.enqueue(new Callback<List<SongResult>>() {

            @Override
            public void onResponse(Call<List<SongResult>> call, Response<List<SongResult>> response) {
                if(response.isSuccessful() && response.body() != null){
//                    searchResults = response.body();
                    adapter.updateResults(response.body());
                }else {
                    int statusCode = response.code();
                    if(statusCode == 404){

                        adapter.updateResults(new ArrayList<>());
                        searchErrorTV.setText("No songs found");
                    }else {
                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SongResult>> call, Throwable t) {
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