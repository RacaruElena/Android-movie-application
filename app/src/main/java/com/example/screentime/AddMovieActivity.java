package com.example.screentime;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.screentime.adapters.AddMovieAdapter;
import com.example.screentime.adapters.MoviesInTheatresAdapter;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.firebase.FirebaseService;
import com.example.screentime.network.AsyncTaskRunner;
import com.example.screentime.network.Callback;
import com.example.screentime.network.HttpManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AddMovieActivity extends AppCompatActivity {
    private final static String searched_movies_url = "https://api.themoviedb.org/3/search/movie?api_key=09e34e2dbdf8a7d071519c01d0c87e6c&query=";
    public static final String MOVIE_KEY = "movie";
    public static final String MOVIE_WITH_GENRE_KEY = "MOVIE_WITH_GENRE_KEY";
    private SearchView searchViewMovies;
    private RecyclerView recyclerViewResults;
    private final AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private List<MovieModel> searched_movies = new ArrayList<>();
    private AddMovieAdapter.RecyclerViewClickListener listener;
    //private Intent intent;
    private ActivityResultLauncher<Intent> getMovieLauncher;
    private Intent intent;
    private FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        initComponents();
        //loadMoviesFromHttp();
        intent = getIntent();
//        firebaseService = FirebaseService.getInstance();


    }

    private void initComponents() {
        searchViewMovies = findViewById(R.id.search_view);
        recyclerViewResults = findViewById(R.id.recycler_view_results);
        searchViewMovies.setOnQueryTextListener(OnSearch());
        getMovieLauncher = registerGetMovieLauncher();
        addMovieAdapter();
    }

    private ActivityResultLauncher<Intent> registerGetMovieLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddMovieActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getAddMovieActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) { // am primit si intentul
                    MovieModel movie = (MovieModel) result.getData().getParcelableExtra(AddMovieGenreActivity.MOVIE_GENRE_KEY); //iau intentul
                    if (movie == null || (movie.getId() != null && !movie.getId().trim().isEmpty())) {
                        return;
                    }
                    if (movie != null) {
                        //trimitem catre MovieListActivity
                        intent.putExtra(MOVIE_WITH_GENRE_KEY, movie);
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                }
            }
        };
    }


    private void addMovieAdapter() {
        setOnClickListener();
        AddMovieAdapter adapter = new AddMovieAdapter(getApplicationContext(), searched_movies, listener);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewResults.setHasFixedSize(true);
        recyclerViewResults.setLayoutManager(new GridLayoutManager(this, 2));



        //recyclerViewResults.setLayoutManager(layoutManager1);

        recyclerViewResults.setAdapter(adapter);


    }

    private void setOnClickListener() {
        listener = new AddMovieAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent1 = new Intent(getApplicationContext(), AddMovieGenreActivity.class);
                intent1.putExtra(MOVIE_KEY, searched_movies.get(position));
                getMovieLauncher.launch(intent1);

            }
        };
    }


    private Callback<String> getMainThreadOperation() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                searched_movies.clear();
                searched_movies.addAll(ExamJsonParser.fromJson(result));
                notifyAdapter();
            }
        };
    }

    private void notifyAdapter() {
        AddMovieAdapter adapter = (AddMovieAdapter) recyclerViewResults.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private SearchView.OnQueryTextListener OnSearch() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Callable<String> asyncOperation = new HttpManager(searched_movies_url + query);
                Callback<String> mainThreadOperation = getMainThreadOperation();
                asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
                searchViewMovies.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }
}