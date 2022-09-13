package com.example.screentime.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.screentime.AddMovieActivity;
import com.example.screentime.DisplayUserMoviesListActivity;
import com.example.screentime.FollowingActivity;
import com.example.screentime.R;
import com.example.screentime.adapters.FollowingAdapter;
import com.example.screentime.entities.ListModel;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowingFragment extends Fragment {
    public static final String USER_LIST_KEY = "USER_LIST_KEY";
    public static final String USERNAME_KEY = "USERNAME_KEY";
    private ListView lvFollowing;
    private ArrayList<User> following = new ArrayList<>();
    public static ArrayList<String> userNames = new ArrayList<>();
    private ArrayList<MovieModel> userMovies;
    private ActivityResultLauncher<Intent> addFollowingLauncher;
    private DatabaseReference usersReference;
    private DatabaseReference completeListReference;
    private DatabaseReference followingListReference;
    private Spinner spn;
    ArrayAdapter<String> adapter;;

    public FollowingFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_following, container, false);
        initComponents(view);
        populateListView();
        //addSpinnerAdapter();
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //loadUsers();
        return view;
    }

    private void initComponents(View view) {
        lvFollowing = view.findViewById(R.id.lv_following);
        lvFollowing.setOnItemClickListener(ViewFollowingMovieList());
        lvFollowing.setOnItemLongClickListener(deleteFollowing());
        addFollowingLauncher = registerAddFollowingLauncher();
        addFollowingAdapter();
        spn = view.findViewById(R.id.spn_ceva);
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersReference = FirebaseDatabase.getInstance().getReference("Users");
        followingListReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("following");
    }

    private void addSpinnerAdapter() {
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, userNames);
        spn.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemLongClickListener deleteFollowing() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                followingListReference.child(following.get(position).getId()).removeValue();
                userNames.remove(following.get(position).getFullName());
                return true;
            }
        };
    }

    private void populateListView() {
        followingListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.clear();
                userNames.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    following.add(user);
                    String name = data.child("fullName").getValue(String.class);
                    userNames.add(name);
                }
                notifyAdapter();
                addSpinnerAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Data is not available");
            }

        });
    }


    private AdapterView.OnItemClickListener ViewFollowingMovieList() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String name = data.child("fullName").getValue(String.class);
                            if (name.equals(following.get(position).getFullName())) {
                                if (data.hasChild("movies")) {
                                    if (data.hasChild("isPublic")) {
                                        boolean isPublic = data.child("isPublic").getValue(Boolean.class);
                                        if (isPublic) {
                                            completeListReference = FirebaseDatabase.getInstance().getReference("Users").child(data.getKey()).child("movies");
                                            completeListReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    userMovies = new ArrayList<>();
                                                    for (DataSnapshot data : snapshot.getChildren()) {
                                                        MovieModel movie = data.getValue(MovieModel.class);
                                                        userMovies.add(movie);
                                                    }
                                                    Intent intent = new Intent(getContext().getApplicationContext(), DisplayUserMoviesListActivity.class);
                                                    intent.putParcelableArrayListExtra(USER_LIST_KEY, userMovies);
                                                    intent.putExtra(USERNAME_KEY, following.get(position).getFullName());
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.e("FirebaseService", "Data is not available");
                                                }
                                            });
                                        }
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("This profile is private! You don't have access to the movie list!")
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                                    }

                                } else {
                                    ArrayList<MovieModel> emptyMovieList = new ArrayList<>();
                                    Intent intent = new Intent(getContext().getApplicationContext(), DisplayUserMoviesListActivity.class);
                                    intent.putParcelableArrayListExtra(USER_LIST_KEY, emptyMovieList);
                                    intent.putExtra(USERNAME_KEY, following.get(position).getFullName());
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseService", "Data is not available");
                    }
                });
            }


        };
    }

    private ActivityResultLauncher<Intent> registerAddFollowingLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddFollowingActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getAddFollowingActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String userName = result.getData().getStringExtra(FollowingActivity.FOLLOWING_KEY);
                    if (userName != null || userName.length() != 0) {
                        //userNames.add(userName);
                        //insert in firebase

                        String id = followingListReference.push().getKey();
                        User user = new User(id, userName);
                        Log.d("idd", user.getId());
                        followingListReference.child(id).setValue(user);

                    }
                }

            }
        };
    }

    private void notifyAdapter() {
        FollowingAdapter adapter = (FollowingAdapter) lvFollowing.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void addFollowingAdapter() {
        FollowingAdapter adapter = new FollowingAdapter(getContext().getApplicationContext(),
                R.layout.following_adapter, following, getLayoutInflater());
        lvFollowing.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.following_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_following) {
            Intent intent = new Intent(getContext().getApplicationContext(), FollowingActivity.class);
            addFollowingLauncher.launch(intent);

        }
        return true;
    }


}