package com.example.foodyapp.Activity.onboarding.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodyapp.Activity.LoginActivity; // Nhập LoginActivity
import com.example.foodyapp.R;

public class ThirdScreen extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        view.findViewById(R.id.btn_finish).setOnClickListener(v -> {
            onBoardingFinished();
            navigateToLoginActivity();
        });

        return view;
    }

    private void onBoardingFinished() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Finished", true);
        editor.apply();
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
