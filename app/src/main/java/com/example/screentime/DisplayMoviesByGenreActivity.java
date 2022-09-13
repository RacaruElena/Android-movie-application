package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.entities.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class DisplayMoviesByGenreActivity extends AppCompatActivity {
    private ListView lvMoviesByGenre;
    private List<MovieModel> movies = new ArrayList<>();
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies_by_genre);
        initComponents();
        intent = getIntent();
        if (intent != null) {
            movies = intent.getParcelableArrayListExtra(MoviesByGenreActivity.MOVIES_BY_GENRE_KEY);
            notifyAdapter();
        }
        addMovieAdapter();
    }

    private void initComponents() {
        lvMoviesByGenre = findViewById(R.id.lv_movies_by_genre);
        addMovieAdapter();
    }

    private void notifyAdapter() {
        MovieListAdapter adapter = (MovieListAdapter) lvMoviesByGenre.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void addMovieAdapter() {
        MovieListAdapter adapter = new MovieListAdapter(getApplicationContext(), R.layout.my_movie_list_item_layout, movies, getLayoutInflater());
        lvMoviesByGenre.setAdapter(adapter);

    }


}