package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.screentime.entities.MovieModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddToWatchlistActivity extends AppCompatActivity {
    private Intent intent;
    private MovieModel movie;
    private TextView tv_title;
    private TextView tvReleaseDate;
    private TextView tv_overview;
    private TextView tvVoteAverage;
    private ImageView iv_poster;
    private Button btnAdd;
    private DatabaseReference watchlistReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_to_watchlist);
        initComponents();
        intent = getIntent();
        if (intent != null) {
            movie = (MovieModel) intent.getParcelableExtra(DisplayRecommendationsActivity.ADD_TO_WATCHLIST_KEY);
            tv_title.setText(movie.getName());
            Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500" + movie.getImage()).into(iv_poster);
            tv_overview.setText(movie.getOverview());
            tvReleaseDate.setText(getString(R.string.release_date, movie.getReleaseDate()));
            tvVoteAverage.setText(getString(R.string.vote_average, String.valueOf(movie.getVoteAverage())));
        }
    }

    private void initComponents() {
        tv_title = findViewById(R.id.tv_title);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tv_overview = findViewById(R.id.tv_overview_body);
        tvVoteAverage = findViewById(R.id.tv_vote_average);
        iv_poster = findViewById(R.id.imageView_poster);
        btnAdd = findViewById(R.id.btn_add_to_watchlist);
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        watchlistReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("watchlist");
        btnAdd.setOnClickListener(addToWatchlist());
    }

    private View.OnClickListener addToWatchlist() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = watchlistReference.push().getKey();
                movie.setId(id);
                watchlistReference.child(id).setValue(movie);
                finish();
            }
        };
    }
}