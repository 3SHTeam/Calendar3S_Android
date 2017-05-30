package com.bignerdranch.android.calendar3s;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {
//Notification 취소 버튼 눌렀을 때 알림도 사라지고 액티비티도 바로 종료되므로 아무일도 하지 않는 것처럼 보임

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager!=null){
            notificationManager.cancel(0);
        }

        finish();
    }
}
