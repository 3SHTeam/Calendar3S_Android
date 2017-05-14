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

public class SendToDB extends Thread{
    private static final String TAG = "SendToDB";
    private String PHP, message;
    private String result;
    private static OkHttpClient client;

    public SendToDB(String php, String message){
        PHP = php;
        this.message = message;
        Log.d("sendDB","php = " + PHP + " message = " + this.message);

    }

    @Override
    public void run() {
        Log.d("sendDB","start");
        client = new OkHttpClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http");
        builder.host("113.198.84.66");
        builder.port(80);
        builder.addPathSegments("Calendar3S");
        builder.addPathSegments(PHP);

        RequestBody body = new FormBody.Builder()
                .add("message",message)
                .build();

        Log.d("sendDB","url = " + builder.build());
        Log.d("sendDB","builderString  = " + builder.toString());

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

        Log.d("sendDB","end");
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}


