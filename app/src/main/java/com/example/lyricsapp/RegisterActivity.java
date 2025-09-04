package com.example.lyricsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lyricsapp.entities.RequestRegister;
import com.example.lyricsapp.entities.UserData;
import com.example.lyricsapp.services.RegisterService;
import com.example.lyricsapp.utility.RequestHelper;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText registerUsernameET;
    EditText registerPasswordET;
    EditText registerEmailET;
    EditText registerPasswordConfirmET;
    TextView registerErrorTV;
    Button registerAccB;

    Button cancelB;

    TextInputLayout regUsernameTextInputLayout;
    TextInputLayout regEmailTextInputLayout;
    TextInputLayout regPasswordTextInputLayout;
    TextInputLayout regPasswordConfirmTextInputLayout;

    boolean isEmpty;
    boolean invalidEmail;
    boolean invalidPass;

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerUsernameET = findViewById(R.id.registerUsernameET);
        registerPasswordET = findViewById(R.id.registerPasswordET);
        registerEmailET = findViewById(R.id.registerEmailET);
        registerErrorTV = findViewById(R.id.registerErrorTV);
        registerPasswordConfirmET = findViewById(R.id.registerPasswordConfirmET);
        registerAccB = findViewById(R.id.registerAccB);
        cancelB = findViewById(R.id.cancelB);

        regUsernameTextInputLayout = findViewById(R.id.regUsernameTextInputLayout);
        regEmailTextInputLayout = findViewById(R.id.regEmailTextInputLayout);
        regPasswordTextInputLayout = findViewById(R.id.regPasswordTextInputLayout);
        regPasswordConfirmTextInputLayout = findViewById(R.id.regPasswordConfirmTextInputLayout);

    }

    public void registerButtonClicked(View view) {
        String username = registerUsernameET.getText().toString();
        String password = registerPasswordET.getText().toString();
        String passwordConfirm = registerPasswordConfirmET.getText().toString();
        String email = registerEmailET.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);

        if (username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || email.isEmpty()) {
            regUsernameTextInputLayout.setError(null);
            regPasswordTextInputLayout.setError(null);
            regPasswordConfirmTextInputLayout.setError(null);
            regEmailTextInputLayout.setError(null);
            if (username.isEmpty()) {
                regUsernameTextInputLayout.setError("Please fill out the field!");
            }
            if (password.isEmpty()) {
                regPasswordTextInputLayout.setError("Please fill out the field!");
            }
            if (passwordConfirm.isEmpty()) {
                regPasswordConfirmTextInputLayout.setError("Please fill out the field!");
            }
            if (email.isEmpty()) {
                regEmailTextInputLayout.setError("Please fill out the field!");
            }
            isEmpty = true;
        }



        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            regEmailTextInputLayout.setError("Please enter a valid email!");
            invalidEmail = true;
        }



        if(!password.equals(passwordConfirm)){
            regPasswordTextInputLayout.setError("Passwords do not match");
            regPasswordConfirmTextInputLayout.setError("Passwords do not match");
            invalidPass = true;
        }

        if(isEmpty || invalidEmail || invalidPass){
            return;
        }

        regUsernameTextInputLayout.setError(null);
        regEmailTextInputLayout.setError(null);
        regPasswordTextInputLayout.setError(null);
        regPasswordConfirmTextInputLayout.setError(null);



        Retrofit retrofit = new Retrofit.Builder().baseUrl(RequestHelper.ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterService registerService = retrofit.create(RegisterService.class);

        RequestRegister request = new RequestRegister(username, password, email);

        Call<UserData> call = registerService.register(request);

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if(response.isSuccessful() && response.body() != null){
                    UserData userData = response.body();
                    startActivity(intent);
                }else {
                    int statusCode = response.code();
                    if(statusCode == 409){
                        String message;
                        try {
                            message = response.errorBody().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        if(message.contains("Username")){
                            regUsernameTextInputLayout.setError("Username already exists!");
                        }
                        if(message.contains("Email")){
                            regEmailTextInputLayout.setError("Email already exists!");
                        }else {
                            Log.e("API_ERROR", "Code: " + statusCode + " Message: " + response.errorBody()+ "\"" + message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });

        }
    public void cancel (View view){
        finish();
    }
}

