package com.nextsol.digitalcompass.fragment;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.nextsol.digitalcompass.R;
import com.nextsol.digitalcompass.Utils.NumderUltils;


public class FragmentCompass extends Fragment implements SensorEventListener {
    TextView textViewHienthi, textViewmgnetic, textViewDirection;
    ImageView imageViewCompass, imageViewLocation, imageViewAngle, imageViewDirection;
    ImageView imageViewInfo;
    SensorManager sensorManager;
    SensorEventListener registerListener;
    Sensor sensorAcclerometor;
    Sensor sensorMagtic;
    Sensor sensorGyroscope;
    private float currentDegree = 0f;
    private float currentDirection = 0f;
    float[] mGravity;
    float[] mGeomagnetic;
     SeekBar seekBarAngle;
    boolean isseekbar = false;
    LinearLayout linearLayoutDirection;
    int degree_directtion=0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        init(view);
        initSensor();
        listener();
        return view;

    }

    private void listener() {
        imageViewAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isseekbar = !isseekbar;
                if (isseekbar) {

                    seekBarAngle.setVisibility(View.VISIBLE);
                    imageViewDirection.setVisibility(View.VISIBLE);
                    linearLayoutDirection.setVisibility(View.VISIBLE);


                } else {
                    seekBarAngle.setVisibility(View.INVISIBLE);
                    imageViewDirection.setVisibility(View.INVISIBLE);
                    linearLayoutDirection.setVisibility(View.INVISIBLE);
                    


                }
            }
        });
        seekBarAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 degree_directtion = (progress * 360) / 100;
                textViewDirection.setText(degree_directtion + "");
                textViewDirection.setText(degree_directtion+"°"+NumderUltils.getsymbolDirection(degree_directtion));

                RotateAnimation rd = new RotateAnimation(currentDirection, degree_directtion, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rd.setFillAfter(true);
                rd.setDuration(210);
                currentDirection = -degree_directtion;
                imageViewDirection.setAnimation(rd);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void initSensor() {
        sensorMagtic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorAcclerometor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);


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
        imageViewDirection = (ImageView) view.findViewById(R.id.imageviewDirection);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        textViewHienthi = (TextView) view.findViewById(R.id.textviewHienthi);
        textViewmgnetic = (TextView) view.findViewById(R.id.textviewmgnetic_Compass);
        textViewDirection = (TextView) view.findViewById(R.id.textviewDiriection_Compass);
        seekBarAngle = (SeekBar) view.findViewById(R.id.seekbarAngle);
        linearLayoutDirection = (LinearLayout) view.findViewById(R.id.linenerDirection_Compass);


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
                textViewHienthi.setText(degree + "°" + NumderUltils.getsymbolDirection(degree));

                RotateAnimation ra = new RotateAnimation(currentDegree, degree, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setFillAfter(true);
                ra.setDuration(210);
                currentDegree = -degree;
                imageViewCompass.setAnimation(ra);
                if (isseekbar) {

                    RotateAnimation rd2 = new RotateAnimation(currentDirection, degree, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    ra.setFillAfter(true);
                    ra.setDuration(210);
                    currentDirection = -degree;
                    imageViewDirection.setAnimation(rd2);


                }


            }

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
}
