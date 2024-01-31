package com.example.auto_maatklantenapp;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
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
import com.example.auto_maatklantenapp.classes.Customer;
import com.example.auto_maatklantenapp.dao.CustomerDao;
import com.example.auto_maatklantenapp.helper_classes.ApiCallback;
import com.example.auto_maatklantenapp.helper_classes.ApiCalls;
import com.example.auto_maatklantenapp.helper_classes.CameraFunctions;
import com.example.auto_maatklantenapp.helper_classes.InternetChecker;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class AccidentRapportFragment extends Fragment {
    CustomerDao customerDao;
    Customer customer;

    EditText odoMeter;
    ImageView accidentPicture;
    Button takePicture;
    Button submit;

    CameraFunctions cameraFunctions;
    String authToken;
    ActivityResultLauncher<Intent> startCamera;
    ActivityResultLauncher<String[]> cameraPermissionResultLauncher;
    Uri cam_uri;
    String encodedImage = "";
    Handler submitResponseMessageHandler;

    public static AccidentRapportFragment newInstance() {
        return new AccidentRapportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        InternetChecker internetChecker = new InternetChecker();

        if (!(internetChecker.isOnline(getActivity()))) {
            Log.w("Connection", "CONNECTION");

            NoConnectionFragment dFragment = NoConnectionFragment.newInstance();
            dFragment.show(getActivity().getSupportFragmentManager(), "NoConnectionFragment");
        }
        View view = inflater.inflate(R.layout.fragment_accident_rapport, container, false);
        customerDao = ((MainActivity) getActivity()).db.customerDao();

        cameraFunctions = new CameraFunctions();
        submitResponseMessageHandler = new Handler(Looper.getMainLooper());

        odoMeter = view.findViewById(R.id.etOdoMeterInput);
        accidentPicture = view.findViewById(R.id.ivAccidentPicture);
        takePicture = view.findViewById(R.id.btnTakeFoto);
        submit = view.findViewById(R.id.btnReportAccident);

        registerCameraResult();
        initializeCameraPermissionActivityResult();

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

        return view;
    }

    private void resetForm() {
        odoMeter.setText("");
        encodedImage = "";
        accidentPicture.setImageURI(null);
        accidentPicture.setBackgroundResource(R.drawable.accident_empty_image);
    }

    private void enableSubmitButton() {
        submit.setBackgroundResource(R.drawable.color_primary_button);
        submit.setEnabled(true);
    }

    private void disableSubmitButton() {
        submit.setBackgroundResource(R.drawable.color_grey_button);
        submit.setEnabled(false);
    }

    private void showProcessingDataFeedback() {
        submit.setText(R.string.processing_text);
    }

    private void endProcessingDataFeedback() {
        submit.setText(R.string.submit_text);
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

    private void registerCameraResult() {
        startCamera  = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (result.getResultCode() == RESULT_OK) {
                            accidentPicture.setImageURI(cam_uri);
                            enableSubmitButton();
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
                        enableSubmitButton();
                    }
                });
    }

    private void submitReport() throws JSONException {
        ApiCalls api = new ApiCalls();
        if (validateData()) {
            disableSubmitButton();
            showProcessingDataFeedback();

            new Thread(() -> {
                customer = customerDao.getCustomer(1);
                Log.d("AutoMaatApp", customer.authToken);
                api.sendAccidentReport(createAccidentRapport(), customer.authToken, new ApiCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        Log.d("AutoMaatApp", "success");
                        submitResponseMessageHandler.post(() -> {
                            try {
                                String title = jsonArray.getJSONObject(0).getString("title");
                                String message = jsonArray.getJSONObject(0).getString("message");
                                onSubmitResponseDialog(title, message);
                                resetForm();
                                endProcessingDataFeedback();
                            } catch (JSONException e)  {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onFailure(IOException e) {
                        submitResponseMessageHandler.post(() -> {
                            enableSubmitButton();
                            endProcessingDataFeedback();
                        });
                        e.printStackTrace();
                    }
                });
            }).start();
        }
    }

    private boolean validateData() {
        if (odoMeter.getText().toString().trim().matches("")) {
            odoMeter.setError("Dit veld mag niet leeg zijn.");
            return false;
        }

        if (Objects.equals(encodedImage, "")) {
            return false;
        }

        return true;
    }

    private AccidentRapport createAccidentRapport() {
        AccidentRapport accidentRapport = new AccidentRapport();
        accidentRapport.setCode("This is a code");
        accidentRapport.setOdoMeter(Integer.parseInt(odoMeter.getText().toString()));
        accidentRapport.setResult("");
        accidentRapport.setPhoto(encodedImage);
        accidentRapport.setCompleted("");
        return accidentRapport;
    }

    private void onSubmitResponseDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary, null));
    }
}