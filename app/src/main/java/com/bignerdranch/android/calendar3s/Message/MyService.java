package com.bignerdranch.android.calendar3s.Message;

import android.app.Activity;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.bignerdranch.android.calendar3s.MainActivity.context;

public class MyService extends Service {
    public static SharedPreferences pref;
    private String Keyword = "약속,회의,모임,미팅,만남,소개팅,전시회,발표회,컨퍼런스,시연회,동창회,결혼,생일,수업,초대,동아리,예약,면접,상견," +
            "시험,과제,업무," +
            "마감,휴일,연휴,휴가,연수,출발,여행,간담회,정모,다과회,파티,초상,문안,장례식,엠티,축제,보강,제출,일정,참석,출석,참가,참여,기념일," +
            "입회,참관,청강,임석,경연,야회,야유회,외식,친목회,참예,선거,청견,근광,승안,연견,회견,접견,면회,보자,보는,봅시다,보도록,보재,봐,뵙," +
            "만나,만납,잡다,잡는,잡아,잡어,잡았,모여,모이는,모입,모이기,모이시,볼래,시간,강좌,강의,나와,나오,공연,뮤지컬,전람회,박물관,연극,극장,"+
            "보러,볼까,갈래,가자,잡자,잡으,잡기,잡길,갑시,납입,기일,데이트,면담,쇼핑,등록,방문,오셔,오시,오길,와주";
    private String KeyWordArray[];

    public MyService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE); //클립보드 간단 예시
        clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                String a = clipboard.getText().toString();
                KeyWordArray = Keyword.split(",");

                for(String clip : KeyWordArray){
                    if(a.contains(clip)){
                        saveMMSInfo(a, "Clipboard", context);
                        break;
                    }
                }
            }
        });
        return START_STICKY;
    }

    private void saveMMSInfo(String str, String phNum, Context context) {
        pref = this.getApplicationContext().getSharedPreferences("acceptSMS", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String  prevStr= pref.getString("SMS", "");
        String input_date;

        long now = System.currentTimeMillis();  //현재 날짜 구하기
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd  HH:mm", Locale.KOREA);
        input_date = sdfNow.format(date);

        String  currStr= str +"@,@,@,@"+phNum + "  " + input_date;
        if(prevStr == ""){
            prevStr= "@@@@@@@"+currStr;
        }else{
            prevStr= prevStr +"@@@@@@@"+currStr;
        }
        Log.d("ssh", prevStr);
        editor.putString("SMS", prevStr);
        editor.commit();
    }
}
