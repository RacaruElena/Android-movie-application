package com.example.screentime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.screentime.entities.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView profilePicture;
    private FloatingActionButton fabChangePicture;
    private AppCompatButton btnSettings;
    private SwitchCompat switchDarkTheme;
    private SwitchCompat switchPublicList;
    private Uri imageUri;
    private static String MY_PREFS = "saveSwitchState";
    private static String SWITCH_STATUS = "switch_status";

    private TextView textView;
    private User currentUser;
    boolean switchStatus;
    SharedPreferences myPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth;


    private DatabaseReference usersReference;
    private DatabaseReference publicListReference;
    private String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initComponents();
        populateTextViewUsername();
        setPublicList();
        String filename = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);
        try {
            File localFile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profilePicture.setImageBitmap(bitmap);

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setPublicList() {
        switchPublicList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isPublic = switchPublicList.isChecked();
                publicListReference.setValue(isPublic);
            }
        });
    }

    private void initComponents() {
        profilePicture = findViewById(R.id.circleImageView);
        fabChangePicture = findViewById(R.id.fab_change_profile_picture);
        fabChangePicture.setOnClickListener(changePicture());
        btnSettings = findViewById(R.id.button_settings);
        btnSettings.setOnClickListener(editSettings());
        switchDarkTheme = findViewById(R.id.sw_dark_theme);

        textView = findViewById(R.id.tv_nickname);

        usersReference = FirebaseDatabase.getInstance().getReference("Users");
        publicListReference = FirebaseDatabase.getInstance().getReference("Users").child(userKey).child("isPublic");
        mAuth = FirebaseAuth.getInstance();
        myPreferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        switchStatus = myPreferences.getBoolean(SWITCH_STATUS, false);
        switchDarkTheme.setChecked(switchStatus);
        switchPublicList = findViewById(R.id.switch_public_list);

        switchDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchDarkTheme.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean(SWITCH_STATUS, true);
                    editor.apply();
                    switchDarkTheme.setChecked(true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean(SWITCH_STATUS, false);
                    editor.apply();
                    switchDarkTheme.setChecked(false);
                }

            }
        });
    }

    private void populateTextViewUsername() {
        usersReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals(userKey)) {
                        textView.setText(data.child("fullName").getValue(String.class));
                        if (data.hasChild("isPublic")){
                            boolean isPublic = data.child("isPublic").getValue(Boolean.class);
                            switchPublicList.setChecked(isPublic);
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

    private View.OnClickListener editSettings() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditSettingsActivity.class);
                startActivity(intent);

            }
        };
    }

    private View.OnClickListener changePicture() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(ProfileActivity.this)
//                        .galleryOnly()
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageUri = data.getData();
        profilePicture.setImageURI(imageUri);

        //upload in storage

        String filename = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);
        storageReference.putFile(imageUri);

    }
}