package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.fragments.FollowingFragment;

import java.util.ArrayList;
import java.util.List;

public class DisplayUserMoviesListActivity extends AppCompatActivity {
    private ListView lvFollowingMovies;
    private List<MovieModel> userMovies = new ArrayList<>();
    private Intent intent;
    private String username;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_movies_list);
        initComponents();
        intent = getIntent();
        if (intent != null) {
            userMovies = intent.getParcelableArrayListExtra(FollowingFragment.USER_LIST_KEY);
            username = intent.getStringExtra(FollowingFragment.USERNAME_KEY);
            notifyAdapter();
        }
        addMovieAdapter();
        //textView.setText(getString(R.string.user_movie_list, username));
    }

    private void initComponents() {
        lvFollowingMovies = findViewById(R.id.lv_user_movie_list);
        //textView = findViewById(R.id.tv_username);
        addMovieAdapter();
    }

    private void notifyAdapter() {
        MovieListAdapter adapter = (MovieListAdapter) lvFollowingMovies.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void addMovieAdapter() {
        MovieListAdapter adapter = new MovieListAdapter(getApplicationContext(), R.layout.following_list_movie_layout, userMovies, getLayoutInflater());
        lvFollowingMovies.setAdapter(adapter);

    }
}