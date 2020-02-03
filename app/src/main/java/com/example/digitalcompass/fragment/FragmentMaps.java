package com.example.digitalcompass.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.example.digitalcompass.R;


public class FragmentMaps extends Fragment {
    FrameLayout frameLayoutMaps;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_maps, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        frameLayoutMaps=(FrameLayout) view.findViewById(R.id.frameMaps_Maps);
    }




}
