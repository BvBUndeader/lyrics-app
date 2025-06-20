package com.example.lyricsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.autofill.Validator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lyricsapp.utility.RequestHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText usernameET;
    EditText passwordET;
    Button loginB;
    Button registerB;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        loginB = findViewById(R.id.loginB);
        registerB = findViewById(R.id.registerB);

    }

    public void login(View view){
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill out the fields", Toast.LENGTH_SHORT).show();
        }

        executor.execute(() -> {
           try {
               String endpointString = RequestHelper.ADDRESS + RequestHelper.LOGIN_ENDPOINT;
               endpointString = String.format(endpointString, username, password);
               URL url = new URL(endpointString);
               HttpURLConnection connection = (HttpURLConnection) url.openConnection();
               connection.setRequestMethod("GET");
               connection.connect();


               int responseCode = connection.getResponseCode();

               Log.i("LyricsApp", "the response is " + responseCode);
               System.out.println(responseCode);

               handler.post(() -> {
                   if(responseCode == 200){
                       Toast.makeText(this, "Bravo, shit works", Toast.LENGTH_SHORT).show();
                   } else if (responseCode == 404) {
                       Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(this, "Something broke", Toast.LENGTH_SHORT).show();
                   }
               });


           }
           catch (IOException e) {
               throw new RuntimeException(e);
           }
        });

    }

    public void register(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}