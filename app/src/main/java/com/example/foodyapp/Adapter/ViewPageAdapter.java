package com.example.foodyapp.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragmentList;

    // Constructor
    public ViewPageAdapter(ArrayList<Fragment> list, FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
        this.fragmentList = list;
    }

    // Return the number of fragments in the list
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    // Create fragment based on the position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }
}
