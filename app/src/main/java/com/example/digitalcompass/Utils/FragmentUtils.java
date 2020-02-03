package com.example.digitalcompass.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.digitalcompass.R;

public class FragmentUtils {
    public static void openFragment(Fragment fragment, FragmentManager fragmentManager, int layout){
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(layout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }



}
