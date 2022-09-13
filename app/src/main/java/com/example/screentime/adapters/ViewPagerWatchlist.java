package com.example.screentime.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.screentime.fragments.GetRecommendationsFragment;
import com.example.screentime.fragments.WatchlistFragment;


public class ViewPagerWatchlist extends FragmentStateAdapter {
    public ViewPagerWatchlist(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new WatchlistFragment();
        }
        return new GetRecommendationsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
