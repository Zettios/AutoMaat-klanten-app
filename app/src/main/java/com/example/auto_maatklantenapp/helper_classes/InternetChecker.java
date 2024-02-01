package com.example.auto_maatklantenapp.helper_classes;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.auto_maatklantenapp.PasswordRecoveryActivity;
import com.example.auto_maatklantenapp.R;

public class InternetChecker {
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void networkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);

        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary, null));
    }

    public void networkErrorDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);

        builder.setTitle("Internet error").setMessage(message);
        builder.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary, null));
    }
}
