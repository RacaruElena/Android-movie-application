package com.example.screentime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.adapters.ViewPagerAdapter;
import com.example.screentime.entities.MovieModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FavouriteListActivity extends AppCompatActivity implements MovieListAdapter.CheckboxCheckedListener {
    private ListView lvFavourites;
    private List<MovieModel> favouriteMovies = new ArrayList<>();

    private DatabaseReference favouriteListReference;
    private DatabaseReference completeListReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        initComponents();
        Log.d("gigi", String.valueOf(MovieListActivity.movieIds));
        populateListView();
    }

    private void saveHashMap() {

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("info.txt", getApplicationContext().MODE_PRIVATE);
            final long initialPosition = fos.getChannel().position();
            fos.getChannel().position(initialPosition);
            fos.getChannel().truncate(initialPosition);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(MovieListActivity.movieIds);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateListView() {
        favouriteMovies.clear();
        favouriteListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    MovieModel movie = data.getValue(MovieModel.class);
                    favouriteMovies.add(movie);

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
        MovieListAdapter adapter = (MovieListAdapter) lvFavourites.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private HashMap<String, String> restoreHashMap() {
        HashMap<String, String> outputMap = new HashMap<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(getApplicationContext().getFilesDir() + "/info.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            outputMap = (HashMap<String, String>) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException | ClassCastException e) {
            e.printStackTrace();
        }
        return outputMap;


    }

    public void initComponents() {
        lvFavourites = findViewById(R.id.lv_favourite_list);
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favouriteListReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("Favourite movies");
        completeListReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("movies");
        addMovieAdapter();
        MovieListActivity.movieIds = restoreHashMap();


    }

    private void addMovieAdapter() {
        MovieListAdapter adapter = new MovieListAdapter(getApplicationContext(), R.layout.my_movie_list_item_layout, favouriteMovies, getLayoutInflater());
        lvFavourites.setAdapter(adapter);
        adapter.setCheckedListener(this);
    }

    @Override
    public void getCheckBoxCheckedListener(int position) {

        //actualizam complete list(facem false la favourite)

        for (Map.Entry<String, String> set : MovieListActivity.movieIds.entrySet()) {
            if (set.getValue().equals(favouriteMovies.get(position).getId())) {
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("favourite", favouriteMovies.get(position).isFavourite());
                completeListReference.child(set.getKey()).updateChildren(taskMap);

            }
        }
        //stergem din lista de favorite

        favouriteListReference.child(favouriteMovies.get(position).getId()).removeValue();
        //stergem din HashMap

        Iterator<Map.Entry<String, String>> iterator = MovieListActivity.movieIds.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue().equals(favouriteMovies.get(position).getId())) {
                iterator.remove();
            }
        }
        favouriteMovies.clear();
        saveHashMap();
    }
}