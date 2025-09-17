package com.example.lyricsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lyricsapp.entities.UserData;

public class HomeFragment extends Fragment {

    TextView homeFragTV;
    UserData userData;

    @Nullable
    @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        TextView homeFragTV = view.findViewById(R.id.homeFragTV);
        Button homeFragSearchBtn = view.findViewById(R.id.homeFragSearchBtn);
        String username;

        if(getArguments() != null){
            userData = getArguments().getParcelable("userData", UserData.class);

            if(userData != null){
                username = userData.getUsername();
                homeFragTV.setText("Welcome " + username);
            }
        }

        homeFragSearchBtn.setOnClickListener(v -> searchBtnClicked());

        return view;
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