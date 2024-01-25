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

    public void GetDataFromUsers(ApiCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + "/api/users";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    String myResponse = response.body().toString();
                    JSONArray array;
                    try{
                        array = new JSONArray(myResponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    callback.onSuccess(array);
                }
            }
        });
    }

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

    public void Authenticate(ApiCallback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + "/api/authenticate";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", "admin");
            jsonBody.put("password", "admin");
            jsonBody.put("rememberMe", true);
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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", accidentReport.getCode());
        jsonObject.put("odometer", accidentReport.getOdoMeter());
        jsonObject.put("result", accidentReport.getResult());
        jsonObject.put("photo", accidentReport.getPhoto());
        jsonObject.put("photoContentType", accidentReport.getPhotoContentType());
        jsonObject.put("completed", accidentReport.getCompleted());

        Log.d("AutoMaatApp", jsonObject.toString());

        RequestBody formBody = RequestBody.create(jsonObject.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + authToken)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("AutoMaatApp", "Failed :(");
                apiCallback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("AutoMaatApp", "Success!");
                Log.d("AutoMaatApp", response.toString());
                Log.d("AutoMaatApp", String.valueOf(response.body()));
                Log.d("AutoMaatApp", response.body().string());
                Log.d("AutoMaatApp", String.valueOf(response.isSuccessful()));
//                if (response.isSuccessful()) {
//                    String myResponse = response.body().string();
//                    JSONArray jsonArray;
//                    try {
//                        jsonArray = new JSONArray(myResponse);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                }
                apiCallback.onSuccess(new JSONArray());
            }
        });
    }
}
