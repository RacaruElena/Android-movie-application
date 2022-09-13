package com.example.screentime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.screentime.fragments.FollowingFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditSettingsActivity extends AppCompatActivity {
    private DatabaseReference usersReference;
    private String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextInputEditText tietFullName;
    private TextInputEditText tietEmail;
    private TextInputEditText tietPhoneNumber;
    private Button btnSave;
    private TextView tvResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_settings);
        initComponents();
        populateEditTexts();
    }

    private void initComponents() {
        tietFullName = findViewById(R.id.tiet_settings_fullName);
        tietEmail = findViewById(R.id.tiet_settings_email);
        tietPhoneNumber = findViewById(R.id.tiet_settings_phone_number);
        usersReference = FirebaseDatabase.getInstance().getReference("Users");
        btnSave = findViewById(R.id.btn_settings_save);
        btnSave.setOnClickListener(save());
        tvResetPassword = findViewById(R.id.tv_reset_password);
        tvResetPassword.setPaintFlags(tvResetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvResetPassword.setOnClickListener(resetPassword());

    }

    private View.OnClickListener resetPassword() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
                finish();

            }
        };
    }

    private View.OnClickListener save() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFullName = tietFullName.getText() != null ? tietFullName.getText().toString() : "";
                String newEmail = tietEmail.getText() != null ? tietEmail.getText().toString() : "";
                String newPhoneNumber = tietPhoneNumber.getText() != null ? tietPhoneNumber.getText().toString() : "";

                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("email", newEmail);
                taskMap.put("fullName", newFullName);
                taskMap.put("phoneNumber", newPhoneNumber);

                usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getKey().equals(userKey)) {
                                usersReference.child(data.getKey()).updateChildren(taskMap);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                finish();
            }
        };
    }

    private void populateEditTexts() {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals(userKey)) {
                        tietFullName.setText(data.child("fullName").getValue(String.class));
                        tietEmail.setText(data.child("email").getValue(String.class));
                        tietPhoneNumber.setText(data.child("phoneNumber").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Data is not available");
            }

        });
    }


}