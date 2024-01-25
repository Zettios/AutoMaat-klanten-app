package com.example.auto_maatklantenapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Base64;

import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class CameraFunctions {
    private String[] REQUIRED_PERMISSIONS;

    public CameraFunctions() {
        getCamerPermissions();
    }

    public void getCamerPermissions() {
        String[] requiredPermissionsList = new String[] {
                Manifest.permission.CAMERA,
        };

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            String[] additionalPermissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            String[] combinedPermissions = new String[requiredPermissionsList.length + additionalPermissions.length];
            System.arraycopy(requiredPermissionsList, 0, combinedPermissions, 0, requiredPermissionsList.length);
            System.arraycopy(additionalPermissions, 0, combinedPermissions, requiredPermissionsList.length, additionalPermissions.length);
            requiredPermissionsList = combinedPermissions;
        }
        REQUIRED_PERMISSIONS = requiredPermissionsList;
    }

    public boolean allPermissionsGranted(Context context) {
        for (String permission : getRequiredPermissions()) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.NO_WRAP);

        return encImage;
    }

    public String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }

}


