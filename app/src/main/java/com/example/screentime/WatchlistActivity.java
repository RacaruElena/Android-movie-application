package com.example.screentime;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.adapters.ViewPagerAdapter;
import com.example.screentime.adapters.ViewPagerWatchlist;
import com.example.screentime.entities.MovieModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WatchlistActivity extends AppCompatActivity {
    //implements MovieListAdapter.CustomButtonListener

//
//    private TabLayout tabLayout;
//    private ViewPager2 viewPager;
//    private ViewPagerWatchlist vpAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_complete_list);
//        initComponents();
//    }
//
//
//
//    private void initComponents() {
//        tabLayout = findViewById(R.id.a);
//        viewPager = findViewById(R.id.b);
//
//        tabLayout.addTab(tabLayout.newTab().setText("List"));
//        tabLayout.addTab(tabLayout.newTab().setText("Get Recommendations"));
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        vpAdapter = new ViewPagerWatchlist(fragmentManager, getLifecycle());
//        viewPager.setAdapter(vpAdapter);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                vpAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                tabLayout.selectTab(tabLayout.getTabAt(position));
//                vpAdapter.notifyDataSetChanged();
//            }
//        });
//
//
//    }



    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerWatchlist vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_list);
        initComponents();
    }



    private void initComponents() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Get Recommendations"));
        //tabLayout.addTab(tabLayout.newTab().setText("Statistics"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        vpAdapter = new ViewPagerWatchlist(fragmentManager, getLifecycle());
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

//    private ActivityResultLauncher<Intent> addMovieLauncher;
//    private DatabaseReference reference;
//    private ListView lvWatchlist;
//    private List<MovieModel> watchlist = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_watchlist);
//        initComponents();
//        populateListView();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.add_movie_menu, menu);
//        return true;
//
//
//    }
//
//    public void initComponents() {
//        lvWatchlist = findViewById(R.id.lv_watchlist);
//        addMovieLauncher = registerAddMovieLauncher();
//        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("watchlist");
//        addMovieAdapter();
//    }
//
//    private void addMovieAdapter() {
//        MovieListAdapter adapter = new MovieListAdapter(getApplicationContext(), R.layout.my_movie_list_item_layout, watchlist, getLayoutInflater());
//        lvWatchlist.setAdapter(adapter);
//        adapter.setCustomButtonListener(this);
//    }
//
//    private void populateListView() {
//        //watchlist.clear();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                watchlist.clear();
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    MovieModel movie = data.getValue(MovieModel.class);
//                    watchlist.add(movie);
//                }
//                notifyAdapter();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("FirebaseService", "Data is not available");
//            }
//
//        });
//    }
//
//    private void notifyAdapter() {
//        MovieListAdapter adapter = (MovieListAdapter) lvWatchlist.getAdapter();
//        adapter.notifyDataSetChanged();
//    }
//
//    private ActivityResultLauncher<Intent> registerAddMovieLauncher() {
//        ActivityResultCallback<ActivityResult> callback = getAddMovieActivityResultCallback();
//        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
//    }
//
//    private ActivityResultCallback<ActivityResult> getAddMovieActivityResultCallback() {
//        return new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if (result.getResultCode() == RESULT_OK && result.getData() != null) { // am primit si intentul
//                    MovieModel movie = (MovieModel) result.getData().getParcelableExtra(AddMovieActivity.MOVIE_WITH_GENRE_KEY); //iau intentul
//                    if (movie == null || (movie.getId() != null && !movie.getId().trim().isEmpty())) {
//                        return;
//                    }
//                    if (movie != null) {
//                        //adaugam in firebase
//                        String id = reference.push().getKey();
//                        movie.setId(id);
//                        reference.child(id).setValue(movie);
//                    }
//                }
//
//            }
//        };
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.menu_item_add_movie) {
//            Intent intent = new Intent(getApplicationContext(), AddMovieActivity.class);
//            addMovieLauncher.launch(intent);
//
//        }
//        return true;
//    }
//
//
//    @Override
//    public void onButtonClickListener(int position) {
//        reference.child(watchlist.get(position).getId()).removeValue();
//        watchlist.clear();
//    }
}