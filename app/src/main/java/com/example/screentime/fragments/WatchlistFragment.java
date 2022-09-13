package com.example.screentime.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.screentime.AddMovieActivity;
import com.example.screentime.FollowingActivity;
import com.example.screentime.R;
import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.entities.MovieModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment implements MovieListAdapter.CustomButtonListener{

    private ActivityResultLauncher<Intent> addMovieLauncher;
    private DatabaseReference reference;
    private ListView lvWatchlist;
    private List<MovieModel> watchlist = new ArrayList<>();

    public WatchlistFragment() {
    }


    @Override
    public void onResume() {
        super.onResume();
        lvWatchlist.requestLayout();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        initComponents(view);
        populateListView();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_movie_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initComponents(View view) {
        lvWatchlist = view.findViewById(R.id.lv_watchlist);
        addMovieLauncher = registerAddMovieLauncher();
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("watchlist");
        addMovieAdapter();
    }


    private void addMovieAdapter() {
        MovieListAdapter adapter = new MovieListAdapter(getContext().getApplicationContext(), R.layout.watchlist_movie_layout, watchlist, getLayoutInflater());
        lvWatchlist.setAdapter(adapter);
        adapter.setCustomButtonListener(this);
    }

    private void populateListView() {
        //watchlist.clear();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                watchlist.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MovieModel movie = data.getValue(MovieModel.class);
                    watchlist.add(movie);
                }
                notifyAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Data is not available");
            }

        });
    }

    private void notifyAdapter() {
        MovieListAdapter adapter = (MovieListAdapter) lvWatchlist.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private ActivityResultLauncher<Intent> registerAddMovieLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddMovieActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getAddMovieActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) { // am primit si intentul
                    MovieModel movie = (MovieModel) result.getData().getParcelableExtra(AddMovieActivity.MOVIE_WITH_GENRE_KEY); //iau intentul
                    if (movie == null || (movie.getId() != null && !movie.getId().trim().isEmpty())) {
                        return;
                    }
                    if (movie != null) {
                        //adaugam in firebase
                        String id = reference.push().getKey();
                        movie.setId(id);
                        reference.child(id).setValue(movie);
                    }
                }

            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add_movie) {
            Intent intent = new Intent(getContext(), AddMovieActivity.class);
            addMovieLauncher.launch(intent);

        }
        return true;
    }


    @Override
    public void onButtonClickListener(int position) {
        reference.child(watchlist.get(position).getId()).removeValue();
        watchlist.clear();
    }
}