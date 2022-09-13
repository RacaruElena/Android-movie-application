package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.screentime.entities.MovieModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsalf.smilerating.SmileRating;

public class MovieDetailsPopupActivity extends AppCompatActivity {
    public static final String UPDATED_MOVIE_KEY = "UPDATED_MOVIE";
    private Intent intent;
    private TextView tv_title;
    private TextView tvReleaseDate;
    private TextView tv_overview;
    private TextView tvMyRating;
    private TextView tvVoteAverage;
    private ImageView iv_poster;
    private TextInputEditText tietNotes;
    private Button btnSave;
    private MovieModel movie;
    private CheckBox checkBoxFavourite;
    private DatabaseReference favouriteListReference;
    private SmileRating myRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_movie_details_popup);
        initComponents();
        intent = getIntent();
        if (intent != null) {
            movie = (MovieModel) intent.getParcelableExtra(MovieListActivity.MOVIE_POPUP_KEY);
            tv_title.setText(movie.getName());
            Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500" + movie.getImage()).into(iv_poster);
            tv_overview.setText(movie.getOverview());
            tietNotes.setText(movie.getNotes());
            tvReleaseDate.setText(getString(R.string.release_date, movie.getReleaseDate()));
            tvVoteAverage.setText(getString(R.string.vote_average, String.valueOf(movie.getVoteAverage())));
            tvMyRating.setText(getString(R.string.rating_value_popup, String.valueOf(movie.getRatingValue())));
            switch (movie.getMyRating()) {
                case "TERRIBLE":
                    myRating.setSelectedSmile(SmileRating.TERRIBLE);
                    break;
                case "BAD":
                    myRating.setSelectedSmile(SmileRating.BAD);
                    break;
                case "OKAY":
                    myRating.setSelectedSmile(SmileRating.OKAY);
                    break;
                case "GOOD":
                    myRating.setSelectedSmile(SmileRating.GOOD);
                    break;
                case "GREAT":
                    myRating.setSelectedSmile(SmileRating.GREAT);
                    break;
            }

        }
    }


    private void initComponents() {
        tv_title = findViewById(R.id.tv_popup_title);
        tvMyRating = findViewById(R.id.tv_popup_my_rating);
        tvVoteAverage = findViewById(R.id.tv_vote_average);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        iv_poster = findViewById(R.id.imageView_popup_poster);
        tv_overview = findViewById(R.id.tv_popup_overview_body);
        tietNotes = findViewById(R.id.popup_tiet_notes);
        btnSave = findViewById(R.id.popup_btn_save);
        btnSave.setOnClickListener(SaveNotes());
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favouriteListReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("Favourite movies");
        myRating = findViewById(R.id.my_rating);

    }


    private View.OnClickListener SaveNotes() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notes = tietNotes.getText() != null ? tietNotes.getText().toString() : "";
                int rating = myRating.getSelectedSmile();

                if (rating == 0) {
                    movie.setMyRating("TERRIBLE");
                    movie.setRatingValue(1);
                } else if (rating == 1) {
                    movie.setMyRating("BAD");
                    movie.setRatingValue(2);

                } else if (rating == 2) {
                    movie.setMyRating("OKAY");
                    movie.setRatingValue(3);

                } else if (rating == 3) {
                    movie.setMyRating("GOOD");
                    movie.setRatingValue(4);

                } else if (rating == 4) {
                    movie.setMyRating("GREAT");
                    movie.setRatingValue(5);

                }

                movie.setNotes(notes);
                intent.putExtra(UPDATED_MOVIE_KEY, movie);
                setResult(RESULT_OK, intent);

                finish();

            }
        };
    }
}