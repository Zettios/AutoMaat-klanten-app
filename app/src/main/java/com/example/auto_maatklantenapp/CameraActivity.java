package com.example.auto_maatklantenapp;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.jvm.internal.Intrinsics;

public class CameraActivity extends AppCompatActivity {
    private ImageCapture imageCapture;
    ImageButton capture;
    PreviewView cameraPreview;
    Camera camera;

    private ExecutorService cameraExecutor;
    private ActivityResultLauncher<String[]> activityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camera = new Camera();
        initializeActivityResultLauncher();

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            requestPermissions();
        }

        cameraPreview = findViewById(R.id.cameraPreview);
        capture = findViewById(R.id.capture);
        capture.setOnClickListener(v -> {
            takePhoto();
        });

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private void initializeActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                    // Handle Permission granted/rejected
                    boolean permissionGranted = true;
                    for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                        Log.v("TAGWAGGE", String.valueOf(entry));
                        Log.v("TAGWAGGE", Arrays.toString(camera.getRequiredPermissions()));
                        if (Arrays.asList(camera.getRequiredPermissions()).contains(entry.getKey()) && !entry.getValue()) {
                            permissionGranted = false;
                        }
                    }
                    if (!permissionGranted) {
                        Toast.makeText(
                                getApplicationContext(),
                                "Permission request denied",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        startCamera();
                    }
                });
    }

    private void startCamera() {
        Log.v("AutomaatApp", "Starting camera");
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

                // Select back camera as a default
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                imageCapture = new ImageCapture.Builder().setTargetRotation(
                        this.getWindowManager().getDefaultDisplay().getRotation()).build();

                // Unbind use cases before rebinding
                cameraProvider.unbindAll();

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
            } catch(Exception exc) {
                Log.e("AutoMaatApp", "Use case binding failed", exc);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void requestPermissions() {
        activityResultLauncher.launch(camera.getRequiredPermissions());
    }

    private boolean allPermissionsGranted() {
        for (String permission : camera.getRequiredPermissions()) {
            if (ContextCompat.checkSelfPermission(CameraActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void takePhoto() {
        String name = new SimpleDateFormat(camera.getFileNameFormat(), Locale.US).format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > 28) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(
                        this.getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                .build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
            new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    Intrinsics.checkNotNullParameter(outputFileResults, "output");
                    String msg = "Photo capture succeeded: " + outputFileResults.getSavedUri();
                    Toast.makeText(CameraActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Log.d("CameraXApp", msg);
                    //incidentPictureListener.IncidentPictureListener(msg);
                    finish();
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    Intrinsics.checkNotNullParameter(exception, "exc");
                    Log.e("CameraXApp", "Photo capture failed: " + exception.getMessage(), exception);
                }
            }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}