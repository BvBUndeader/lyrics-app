package com.example.lyricsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lyricsapp.entities.UserData;
import com.example.lyricsapp.services.LoginService;
import com.example.lyricsapp.utility.RequestHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET;
    TextInputLayout userTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    EditText passwordET;
    Button loginB;
    Button registerB;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        loginB = findViewById(R.id.loginB);
        registerB = findViewById(R.id.registerB);
        userTextInputLayout = findViewById(R.id.userTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);

    }

    public void login(View view){
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);

        if(username.isEmpty() || password.isEmpty()){
            userTextInputLayout.setError(null);
            if(username.isEmpty()){
                userTextInputLayout.setError("Please fill out the field");
//                return;
            }
            passwordTextInputLayout.setError("Please fill out the field");
            return;
        }

        userTextInputLayout.setError(null);
        passwordTextInputLayout.setError(null);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginService loginService = retrofit.create(LoginService.class);

        Call<UserData> call = loginService.login(username, password);

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if(response.isSuccessful() && response.body()!=null){
                    UserData userData = response.body();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("userData", userData);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    int statusCode = response.code();
                    if(statusCode == 404){
                        userTextInputLayout.setError("Incorrect username or password");
                        passwordTextInputLayout.setError("Incorrect username or password");
                    } else{
                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });
    }

    public void register(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}