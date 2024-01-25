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
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCalls {

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

    public void Authenticate(ApiCallback callback, String username, String password, String persistence) throws IOException{
        OkHttpClient client = new OkHttpClient();
        String path = "/api/authenticate";
        String url = baseurl + path;

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("rememberMe", persistence)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
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

    public void GetAllRentals(ApiCallback callback, String path) {
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
