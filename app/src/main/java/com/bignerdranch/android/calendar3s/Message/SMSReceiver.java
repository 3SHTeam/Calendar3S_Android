package com.bignerdranch.android.calendar3s.Message;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SMSReceiver extends BroadcastReceiver {

    private String phNum = "";
    private String str = "";
    private SharedPreferences pref;
    private String Keyword = "약속,회의,모임,미팅,만남,소개팅,전시회,발표회,컨퍼런스,시연회,동창회,결혼,생일,수업,초대,동아리,예약,면접,상견," +
            "시험,과제,업무," +
            "마감,휴일,연휴,휴가,연수,출발,여행,간담회,정모,다과회,파티,초상,문안,장례식,엠티,축제,보강,제출,일정,참석,출석,참가,참여,기념일," +
            "입회,참관,청강,임석,경연,야회,야유회,외식,친목회,참예,선거,청견,근광,승안,연견,회견,접견,면회,보자,보는,봅시다,보도록,보재,봐,뵙," +
            "만나,만납,잡다,잡는,잡아,잡어,잡았,모여,모이는,모입,모이기,모이시,볼래,시간,강좌,강의,나와,나오,공연,뮤지컬,전람회,박물관,연극,극장,"+
            "보러,볼까,갈래,가자,잡자,잡으,잡기,잡길,갑시,납입,기일,데이트,면담,쇼핑,등록,방문,오셔,오시,오길,와주";
    private String KeyWordArray[];
    public static ArrayList<HashMap<String, String>> BCsmsList;

    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            pref = context.getSharedPreferences("acceptSMS", Context.MODE_PRIVATE);

            BCsmsList = new ArrayList<HashMap<String, String>>();
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
                phNum = smsMessage[i].getOriginatingAddress();
            }

            //Toast.makeText(context,"RRR", Toast.LENGTH_LONG).show();
            str = smsMessage[0].getMessageBody().toString();
            KeyWordArray = Keyword.split(",");

            for(String sms : KeyWordArray){
                if(str.contains(sms)){
                    saveSMSInfo(str, phNum, context);
                    Log.d("SMSTEST","saveInfo 실행");
                }
            }
        }
    }


    private void saveSMSInfo(String str, String phNum, Context context) {
        pref = context.getSharedPreferences("acceptSMS", Activity.MODE_PRIVATE);
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
        editor.putString("SMS", prevStr);
        editor.commit();
    }
}
