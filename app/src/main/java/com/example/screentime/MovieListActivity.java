package com.example.screentime;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.firebase.FirebaseService;
import com.example.screentime.fragments.HomeFragment;
import com.example.screentime.fragments.ListsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieListActivity extends AppCompatActivity implements MovieListAdapter.CheckboxCheckedListener {
    public static final String MOVIE_POPUP_KEY = "movie";
    public static final String LIST_KEY = "LIST_KEY";
    private Button btn_add_movie;
    private TextView tvName;
    private ActivityResultLauncher<Intent> addSMovieLauncher;
    private ActivityResultLauncher<Intent> saveMovieNotesLauncher;
    private ListView lvMovies;
    private ArrayList<MovieModel> movies = new ArrayList<>();
    public static HashMap<String, String> movieIds;
    private CheckBox checkBoxFavourite;
    private NavigationBarView bottomNavigationView;


    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private DatabaseReference completeListReference;
    private DatabaseReference favouriteListReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // try {
        initComponents();
        bottomNavigationView.setSelectedItemId(R.id.menu_item_complete_list);
        Log.d("gigi", String.valueOf(movieIds));
//        } catch (IOException |
//        ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        populateListView();
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            saveHashMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            saveHashMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveHashMap() {

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("info.txt", getApplicationContext().MODE_PRIVATE);
            final long initialPosition = fos.getChannel().position();
            fos.getChannel().position(initialPosition);
            fos.getChannel().truncate(initialPosition);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(movieIds);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void populateListView() {
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                //super.run();
////                new Handler(getMainLooper()).post(new Runnable() {
////                    @Override
////                    public void run() {
////
////
////                    }
////                });
        completeListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movies.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MovieModel movie = data.getValue(MovieModel.class);
                    movies.add(movie);
                    notifyAdapter();
                    //lvMovies.requestLayout();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Data is not available");
            }

        });

//            }
//        };

    }

    private void initComponents() {
        btn_add_movie = findViewById(R.id.btn_add_movie);
        checkBoxFavourite = findViewById(R.id.cb_favourite);
        btn_add_movie.setOnClickListener(openAddMovieActivity());
        addSMovieLauncher = registerAddMovieLauncher();
        saveMovieNotesLauncher = registerSaveMovieNotesLauncher();
        lvMovies = findViewById(R.id.lv_movies);
        lvMovies.setOnItemClickListener(openDetailsPopup());
        lvMovies.setOnItemLongClickListener(deleteMovie());
        addMovieAdapter();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        completeListReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("movies");
        favouriteListReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("Favourite movies");
        movieIds = restoreHashMap();
        //Toast.makeText(getApplicationContext(), (CharSequence) movieIds, Toast.LENGTH_SHORT).show();
        Log.d("afis", String.valueOf(movieIds));
        bottomNavigationView = findViewById(R.id.nav_view_genre);
        //set complete list selected

        bottomNavigationView.setOnItemSelectedListener(navigate());

    }


    private NavigationBarView.OnItemSelectedListener navigate() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_complete_list:
                        return true;
                    case R.id.menu_item_genre:
                        Intent intent = new Intent(getApplicationContext(), MoviesByGenreActivity.class);
                        intent.putParcelableArrayListExtra(LIST_KEY, movies);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_item_back:

                        return true;
                }

                return false;
            }
        };
    }


    private ActivityResultLauncher<Intent> registerSaveMovieNotesLauncher() {
        ActivityResultCallback<ActivityResult> callback = getSaveNotesMovieResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getSaveNotesMovieResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                //update
                if (result.getResultCode() == RESULT_OK && result.getData() != null) { // am primit si intentul
                    MovieModel movie = (MovieModel) result.getData().getParcelableExtra(MovieDetailsPopupActivity.UPDATED_MOVIE_KEY); //iau intentul
                    if (movie != null) {
                        //update in firebase
                        completeListReference.child(movie.getId()).setValue(movie);
                    }
                }
            }
        };
    }

    private AdapterView.OnItemLongClickListener deleteMovie() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (movies.get(position) == null || (movies.get(position).getId() == null || movies.get(position).getId().trim().isEmpty())) {
                    return false;
                }
                completeListReference.child(movies.get(position).getId()).removeValue();
                //lvMovies.requestLayout();
                //delete din lista de favorite daca e bifat
                Log.d("sterge", movies.get(position).getName());
                if (movieIds.containsKey(movies.get(position).getId())) {
                    favouriteListReference.child(movieIds.get(movies.get(position).getId())).removeValue();
                    movieIds.remove(movies.get(position).getId());
                    Log.d("sterge", movies.get(position).getName());
                }

                return true;
            }
        };
    }

    private AdapterView.OnItemClickListener openDetailsPopup() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"fd" , Toast.LENGTH_SHORT).show();
                //  Log.d("note", "note: " + movies.get(position).getNotes());
                Intent intent = new Intent(getApplicationContext(), MovieDetailsPopupActivity.class);
                intent.putExtra(MOVIE_POPUP_KEY, movies.get(position));
                saveMovieNotesLauncher.launch(intent);
            }
        };
    }

    private void addMovieAdapter() {
        MovieListAdapter adapter = new MovieListAdapter(getApplicationContext(), R.layout.my_movie_list_item_layout, movies, getLayoutInflater());
        lvMovies.setAdapter(adapter);
        adapter.setCheckedListener(this);
        //adapter.setCustomButtonListener(this);
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
                        String id = completeListReference.push().getKey();
                        movie.setId(id);
                        completeListReference.child(id).setValue(movie);
                    }
                }
            }
        };
    }

    private void notifyAdapter() {
        MovieListAdapter adapter = (MovieListAdapter) lvMovies.getAdapter();
        adapter.notifyDataSetChanged();
        //lvMovies.requestLayout();
    }

    private View.OnClickListener openAddMovieActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMovieActivity.class);
                addSMovieLauncher.launch(intent);
            }
        };
    }

    @Override
    public void getCheckBoxCheckedListener(int position) {
        Toast.makeText(getApplicationContext(), movies.get(position).getName(),
                Toast.LENGTH_SHORT).show();

        if (movies.get(position).isFavourite()) {
            Toast.makeText(getApplicationContext(), movies.get(position).getName(),
                    Toast.LENGTH_SHORT).show();
            String completeListId = movies.get(position).getId();
            Log.d("favorite", completeListId);
            //insert in firebase in lista de favorite si actualizeaza in lista completa
            completeListReference.child(completeListId).setValue(movies.get(position));
            String favouriteListId = favouriteListReference.push().getKey();
            movies.get(position).setId(favouriteListId);
            favouriteListReference.child(favouriteListId).setValue(movies.get(position));
            movieIds.put(completeListId, favouriteListId);
        } else {
            //sterge din firebase si actualizeaza in lista completa

            favouriteListReference.child(movieIds.get(movies.get(position).getId())).removeValue();
            completeListReference.child(movies.get(position).getId()).setValue(movies.get(position));
            movieIds.remove(movies.get(position).getId());
        }
    }

//    @Override
//    public void onButtonClickListener(int position) {
//        Log.d("ceva", movies.get(position).getName());
//        Toast.makeText(getApplicationContext(), movies.get(position).getName(),
//                Toast.LENGTH_SHORT).show();
//    }
}