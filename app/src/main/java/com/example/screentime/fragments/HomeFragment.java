package com.example.screentime.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.screentime.AddMovieActivity;
import com.example.screentime.ExamJsonParser;
import com.example.screentime.GetRecommendationsActivity;
import com.example.screentime.MainActivity;
import com.example.screentime.ProfileActivity;
import com.example.screentime.R;
import com.example.screentime.adapters.MovieAdapter;
import com.example.screentime.adapters.MoviesInTheatresAdapter;
import com.example.screentime.adapters.SnapHelperOneByOne;
import com.example.screentime.adapters.VideoAdapter;
import com.example.screentime.adapters.YoutubeAdapter;
import com.example.screentime.entities.DataSetList;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.network.AsyncTaskRunner;
import com.example.screentime.network.Callback;
import com.example.screentime.network.HttpManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;


public class HomeFragment extends Fragment {
    private Button btnLogOut;
    private Button video;

    FirebaseAuth mAuth;

    private static String MY_PREFS = "saveSwitchState";
    private static String SWITCH_STATUS = "switch_status";
    private boolean switchStatus;
    private SharedPreferences myPreferences;

    Vector<DataSetList> youtubeVideos = new Vector<>();

    private RecyclerView rvTrailers;
    private ArrayList<DataSetList> arrayList;

    private final static String top_rated_movies_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=09e34e2dbdf8a7d071519c01d0c87e6c";
    private final static String in_theatres_movies_url = "https://api.themoviedb.org/3/movie/now_playing?api_key=09e34e2dbdf8a7d071519c01d0c87e6c";
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private List<MovieModel> top_rated_movies = new ArrayList<>();
    private List<MovieModel> in_theatres_movies = new ArrayList<>();
    private final AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();

    ArrayList<DataSetList> ytVideos = new ArrayList<>();

    private final static String upcoming_movies_url = "https://api.themoviedb.org/3/movie/upcoming?api_key=09e34e2dbdf8a7d071519c01d0c87e6c";
    private List<String> movieIds = new ArrayList<>();
    private List<String> movieVideoKeys = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initComponents(view);
        loadMoviesFromHttp();
        loadUpcomingMoviesFromHttp();

        return view;
    }

    private void initComponents(View view) {
        video = view.findViewById(R.id.video);
        rvTrailers = view.findViewById(R.id.rv_trailers);
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(rvTrailers);
        video.setOnClickListener(video());

        myPreferences = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        switchStatus = myPreferences.getBoolean(SWITCH_STATUS, false);
        if (switchStatus) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        mAuth = FirebaseAuth.getInstance();

        recyclerView1 = view.findViewById(R.id.rv_movies);
        recyclerView2 = view.findViewById(R.id.rv_movies_in_theatres);

        addExamAdapter();

    }

    private View.OnClickListener video() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), movieIds.get(0), Toast.LENGTH_SHORT).show();
                //loadVideosFromHttp();

                Toast.makeText(getActivity(), movieVideoKeys.get(0), Toast.LENGTH_SHORT).show();
                //Log.d("mr", "yhgj"+"https://api.themoviedb.org/3/movie/" + movieIds.get(1) + "/videos?api_key=09e34e2dbdf8a7d071519c01d0c87e6c" );
                rvTrailers.setHasFixedSize(true);

                for (String s : movieVideoKeys) {

                    ytVideos.add(new DataSetList("https://www.youtube.com/embed/" + s));

                }

                LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
                layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                rvTrailers.setLayoutManager(layoutManager1);


                YoutubeAdapter youtubeAdapter = new YoutubeAdapter(ytVideos, getActivity());
                rvTrailers.setAdapter(youtubeAdapter);


            }
        };
    }


    private void loadUpcomingMoviesFromHttp() {
        Callable<String> asyncOperation = new HttpManager(upcoming_movies_url);
        Callback<String> mainThreadOperation = getMainThreadOperationUpcoming();
        asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);

    }


    private void loadVideosFromHttp() {
        for (String s : movieIds) {
            Callable<String> asyncOperation = new HttpManager(
                    "https://api.themoviedb.org/3/movie/" + s + "/videos?api_key=09e34e2dbdf8a7d071519c01d0c87e6c");
            Callback<String> mainThreadOperation = getMainThreadOperation2();
            asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
        }

    }

    private Callback<String> getMainThreadOperationUpcoming() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                movieIds.addAll(ExamJsonParser.fromJson2(result));
                loadVideosFromHttp();
            }
        };
    }

    private Callback<String> getMainThreadOperation2() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                movieVideoKeys.add(ExamJsonParser.fromJson3(result));

            }
        };
    }


    private void loadMoviesFromHttp() {
        Callable<String> asyncOperation = new HttpManager(top_rated_movies_url);
        Callback<String> mainThreadOperation = getMainThreadOperation();
        asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);

        Callable<String> asyncOperationInTheatresMovies = new HttpManager(in_theatres_movies_url);
        Callback<String> mainThreadOperationInTheatresMovies = getMainThreadOperationInTheatresMovies();
        asyncTaskRunner.executeAsync(asyncOperationInTheatresMovies, mainThreadOperationInTheatresMovies);
    }

    private Callback<String> getMainThreadOperationInTheatresMovies() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                in_theatres_movies.addAll(ExamJsonParser.fromJson(result));
                notifyAdapter2();

            }
        };
    }


    private Callback<String> getMainThreadOperation() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                top_rated_movies.addAll(ExamJsonParser.fromJson(result));
                notifyAdapter1();
            }
        };
    }

    private void notifyAdapter1() {
        MovieAdapter adapter = (MovieAdapter) recyclerView1.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void notifyAdapter2() {
        MoviesInTheatresAdapter adapter = (MoviesInTheatresAdapter) recyclerView2.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void addExamAdapter() {
        MovieAdapter adapter1 = new MovieAdapter(getActivity(), top_rated_movies);
        MoviesInTheatresAdapter adapter2 = new MoviesInTheatresAdapter(getActivity(), in_theatres_movies);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(layoutManager2);

        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);


    }

    private View.OnClickListener LogOut() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_profile) {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_item_logout) {
            mAuth.signOut();
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }
        return true;
    }


}