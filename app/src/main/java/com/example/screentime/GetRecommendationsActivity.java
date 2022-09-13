package com.example.screentime;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.screentime.adapters.CategoryRecommendationAdapter;
import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.entities.CategoryRecommendation;
import com.example.screentime.entities.Genre;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.entities.User;
import com.example.screentime.fragments.CompleteListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class GetRecommendationsActivity extends AppCompatActivity {
    public static final String REC_KEY = "REC_KEY";
    private Map<String, Integer> numberOfMoviesByGenre = new HashMap<>();
    private Map<String, Double> sumRatingByGenre = new HashMap<>();
    private ArrayList<CategoryRecommendation> recommendations = new ArrayList<>();
    private ListView lvRecommendations;
    private List<MovieModel> movies;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recommendations);
        movies = this.getIntent().getParcelableArrayListExtra(CompleteListFragment.RECOMMENDATIONS_KEY);
        initComponents();
        addRecommendationAdapter();

    }

    private void addRecommendationAdapter() {
        CategoryRecommendationAdapter adapter = new CategoryRecommendationAdapter(getApplicationContext(), R.layout.category_recommendation_layout, recommendations, getLayoutInflater());
        lvRecommendations.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initComponents() {
        lvRecommendations = findViewById(R.id.lv_category_recommendations);
        lvRecommendations.setOnItemClickListener(showMovies());
        numberOfMoviesByGenre = getNumberOfMoviesByGenre(CompleteListFragment.movies);
        sumRatingByGenre = getSumRatingByGenre(CompleteListFragment.movies);
        numberOfMoviesByGenre.forEach((k, v) -> sumRatingByGenre.merge(k, Double.valueOf(v), (v1, v2) -> v1 / v2));
        recommendations = getMovieGenresWithMaxMovies(numberOfMoviesByGenre, getMax());
        sort(recommendations);
        addRecommendationAdapter();
    }

    private AdapterView.OnItemClickListener showMovies() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (recommendations.get(position).getGenre()) {
                    case "Action":
                        sendList(Genre.Action);
                        break;
                    case "Adventure":
                        sendList(Genre.Adventure);
                        break;
                    case "Animation":
                        sendList(Genre.Animation);
                        break;
                    case "Comedy":
                        sendList(Genre.Comedy);
                        break;
                    case "Drama":
                        sendList(Genre.Drama);
                        break;
                    case "Fantasy":
                        sendList(Genre.Fantasy);
                        break;
                    case "History":
                        sendList(Genre.History);
                        break;
                    case "Horror":
                        sendList(Genre.Horror);
                        break;
                    case "SF":
                        sendList(Genre.SF);
                        break;
                    case "Thriller-Mistery":
                        sendList(Genre.Thriller_Mistery);
                        break;
                    case "Western":
                        sendList(Genre.Western);
                        break;
                }

            }
        };
    }

    public static void sortByRating(List<MovieModel> list) {
        Collections.sort(list, (p1, p2) -> p2.getRatingValue() - (p1.getRatingValue()));
    }

    private void sendList(Genre genre) {
        ArrayList<MovieModel> moviesByGenre = new ArrayList<>();
        ArrayList<MovieModel> topMovies = new ArrayList<>();
        Intent intent = new Intent(getApplicationContext(), TopMoviesByGenreActivity.class);
        for (MovieModel movie : movies) {
            if (movie.getGenre() == genre && !moviesByGenre.contains(movie)) {
                moviesByGenre.add(movie);
            }
        }
        sortByRating(moviesByGenre);
        if (moviesByGenre.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                topMovies.add(moviesByGenre.get(i));
            }
        } else {
            for (int i = 0; i < moviesByGenre.size(); i++) {
                topMovies.add(moviesByGenre.get(i));
            }

        }
        intent.putParcelableArrayListExtra(REC_KEY, topMovies);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void sort(ArrayList<CategoryRecommendation> list) {

        list.sort((o1, o2)
                -> o2.getRatingAverage().compareTo(
                o1.getRatingAverage()));
    }

    private Map<String, Integer> getNumberOfMoviesByGenre(List<MovieModel> movies) {
        if (movies == null || movies.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Integer> source = new HashMap<>();
        for (MovieModel movie : movies) {
            if (source.containsKey(movie.getGenre().toString())) {
                Integer currentValue = source.get(movie.getGenre().toString());
                source.put(movie.getGenre().toString(), currentValue + 1);
            } else {
                source.put(movie.getGenre().toString(), 1);
            }
        }
        return source;
    }

    private Map<String, Double> getSumRatingByGenre(List<MovieModel> movies) {
        if (movies == null || movies.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Double> source = new HashMap<>();
        for (MovieModel movie : movies) {
            if (source.containsKey(movie.getGenre().toString())) {
                Double currentValue = source.get(movie.getGenre().toString());
                source.put(movie.getGenre().toString(), currentValue + movie.getRatingValue());
            } else {
                source.put(movie.getGenre().toString(), (double) movie.getRatingValue());

            }
        }
        return source;
    }

    private int getMax() {
        List<Integer> noMovies = new ArrayList(numberOfMoviesByGenre.values());
        int max = noMovies.get(0);
        for (int i = 0; i < noMovies.size(); i++) {
            if (noMovies.get(i) > max) {
                max = noMovies.get(i);
            }
        }
        return max;
    }

    private ArrayList<CategoryRecommendation> getMovieGenresWithMaxMovies(Map<String, Integer> map, Integer value) {

        ArrayList<CategoryRecommendation> result = new ArrayList<>();
        if (map.containsValue(value)) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (Objects.equals(entry.getValue(), value)) {
                    CategoryRecommendation recommendation = new CategoryRecommendation(entry.getKey(), value, sumRatingByGenre.get(entry.getKey()));
                    result.add(recommendation);
                }
            }
        }
        return result;
    }

}