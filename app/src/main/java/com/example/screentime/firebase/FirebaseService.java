package com.example.screentime.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.screentime.entities.MovieModel;
import com.example.screentime.network.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FirebaseService {
    private static FirebaseService firebaseService;

    private static final String USER_REFERENCE = "Users";
    private static final String MOVIE_REFERENCE = "movies";
    private final DatabaseReference reference;


    private FirebaseService(String key) {
        //String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference(USER_REFERENCE).child(key).child(MOVIE_REFERENCE);

    }

    public static FirebaseService getInstance(String key) {
        if (firebaseService == null) {
            synchronized (FirebaseService.class) {
                if (firebaseService == null) {

                    firebaseService = new FirebaseService(key);
                }
            }
        }
        return firebaseService;
    }

    public void insert(MovieModel movie) {
        if (movie == null || (movie.getId() != null && !movie.getId().trim().isEmpty())) {
            return;
        }
//        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        reference = FirebaseDatabase.getInstance().getReference(USER_REFERENCE).child(userKey).child(MOVIE_REFERENCE);
        String id = reference.push().getKey();
        movie.setId(id);
        reference.child(movie.getId()).setValue(movie);
        Log.d("cauta", reference.getParent().getKey());
    }

    public void update(MovieModel movie) {
        if (movie == null || (movie.getId() == null || movie.getId().trim().isEmpty())) {
            return;
        }
        reference.child(movie.getId()).setValue(movie);
    }

    public void delete(MovieModel movie) {
        if (movie == null || (movie.getId() == null || movie.getId().trim().isEmpty())) {
            return;
        }
        reference.child(movie.getId()).removeValue();

    }

    public void attachDataChangedListener(Callback<List<MovieModel>> callback) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MovieModel> movies = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MovieModel movie = data.getValue(MovieModel.class);
                    movies.add(movie);
                }
                callback.runResultOnUiThread(movies);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Data is not available");
            }
        });

    }
}
