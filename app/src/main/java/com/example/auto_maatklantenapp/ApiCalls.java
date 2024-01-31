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
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    private static final String LOGIN_USER_URL = "/api/authenticate";
    private static final String REGISTER_USER_URL = "/api/AM/register";
    private static final String RESET_USER_PASSWORD_INIT_URL =  "/api/account/reset-password/init";
    private static final String USERS_ENDPOINT_URL = "/api/users";
    private static final String CARS_ENDPOINT_URL = "/api/cars";
    private static final String RENTALS_ENDPOINT_URL = "/api/rentals";
    private static final String ACCIDENT_RAPPORT_ENDPOINT_URL = "/api/inspections";

    public void LoginUser(ApiCallback callback, String username, String password, boolean persistence) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + LOGIN_USER_URL;

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

    public String getAuthToken() throws JSONException {
        if (authToken != null) {
            try {
                return authToken.getString("id_token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void registerNewAccount(JSONObject jsonBody, ApiCallback callback) {
        Log.d("AutoMaatApp", jsonBody.toString());
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + REGISTER_USER_URL;
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
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                JSONArray responseData = new JSONArray();
                if(response.isSuccessful()) {
                    responseData.put(1);
                    responseData.put("Account aangemaakt, log alstublieft in.");
                    callback.onSuccess(responseData);
                } else {
                    responseData.put(-1);
                    responseData.put("Gebruiker bestaat al.");
                    callback.onSuccess(responseData);
                }
            }
        });
    }

    public void resetPasswordInit(String email, ApiCallback callback) {
        Log.d("AutoMaatApp", email);
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + RESET_USER_PASSWORD_INIT_URL;
        RequestBody requestBody = RequestBody.create(email, MEDIA_TYPE_MARKDOWN);
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
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                callback.onSuccess(new JSONArray());
            }
        });
    }

    public void GetDataFromUsers(ApiCallback callback){
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + USERS_ENDPOINT_URL;
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

    public void GetAllCars(ApiCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + CARS_ENDPOINT_URL;
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

    public void GetAllRentals(String authToken, ApiCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + RENTALS_ENDPOINT_URL;
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
        String url = baseurl + ACCIDENT_RAPPORT_ENDPOINT_URL;

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
