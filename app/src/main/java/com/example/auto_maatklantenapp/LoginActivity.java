package com.example.auto_maatklantenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;

import org.json.JSONArray;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginBtn;
    TextView createBtn, passwordRecoveryBtn;
    CheckBox loginPersistanceBox;
    private Handler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        createBtn = findViewById(R.id.textViewCreateAccount);
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        passwordRecoveryBtn = relativeLayout.findViewById(R.id.textViewPasswordRecovery);
        loginPersistanceBox = relativeLayout.findViewById(R.id.checkBox);

        loginHandler = new Handler(Looper.getMainLooper());

        loginBtn.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String persistence = String.valueOf(loginPersistanceBox.isChecked());
            if(validateLoginData(usernameField, passwordField, username, password)) {
                Log.d("AutoMaatApp", "valid info");
                loginWithEmailAndPassword(username, password, persistence);
            }
        });

        createBtn.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

        passwordRecoveryBtn.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });
    }

    public boolean validateLoginData(EditText usernameField, EditText passwordField, String username, String password){
        if(TextUtils.isEmpty(username)){
            usernameField.setError("Username is Required.");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            passwordField.setError("Password is Required.");
            return false;
        }
        return true;
    }

    private void loginWithEmailAndPassword(String username, String password, String persistence){
        ApiCalls api = new ApiCalls();
        try {
            Log.d("AutoMaatApp", "Start thread");
            api.Authenticate(new ApiCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    Log.d("AutoMaatApp", jsonArray.toString());
                    loginHandler.post(() -> swapScene());
                }

                @Override
                public void onFailure(IOException e) {
                    e.printStackTrace();
                }
            }, username, password, Boolean.parseBoolean(persistence));
        } catch (IOException e) {
            Log.d("AutoMaatApp", "Thread error");
            throw new RuntimeException(e);
        }
    }

    public void swapScene(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }
}
