package com.example.screentime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextInputEditText editTextEmail;
    private AppCompatButton btnResetPassword;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initComponents();
    }

    private void initComponents() {
        editTextEmail = findViewById(R.id.tiet_email_reset_password);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        auth = FirebaseAuth.getInstance();
        btnResetPassword.setOnClickListener(resetPassword());
    }

    private View.OnClickListener resetPassword() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required!");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please provide valid email!");
                    editTextEmail.requestFocus();
                    return;
                }
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong! Try again!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                //finish();

            }
        };
    }
}
