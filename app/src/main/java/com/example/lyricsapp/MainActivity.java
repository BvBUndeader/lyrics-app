package com.example.lyricsapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.lyricsapp.entities.UserData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            UserData userData = bundle.getParcelable("userData", UserData.class);

            if(userData != null){
                long id = userData.getId();
                String username = userData.getUsername();
                String password = userData.getPassword();
                String email = userData.getEmail();
            }
        }



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment homeFragment = new HomeFragment();
        Fragment searchFragment = new SearchFragment();
        Fragment favoritesFragment = new FavoritesFragment();
        Fragment settingsFragment = new SettingsFragment();

        homeFragment.setArguments(bundle);
        setCurrentFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    homeFragment.setArguments(bundle);
                    setCurrentFragment(homeFragment);
                    break;
                case R.id.search:
                    searchFragment.setArguments(bundle);
                    setCurrentFragment(searchFragment);
                    break;
                case R.id.favorites:
                    favoritesFragment.setArguments(bundle);
                    setCurrentFragment(favoritesFragment);
                    break;
                case R.id.more:
                    settingsFragment.setArguments(bundle);
                    setCurrentFragment(settingsFragment);
                    break;
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}