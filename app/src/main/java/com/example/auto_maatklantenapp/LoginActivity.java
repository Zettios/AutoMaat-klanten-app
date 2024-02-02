package com.example.auto_maatklantenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

import com.example.auto_maatklantenapp.classes.Customer;
import com.example.auto_maatklantenapp.dao.CustomerDao;
import com.example.auto_maatklantenapp.database.AutoMaatDatabase;
import com.example.auto_maatklantenapp.helper_classes.ApiCallback;
import com.example.auto_maatklantenapp.helper_classes.ApiCalls;
import com.example.auto_maatklantenapp.helper_classes.InternetChecker;

import org.json.JSONArray;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginBtn;
    TextView createBtn, passwordRecoveryBtn;
    CheckBox loginPersistanceBox;

    Handler loginHandler;
    InternetChecker internetChecker;
    Customer customer;

    AutoMaatDatabase db;
    CustomerDao customerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AutoMaatDatabase.class,
                        getResources().getString(R.string.database_name))
                .fallbackToDestructiveMigration()
                .build();

        internetChecker = new InternetChecker();
        customerDao = db.customerDao();

        checkForAutoLogin();

        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        createBtn = findViewById(R.id.textViewCreateAccount);
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        passwordRecoveryBtn = relativeLayout.findViewById(R.id.textViewPasswordRecovery);
        loginPersistanceBox = relativeLayout.findViewById(R.id.checkBox);

        loginHandler = new Handler(Looper.getMainLooper());


        loginBtn.setOnClickListener(v -> {
            if (internetChecker.isOnline(LoginActivity.this)) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String persistence = String.valueOf(loginPersistanceBox.isChecked());
                if(validateLoginData(usernameField, passwordField, username, password)) {
                    loginWithEmailAndPassword(username, password, persistence);
                }
            } else {
                internetChecker.networkErrorDialog(LoginActivity.this,
                        "U moet verbonden zijn met het internet om in te loggen.");
            }
        });

        createBtn.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        passwordRecoveryBtn.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
            startActivity(i);
        });
    }

    public void checkForAutoLogin() {
        try {
            new Thread(() -> {
                if (checkForUser()) {
                    if (customer.getPersistence()) {
                        loginHandler.post(this::swapScene);
                    }
                }
            }).start();
        } catch (Exception e) {
            Log.d("AutoMaatApp", e.toString());
            e.printStackTrace();
        }
    }

    public boolean checkForUser() {
        customer = customerDao.getFirstCustomer();
        return customer != null;
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
            api.LoginUser(new ApiCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    try {
                        int responseCode = (int) jsonArray.get(0);
                        if (responseCode == 200 || responseCode == 201 || responseCode == 202) {
                            customer = new Customer();
                            customer.setLogin((String) jsonArray.get(1));
                            customer.setPassword((String) jsonArray.get(2));
                            customer.setPersistence((Boolean) jsonArray.get(3));
                            customer.setAuthToken((String) jsonArray.get(4));

                            getUserData(api, (String) jsonArray.get(4));
                        } else if (responseCode == 400 || responseCode == 401) {
                            loginHandler.post(() ->
                                    internetChecker
                                    .networkErrorDialog(LoginActivity.this,
                                            "Account error",
                                            "Geen gebruiker gevonden. Controleer uw gebruikersnaam en wachtwoord."));
                        } else {
                            loginHandler.post(() ->
                                    internetChecker
                                    .networkErrorDialog(LoginActivity.this,
                                            "Login error",
                                            "Er is iets misgegaan bij het inloggen. Controleer uw internet of probeer het later opnieuw."));
                        }

                    } catch (Exception e) {
                        Log.d("AutoMaatApp", e.toString());
                        e.printStackTrace();
                    }
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

    public void getUserData(ApiCalls apiCalls, String authToken){
        apiCalls.GetUser(authToken, new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                try {
                    customer.setId((int) jsonArray.get(0));
                    customer.setSystemId((int) jsonArray.get(1));
                    customer.setNr((int) jsonArray.get(2));
                    customer.setFirstName((String) jsonArray.get(3));
                    customer.setLastName((String) jsonArray.get(4));
                    customer.setEmail((String) jsonArray.get(5));
                    customerDao.deleteAll();
                    customerDao.insertCustomer(customer);
                    loginHandler.post(() -> swapScene());
                } catch (Exception e) {
                    Log.d("AutoMaatApp", e.toString());
                }
            }

            @Override
            public void onFailure(IOException e) {
                Log.d("AutoMaatApp", e.toString());
                e.printStackTrace();
            }
        });
    }

    public void swapScene(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
