package com.example.auto_maatklantenapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public interface ApiCallback {
    void onSuccess(JSONArray jsonArray);
    void onFailure(IOException e);
}

