package com.example.screentime.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.screentime.fragments.CompleteListFragment;
import com.example.screentime.fragments.GenresFragment;
import com.example.screentime.fragments.StatisticsFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new CompleteListFragment();
        } else if (position == 1) {
            return new GenresFragment();
        }
        return new StatisticsFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
