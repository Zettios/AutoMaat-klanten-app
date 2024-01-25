package com.example.auto_maatklantenapp;

import android.Manifest;
import android.os.Build;

public class Camera {
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final String[] REQUIRED_PERMISSIONS;

    static {
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

    public String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    public String getFileNameFormat() {
        return FILENAME_FORMAT;
    }
}


