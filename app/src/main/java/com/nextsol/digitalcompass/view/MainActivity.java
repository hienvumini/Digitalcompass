package com.nextsol.digitalcompass.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.nextsol.digitalcompass.Utils.GlobalApplication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nextsol.digitalcompass.R;
import com.nextsol.digitalcompass.Utils.FragmentUtils;

import com.nextsol.digitalcompass.fragment.FragmentCompass;
import com.nextsol.digitalcompass.fragment.FragmentFlashlight;
import com.nextsol.digitalcompass.fragment.FragmentForeCast;
import com.nextsol.digitalcompass.fragment.FragmentMaps;

public class MainActivity extends AppCompatActivity implements LocationListener {
    BottomNavigationView bottomNavigationView;
    FragmentCompass fragmentCompass;
    FragmentFlashlight fragmentFlashlight;
    FragmentForeCast fragmentForeCast;
    FragmentMaps fragmentMaps;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    GlobalApplication globalApplication;
    public static Location mylocation;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        FragmentUtils.openFragment(fragmentForeCast, getSupportFragmentManager(), R.id.framelayoutFragment);
        listener();
        getLastLocation();
    }

    private void listener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragmentselect = null;
                switch (menuItem.getItemId()) {
                    case R.id.menu_nav_compass:
                        fragmentselect = fragmentCompass;
                        break;
                    case R.id.menu_nav_map:
                        fragmentselect = fragmentMaps;
                        break;
                    case R.id.menu_nav_forecast:
                        fragmentselect = fragmentForeCast;
                        break;
                    case R.id.menu_nav_flashlight:
                        fragmentselect = fragmentFlashlight;
                        break;
                    default:
                        fragmentselect = fragmentForeCast;

                        break;
                }

                FragmentUtils.openFragment(fragmentselect, getSupportFragmentManager(), R.id.framelayoutFragment);

                return true;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.ctNavigationbotton);
        fragmentCompass = new FragmentCompass();
        fragmentFlashlight = new FragmentFlashlight();
        fragmentForeCast = new FragmentForeCast();

        fragmentMaps = new FragmentMaps();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        globalApplication = (GlobalApplication) getApplicationContext();


    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    Toast.makeText(MainActivity.this, "Location: Null", Toast.LENGTH_SHORT).show();
                                    requestNewLocationData();

                                } else {

                                    globalApplication.location = location;
                                    mylocation = location;
                                    SharedPreferences sharedPreferences = getSharedPreferences("location", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putFloat("lat", (float) location.getLatitude());
                                    editor.putFloat("lon", (float) location.getLongitude());
                                    editor.apply();

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Toast.makeText(MainActivity.this, mLastLocation.getLatitude() + "--" + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        getLastLocation();

    }


}
