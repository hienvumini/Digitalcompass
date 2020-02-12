package com.nextsol.digitalcompass.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nextsol.digitalcompass.R;
import com.nextsol.digitalcompass.Utils.GlobalApplication;
import com.nextsol.digitalcompass.Utils.MapUtils;
import com.nextsol.digitalcompass.Utils.NumderUltils;
import com.github.shchurov.horizontalwheelview.HorizontalWheelView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class FragmentMaps extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener, SensorEventListener {
    FrameLayout frameLayoutMaps;
    GoogleMap map;
    LatLng center;
    TextView textViewLatCenter, textViewLonCenter, textViewLatCenterDegree,
            textViewLonCenterDegree, textViewTimeLocal, textViewTimeGmt,
            textViewDate, textViewLat, textViewLon, textViewAddress, textViewDirection;
    ImageView imageViewZomin, imageViewZomout, imageViewMode, imageViewGrid,
            imageViewLocation, imageViewShare, imageViewGridImage;
    String string_lat = "";
    String string_lon = "";
    double lat = 0;
    double lon = 0;
    ArrayList<Integer> listmode;
    int mode = 3;
    boolean enableGrid = false;
    GlobalApplication globalApplication;
    Location location;
    HorizontalWheelView horizontalWheelView;
    SensorManager sensorManager;
    Sensor sensorAcclerometor;
    Sensor sensorMagtic;
    float[] mGravity;
    float[] mGeomagnetic;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_Map);
        supportMapFragment.getMapAsync(this);
        init(view);
        listener();
        return view;
    }

    private void listener() {


    }

    private void init(View view) {
        frameLayoutMaps = (FrameLayout) view.findViewById(R.id.map_Map);
        textViewDate = (TextView) view.findViewById(R.id.textviewdate_Map);
        textViewLatCenter = (TextView) view.findViewById(R.id.textviewCenterLat_Map);
        textViewLonCenter = (TextView) view.findViewById(R.id.textviewCenterLon_Map);
        textViewLatCenterDegree = (TextView) view.findViewById(R.id.textviewCenterLatDegree_Map);
        textViewLonCenterDegree = (TextView) view.findViewById(R.id.textviewCenterLonDegree_Map);
        textViewTimeLocal = (TextView) view.findViewById(R.id.textviewTimeLocal_Map);
        textViewTimeGmt = (TextView) view.findViewById(R.id.textviewTimeGmt_Map);
        textViewLat = (TextView) view.findViewById(R.id.textviewLat_Map);
        textViewLon = (TextView) view.findViewById(R.id.textviewLon_Map);
        textViewAddress = (TextView) view.findViewById(R.id.textviewlocation_Maps);
        textViewDirection = (TextView) view.findViewById(R.id.textviewDirection_Map);
        imageViewZomin = (ImageView) view.findViewById(R.id.imageviewZomin_Map);
        imageViewZomout = (ImageView) view.findViewById(R.id.imageviewZomout_Map);
        imageViewMode = (ImageView) view.findViewById(R.id.imageviewMode_Map);
        imageViewGrid = (ImageView) view.findViewById(R.id.imageviewGrid_Map);
        imageViewLocation = (ImageView) view.findViewById(R.id.imageviewMyLocation_Map);
        imageViewShare = (ImageView) view.findViewById(R.id.imageviewShareMap_Map);
        imageViewGridImage = (ImageView) view.findViewById(R.id.imageviewGridImage_Map);
//        imageViewRuller=(ImageView) view.findViewById(R.id.imageviewRuler_Map);
        imageViewZomin.setOnClickListener(this);
        imageViewZomout.setOnClickListener(this);
        imageViewMode.setOnClickListener(this);
        imageViewGrid.setOnClickListener(this);
        imageViewLocation.setOnClickListener(this);
        imageViewShare.setOnClickListener(this);
        listmode = new ArrayList<>();

        listmode.add(GoogleMap.MAP_TYPE_SATELLITE);
        listmode.add(GoogleMap.MAP_TYPE_HYBRID);
        listmode.add(GoogleMap.MAP_TYPE_NORMAL);
        listmode.add(GoogleMap.MAP_TYPE_TERRAIN);

        globalApplication = (GlobalApplication) getActivity().getApplicationContext();

        horizontalWheelView = (HorizontalWheelView) view.findViewById(R.id.hw);

        horizontalWheelView.setMarksCount(50);
        horizontalWheelView.setShowActiveRange(true);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        initSensor();



    }

    private void initSensor() {
        sensorMagtic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorAcclerometor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

            if (MapUtils.getCompleteAddressString(getActivity(), location.getLatitude(), location.getLongitude()).length() >= 0) {
                textViewAddress.setText(MapUtils.getCompleteAddressString(getActivity(), location.getLatitude(), location.getLongitude()));

            }
            globalApplication.location = location;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {


        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(false);

        }

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCameraMove() {
                center = map.getCameraPosition().target;
                lat = center.latitude;
                lon = center.longitude;

                string_lat = NumderUltils.getFormattedLattitudeInDegree(lat);
                string_lon = NumderUltils.getFormattedLongtitudeInDegree(lon);
                textViewLatCenter.setText((int) (lat) + "°" + Math.round((lat - (int) (lat)) * 60));
                textViewLonCenter.setText((int) (lon) + "°" + Math.round((lon - (int) (lon)) * 60));
                textViewLatCenterDegree.setText(string_lat);
                textViewLonCenterDegree.setText(string_lon);
                textViewLat.setText(string_lat);
                textViewLon.setText(string_lon);


            }
        });

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMinZoomPreference(1);
        try {
            ZoomtoMyLocation(location);
        } catch (Exception e) {
        }
        if (location != null) {
            if (MapUtils.getCompleteAddressString(getActivity(), location.getLatitude(), location.getLongitude()).length() >= 0) {
                textViewAddress.setText(MapUtils.getCompleteAddressString(getActivity(), location.getLatitude(), location.getLongitude()));

            }


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageviewZomin_Map:
                map.animateCamera(CameraUpdateFactory.zoomIn());

                break;
            case R.id.imageviewZomout_Map:
                map.animateCamera(CameraUpdateFactory.zoomOut());

                break;
            case R.id.imageviewMode_Map:
                if (mode + 1 >= listmode.size()) {
                    mode = 0;

                } else {
                    mode += 1;
                }
                map.setMapType(listmode.get(mode));
                break;
            case R.id.imageviewGrid_Map:
                enableGrid = !enableGrid;
                if (enableGrid) {

                    imageViewGridImage.setVisibility(View.VISIBLE);

                } else {
                    imageViewGridImage.setVisibility(View.INVISIBLE);
                }


                break;
            case R.id.imageviewMyLocation_Map:
                if (globalApplication.location != null) {
                    ZoomtoMyLocation(globalApplication.location);

                }


                break;
            case R.id.imageviewShareMap_Map:
                if (globalApplication.location != null) {
                    Location mlocation = globalApplication.location;
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share Your Location");
                    intent.putExtra(Intent.EXTRA_TEXT, mlocation.getLatitude() + "," + mlocation.getLongitude());
                    startActivity(Intent.createChooser(intent, "Share Using"));

                }

                break;


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ZoomtoMyLocation(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the mapicon to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor
                .TYPE_ACCELEROMETER) {
            mGravity = event.values;


        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];
            float mgs = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
            mGeomagnetic = event.values;


        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {


        }
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {

                // orientation contains azimut, pitch and roll
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                float azimut = orientation[0];

                float rotation = Math.round(-azimut * 360 / (2 * 3.14159f));
                float degree = Math.round((float) (Math.toDegrees(azimut) + 360) % 360);

                horizontalWheelView.setDegreesAngle(degree);
                textViewDirection.setText((int) (degree) + NumderUltils.getsymbolDirection(degree));
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }



}


