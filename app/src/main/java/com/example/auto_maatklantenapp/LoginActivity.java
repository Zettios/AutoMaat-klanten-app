package com.example.auto_maatklantenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Button loginBtn;
    TextView createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        createBtn = findViewById(R.id.textViewCreateAccount);

        loginBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if(ValidateLoginData(emailField, passwordField, email, password)){
                loginWithEmailAndPassword(email, password);
            }
        });

        createBtn.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

    }

    public boolean ValidateLoginData(EditText emailField, EditText passwordField, String email, String password){
        if(TextUtils.isEmpty(email)){
            emailField.setError("Email is Required.");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            passwordField.setError("Password is Required.");
            return false;
        }
        return true;
    }

    private void loginWithEmailAndPassword(String email, String password){
        //TODO: Implement Login Functionality
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}
