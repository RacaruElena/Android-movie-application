package com.example.screentime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.screentime.entities.Genre;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.fragments.ListsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MoviesByGenreActivity extends AppCompatActivity {
    public static final String MOVIES_BY_GENRE_KEY = "MOVIES_BY_GENRE_KEY";
    private BottomNavigationView bottomNavigationView;
    private NavigationBarView navigationBarView;
    private ListView lvgenres;
    private List<MovieModel> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_by_genre);
        initComponents();
        movies = this.getIntent().getParcelableArrayListExtra(MovieListActivity.LIST_KEY);

    }

    private void initComponents() {
        navigationBarView = findViewById(R.id.nav_view_genre);
        //set complete list selected
        navigationBarView.setSelectedItemId(R.id.menu_item_genre);
        navigationBarView.setOnItemSelectedListener(navigate());
        lvgenres = findViewById(R.id.lv_genres);
        movieGenresAdapter();
        lvgenres.setOnItemClickListener(ShowMoviesByCategory());
    }


    private AdapterView.OnItemClickListener ShowMoviesByCategory() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CharSequence text = ((TextView) view).getText();
                if ("None".equals(text)) {
                } else if ("Action".equals(text)) {
                    sendList(Genre.Action);
                } else if ("Adventure".equals(text)) {
                    sendList(Genre.Adventure);
                } else if ("Animation".equals(text)) {
                    sendList(Genre.Animation);
                } else if ("Comedy".equals(text)) {
                    sendList(Genre.Comedy);
                } else if ("Drama".equals(text)) {
                    sendList(Genre.Drama);
                } else if ("Fantasy".equals(text)) {
                    sendList(Genre.Fantasy);
                } else if ("History".equals(text)) {
                    sendList(Genre.History);
                } else if ("Horror".equals(text)) {
                    sendList(Genre.Horror);
                } else if ("SF".equals(text)) {
                    sendList(Genre.SF);
                } else if ("Thriller-Mistery".equals(text)) {
                    sendList(Genre.Thriller_Mistery);
                } else if ("Western".equals(text)) {
                    sendList(Genre.Western);
                }


            }
        };
    }

    private void sendList(Genre genre) {
        ArrayList<MovieModel> moviesByGenre = new ArrayList<>();
        Intent intent = new Intent(getApplicationContext(), DisplayMoviesByGenreActivity.class);

        for (MovieModel movie : movies) {
            if (movie.getGenre() == genre && !moviesByGenre.contains(movie)) {
                moviesByGenre.add(movie);
            }

        }
        intent.putParcelableArrayListExtra(MOVIES_BY_GENRE_KEY, moviesByGenre);
        startActivity(intent);

    }

    private void movieGenresAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(getApplicationContext(),
                        R.array.add_genre_values,
                        android.R.layout.simple_list_item_1);
        lvgenres.setAdapter(adapter);

    }

    private NavigationBarView.OnItemSelectedListener navigate() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_complete_list:
                        startActivity(new Intent(getApplicationContext(), MovieListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_item_genre:
                        return true;
                    case R.id.menu_item_back:
                        finish();
                        return true;
                }
                return false;
            }
        };
    }
}