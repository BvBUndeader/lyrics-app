package com.example.lyricsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.example.lyricsapp.utility.RequestHelper;

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

public class RegisterActivity extends AppCompatActivity {

    EditText registerUsernameET;
    EditText registerPasswordET;
    EditText registerEmailET;
    TextView registerErrorTV;
    Button registerAccB;

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
        registerAccB = findViewById(R.id.registerAccB);

    }

    public void registerButtonClicked(View view){
        String username = registerUsernameET.getText().toString();
        String password = registerPasswordET.getText().toString();
        String email = registerEmailET.getText().toString();

        if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
            registerErrorTV.setText("Please fill out the fields");
            return;
        }

        executor.execute(() -> {
            try {
                String endpointString = RequestHelper.ADDRESS + RequestHelper.REGISTER_ENDPOINT;
                URL url = new URL(endpointString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json; utf-8");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(true);

                JSONObject jsonInput = new JSONObject();
                jsonInput.put("username", username);
                jsonInput.put("password", password);
                jsonInput.put("email", email);

                try(OutputStream outputStream = connection.getOutputStream()){
                    byte[] input = jsonInput.toString().getBytes("utf-8");
                    outputStream.write(input,0,input.length);
                }

                int responseCode = connection.getResponseCode();

                handler.post(()->{
                    if(responseCode == 201){
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, SearchActivity.class);
                        startActivity(intent);
                    }
                    if (responseCode == 409){
                        try {
                            InputStream stream = (responseCode < 400) ? connection.getInputStream() : connection.getErrorStream();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                            StringBuilder response = new StringBuilder();
                            String line = reader.readLine();

                            if(line.contains("Username")){
                                registerErrorTV.setText("Username already exists");
                                return;
                            }
                            if(line.contains("Email")){
                                registerErrorTV.setText("Email already exists");
                                return;
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        Toast.makeText(this, "Something broke " + responseCode, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });


    }
}

