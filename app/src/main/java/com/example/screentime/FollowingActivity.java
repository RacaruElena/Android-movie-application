package com.example.screentime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.screentime.entities.User;
import com.example.screentime.fragments.FollowingFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowingActivity extends AppCompatActivity {
    public static final String FOLLOWING_KEY = "FOLLOWING";
    private ListView lvUsers;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<String> userNames = new ArrayList<>();
    private DatabaseReference usersReference;
    private Intent intent;
    String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        intent = getIntent();
        initComponents();
        populateListView();
        //Log.d("curent", userKey);
    }

    private void populateListView() {
        userNames.clear();
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (!(data.getKey().equals(userKey))
                            && !(FollowingFragment.userNames.contains(data.child("fullName").getValue(String.class)))) {
                        String name = data.child("fullName").getValue(String.class);
                        userNames.add(name);
                    }
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
        ArrayAdapter adapter = (ArrayAdapter) lvUsers.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void initComponents() {
        lvUsers = findViewById(R.id.lv_users);
        usersReference = FirebaseDatabase.getInstance().getReference("Users");
        lvUsers.setOnItemClickListener(AddFollowing());
        addUserAdapter();

    }

    private AdapterView.OnItemClickListener AddFollowing() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra(FOLLOWING_KEY, userNames.get(position));
                setResult(RESULT_OK, intent);
                finish();

            }
        };
    }

    private void addUserAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.listview_text_color_layout, userNames);
        lvUsers.setAdapter(adapter);
    }
}