package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;

import com.example.screentime.adapters.MovieAdapter;
import com.example.screentime.adapters.MoviesInTheatresAdapter;
import com.example.screentime.databinding.ActivityHomeBinding;
import com.example.screentime.databinding.ActivityMainBinding;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.fragments.FollowingFragment;
import com.example.screentime.fragments.HomeFragment;
import com.example.screentime.fragments.ListsFragment;
import com.example.screentime.network.AsyncTaskRunner;
import com.example.screentime.network.Callback;
import com.example.screentime.network.HttpManager;
import com.google.android.material.progressindicator.BaseProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class HomeActivity extends AppCompatActivity {
    //    private Button btnLogOut;
//    private Button btnMyMovies;
//    FirebaseAuth mAuth;
//
//    private final static String top_rated_movies_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=09e34e2dbdf8a7d071519c01d0c87e6c";
//    private final static String in_theatres_movies_url = "https://api.themoviedb.org/3/movie/now_playing?api_key=09e34e2dbdf8a7d071519c01d0c87e6c";
//    private RecyclerView recyclerView1;
//    private RecyclerView recyclerView2;
//    private List<MovieModel> top_rated_movies = new ArrayList<>();
//    private List<MovieModel> in_theatres_movies = new ArrayList<>();
//    private final AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    //private ActivityMainBinding binding;
    private ActivityHomeBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.menu_item_movies:
                    replaceFragment(new ListsFragment());
                    break;
                case R.id.menu_item_following:
                    replaceFragment(new FollowingFragment());
                    break;
            }
            return true;
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_frameLayout, fragment);
        fragmentTransaction.commit();

    }

//    private void loadMoviesFromHttp() {
//        Callable<String> asyncOperation = new HttpManager(top_rated_movies_url);
//        Callback<String> mainThreadOperation = getMainThreadOperation();
//        asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
//
//        Callable<String> asyncOperationInTheatresMovies = new HttpManager(in_theatres_movies_url);
//        Callback<String> mainThreadOperationInTheatresMovies = getMainThreadOperationInTheatresMovies();
//        asyncTaskRunner.executeAsync(asyncOperationInTheatresMovies, mainThreadOperationInTheatresMovies);
//    }
//
//    private Callback<String> getMainThreadOperationInTheatresMovies() {
//        return new Callback<String>() {
//            @Override
//            public void runResultOnUiThread(String result) {
//                in_theatres_movies.addAll(ExamJsonParser.fromJson(result));
//                notifyAdapter2();
//
//            }
//        };
//    }
//
//
//    private Callback<String> getMainThreadOperation() {
//        return new Callback<String>() {
//            @Override
//            public void runResultOnUiThread(String result) {
//                top_rated_movies.addAll(ExamJsonParser.fromJson(result));
//                notifyAdapter1();
//            }
//        };
//    }
//
//    private void notifyAdapter1() {
//        MovieAdapter adapter = (MovieAdapter) recyclerView1.getAdapter();
//        adapter.notifyDataSetChanged();
//    }
//
//    private void notifyAdapter2() {
//        MoviesInTheatresAdapter adapter = (MoviesInTheatresAdapter) recyclerView2.getAdapter();
//        adapter.notifyDataSetChanged();
//    }
//
//
//    private void initComponents() {
//        btnLogOut = findViewById(R.id.btn_log_out);
//        btnMyMovies = findViewById(R.id.btn_my_movies);
//        btnLogOut.setOnClickListener(LogOut());
//        btnMyMovies.setOnClickListener(MyMoviesList());
//        mAuth = FirebaseAuth.getInstance();
//
//        recyclerView1 = findViewById(R.id.rv_movies);
//        recyclerView2 = findViewById(R.id.rv_movies_in_theatres);
//        addExamAdapter();
//    }
//
//    private View.OnClickListener MyMoviesList() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MovieListActivity.class);
//                startActivity(intent);
//            }
//        };
//    }
//
//    private void addExamAdapter() {
//        MovieAdapter adapter1 = new MovieAdapter(getApplicationContext(), top_rated_movies);
//        MoviesInTheatresAdapter adapter2 = new MoviesInTheatresAdapter(getApplicationContext(), in_theatres_movies);
//
//        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
//        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView1.setLayoutManager(layoutManager1);
//
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
//        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView1.setHasFixedSize(true);
//        recyclerView2.setHasFixedSize(true);
//        recyclerView2.setLayoutManager(layoutManager2);
//
//        recyclerView1.setAdapter(adapter1);
//        recyclerView2.setAdapter(adapter2);
//
//
//    }
//
//    private View.OnClickListener LogOut() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        };
//    }
}