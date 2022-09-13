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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.screentime.AddMovieActivity;
import com.example.screentime.GetRecommendationsActivity;
import com.example.screentime.MovieDetailsPopupActivity;
import com.example.screentime.MoviesByGenreActivity;
import com.example.screentime.R;
import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.entities.MovieModel;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class CompleteListFragment extends Fragment implements MovieListAdapter.CheckboxCheckedListener, MovieListAdapter.CustomButtonListener {
    public static final String MOVIE_POPUP_KEY = "movie";
    public static final String LIST_KEY = "LIST_KEY";
    public static final String RECOMMENDATIONS_KEY = "RECOMMENDATIONS_KEY";
    private Button btn_add_movie;
    private TextView tvName;
    private ActivityResultLauncher<Intent> addSMovieLauncher;
    private ActivityResultLauncher<Intent> saveMovieNotesLauncher;
    private ListView lvMovies;
    public static ArrayList<MovieModel> movies = new ArrayList<>();
    public static HashMap<String, String> movieIds;
    private CheckBox checkBoxFavourite;
    private FrameLayout frameLayout;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private DatabaseReference completeListReference;
    private DatabaseReference favouriteListReference;

    public CompleteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete_list, container, false);
        initComponents(view);
        populateListView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        frameLayout.requestLayout();
    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            saveHashMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            saveHashMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveHashMap() {

        try {
            FileOutputStream fos = getActivity().openFileOutput("info.txt", getActivity().MODE_PRIVATE);
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
            FileInputStream fileInputStream = new FileInputStream(getActivity().getFilesDir() + "/info.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            outputMap = (HashMap<String, String>) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException | ClassCastException e) {
            e.printStackTrace();
        }
        return outputMap;


    }

    private void populateListView() {
        completeListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movies.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MovieModel movie = data.getValue(MovieModel.class);
                    movies.add(movie);
                }
                notifyAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Data is not available");
            }
        });
    }

    private void initComponents(View view) {
        //btn_add_movie = view.findViewById(R.id.btn_add_movie);
        checkBoxFavourite = view.findViewById(R.id.cb_favourite);
        //btn_add_movie.setOnClickListener(openAddMovieActivity());
        addSMovieLauncher = registerAddMovieLauncher();
        saveMovieNotesLauncher = registerSaveMovieNotesLauncher();
        lvMovies = view.findViewById(R.id.lv_moviess);
        lvMovies.setOnItemClickListener(openDetailsPopup());
        addMovieAdapter();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        frameLayout = view.findViewById(R.id.frame_layout_complete_list);
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        completeListReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("movies");
        favouriteListReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("Favourite movies");
        movieIds = restoreHashMap();
        Log.d("afis", String.valueOf(movieIds));

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


    private AdapterView.OnItemClickListener openDetailsPopup() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Log.d("note", "note: " + movies.get(position).getNotes());
                Intent intent = new Intent(getActivity(), MovieDetailsPopupActivity.class);
                intent.putExtra(MOVIE_POPUP_KEY, movies.get(position));
                saveMovieNotesLauncher.launch(intent);
            }
        };
    }

    private void addMovieAdapter() {
        MovieListAdapter adapter = new MovieListAdapter(getContext().getApplicationContext(), R.layout.my_movie_list_item_layout, movies, getLayoutInflater());
        lvMovies.setAdapter(adapter);
        adapter.setCheckedListener(this);
        adapter.setCustomButtonListener(this);
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
        lvMovies.requestLayout();
    }


    @Override
    public void getCheckBoxCheckedListener(int position) {
        if (movies.get(position).isFavourite()) {
            String completeListId = movies.get(position).getId();
            completeListReference.child(completeListId).setValue(movies.get(position));
            String favouriteListId = favouriteListReference.push().getKey();
            movies.get(position).setId(favouriteListId);
            favouriteListReference.child(favouriteListId).setValue(movies.get(position));
            movieIds.put(completeListId, favouriteListId);
        } else {
            favouriteListReference.child(movieIds.get(movies.get(position).getId())).removeValue();
            completeListReference.child(movies.get(position).getId()).setValue(movies.get(position));
            movieIds.remove(movies.get(position).getId());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_movie_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add_movie) {
            Intent intent = new Intent(getContext().getApplicationContext(), AddMovieActivity.class);
            addSMovieLauncher.launch(intent);

        }
        return true;
    }

    @Override
    public void onButtonClickListener(int position) {
        completeListReference.child(movies.get(position).getId()).removeValue();
        if (movieIds.containsKey(movies.get(position).getId())) {
            favouriteListReference.child(movieIds.get(movies.get(position).getId())).removeValue();
            movieIds.remove(movies.get(position).getId());
            saveHashMap();
        }
        movies.clear();
    }
}