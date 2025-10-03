package com.example.lyricsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.lyricsapp.entities.PassChangeRequest;
import com.example.lyricsapp.entities.UserData;
import com.example.lyricsapp.services.UserService;
import com.example.lyricsapp.utility.RequestHelper;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePassDialog extends DialogFragment {

    private EditText oldPassET;
    private EditText newPassET;

    TextInputLayout oldPassTextInputLayout;
    TextInputLayout newPassTextInputLayout;

    Button changePassB;
    Button cancelChangePassB;

    private UserData userData;

    PassChangeListener listener;

    public ChangePassDialog(PassChangeListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_password, container, false);

        if (getArguments() != null){
            userData = getArguments().getParcelable("userData", UserData.class);
        }

        oldPassET = view.findViewById(R.id.oldPassET);
        newPassET = view.findViewById(R.id.newPassET);
        oldPassTextInputLayout = view.findViewById(R.id.oldPassTextInputLayout);
        newPassTextInputLayout = view.findViewById(R.id.newPassTextInputLayout);
        changePassB = view.findViewById(R.id.changePassB);
        cancelChangePassB = view.findViewById(R.id.cancelChangePassB);
        long userId = userData.getId();

        changePassB.setOnClickListener(v -> changePass(userId));
        cancelChangePassB.setOnClickListener(v -> dismiss());

        return view;
    }

    public void changePass(long userId){

        String oldPass = oldPassET.getText().toString();
        String newPass = newPassET.getText().toString();

        oldPassTextInputLayout.setError(null);
        newPassTextInputLayout.setError(null);

        if(oldPass.isEmpty() || newPass.isEmpty()){
            oldPassTextInputLayout.setError(null);
            if(oldPass.isEmpty()){
                oldPassTextInputLayout.setError("Please fill the field");
            }
            newPassTextInputLayout.setError(null);
            if(newPass.isEmpty()){
                newPassTextInputLayout.setError("Please fill the field");
            }
            return;
        }

        if(oldPass.equals(newPass)){
            oldPassTextInputLayout.setError("New Password cannot be the current one");
            newPassTextInputLayout.setError("New Password cannot be the current one");
            return;
        }

        PassChangeRequest request = new PassChangeRequest(userId, newPass);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);

        Call<String> call = userService.changePassword(request);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null){
                    listener.onPasswordChange(response.body());
                    dismiss();
                }
                else {
                    Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });
//
//        call.enqueue(new Callback<PassChangeResponse>() {
//            @Override
//            public void onResponse(Call<PassChangeResponse> call, Response<PassChangeResponse> response) {
//                if(response.isSuccessful() && response.body() != null){
//                    listener.onPasswordChange(String.valueOf(response.body()));
//                    dismiss();
//                }
//                else {
//                    Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PassChangeResponse> call, Throwable t) {
//                Log.e("API_FAILURE", t.getMessage(), t);
//            }
//        });


    }

    public interface PassChangeListener{
        void onPasswordChange(String message);
    }
}
