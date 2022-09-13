package com.example.screentime.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.screentime.DisplayMoviesByGenreActivity;
import com.example.screentime.R;
import com.example.screentime.entities.Genre;
import com.example.screentime.entities.MovieModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class GenresFragment extends Fragment {
    public static final String MOVIES_BY_GENRE_KEY = "MOVIES_BY_GENRE_KEY";
    private NavigationBarView navigationBarView;
    private ListView lvgenres;
    //private List<MovieModel> movies;


    public GenresFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        lvgenres.requestLayout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genres, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        lvgenres = view.findViewById(R.id.lv_genres);
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
        Intent intent = new Intent(getActivity(), DisplayMoviesByGenreActivity.class);

        for (MovieModel movie : CompleteListFragment.movies) {
            if (movie.getGenre() == genre && !moviesByGenre.contains(movie)) {
                moviesByGenre.add(movie);
            }
        }
        intent.putParcelableArrayListExtra(MOVIES_BY_GENRE_KEY, moviesByGenre);
        startActivity(intent);

    }

    private void movieGenresAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(getActivity(),
                        R.array.genre_values,
                        R.layout.listview_text_color_layout);
        lvgenres.setAdapter(adapter);
    }
}