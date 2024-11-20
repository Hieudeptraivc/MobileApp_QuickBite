package com.example.foodyapp.Activity.onboarding.screens;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.example.foodyapp.R;

public class SecondScreen extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);

        view.findViewById(R.id.btn_next2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager != null) {
                    viewPager.setCurrentItem(2);
                }
            }
        });

        return view;
    }
}
