package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.network.AsyncTaskRunner;
import com.example.screentime.network.Callback;
import com.example.screentime.network.HttpManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TopMoviesByGenreActivity extends AppCompatActivity {
    public static final String RECOMMENDED_MOVIES = "RECOMMENDED_MOVIES";
    private ListView lvTopMovies;
    private List<MovieModel> movies = new ArrayList<>();
    private ArrayList<MovieModel> recommendedMovies = new ArrayList<>();
    private Intent intent;

    private final AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_movies_by_genre);
        initComponents();
        intent = getIntent();
        if (intent != null) {
            movies = intent.getParcelableArrayListExtra(GetRecommendationsActivity.REC_KEY);
            notifyAdapter();
        }
        addMovieAdapter();
    }

    private void initComponents() {
        lvTopMovies = findViewById(R.id.lv_top_movies);
        lvTopMovies.setOnItemClickListener(getRecommendations());
        addMovieAdapter();
    }

    private AdapterView.OnItemClickListener getRecommendations() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Callable<String> asyncOperation = new HttpManager("https://api.themoviedb.org/3/movie/"
                        + movies.get(position).getTmdbId() + "/recommendations?api_key=09e34e2dbdf8a7d071519c01d0c87e6c");
                Callback<String> mainThreadOperation = getMainThreadOperation();
                asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);


            }
        };
    }

    private Callback<String> getMainThreadOperation() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                recommendedMovies.clear();
                recommendedMovies.addAll(ExamJsonParser.fromJson(result));
                Intent intent = new Intent(getApplicationContext(), DisplayRecommendationsActivity.class);
                intent.putParcelableArrayListExtra(RECOMMENDED_MOVIES, recommendedMovies);
                startActivity(intent);
            }
        };
    }

    private void notifyAdapter() {
        MovieListAdapter adapter = (MovieListAdapter) lvTopMovies.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void addMovieAdapter() {
        MovieListAdapter adapter = new MovieListAdapter(getApplicationContext(), R.layout.top_movie_layout, movies, getLayoutInflater());
        lvTopMovies.setAdapter(adapter);

    }
}