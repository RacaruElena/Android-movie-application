package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.screentime.entities.Genre;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.fragments.CompleteListFragment;
import com.hsalf.smilerating.SmileRating;

public class AddMovieGenreActivity extends AppCompatActivity {
    public static final String MOVIE_GENRE_KEY = "MOVIE_GENRE_KEY";
    private Intent intent;
    private MovieModel movie;
    private Spinner spnMovieGenre;
    private Button btnAddMovie;
    private SmileRating smileRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_movie_genre);
        initComponents();
        intent = getIntent();
        if (intent != null) {
            movie = (MovieModel) intent.getParcelableExtra(AddMovieActivity.MOVIE_KEY);
            Toast.makeText(getApplicationContext(), movie.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initComponents() {
        spnMovieGenre = findViewById(R.id.spn_add_movie_genre);
        smileRating = findViewById(R.id.smile_rating);
        smileRating.setOnSmileySelectionListener(addMyRating());
        addSpinnerMovieGenreAdapter();
        btnAddMovie = findViewById(R.id.btn_movie_genre);
        btnAddMovie.setOnClickListener(AddMovie());


    }

    private SmileRating.OnSmileySelectionListener addMyRating() {
        return new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.BAD:
                        movie.setMyRating("BAD");
                        movie.setRatingValue(2);
                        break;
                    case SmileRating.GOOD:
                        movie.setMyRating("GOOD");
                        movie.setRatingValue(4);
                        break;
                    case SmileRating.GREAT:
                        movie.setMyRating("GREAT");
                        movie.setRatingValue(5);
                        break;
                    case SmileRating.OKAY:
                        movie.setMyRating("OKAY");
                        movie.setRatingValue(3);
                        break;
                    case SmileRating.TERRIBLE:
                        movie.setMyRating("TERRIBLE");
                        movie.setRatingValue(1);
                        break;
                }
            }
        };
    }

    private View.OnClickListener AddMovie() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (spnMovieGenre.getSelectedItem().toString()) {
                    case "None":
                        movie.setGenre(Genre.None);
                        break;
                    case "Action":
                        movie.setGenre(Genre.Action);
                        break;
                    case "Adventure":
                        movie.setGenre(Genre.Adventure);
                        break;
                    case "Animation":
                        movie.setGenre(Genre.Animation);
                        break;
                    case "Comedy":
                        movie.setGenre(Genre.Comedy);
                        break;
                    case "Drama":
                        movie.setGenre(Genre.Drama);
                        break;
                    case "Fantasy":
                        movie.setGenre(Genre.Fantasy);
                        break;
                    case "History":
                        movie.setGenre(Genre.History);
                        break;
                    case "Horror":
                        movie.setGenre(Genre.Horror);
                        break;
                    case "SF":
                        movie.setGenre(Genre.SF);
                        break;
                    case "Thriller-Mistery":
                        movie.setGenre(Genre.Thriller_Mistery);
                        break;
                    case "Western":
                        movie.setGenre(Genre.Western);
                        break;
                }
                //if (!(CompleteListFragment.movies.contains(movie))) {
                intent.putExtra(MOVIE_GENRE_KEY, movie);
                setResult(RESULT_OK, intent);
                finish();
//                } else {
//                    Toast.makeText(getApplicationContext(), "nu", Toast.LENGTH_SHORT).show();
//                }

            }
        };
    }

    private void addSpinnerMovieGenreAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(getApplicationContext(),
                        R.array.add_genre_values,
                        android.R.layout.simple_spinner_dropdown_item);
        spnMovieGenre.setAdapter(adapter);
    }
}