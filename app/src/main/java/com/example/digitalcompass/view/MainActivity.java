package com.example.digitalcompass.view;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.digitalcompass.R;
import com.example.digitalcompass.Utils.FragmentUtils;
import com.example.digitalcompass.fragment.FragmentCompass;
import com.example.digitalcompass.fragment.FragmentFlashlight;
import com.example.digitalcompass.fragment.FragmentForeCast;
import com.example.digitalcompass.fragment.FragmentLocation;
import com.example.digitalcompass.fragment.FragmentMaps;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentCompass fragmentCompass;
    FragmentFlashlight fragmentFlashlight;
    FragmentForeCast fragmentForeCast;
    FragmentLocation fragmentLocation;
    FragmentMaps fragmentMaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        FragmentUtils.openFragment(fragmentCompass,getSupportFragmentManager(),R.id.framelayoutFragment);
        listener();
    }

    private void listener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragmentselect = null;
                switch (menuItem.getItemId()) {
                    case R.id.menu_nav_compass:
                        fragmentselect=fragmentCompass;
                        break;
                    case R.id.menu_nav_map:
                        fragmentselect=fragmentMaps;
                        break;
                    case R.id.menu_nav_forecast:
                        fragmentselect=fragmentForeCast;
                        break;
                    case R.id.menu_nav_location:
                        fragmentselect=fragmentLocation;
                        break;
                    case R.id.menu_nav_flashlight:
                        fragmentselect=fragmentFlashlight;
                        break;
                    default:
                        fragmentselect=fragmentCompass;

                        break;
                }

                FragmentUtils.openFragment(fragmentselect,getSupportFragmentManager(),R.id.framelayoutFragment);

                return true;
            }
        });

    }

    private void init() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.ctNavigationbotton);
        fragmentCompass = new FragmentCompass();
        fragmentFlashlight = new FragmentFlashlight();
        fragmentForeCast = new FragmentForeCast();
        fragmentLocation = new FragmentLocation();
        fragmentMaps = new FragmentMaps();

    }
}
