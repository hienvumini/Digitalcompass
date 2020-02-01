package com.example.digitalcompass.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.digitalcompass.R;
import com.example.digitalcompass.R;


public class FragmentCompass extends Fragment implements SensorEventListener {
    TextView textViewHienthi, textViewmgnetic;
    ImageView imageViewCompass, imageViewLocation, imageViewAngle;
    ImageView imageViewInfo;
    SensorManager sensorManager;
    SensorEventListener registerListener;
    Sensor sensorAcclerometor;
    Sensor sensorMagtic;
    Sensor sensorGyroscope;
    private float currentDegree = 0f;
    private final static float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    float[] mGravity;
    float[] mGeomagnetic;
    String string_degree="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        init(view);
        initSensor();
        return view;

    }

    private void initSensor() {
        sensorMagtic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorAcclerometor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorGyroscope=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_FASTEST);


    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void init(View view) {
        imageViewInfo = (ImageView) view.findViewById(R.id.imageviewInfo_Compass);
        imageViewInfo.setColorFilter(view.getContext().getResources().getColor(R.color.mwhite));

        imageViewAngle = (ImageView) view.findViewById(R.id.imageviewAngle_Compass);
        imageViewAngle.setColorFilter(view.getContext().getResources().getColor(R.color.mwhite));
        imageViewLocation = (ImageView) view.findViewById(R.id.imageviewlocation_Compass);
        imageViewLocation.setColorFilter(view.getContext().getResources().getColor(R.color.mwhite));
        imageViewCompass = (ImageView) view.findViewById(R.id.ImageviewCompass_Compasss);
        imageViewCompass.setColorFilter(view.getContext().getResources().getColor(R.color.mwhite));
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        textViewHienthi = (TextView) view.findViewById(R.id.textviewHienthi);
        textViewmgnetic = (TextView) view.findViewById(R.id.textviewmgnetic_Compass);


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
            textViewmgnetic.setText(Math.round(mgs) + "μT");
            mGeomagnetic = event.values;


        }
        if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE){


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
                float degree = Math.round((float)(Math.toDegrees(azimut)+360)%360);

                if (degree>=0 && degree<22.5 || degree>=337.5){

                    string_degree=degree+"°N";
                } else if (degree>=22.5 && degree<67.5){

                    string_degree=degree+"°NW";
                }
                else if (degree>=67.5 && degree<112.5){

                    string_degree=degree+"°W";
                }
                else if (degree>=122.5 && degree<157.5){

                    string_degree=degree+"°SW";
                }
                else if (degree>=157.5 && degree<202.5){

                    string_degree=degree+"°S";
                }
                else if (degree>=202.5 && degree<247.5){

                    string_degree=degree+"°SE";
                }
                else if (degree>=247.5 && degree<292.5){

                    string_degree=degree+"°E";
                }
                else if (degree>=292.5 && degree<337.5){

                    string_degree=degree+"°NE";
                }



                        textViewHienthi.setText(string_degree);

                RotateAnimation ra=new RotateAnimation(currentDegree,degree,Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setFillAfter(true);
                ra.setDuration(210);
                currentDegree = -degree;
                imageViewCompass.setAnimation(ra);


            }

        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
