package com.example.lisa.datensammlerapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Communicator extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("URL", objects[0].toString());
        Log.d("JSON", objects[1].toString());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, objects[1].toString());
        Request request = new Request.Builder()
                .url(objects[0].toString())
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            Log.d("Response", "ResponseCode: " + response.code() + response.body().string());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.d("Response", "Fehler");
        }
        return null;
    }
}
