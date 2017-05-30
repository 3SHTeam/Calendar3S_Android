package com.bignerdranch.android.calendar3s.database;

import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ieem5 on 2017-04-14.
 */

public class UpdateToDB extends Thread{
    private static final String TAG = "SendToDB";
    private String PHP, message, id;
    private String result;
    private static OkHttpClient client;

    public UpdateToDB(String php, String message, String id){
        PHP = php;
        this.message = message;
        this.id = id;
        Log.d("sendDB","php = " + PHP + " message = " + this.message + " id = " + this.id);

    }

    @Override
    public void run() {
        Log.d("UpdateToDB","start");
        client = new OkHttpClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http");
        builder.host("113.198.84.67");
        builder.port(80);
        builder.addPathSegments("Calendar3S");
        builder.addPathSegments(PHP);

        RequestBody body = new FormBody.Builder()
                .add("message",message)
                .add("id",id)
                .build();

        Log.d("UpdateToDB","url = " + builder.build());
        Log.d("UpdateToDB","builderString  = " + builder.toString());

        //request
        Request request = new Request.Builder()
                .url(builder.build())
                .post(body)
                .build();

        try {
            //response
            Response response = null;
            response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                throw new IOException(response.message() + " " + response.toString());
            }
            setResult(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("UpdateToDB","end");
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}


