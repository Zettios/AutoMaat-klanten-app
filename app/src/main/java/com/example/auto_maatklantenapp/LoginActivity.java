package com.example.auto_maatklantenapp;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginBtn;
    TextView createBtn, passwordRecoveryBtn;
    CheckBox loginPersistanceBox;

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

        loginBtn.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String persistence = String.valueOf(loginPersistanceBox.isChecked());

            if(validateLoginData(usernameField, passwordField, username, password)){
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    api.Authenticate(new ApiCallback() {
                        @Override
                        public void onSuccess(JSONArray jsonArray) {
                            //Change Scene
                            swapScene();
                        }

                        @Override
                        public void onFailure(IOException e) {
                        }
                    },username, password, persistence);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void swapScene(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}
