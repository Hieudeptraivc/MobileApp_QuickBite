package com.example.foodyapp.Activity.onboarding;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;

import com.example.foodyapp.Activity.LoginActivity;
import com.example.foodyapp.R;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (onBoardingFinished()) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    NavHostFragment.findNavController(SplashFragment.this)
                            .navigate(R.id.action_splashFragment_to_viewPagerFragment);
                }
            }
        }, 3000);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    private boolean onBoardingFinished() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("Finished", false);
    }
}
