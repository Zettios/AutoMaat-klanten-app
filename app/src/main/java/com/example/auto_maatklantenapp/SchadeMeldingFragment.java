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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.auto_maatklantenapp.accident.AccidentReport;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SchadeMeldingFragment extends Fragment {
    ImageView accidentPicture;
    Button takePicture;
    Button submit;

    ActivityResultLauncher<Intent> startCamera;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schade_melding, container, false);

        accidentPicture = view.findViewById(R.id.ivAccidentPicture);
        takePicture = view.findViewById(R.id.btnTakeFoto);
        submit = view.findViewById(R.id.btnReportAccident);
        registerResult();

        takePicture.setOnClickListener(v -> {
//            Intent myIntent = new Intent(getActivity(), CameraActivity.class);
//            getActivity().startActivity(myIntent);
            takePicture();
        });

        submit.setOnClickListener(v -> {
            try {
                submitReport(cam_uri);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        return view;
    }

    private void takePicture() {
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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
                            encodedImage = encodeImage(selectedImage);
                        }
                    } catch (Exception e) {
                        Toast.makeText(
                                getActivity(),
                                "No image selected",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
        );
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.NO_WRAP);

        return encImage;
    }

    private void submitReport(Uri uri) throws JSONException {
        AccidentReport accidentReport = new AccidentReport();
        accidentReport.setCode("");
        accidentReport.setOdoMeter(17061);
        accidentReport.setResult("This is a result");
        accidentReport.setPhoto(encodedImage);
        accidentReport.setCompleted("");

        ApiCalls api = new ApiCalls();
        api.sendAccidentReport(accidentReport, new ApiCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                Log.d("AutoMaatApp", "Success!");
                Log.d("AutoMaatApp", jsonArray.toString());
            }

            @Override
            public void onFailure(IOException e) {
                Log.d("AutoMaatApp", "Failed :(");
            }
        });
    }
}