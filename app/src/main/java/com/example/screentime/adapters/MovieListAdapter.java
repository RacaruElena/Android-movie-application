package com.example.screentime.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.screentime.R;
import com.example.screentime.entities.Genre;
import com.example.screentime.entities.MovieModel;

import java.util.List;

public class MovieListAdapter extends ArrayAdapter<MovieModel> {
    private Context context;
    private int resource;
    private List<MovieModel> movies;
    private LayoutInflater inflater;
    private CheckBox checkBoxFavourite;
    private CheckboxCheckedListener checkedListener;
    private ImageView btnDeleteMovie;
    private CustomButtonListener customListener;


    public MovieListAdapter(@NonNull Context context, int resource, @NonNull List<MovieModel> objects,
                            LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.movies = objects;
        this.inflater = inflater;

    }


    public interface CustomButtonListener {
        void onButtonClickListener(int position);
    }

    public void setCustomButtonListener(CustomButtonListener listener) {
        this.customListener = listener;
    }

    public interface CheckboxCheckedListener {
        void getCheckBoxCheckedListener(int position);
    }

    public void setCheckedListener(CheckboxCheckedListener checkedListener) {
        this.checkedListener = checkedListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View cview, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = inflater.inflate(resource, parent, false);
        MovieModel movie = movies.get(position);
        if (movie == null) {
            return view;
        }

        addVoteAverage(view, movie.getVoteAverage());
        addName(view, movie.getName());
        addImage(view, movie.getImage());
        addCheckBox(view, movie.isFavourite());
        addGenre(view, movie.getGenre());
        addReleaseDate(view, movie.getReleaseDate());
        addRating(view, movie.getRatingValue());

        btnDeleteMovie = view.findViewById(R.id.btn_delete_movie);
        btnDeleteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customListener != null) {
                    Log.d("ceva", movies.get(position).getName());
                    customListener.onButtonClickListener(position);
                }
            }
        });
        checkBoxFavourite = view.findViewById(R.id.cb_favourite);
        checkBoxFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkedListener != null) {
                    if (buttonView.isChecked()) {

                        movie.setFavourite(true);
                    } else {
                        movie.setFavourite(false);
                    }
                    checkedListener.getCheckBoxCheckedListener(position);
                }

            }
        });
        return view;

    }

    private void addRating(View view, int ratingValue) {
        TextView textView = view.findViewById(R.id.tv_your_rating);
        populateTextViewContent(textView, context.getString(R.string.rating_value, String.valueOf(ratingValue)));
    }

    private void addReleaseDate(View view, String releaseDate) {
        TextView textView = view.findViewById(R.id.tv_release_date);
        populateTextViewContent(textView, context.getString(R.string.release_date, releaseDate));
    }

    private void addGenre(View view, Genre genre) {
        TextView textView = view.findViewById(R.id.tv_genre);
        populateTextViewContent(textView, context.getString(R.string.genre, String.valueOf(genre)));
    }


    private void addCheckBox(View view, boolean favourite) {
        CheckBox checkBoxFavourite = view.findViewById(R.id.cb_favourite);
        checkBoxFavourite.setChecked(favourite);
    }

    private void addImage(View view, String image) {
        ImageView imageView = view.findViewById(R.id.imageView_movie);
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + image).into(imageView);

    }

    private void addName(View view, String name) {
        TextView textView = view.findViewById(R.id.tv_name);
        populateTextViewContent(textView, name);
    }

    private void addVoteAverage(View view, double vote_average) {
        RatingBar rbVoteAverage = view.findViewById(R.id.rb_vote_average);
        //rbVoteAverage.setRating(2);
        if (vote_average < 2)
            rbVoteAverage.setRating((float) vote_average);
        if (vote_average >= 2 && vote_average < 4)
            rbVoteAverage.setRating((float) vote_average);
        if (vote_average >= 4 && vote_average < 6)
            rbVoteAverage.setRating((float) vote_average);
        if (vote_average >= 6 && vote_average < 8)
            rbVoteAverage.setRating((float) vote_average);
        if (vote_average >= 8 && vote_average < 10)
            rbVoteAverage.setRating((float) vote_average);

    }


    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText("-");
        }
    }

}


