package com.example.foodyapp.Activity.onboarding;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager2.widget.ViewPager2;

import com.example.foodyapp.Activity.onboarding.screens.FirstScreen;
import com.example.foodyapp.Activity.onboarding.screens.SecondScreen;
import com.example.foodyapp.Activity.onboarding.screens.ThirdScreen;
import com.example.foodyapp.Adapter.ViewPageAdapter;
import com.example.foodyapp.R;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FirstScreen());
        fragmentList.add(new SecondScreen());
        fragmentList.add(new ThirdScreen());

        ViewPageAdapter adapter = new ViewPageAdapter(
                fragmentList,
                requireActivity().getSupportFragmentManager(),
                getLifecycle()
        );

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        return view;
    }
}
