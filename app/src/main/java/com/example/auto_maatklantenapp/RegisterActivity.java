package com.example.auto_maatklantenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText[] registerFieldData;
    Button registerBtn;
    TextView loginBtn;
    int minPasswordLength = 6;

    enum RegistrationData {
        FIRST_NAME(0), LAST_NAME(1), USER_NAME(2), EMAIL(3), PASSWORD(4), REP_PASSWORD(5);
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
                findViewById(R.id.editTextFirstName),
                findViewById(R.id.editTextLastName),
                findViewById(R.id.editTextUserName),
                findViewById(R.id.editTextEmail),
                findViewById(R.id.editTextPassword),
                findViewById(R.id.editTextRepeatPassword),
        };
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);

        registerBtn.setOnClickListener(v -> {
            if(validateRegistrationData(registerFieldData)){
                registerUsingData();
            }
        });

        loginBtn.setOnClickListener(v -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });
    }

    public boolean validateRegistrationData(EditText[] regFields) {
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
        ApiCalls api = new ApiCalls();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("login", registerFieldData[RegistrationData.USER_NAME.toInt()].getText().toString().trim());
            jsonBody.put("firstName", registerFieldData[RegistrationData.FIRST_NAME.toInt()].getText().toString().trim());
            jsonBody.put("lastName", registerFieldData[RegistrationData.LAST_NAME.toInt()].getText().toString().trim());
            jsonBody.put("email", registerFieldData[RegistrationData.EMAIL.toInt()].getText().toString().trim());
            jsonBody.put("password", registerFieldData[RegistrationData.PASSWORD.toInt()].getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.w("myApp", jsonBody.toString());

        api.registerNewAccount(new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                swapScene();
            }

            @Override
            public void onFailure(IOException e) {
            }
        }, jsonBody);
    }

    public void swapScene(){
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}
