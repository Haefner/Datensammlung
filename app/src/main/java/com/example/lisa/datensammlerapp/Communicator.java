package com.example.lisa.datensammlerapp;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Communicator extends AsyncTask {

    Datensammlung thread;
    public Communicator(Datensammlung ithread)
    {
        thread = ithread;
    }
    @Override
    protected Object doInBackground(final Object[] objects) {
        final Object[] fObjects = objects;
        Log.d("URL",fObjects[0].toString());
        Log.d("JSON", fObjects[1].toString());
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final OkHttpClient client = new OkHttpClient();
        final RequestBody body = RequestBody.create(JSON, objects[1].toString());
        final Request request = new Request.Builder()
                .url(objects[0].toString())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                //final String myResponse = response.body().string();
                Log.d("Response", "ResponseCode: " + response.code() + response.body().string());
                //Log.d("daVal", datenUebertragung.getText() + "");
                Log.d("Test", fObjects[2].toString());
                if (response.code() == 201) {

                    thread.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 thread.daVal.setText(thread.daVal.getText().toString().replaceAll("[0-9]+/", (Integer.parseInt(fObjects[2].toString())) + "/"));
                             }
                         }
                    );
                }
                if (response.code() == 200)
                {
                    thread.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 thread.daVal.setText(fObjects[2].toString());
                             }
                         }
                    );
                }
                if (response.code() > 201)
                {
                    thread.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 thread.daVal.setText("Fehler: " + response.code());
                             }
                         }
                    );
                }

        }});
        return null;
    }
}