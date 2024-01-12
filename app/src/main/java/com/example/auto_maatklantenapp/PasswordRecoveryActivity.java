package com.example.auto_maatklantenapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordRecoveryActivity extends AppCompatActivity {
    private Button recoverBtn, returnBtn;
    private EditText emailField;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // recoverBtn = findViewById(R.id.recoverBtn);
       // returnBtn = findViewById(R.id.returnBtn);
       // emailField = findViewById(R.id.editTextEmail);

        returnBtn.setOnClickListener(v -> {
            Intent i = new Intent(PasswordRecoveryActivity.this, LoginActivity.class);
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

        recoverBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();

            if(isValidEmailAddress(emailField, email)){
                SendRecoveryEmail(email);
            }
        });
    }

    private boolean isValidEmailAddress(EditText emailField, String email){
        if(TextUtils.isEmpty(email)){
            emailField.setError("A valid Email-address is required.");
            return false;
        }
        //TODO: Check if Email-address is within our system, if not: return false.
        return true;
    }

    private void SendRecoveryEmail(String email){
        //TODO: Create Recovery Email System.
    }
}
