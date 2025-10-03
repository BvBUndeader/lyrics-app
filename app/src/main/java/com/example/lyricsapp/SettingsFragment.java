package com.example.lyricsapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lyricsapp.entities.UserData;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment implements ChangePassDialog.PassChangeListener{

    private UserData userData;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState){
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        bundle = getArguments();
        if(bundle == null){
            bundle = new Bundle();
        }

        if(getArguments() != null){
            userData = getArguments().getParcelable("userData", UserData.class);

            if(userData != null){
                bundle.putParcelable("userData", userData);
            }
        }

        View passChangeRow = view.findViewById(R.id.passChangeRow);

        View logOutRow = view.findViewById(R.id.logOutRow);

        passChangeRow.setOnClickListener(v -> {
            ChangePassDialog dialog = new ChangePassDialog(this);
            dialog.setArguments(bundle);
            dialog.show(getParentFragmentManager(), "ChangePassDialog");
        });

        logOutRow.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }

    @Override
    public void onPasswordChange(String message) {
        Snackbar.make(requireView(),message, Snackbar.LENGTH_LONG).show();
    }
}