package com.example.auto_maatklantenapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auto_maatklantenapp.helper_classes.ApiCallback;
import com.example.auto_maatklantenapp.helper_classes.ApiCalls;
import com.example.auto_maatklantenapp.helper_classes.InternetChecker;

import org.json.JSONArray;

import java.io.IOException;

public class PasswordRecoveryActivity extends AppCompatActivity {

    private Button recoverBtn, returnBtn;
    private EditText emailField;

    InternetChecker internetChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        internetChecker = new InternetChecker();

        emailField = findViewById(R.id.etPasswordForRecovery);
        returnBtn = findViewById(R.id.btnCancelPasswordRecovery);
        recoverBtn = findViewById(R.id.btnSubmitPasswordRecovery);

        returnBtn.setOnClickListener(v -> {
            Intent i = new Intent(PasswordRecoveryActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        recoverBtn.setOnClickListener(v -> {
            if (internetChecker.isOnline(PasswordRecoveryActivity.this)) {
                String email = emailField.getText().toString().trim();
                if(isValidEmailAddress(email)){
                    SendRecoveryEmail(email);
                }
            } else {
                internetChecker.networkErrorDialog(PasswordRecoveryActivity.this,
                        "U moet verbonden zijn met het internet om uw wachtwoord opnieuw in te stellen.");
            }
        });
    }

    public boolean isValidEmailAddress(String email){
        boolean valid = true;
        if(email.equals("")){
            if (emailField != null) emailField.setError("E-mail adres mag niet leeg zijn.");
            valid = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (emailField != null) emailField.setError("Voer een geldige e-mail in.");
            valid = false;
        }
        return valid;
    }

    private void SendRecoveryEmail(String email) {
        ApiCalls apiCalls = new ApiCalls();
        apiCalls.resetPasswordInit(email, new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                PasswordRecoveryActivity.this.runOnUiThread(() -> {
                    onResponseDialog("Bericht", "Er is een e-mail verstuurt naar het gegeven e-mail adres.\nControleer uw e-mail om uw wachtwoord opnieuw in te stellen.");
                });
            }

            @Override
            public void onFailure(IOException e) {
                Log.d("AutoMaatApp", e.toString());
            }
        });
    }

    private void onResponseDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordRecoveryActivity.this, R.style.MyDialogTheme);

        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary, null));
    }
}
