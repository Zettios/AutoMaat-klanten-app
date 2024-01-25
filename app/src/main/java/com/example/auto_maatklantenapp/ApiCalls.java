package com.example.auto_maatklantenapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.auto_maatklantenapp.accident.AccidentReport;

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

    public void Authenticate(ApiCallback callback) throws IOException{
        Log.w("myApp", "inside authenticate");
        OkHttpClient client = new OkHttpClient();
        String path = "/api/authenticate";
        String url = baseurl + path;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", "admin");
            jsonBody.put("password", "admin");
            jsonBody.put("rememberMe", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Log.w("myApp", "starting call");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("myApp", "failure");
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.w("myApp", "DIT IS DE RESPONSE: " + response);
                Log.w("myApp", "onresponse");
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    Log.w("myApp", "response succesfull");
                    try {
                        authToken = new JSONObject(myResponse);
                        Log.w("myApp", "MYRESPONSE: " +myResponse);
                        Log.w("myApp", "AUTHTOKEN IN AUTHENTICATE: " +authToken);
                        callback.onSuccess(new JSONArray());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.w("myApp", "authentication failed");
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

    public void GetAllRentals(ApiCallback callback, String path, String authToken){
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

    public void sendAccidentReport(AccidentReport accidentReport, ApiCallback apiCallback) throws JSONException {
        OkHttpClient client = new OkHttpClient();

        String url = baseurl + "/api/inspections";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", accidentReport.getCode());
        jsonObject.put("odometer", accidentReport.getOdoMeter());
        jsonObject.put("result", accidentReport.getResult());
        jsonObject.put("photo", accidentReport.getPhoto());
        jsonObject.put("photoContentType", accidentReport.getPhotoContentType());
        jsonObject.put("completed", accidentReport.getCompleted());
        jsonObject.put("photos", accidentReport.getPhoto());
        jsonObject.put("repairs", accidentReport.getRepairs());
        jsonObject.put("car", accidentReport.getCars());
        jsonObject.put("employee", accidentReport.getEmployee());
        jsonObject.put("rental", accidentReport.getRental());

        RequestBody formBody = RequestBody.create(jsonObject.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
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
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    JSONArray jsonArray;
                    try {
                        jsonArray = new JSONArray(myResponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    apiCallback.onSuccess(jsonArray);
                }
            }
        });
    }
}
