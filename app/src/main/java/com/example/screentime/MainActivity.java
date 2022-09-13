package com.example.screentime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView tvCreateAccount;
    private TextInputEditText tietEmail;
    private TextInputEditText tietPassword;
    private TextView tvForgotPassword;
    private Button btnLogin;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mUser != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initComponents() {
        tvCreateAccount = findViewById(R.id.tv_create_account);
        tvCreateAccount.setPaintFlags(tvCreateAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvCreateAccount.setOnTouchListener(openRegisterPage());
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tietEmail = findViewById(R.id.tiet_login_email);
        tietPassword = findViewById(R.id.tiet_login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(openHomePage());
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private View.OnClickListener openHomePage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeLogin();
            }
        };
    }

    private void executeLogin() {
        String email = tietEmail.getText().toString();
        String password = tietPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            tietEmail.setError("Email is not valid! Try again!");
        } else if (password.isEmpty() || password.length() < 5) {
            tietPassword.setError("Password is too short!");
        } else {
            progressDialog.setMessage("Please wait  while login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener openRegisterPage() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                return true;
            }
        };
    }
}