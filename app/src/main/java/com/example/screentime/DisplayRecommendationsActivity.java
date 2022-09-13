package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.entities.MovieModel;

import java.util.ArrayList;

public class DisplayRecommendationsActivity extends AppCompatActivity {
    public static final String ADD_TO_WATCHLIST_KEY = "ADD_TO_WATCHLIST_KEY";
    private ArrayList<MovieModel> recommendedMovies = new ArrayList<>();
    private Intent intent;
    private ListView lvRecommendations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recommendations);
        initComponents();
        intent = getIntent();
        if (intent != null) {

            recommendedMovies = intent.getParcelableArrayListExtra(TopMoviesByGenreActivity.RECOMMENDED_MOVIES);
            notifyAdapter();
        }

        addMovieAdapter();
    }

    private void initComponents() {
        lvRecommendations = findViewById(R.id.lv_recommended_movies);
        addMovieAdapter();
        lvRecommendations.setOnItemClickListener(addToWatchlist());
    }

    private AdapterView.OnItemClickListener addToWatchlist() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddToWatchlistActivity.class);
                intent.putExtra(ADD_TO_WATCHLIST_KEY, recommendedMovies.get(position));
                startActivity(intent);
            }
        };
    }

    private void addMovieAdapter() {
        MovieListAdapter adapter = new MovieListAdapter(getApplicationContext(), R.layout.recommended_movie_layout, recommendedMovies, getLayoutInflater());
        lvRecommendations.setAdapter(adapter);
    }

    private void notifyAdapter() {
        MovieListAdapter adapter = (MovieListAdapter) lvRecommendations.getAdapter();
        adapter.notifyDataSetChanged();
    }
}