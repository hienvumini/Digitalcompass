package com.example.digitalcompass.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.digitalcompass.R;

import es.dmoral.toasty.Toasty;


public class FragmentLocation extends Fragment {
    ImageView imageViewcopyAddress, imageViewcopyLat, imageViewcopyLon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location, container, false);
        initPermission();
        init(view);
        listener();
        return view;
    }



    private void init(View view) {
        imageViewcopyAddress=(ImageView) view.findViewById(R.id.imageviewCopyAddress_Location);
        imageViewcopyAddress.setColorFilter(getActivity().getResources().getColor(R.color.mbrow));
        imageViewcopyLat=(ImageView) view.findViewById(R.id.imageviewCopyLat_Location);
        imageViewcopyLat.setColorFilter(getActivity().getResources().getColor(R.color.mbrow));
        imageViewcopyLon=(ImageView) view.findViewById(R.id.imageviewCopyLon_Location);
        imageViewcopyLon.setColorFilter(getActivity().getResources().getColor(R.color.mbrow));
    }
    private void listener() {

        imageViewcopyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(getActivity(),"Has copied the Address to the clipboard").show();

            }
        });
        imageViewcopyLat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(getActivity(),"Has copied the Latitude to the clipboard").show();

            }
        });
        imageViewcopyLon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(getActivity(),"Has copied the Lontitude to the clipboard").show();

            }
        });
    }

    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(getActivity(), "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(getActivity(), "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
    }
}
