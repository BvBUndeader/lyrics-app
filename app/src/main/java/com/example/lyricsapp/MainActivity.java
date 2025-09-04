package com.example.lyricsapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    TextView idkTV;

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment homeFragment = new HomeFragment();
        Fragment searchFragment = new SearchFragment();
        Fragment favoritesFragment = new FavoritesFragment();
        Fragment settingsFragment = new SettingsFragment();

        setCurrentFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    setCurrentFragment(homeFragment);
                    break;
                case R.id.search:
                    setCurrentFragment(searchFragment);
                    break;
                case R.id.favorites:
                    setCurrentFragment(favoritesFragment);
                    break;
                case R.id.more:
                    setCurrentFragment(settingsFragment);
                    break;
            }
            return true;
        });

//        Intent intent = getIntent();
//
//        String username = intent.getStringExtra("username");
//
//        idkTV = findViewById(R.id.idkTV);
//
//        idkTV.setText("Hello " + username);
//
//        Intent swap = new Intent(this, SongActivity.class);
//        startActivity(swap);
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}