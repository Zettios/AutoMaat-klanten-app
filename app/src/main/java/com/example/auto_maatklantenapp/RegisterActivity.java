package com.example.auto_maatklantenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText[] registerFieldData;
    Button registerBtn;
    TextView loginBtn;
    int minPasswordLength = 6;

    enum RegistrationData {
        USERNAME(0), EMAIL(1), PASSWORD(2), REP_PASSWORD(3), PHONE_NUMBER(4);
        final int value;

        private RegistrationData(int value){
            this.value = value;
        }

        public int toInt(){
            return value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerFieldData = new EditText[]{
                findViewById(R.id.editTextUserName),
                findViewById(R.id.editTextEmail),
                findViewById(R.id.editTextPassword),
                findViewById(R.id.editTextRepeatPassword),
                findViewById(R.id.editTextPhoneNr)
        };
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);

        registerBtn.setOnClickListener(v -> {
            if(ValidateRegistrationData(registerFieldData)){
                registerUsingData();
            }
        });

        loginBtn.setOnClickListener(v -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });
    }

    public boolean ValidateRegistrationData(EditText[] regFields) {
        // Make sure no field is empty.
        for (EditText regField : regFields) {
            if (TextUtils.isEmpty(regField.getText().toString().trim())) {
                regField.setError("Field is Required");
                return false;
            }
        }
        //Only if password length is required will it be checked for.
        if(minPasswordLength > 0){
            if (regFields[RegistrationData.PASSWORD.toInt()].getText().toString().trim().length() < minPasswordLength){
                regFields[RegistrationData.PASSWORD.toInt()].setError(
                        String.format("Password must be at least %s characters long", minPasswordLength));
                return false;
            }
        }
        //Make sure given passwords match.
        if(!regFields[RegistrationData.REP_PASSWORD.toInt()].getText().toString().trim()
                .equals(regFields[RegistrationData.PASSWORD.toInt()].getText().toString().trim())){
            regFields[RegistrationData.REP_PASSWORD.toInt()].setError("Password does not match initial password");
            return false;
        }
        return true;
    }

    private void registerUsingData() {
        //TODO: Implement Registration Functionality
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
