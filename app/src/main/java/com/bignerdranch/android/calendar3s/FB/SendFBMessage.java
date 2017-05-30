package com.bignerdranch.android.calendar3s.FB;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ieem5 on 2017-05-18.
 */

public class SendFBMessage extends AsyncTask<String, String, String> {
    private String token, message;

    public SendFBMessage(String token, String message) {
        this.token = token;
        this.message = message;
    }

    //SendFBMessage sendFBMessage= new SendFBMessage();
    //sendFBMessage.execute(message);


    @Override
    protected String doInBackground(String... params) {
        String  message= params[0];
        String token = "c0NOXp_J_RE:APA91bF-AHbTXGyusrunJL5W9m-ZMSlbynErJlxDa56xJ9U8wRLfi2PGlo6C2Y_f4uCP9DN6mM5o__IX6n_5uhagdtscLr32mFqOXgw2Yf2iugAlzfn9GLBC9uN9Cl8-oL1MEeRnS5RQ";//태블릿



        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .add("message", message)
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://113.198.84.66/Calendar3S/FCMsendMyself.php")
                .post(body)
                .build();

        Log.i("jmlee", "전송완료");

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            Log.i("jmlee", e.toString());
        }
        return null;
    }
}
