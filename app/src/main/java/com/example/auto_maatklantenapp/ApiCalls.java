package com.example.auto_maatklantenapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.auto_maatklantenapp.accident.AccidentRapport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCalls {

    JSONObject authToken;
    String baseurl = "https://cheetah-inviting-miserably.ngrok-free.app";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void GetDataFromCars(String path, ApiCallback callback){
        OkHttpClient client = new OkHttpClient();

        String url = baseurl + path;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    JSONArray jsonArray;
                    try {
                        jsonArray = new JSONArray(myResponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    callback.onSuccess(jsonArray);
                }
            }
        });
    }

    public void registerNewAccount(ApiCallback callback, JSONObject jsonBody){
        Log.w("myApp", "inside Register");
        OkHttpClient client = new OkHttpClient();
        String path = "/api/AM/register";
        String url = baseurl + path;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        Request request = new Request.Builder().url(url).post(requestBody).build();

        Log.w("myApp", "Starting call");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("myApp", "Registration Failed");
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.w("myApp", "DIT IS DE RESPONSE: " + response);
                Log.w("myApp", "onResponse");
                if(response.isSuccessful()) {
                    String myResponse = response.body().toString();
                    Log.w("myApp", "response successful");
                }
                else {
                    Log.w("myApp", "Registration Failed");
                }
            }
        });
    }

    public void Authenticate(ApiCallback callback, String username, String password, boolean persistence) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + "/api/authenticate";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("rememberMe", persistence);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() ) {
                    if (response.body() != null) {
                        String responseData = response.body().string();
                        try {
                            authToken = new JSONObject(responseData);
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(authToken);
                            callback.onSuccess(jsonArray);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    public void resetPasswordInit(ApiCallback callback, String email){
        Log.w("myApp", "inside resetPasswordInit");
        OkHttpClient client = new OkHttpClient();
        String url = baseurl +"/api/account/reset-password/init";
        JSONObject jsonBody = new JSONObject();
        RequestBody requestBody = RequestBody.create(email, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.w("myApp", "my response: " + response);
                }
            }
        });
    }

    public String getAuthToken() throws JSONException {
        if (authToken != null) {
            try {
                return authToken.getString("id_token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;  // Return a default value or handle accordingly
    }

    public void GetAllRentals(String path, String authToken, ApiCallback callback){
        OkHttpClient client = new OkHttpClient();

        String url = baseurl + path;


        Log.w("myApp", "authentication TOKEN: " + authToken);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + authToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.w("myApp", "MYRESPONSE: " +response);
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(myResponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    callback.onSuccess(jsonArray);
                }
            }
        });
    }

    public void sendAccidentReport(AccidentRapport accidentReport, String authToken, ApiCallback apiCallback) throws JSONException {
        OkHttpClient client = new OkHttpClient();

        String url = baseurl + "/api/inspections";

        JSONObject accidentJsonObject = new JSONObject();
        JSONObject carJsonObject = new JSONObject();
        JSONObject rentalJsonObject = new JSONObject();

        accidentJsonObject.put("code", accidentReport.getCode());
        accidentJsonObject.put("odometer", accidentReport.getOdoMeter());
        accidentJsonObject.put("result", accidentReport.getResult());
        accidentJsonObject.put("photo", accidentReport.getPhoto());
        accidentJsonObject.put("photoContentType", accidentReport.getPhotoContentType());
        accidentJsonObject.put("completed", accidentReport.getCompleted());

        carJsonObject.put("id", 1);
        accidentJsonObject.put("car", carJsonObject);

        rentalJsonObject.put("id", 1);
        accidentJsonObject.put("rental", rentalJsonObject);

        RequestBody formBody = RequestBody.create(accidentJsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + authToken)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiCallback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                JSONObject responseData = new JSONObject();
                try {
                    responseData.put("code", response.code());
                    if (response.isSuccessful()) {
                        responseData.put("title", "Success");
                        responseData.put("message", "Uw incident is succesvol binnengekomen");
                    } else {
                        responseData.put("title", "Error");
                        responseData.put("message", "Er is iets misgegaan. Probeer het opnieuw of neem contact met ons op.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiCallback.onSuccess(new JSONArray().put(responseData));
            }
        });
    }
}
