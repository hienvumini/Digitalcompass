package com.example.digitalcompass.fragment;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.digitalcompass.R;
import com.example.digitalcompass.R;

public class FragmentFlashlight extends Fragment {
    LinearLayout linearLayoutSOS;
    Switch aSwitchLight;
    ImageView imageViewLight, imageViewFlash_on, imageView_Flash_off;
    CameraManager cameraManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flashlight, container, false);
        init(view);
        listener();
        return view;
    }

    private void listener() {
        linearLayoutSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "SOS", Toast.LENGTH_SHORT).show();


            }
        });
        aSwitchLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchLight(isChecked);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(View view) {
        linearLayoutSOS = (LinearLayout) view.findViewById(R.id.linelayoutSOS);
        aSwitchLight = (Switch) view.findViewById(R.id.switch_Flashlight);
        imageViewLight = (ImageView) view.findViewById(R.id.light);
        imageViewFlash_on = (ImageView) view.findViewById(R.id.flash_on_Flashlight);
        imageView_Flash_off = (ImageView) view.findViewById(R.id.flash_off_Flashlight);
        cameraManager=(CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void switchLight(boolean isTurnOn) {
        if (isTurnOn) {
            imageViewLight.setVisibility(View.VISIBLE);
            imageView_Flash_off.setVisibility(View.INVISIBLE);
            imageViewFlash_on.setVisibility(View.VISIBLE);


        } else {

            imageViewLight.setVisibility(View.INVISIBLE);
            imageView_Flash_off.setVisibility(View.VISIBLE);
            imageViewFlash_on.setVisibility(View.INVISIBLE);
        }
        try {
            if (cameraManager.getCameraIdList().length>0){
                cameraManager.setTorchMode(cameraManager.getCameraIdList()[0],isTurnOn);

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (Exception e){}
    }


}
