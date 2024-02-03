package com.example.auto_maatklantenapp.helper_classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.auto_maatklantenapp.accident.AccidentRapport;
import com.example.auto_maatklantenapp.classes.Rental;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCalls {

    JSONObject authToken;
    //String baseurl = "https://measured-adder-concrete.ngrok-free.app";
    String baseurl = "https://cheetah-inviting-miserably.ngrok-free.app";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    private static final String LOGIN_USER_URL = "/api/authenticate";
    private static final String GET_USER_URL = "/api/AM/me";
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
                        JSONObject jsonObject;

                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(response.code());
                        jsonArray.put(username);
                        jsonArray.put(password);
                        jsonArray.put(persistence);

                        try {
                            jsonObject = new JSONObject(responseData);
                            jsonArray.put(jsonObject.get("id_token"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        callback.onSuccess(jsonArray);
                    }
                } else if (response.code() == 400 || response.code() == 401) {
                    JSONArray failureResponse = new JSONArray();
                    failureResponse.put(response.code());
                    callback.onSuccess(failureResponse);
                } else {
                    IOException e = new IOException("Iets is totaal mis gegaan.");
                    callback.onFailure(e);
                }
            }
        });
    }

    public void GetUser(String authToken, ApiCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + GET_USER_URL;

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + authToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() ) {
                    try {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray();

                        JSONObject customerObject = new JSONObject(responseData);
                        JSONObject systemUser = (JSONObject) customerObject.get("systemUser");

                        jsonArray.put(customerObject.get("id"));
                        jsonArray.put(systemUser.get("id"));
                        jsonArray.put(customerObject.get("nr"));
                        jsonArray.put(customerObject.get("firstName"));
                        jsonArray.put(customerObject.get("lastName"));
                        jsonArray.put(systemUser.get("email"));

                        callback.onSuccess(jsonArray);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    callback.onSuccess(new JSONArray());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }
        });
    }

    public void registerNewAccount(JSONObject jsonBody, ApiCallback callback) {
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

    public void sendCarReservation(String authToken, Rental rentalData, ApiCallback apiCallback) {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + RENTALS_ENDPOINT_URL;

        JSONObject rentalObject = createRentalPostObject(rentalData);

        RequestBody formBody = RequestBody.create(rentalObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + authToken)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (response.isSuccessful()) {
                        JSONArray responseData = new JSONArray();
                        responseData.put(response.code());
                        responseData.put(rentalData.getCode());
                        responseData.put(rentalData.getFromDate());
                        apiCallback.onSuccess(responseData);
                    } else {
                        JSONArray responseData = new JSONArray();
                        responseData.put(response.code());
                        apiCallback.onSuccess(responseData);
                    }
                } catch (Exception e) {
                    Log.d("AutoMaatApp", e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiCallback.onFailure(e);
            }
        });
    }

    public JSONObject createRentalPostObject(Rental rentalData) {
        JSONObject rentalObject = new JSONObject();
        JSONObject customerObject = new JSONObject();
        JSONObject carObject = new JSONObject();
        try {
            rentalObject.put("code", rentalData.getCode());
            rentalObject.put("longitude", rentalData.getLongitude());
            rentalObject.put("latitude", rentalData.getLatitude());
            rentalObject.put("fromDate", rentalData.getFromDate());
            rentalObject.put("toDate", rentalData.getToDate());
            rentalObject.put("state", rentalData.getState());
            rentalObject.put("inspections", null);

            customerObject.put("id", rentalData.getCustomerId());
            rentalObject.put("customer", customerObject);

            carObject.put("id", rentalData.getCarId());
            rentalObject.put("car", carObject);
        } catch (Exception e) {
            Log.d("AutoMaatApp", e.toString());
            e.printStackTrace();
        }

        return rentalObject;
    }

    public void GetAllRentals(String authToken, int customerId, ApiCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + RENTALS_ENDPOINT_URL;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("customerId.equals", String.valueOf(customerId));
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
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
                    JSONArray jsonArray;
                    try {
                        jsonArray = new JSONArray(myResponse);
                    } catch (JSONException e) {
                        Log.d("AutoMaatApp", e.toString());
                        throw new RuntimeException(e);
                    }
                    callback.onSuccess(jsonArray);
                } else if (response.code() == 401) {
                    IOException ioException = new IOException("401");
                    callback.onFailure(ioException);
                } else {
                    callback.onFailure(new IOException());
                }
            }
        });
    }

    public void sendAccidentRapport(AccidentRapport accidentRapport, String authToken, ApiCallback apiCallback) {
        OkHttpClient client = new OkHttpClient();
        String url = baseurl + ACCIDENT_RAPPORT_ENDPOINT_URL;

        JSONObject accidentJsonObject = createAccidentRapportObject(accidentRapport);

        RequestBody formBody = RequestBody.create(accidentJsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + authToken)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
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
                    Log.d("AutoMaatApp", e.toString());
                    e.printStackTrace();
                }
                apiCallback.onSuccess(new JSONArray().put(responseData));
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiCallback.onFailure(e);
            }
        });
    }

    public JSONObject createAccidentRapportObject(AccidentRapport accidentRapport) {
        JSONObject accidentJsonObject = new JSONObject();
        JSONObject carJsonObject = new JSONObject();
        JSONObject rentalJsonObject = new JSONObject();

        try {
            accidentJsonObject.put("code", accidentRapport.getCode());
            accidentJsonObject.put("odometer", accidentRapport.getOdoMeter());
            accidentJsonObject.put("result", accidentRapport.getResult());
            accidentJsonObject.put("photo", accidentRapport.getPhoto());
            accidentJsonObject.put("photoContentType", accidentRapport.getPhotoContentType());
            accidentJsonObject.put("completed", accidentRapport.getCompleted());

            carJsonObject.put("id", accidentRapport.getCarId());
            accidentJsonObject.put("car", carJsonObject);

            rentalJsonObject.put("id", accidentRapport.getRentalId());
            accidentJsonObject.put("rental", rentalJsonObject);
        } catch (Exception e) {
            Log.e("AutoMaatApp", e.toString());
        }

        return accidentJsonObject;
    }
}
