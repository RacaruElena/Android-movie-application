package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.screentime.adapters.ViewPagerAdapter;
import com.example.screentime.fragments.CompleteListFragment;
import com.example.screentime.fragments.GenresFragment;
import com.google.android.material.tabs.TabLayout;

public class CompleteListActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_list);
        initComponents();
    }



    private void initComponents() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Complete list"));
        tabLayout.addTab(tabLayout.newTab().setText("Genres"));
        tabLayout.addTab(tabLayout.newTab().setText("Statistics"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        vpAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(vpAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                vpAdapter.notifyDataSetChanged();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
                vpAdapter.notifyDataSetChanged();
            }
        });


    }

}