package com.example.auto_maatklantenapp;

import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.auto_maatklantenapp.accident.AccidentRapport;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

public class SchadeMeldingFragment extends Fragment {

    EditText odoMeter;
    ImageView accidentPicture;
    Button takePicture;
    Button submit;

    ApiCalls api;
    CameraFunctions cameraFunctions;
    String authToken;
    ActivityResultLauncher<Intent> startCamera;
    ActivityResultLauncher<String[]> cameraPermissionResultLauncher;
    Uri cam_uri;
    String encodedImage;


    public static SchadeMeldingFragment newInstance() {
        return new SchadeMeldingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schade_melding, container, false);

        api = new ApiCalls();
        cameraFunctions = new CameraFunctions();

        odoMeter = view.findViewById(R.id.etOdoMeterInput);
        accidentPicture = view.findViewById(R.id.ivAccidentPicture);
        takePicture = view.findViewById(R.id.btnTakeFoto);
        submit = view.findViewById(R.id.btnReportAccident);

        registerResult();
        initializeCameraPermissionActivityResult();
        getAuthToken();

        takePicture.setOnClickListener(v -> {
            if (cameraFunctions.allPermissionsGranted(getContext())) {
                takePicture();
            } else {
                cameraPermissionResultLauncher.launch(cameraFunctions.getRequiredPermissions());
            }
        });

        submit.setOnClickListener(v -> {
            try {
                submitReport();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        if (!cameraFunctions.allPermissionsGranted(getContext())) {
            submit.setEnabled(false);
        }

        return view;
    }

    private void getAuthToken() {
        try {
            api.Authenticate(new ApiCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    try {
                        authToken = jsonArray.getJSONObject(0).getString("id_token");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IOException e) {
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void takePicture() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Accident Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        cam_uri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);

        startCamera.launch(cameraIntent);
    }

    private void registerResult() {
        startCamera  = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            accidentPicture.setImageURI(cam_uri);

                            final Uri imageUri = cam_uri;
                            final InputStream imageStream = getActivity()
                                    .getContentResolver()
                                    .openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory
                                    .decodeStream(imageStream);
                            encodedImage = cameraFunctions.encodeImage(selectedImage);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(),"No image selected", Toast.LENGTH_SHORT)
                                .show();
                        e.printStackTrace();
                    }
                }
        );
    }

    private void initializeCameraPermissionActivityResult() {
        cameraPermissionResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                    // Handle Permission granted/rejected
                    boolean permissionGranted = true;
                    for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                        Log.v("TAGWAGGE", String.valueOf(entry));
                        Log.v("TAGWAGGE", Arrays.toString(cameraFunctions.getRequiredPermissions()));
                        if (Arrays.asList(cameraFunctions.getRequiredPermissions()).contains(entry.getKey()) && !entry.getValue()) {
                            permissionGranted = false;
                        }
                    }

                    if (!permissionGranted) {
                        Toast.makeText(
                                getContext(),
                                "Permission request denied",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        submit.setEnabled(true);
                    }
                });
    }

    private void submitReport() throws JSONException {
       api = new ApiCalls();
        api.sendAccidentReport(createAccidentRapport(), authToken, new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                Log.d("AutoMaatApp", "Success :D");
            }

            @Override
            public void onFailure(IOException e) {
                Log.d("AutoMaatApp", "Failure :(");
            }
        });
    }

    private AccidentRapport createAccidentRapport() {
        AccidentRapport accidentRapport = new AccidentRapport();
        accidentRapport.setCode("This is a code");
        accidentRapport.setOdoMeter(Integer.parseInt(odoMeter.getText().toString()));
        accidentRapport.setResult("This is a result");
        accidentRapport.setPhoto(encodedImage);
        accidentRapport.setCompleted("");
        return accidentRapport;
    }
}