package com.example.screentime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.screentime.entities.MovieModel;
import com.example.screentime.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText tietFullName;
    private TextInputEditText tietEmail;
    private TextInputEditText tietPassword;
    private TextInputEditText tietPhoneNumber;
    private Button btnRegister;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
    }

    private void initComponents() {
        tietFullName = findViewById(R.id.tiet_full_name);
        tietEmail = findViewById(R.id.tiet_email);
        tietPassword = findViewById(R.id.tiet_password);
        tietPhoneNumber=findViewById(R.id.tiet_phone_number);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(openHomePage());
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

    }

    private View.OnClickListener openHomePage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        };
    }

    private void Register() {
        String fullName = tietFullName.getText().toString();
        String email = tietEmail.getText().toString();
        String password = tietPassword.getText().toString();
        String phoneNumber = tietPhoneNumber.getText().toString();

        //ArrayList<MovieModel> completeList = new ArrayList<>();
        if (fullName.isEmpty() || fullName.length() < 10) {
            tietFullName.setError("Full Name is too short!");
        } else if (!email.matches(emailPattern)) {
            tietEmail.setError("Email is not valid! Try again!");
        } else if (password.isEmpty() || password.length() < 5) {
            tietPassword.setError("Password is too short!");
        } else if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            tietPassword.setError("Invalid phone number!");
        }else {
            progressDialog.setMessage("Please wait  while registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User user = new User(fullName, email, password, phoneNumber);
                        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference("Users").child(userKey)
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    sendUserToNextActivity();
                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}