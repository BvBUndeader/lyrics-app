package com.example.lyricsapp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyricsapp.adapters.HistoryAdapter;
import com.example.lyricsapp.entities.SongResult;
import com.example.lyricsapp.entities.UserData;
import com.example.lyricsapp.services.HistoryService;
import com.example.lyricsapp.utility.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    TextView homeFragTV;
    UserData userData;
    LinearLayout songHistory;
    TextView songHistoryTV;
    RecyclerView songHistoryRecycler;

    Button homeFragSearchBtn;
    HistoryAdapter adapter;

    private Bundle bundle;

    @Nullable
    @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        homeFragTV = view.findViewById(R.id.homeFragTV);
        homeFragSearchBtn = view.findViewById(R.id.homeFragSearchBtn);
        songHistory = view.findViewById(R.id.songHistory);
        songHistoryTV = view.findViewById(R.id.songHistoryTV);
        songHistoryRecycler = view.findViewById(R.id.songHistoryRecycler);

        String username;

        bundle = getArguments();
        if(bundle == null){
            bundle = new Bundle();
        }

        if(getArguments() != null){
            userData = getArguments().getParcelable("userData", UserData.class);

            if(userData != null){
                bundle.putParcelable("userData", userData);
                username = userData.getUsername();
                homeFragTV.setText("Welcome " + username);
            }
        }

        songHistoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(new ArrayList<>(), new HistoryAdapter.HistoryClickListener() {
            @Override
            public void onItemClick(SongResult item, View v) {
                bundle.putParcelable("songResult", item);
                Fragment songFragment = new SongFragment();
                songFragment.setArguments(bundle);
                setCurrentFragment(songFragment);
            }
        });
        songHistoryRecycler.setAdapter(adapter);

        long userId = userData.getId();
        initializeHistory(userId);

        homeFragSearchBtn.setOnClickListener(v -> searchBtnClicked());

        return view;
   }

    @Override
    public void onResume() {
        super.onResume();
        initializeHistory(userData.getId());
    }

    public void initializeHistory(long userId){
       Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
               .addConverterFactory(GsonConverterFactory.create())
               .build();

       HistoryService historyService = retrofit.create(HistoryService.class);

       Call<List<SongResult>> call = historyService.listHistoryRecords(userId);

       call.enqueue(new Callback<List<SongResult>>() {
           @Override
           public void onResponse(Call<List<SongResult>> call, Response<List<SongResult>> response) {
               if(response.isSuccessful() && response.body() != null){
                   adapter.updateResults(response.body());
                   songHistory.setVisibility(VISIBLE);
               }else {
                   int statusCode = response.code();
                   if (statusCode == 404) {
                       adapter.updateResults(new ArrayList<>());
                       songHistory.setVisibility(GONE);
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

   public void searchBtnClicked(){

        Bundle bundle = new Bundle();
        bundle.putParcelable("userData", userData);

        Fragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        setCurrentFragment(searchFragment);
   }

    private void setCurrentFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}