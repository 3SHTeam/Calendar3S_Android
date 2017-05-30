package com.bignerdranch.android.calendar3s.Message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.calendar3s.Dialog.DatePickerFragment;
import com.bignerdranch.android.calendar3s.Dialog.TimePickerFragment;
import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.DailyScheduleViewFragment;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleTagListViewAdapterNoCBox;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleTagListViewItemNoCBox;
import com.bignerdranch.android.calendar3s.data.EventData;
import com.bignerdranch.android.calendar3s.data.TagData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * Created by ieem5 on 2017-05-26.
 */

public class AddMessageScheduleFragment extends Fragment implements MainActivity.onKeyBackPressedListener{
    private static final int REQUEST_DATE =0;
    public static final int REQUEST_START_TIME = 1;
    public static final int REQUEST_FINISH_TIME = 2;
    private static final String DIALOG_DATE ="dialogDate";
    private static final String  DIALOG_START_TIME="startTime";
    private static final String  DIALOG_FINISH_TIME="finishTime";
    private SharedPreferences pref;

    private  String info[];
    private  String position[];
    private String bring_message_time[];
    private  String displayName;


    //태그 버튼
    private Button TagBtn;
    private  Button selectTagOkBtn;
    //태그 이름 표시할 리스트 뷰
    private ListView scheduleTagNameListView ;
    private LinearLayout tagListLayout ;

    //태그  리스트뷰 어댑터
    // private ArrayAdapter<String> tagListViewAdapter;//밑의 것 쓰고 위의 것 지워야한다.
    private ScheduleTagListViewAdapterNoCBox scheduleTagListViewAdapter ;


    private  TextView selectedTagTv;
    private ArrayList<TagData>tagDatas;


    //선택된 태그 이름
    private String selecetedTagName;
    //선택된 태그 id
    private String selecetedTagId;

    //스케줄명 editTEXT
    private EditText scheduleNameEt;
    private  EditText locationEt;

    public String getMsgContentsStr() {
        return msgContentsStr;
    }

    public void setMsgContentsStr(String msgContentsStr) {
        this.msgContentsStr = msgContentsStr;
    }

    //문자메시지 내용
    private  String locationStr;
    private  String msgContentsStr;
    private  TextView msgContentTv;
    private  Button addMsgScheduleBtn;
    private  MainActivity main;
    private View v=null;
    private TextView NameView; //주소록 이름
    private TextView MessageView;// 메시지 내용 텍스트뷰
    private  Button dateSelectBtn;
    private  Button selectStartTimeBtn;
    private  Button selectfinishTimeBtn;

    public static TextView DateView;// 날짜
  //  public static TextView timeView;//시간: 시작시간~종료시간

    private TextView startTimeTv;
    private TextView endTimeTv;

    private EditText LocationText;//장소

    private Button DeleteButton;//삭제버튼
    private Button CalenderViewButton;
    private Button TimeViewButton;
    private Button SaveButton;//추가버튼
    private String setStartTime;
    private String setEndTime;
    private String setDate;
    public static String InputTime = "";
    public static String InputDate = "";

    private String EndTime;

    public AddMessageScheduleFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = ((MainActivity)getActivity());
        tagDatas = main.getTagDatas();
           ;
        //info[0]=sms내용
              //  info[1]=주소 ,폰번호,날짜 ,시간 ,포지션
        info = ((MainActivity)getActivity()).getStr().split("@@@@@@@");
      //  Log.d("HASHMAP","출력"+pref.getString("ListSelect",""));
        position = info[1].split("@,@,@,@");//position[1] = 리스트 아이템 포지션 번호
        bring_message_time = position[0].split("  ");//bring_message_time[0]= 폰번호
        //bring_message_time[1]= 날짜     bring_message_time[0]= 시간

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(bring_message_time[0].trim()));   //수신번호를 통해 주소록에 저장된 이름 가져오기
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        displayName = "";
        Cursor cursor = getActivity().getContentResolver().query(uri, projection , null, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                displayName = cursor.getString(0);
            }
            cursor.close();
        }
        if(displayName==null){
            //스케줄 명에 넣기 . 연락처에 없는 번호면 번호를 넣기
            displayName = bring_message_time[0];
        }

       if( bring_message_time[0].equals("Clipboard")){
           displayName = "Clipboard";

        }

        long now = System.currentTimeMillis();  //현재 날짜 구하기
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
        setDate = sdfNow.format(date);
        InputDate = setDate;

//  2017/05//22
         //날짜 패턴이랑 일치안하면 N반환
        if(!Date_Recognition(info[0], bring_message_time[1].trim()).equals("N")){
            InputDate = setDate = Date_Recognition(info[0], setDate);
            String datearry[];
            datearry = setDate.split("/");
            if(Integer.valueOf(datearry[1].trim()).intValue()/10==0 ){
                datearry[1] = "0" + datearry[1].trim();
                InputDate = setDate = datearry[0] + "/" + datearry[1] + "/" + datearry[2];
            }
            if(Integer.valueOf(datearry[2].trim()).intValue()/10==0){
                datearry[2] = "0" + datearry[2].trim();
                InputDate = setDate = datearry[0] + "/" + datearry[1] + "/" + datearry[2];
            }
        }


        sdfNow = new SimpleDateFormat("HH:mm",Locale.KOREA);
        //시작 종료 시간 넣기
        setEndTime = setStartTime = sdfNow.format(date);

        String timearry[];
        if(!Time_Recognition(info[0]).equals("N")){
            setEndTime = setStartTime = Time_Recognition(info[0]);
            timearry = setStartTime.split(":");
            if(Integer.valueOf(timearry[0].trim()).intValue()/10==0 ){
                timearry[0] = "0" + Integer.valueOf(timearry[0].trim()).intValue();
                setStartTime = timearry[0] + ":" + timearry[1];
            }
            if(Integer.valueOf(timearry[1].trim()).intValue()/10==0){
                timearry[1] = "0" + Integer.valueOf(timearry[1].trim()).intValue();
                setStartTime = timearry[0] + ":" + timearry[1];
            }
        }



        EndTime = TimeCalc(setEndTime,1,0);
        if(EndTime.contains("tomorrow@")){
            EndTime = "00:00";
        }
        timearry = EndTime.split(":");
        if(Integer.valueOf(timearry[0].trim()).intValue()/10==0  && !EndTime.equals("00:00")){
            timearry[0] = "0" + Integer.valueOf(timearry[0].trim()).intValue();
            EndTime = timearry[0] + ":" + timearry[1];
        }
        if(Integer.valueOf(timearry[1].trim()).intValue()/10==0  && !EndTime.equals("00:00")){
            timearry[1] = "0" + Integer.valueOf(timearry[1].trim()).intValue();
            EndTime = timearry[0] + ":" + timearry[1];
        }


                           if(!Place_Recognition(info[0]).equals("N")){
                              // LocationText.setText(Place_Recognition(info[0]));
                               locationStr =        Place_Recognition(info[0]);
                           }


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
         v = layoutInflater.inflate(R.layout.fragment_add_msg_schdule,null);




        MessageView = (TextView)v.findViewById(R.id.msgcontents);
        tagListLayout = (LinearLayout)v.findViewById(R.id.tagListViewLayoutInaddMsgSchedule);
        msgContentTv = (TextView)v.findViewById(R.id.msgcontents);
        TagBtn = (Button)v.findViewById(R.id.selectTagBtn);
        selectTagOkBtn= (Button)v.findViewById(R.id.selectTagOkBtnInaddMsgSchedule);
        selectedTagTv= (TextView)v.findViewById(R.id.selectedTagTv);
        scheduleTagNameListView = (ListView)v.findViewById(R.id.tagListViewInaddMsgSchedule);
        DateView = (TextView)v.findViewById(R.id.dateTv);

        scheduleNameEt = (EditText)v.findViewById(R.id.scheduleEditText) ;
        locationEt = (EditText)v.findViewById(R.id.LocationEv) ;
        startTimeTv = (TextView)v.findViewById(R.id.startTimeTv);
        endTimeTv = (TextView)v.findViewById(R.id.finishTimeTv);


        dateSelectBtn = (Button)v.findViewById(R.id.dateBtn);
        selectStartTimeBtn = (Button)v.findViewById(R.id.startTimeBtn);
        selectfinishTimeBtn = (Button)v.findViewById(R.id.finishTimeBtn);

        addMsgScheduleBtn = (Button)v.findViewById(R.id.addMsgScheduleBtn);

        msgContentTv.setText(getMsgContentsStr());
        int len  =  tagDatas.size();
        Log.d("tagtag",tagDatas.toString());
        scheduleTagListViewAdapter =new ScheduleTagListViewAdapterNoCBox();
        //String tagId,String tagTitle, String tagColor,String groupId,boolean b
        for(int i=0;i<len;i++) {
            scheduleTagListViewAdapter.addItem(tagDatas.get(i).getData(0), tagDatas.get(i).getData(1), tagDatas.get(i).getData(2),
                    tagDatas.get(i).getData(5), tagDatas.get(i).isCheckbox());

        }


        scheduleTagListViewAdapter.notifyDataSetChanged();

        scheduleTagNameListView.setAdapter(scheduleTagListViewAdapter);



        return v;
    }
public String ymh(String s){

    String dateOnlyNumber = s.replaceAll("[^0-9]", "");
    String year =  dateOnlyNumber.substring(0,4);
    String month = dateOnlyNumber.substring(4,6);
    String date = dateOnlyNumber.substring(6,8);

    return year+" 년 "+month+" 월 "+date+" 일 ";

}

    public Calendar makeCalToDate(String s){


        Calendar calendar = Calendar.getInstance();
        //  bring_message_time[1]
        String dateOnlyNumber = ymh(s).replaceAll("[^0-9]", "");
        String year =  dateOnlyNumber.substring(0,4);
        String month = dateOnlyNumber.substring(4,6);
        String date = dateOnlyNumber.substring(6,8);

        calendar.set(Calendar.YEAR,Integer.parseInt(year));
        calendar.set(Calendar.MONTH,(Integer.parseInt(month)-1));
        calendar.set(Calendar.DATE,Integer.parseInt(date));
        return calendar;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
   ///   2017/05/27
      ;



        scheduleNameEt.setText(displayName);
        startTimeTv.setText(setStartTime);
        endTimeTv.setText(EndTime);
        if( locationStr!=null)
           locationEt.setText(locationStr);
        //메시지 스케줄 추가 버튼
        addMsgScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               String scheduleStr =  scheduleNameEt.getText().toString();
                String locationStr = locationEt.getText().toString();
                //20170529
               String yyyyMMdd =  DateView.getText().toString().replaceAll("[^0-9]", "");

                String startTimeStr = startTimeTv.getText().toString().replaceAll("[^0-9]", "");
                String endTimeStr = endTimeTv.getText().toString().replaceAll("[^0-9]", "");


                //시작시간   14:03
                String startHour = startTimeTv.getText().toString().substring(0,2);
                String  startMin = startTimeTv.getText().toString().substring(3,5);

                String startTimedB = yyyyMMdd+startHour+startMin;





                Calendar startGoogleCalendar = Calendar.getInstance();
                startGoogleCalendar = main.makeGoogleTimeFormat(yyyyMMdd,startHour,startMin);

                //종료시간
                String endHour = endTimeTv.getText().toString().substring(0,2);
                String endMin = endTimeTv.getText().toString().substring(3,5);
                String endTimedB = yyyyMMdd+endHour+endMin;
                Calendar endGoogleCalendar = Calendar.getInstance();
                endGoogleCalendar =    main.makeGoogleTimeFormat(yyyyMMdd,endHour,
                        endMin);

               long startTimeLong = 0;
                startTimeLong = startGoogleCalendar.getTimeInMillis();
                long endTimeLong = 0;
                endTimeLong =  endGoogleCalendar.getTimeInMillis();


                if(startTimeLong ==0 || endTimeLong == 0 || scheduleStr.equals("") || locationStr.equals("")){
                    Toast.makeText(getActivity(),"다 입력하지 않았습니다. 확인해보세요! "
                            ,Toast.LENGTH_LONG).show();
                    return;
                }


                EventData eventData  = new EventData();
                long calId = main.getCalID();

                eventData = main.addEventToCalendarandGetEventID(calId,startTimeLong,endTimeLong,scheduleStr,locationStr);

               // long startMillis =

            }
        });
        //메시지 스케줄 취소 버튼
        scheduleTagNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                //  selecetedTagName = (String)listView.getItemAtPosition(position);

                ScheduleTagListViewItemNoCBox item =   (ScheduleTagListViewItemNoCBox) listView.getItemAtPosition(position);
                selecetedTagName = item.getTagTitle();
                //사용자가 태그를 선택하지 않았으면 기본 태그로 설정

                for( int i=0;i<tagDatas.size();i++){
                    if(tagDatas.get(i).getData(1).equals(selecetedTagName)){
                        //태그이름과 일치하는 태그 아이디 저장

                        selecetedTagId = tagDatas.get(i).getData(0);
                        //아이디를 MAIN으로 보내야한다!!!
                        break;
                    }
                }//end of for
            }


        });
        TagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagListLayout.setVisibility(View.VISIBLE);
            }
        });

        selectTagOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagListLayout.setVisibility(View.GONE);

                //선택된 리스트뷰이 이름으로 텍스트뷰 세팅하고 아이디 출력해야 한다.
                selectedTagTv.setText(selecetedTagName);
                Toast.makeText(getActivity(),"tagName : "+selecetedTagName+" , "+
                        "tagId : "+selecetedTagId,Toast.LENGTH_SHORT).show();


            }
        });
        DateView.setText(ymh(InputDate));

        //DatePicker 불러오기
        dateSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();
                // date.setTime(getCalendar().getTimeInMillis());
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(AddMessageScheduleFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);


            }
        });



        selectStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


/*
                Calendar startCal = Calendar.getInstance();
                startCal = makeCalToDate(ymh(InputDate));*/
               // Date now =     makeCalToDate(st);
              //  Date date = setTimeToTimePickerDate(getEventData().getData(4));
                Date now  = new Date();
               FragmentManager manager = getFragmentManager();
                TimePickerFragment startTimeDialog = TimePickerFragment.newInstance(now);

               startTimeDialog.setTargetFragment(AddMessageScheduleFragment.this,REQUEST_START_TIME);
                startTimeDialog.show(manager,DIALOG_START_TIME);


            }
        });
        selectfinishTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Date now  = new Date();
              FragmentManager manager = getFragmentManager();
                TimePickerFragment startTimeDialog = TimePickerFragment.newInstance(now);


                startTimeDialog.setTargetFragment(AddMessageScheduleFragment.this,REQUEST_FINISH_TIME);
                startTimeDialog.show(manager,DIALOG_FINISH_TIME);




            }
        });

        DeleteButton = (Button)v.findViewById(R.id.deleteBtn);     //해당 메시지와 수신번호 지우기
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent("android.intent.action.MAIN");
              /*  smsList.remove(Integer.parseInt(position[1].trim()) );
                ListViewSMS.clearChoices();
                adapter.notifyDataSetChanged();*/

                //main에서 함수 불러와야 함.
              /* String SMS = pref.getString("SMS", "");
                Log.d("ssh",SMS);
                SMS = SMS.replace("@@@@@@@"+info[0] +"@,@,@,@"+info[1], "");
                Log.d("ssh","@@@@@@@"+info[0] +"@,@,@,@"+info[1]);
                Log.d("ssh",SMS);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("SMS", SMS);
                editor.commit();*/
                //
                //onBackeyListener
                //getActivity().finish();

                //((MainActivity)getActivity()). deleteSelcetedListItem()
                ((MainActivity)getActivity()).deleteListItem(info[0],position[0],position[1].trim());
               /* smsList.remove(Integer.parseInt(position[1].trim() ));

                adapter.notifyDataSetChanged();
                */
                ((MainActivity)getActivity()). deleteSelcetedListItem(Integer.parseInt(position[1].trim()));

                ((MainActivity)getActivity()).ChangeFragment(R.id.ScheduleMSG);
                //smsList.remove(position );
                //Log.d("ssh","position : "+position);

                //리스트뷰 삭제가 왜 바로 반영이 안되지.....







            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            //DatePicker에서 사용자가 선택한 날짜
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            // updateDate();
            //DatePicker에서 사용자가 선택한 날짜 변환


            SimpleDateFormat format = new SimpleDateFormat("yyyy 년 MM 월 dd 일");
            String getDataString = format.format(date);


            DateView.setText(getDataString);



        }
        if (requestCode == REQUEST_START_TIME) {

            int  hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
            int  minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);

            String hourStr=""+hour;
            String minStr =""+minute;
            if(hour < 10) hourStr = "0"+hour;
            if( minute<10)minStr = "0"+minute;
            startTimeTv.setText(hourStr+":"+minStr);




        }
        if (requestCode == REQUEST_FINISH_TIME) {

            int  hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
            int  minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);

            String hourStr=""+hour;
            String minStr =""+minute;
            if(hour < 10) hourStr = "0"+hour;
            if( minute<10)minStr = "0"+minute;
            endTimeTv.setText(hourStr+":"+minStr);






        }
    }

    public String DateCalc(String date, int MM, int dd){ //MM과 dd를 더한 날짜를 반환
        String datearry[];
        int year, month, day;
        datearry = date.split("/");
        year = Integer.valueOf(datearry[0]).intValue();
        month = Integer.valueOf(datearry[1]).intValue();
        day = Integer.valueOf(datearry[2]).intValue();
        int month_last[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if(year%4 == 0 && year%100 != 0 || year%400 == 0){
            month_last[1] = 29;
        }

        if(day + dd > month_last[month-1]){
            day = day + dd - month_last[month - 1];
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
            while(day> month_last[month-1]) {

                if(year%4 == 0 && year%100 != 0 || year%400 == 0){
                    month_last[1] = 29;
                }
                else
                    month_last[1] = 28;

                day = day - month_last[month - 1];
                month++;

                if (month > 12) {
                    month = 1;
                    year++;
                }
            }
        }
        else{
            day += dd;
        }

        if(month + MM > 12){
            month = month + MM - 12;
            if(month == 2 && day == 29){
                day = 28;
            }
            year++;
        }
        else{
            month += MM;
        }

        return year+"/"+month+"/"+day;
    }

    public String DayofWeekCalk(String date, int week, int dayofweek){ //현 날짜와 몇주 이후 어떤 요일로 계산할 것인지 입력(1(일요일)~7(토요일))
        Calendar cal = Calendar.getInstance();
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        String datearry[];
        int day;
        datearry = date.split("/");
        day = Integer.valueOf(datearry[2]).intValue();

        if(week==0 && nWeek < dayofweek){
            return date;
        }

        return DateCalc(date, 0, week*7 + (dayofweek - nWeek));
    }

    public String DateSet(int Y, int M, int d){
        return Y+"/"+M+"/"+d;
    }

    public String DayofWeekCalk_month(String date, int MM, int n, int dayofweek){   //몇월 몇째주 무슨요일
        String datearry[];
        int year, month, day;
        datearry = date.split("/");
        year = Integer.valueOf(datearry[0]).intValue();
        month = Integer.valueOf(datearry[1]).intValue();
        day = Integer.valueOf(datearry[2]).intValue();
        if(MM>12){
            MM = MM-12;
            year++;
        }
        int month_last[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, MM-1);
        cal.set(Calendar.DATE, 1);
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        int new_day = 9-nWeek + 7*(n-2) + (dayofweek-1); //몇째주 몇요일(둘째주 이상)
        Log.d("ssh", "day: " + new_day);
        if(year%4 == 0 && year%100 != 0 || year%400 == 0){
            month_last[1] = 29;
        }

        if(n != 1) {
            if (month_last[MM - 1] >= new_day){
                return DateSet(year, MM , new_day);
            }
        }
        else if(n==1){
            if(dayofweek>=nWeek){
                return DateSet(year, MM, 1 + dayofweek - nWeek);
            }
        }

        return DateSet(year, MM, 1);
    }

    public String TimeCalc(String Time, int h, int m){
        String timearry[];
        int hour, minute;
        boolean tomorrow = false;
        timearry = Time.split(":");
        hour = Integer.valueOf(timearry[0]).intValue();
        minute = Integer.valueOf(timearry[1]).intValue();

        if(minute + m >= 60){
            minute = minute + m -60;
            hour++;
            if(hour >= 24){
                tomorrow = true;
            }
        }
        else
            minute += m;

        if(hour + h >= 24){
            hour = hour + h - 24;
            tomorrow = true;
        }
        else
            hour += h;

        if(tomorrow == false){
            return hour+":"+minute;
        }
        else
            return "tomorrow@"+hour+":"+minute;
    }


    public String Date_Recognition(String message, String date) {
        int dayofweek = 0;
        String datearry[];
        int year, month, day;
        datearry = date.split("/");
        year = Integer.valueOf(datearry[0]).intValue();
        month = Integer.valueOf(datearry[1]).intValue();
        day = Integer.valueOf(datearry[2]).intValue();
        Pattern DateP1 = Pattern.compile("(0[1-9]|1[0-2]|[1-9])월( |)(0[0-9]|[1-9]|[1-2][0-9]|3[0-1])일");
        Pattern DateP1_1 = Pattern.compile("([1-9])월( |)(0[0-9]|[1-9]|[1-2][0-9]|3[0-1])일");
        Pattern DateP2 = Pattern.compile("(0[1-9]|1[0-2]|[1-9])\\/(0[0-9]|[1-9]|[1-2][0-9]|3[0-1])[^\\/\\d]");
        Pattern DateP2_1 = Pattern.compile("([1-9])\\/(0[0-9]|[1-9]|[1-2][0-9]|3[0-1])[^\\/\\d]");
        Pattern DateP3 = Pattern.compile("(0[1-9]|1[0-2]|[1-9])-(0[0-9]|[1-9]|[1-2][0-9]|3[0-1])[^-]");
        Pattern DateP3_1 = Pattern.compile("([1-9])-(0[0-9]|[1-9]|[1-2][0-9]|3[0-1])[^-]");
        Pattern DateP4 = Pattern.compile("(0[1-9]|1[0-2]|[1-9])\\.(0[0-9]|[1-9]|[1-2][0-9]|3[0-1])[^\\.\\d]");
        Pattern DateP4_1 = Pattern.compile("([1-9])\\.(0[0-9]|[1-9]|[1-2][0-9]|3[0-1])[^\\.\\d]");

        Pattern DateP5 = Pattern.compile("(0[1-9]|[1-2][0-9]|3[0-1]|[1-9])일( |)[뒤후]");
        Pattern DateP5_1 = Pattern.compile("([1-9])일( |)[뒤후]");
        Pattern DateP6 = Pattern.compile("(0[1-9]|[1-2][0-9]|3[0-1]|[1-9])일");
        Pattern DateP6_1 = Pattern.compile("([1-9])일");

        Pattern DateP7 = Pattern.compile("(0[1-9]|1[0-2]|[1-9])(월|월달)( |)(첫|둘|셋|넷|다섯)(째주|째)( |)(월|화|수|목|금|토|일)");
        Pattern DateP7_1 = Pattern.compile("([1-9])(월|월달)( |)(첫|둘|셋|넷|다섯)(째주|째)( |)(월|화|수|목|금|토|일)");
        Pattern DateP8 = Pattern.compile("(|이번달|다음달|다다음달)( |)(첫|둘|셋|넷|다섯)(째주|째)( |)(월|화|수|목|금|토|일)");

        Pattern DateP9 = Pattern.compile("(|이번주|다음주|다다음주|담주|다담주)( |)(월|화|수|목|금|토|[^내]일)");

        Pattern Date10 = Pattern.compile("(0[1-9]|1[0-2]|[1-9])개월( |)[뒤후]");
        Pattern Date10_1 = Pattern.compile("([1-9])개월( |)[뒤후]");
        Pattern Date11 = Pattern.compile("([1-4])주( |)[뒤후]");

        Matcher matcher = DateP1.matcher(message);
        if(matcher.find()){
            return year+"/"+matcher.group(1)+"/"+matcher.group(3);
        }
        matcher = DateP1_1.matcher(message);
        if(matcher.find()){
            return year+"/"+matcher.group(1)+"/"+matcher.group(3);
        }
        matcher = DateP2.matcher(message);
        if(matcher.find()){
            return year+"/"+matcher.group(1)+"/"+matcher.group(2);
        }
        matcher = DateP2_1.matcher(message);
        if(matcher.find()){
            return year+"/"+matcher.group(1)+"/"+matcher.group(2);
        }
        matcher = DateP3.matcher(message);
        if(matcher.find()){
            return year+"/"+matcher.group(1)+"/"+matcher.group(2);
        }
        matcher = DateP3_1.matcher(message);
        if(matcher.find()){
            return year+"/"+matcher.group(1)+"/"+matcher.group(2);
        }
        matcher = DateP4.matcher(message);
        if(matcher.find()){
            return year+"/"+matcher.group(1)+"/"+matcher.group(2);
        }
        matcher = DateP4_1.matcher(message);
        if(matcher.find()){
            return year+"/"+matcher.group(1)+"/"+matcher.group(2);
        }
        matcher = DateP5.matcher(message);
        if(matcher.find()){
            return DateCalc(date,0,Integer.valueOf(matcher.group(1).trim()).intValue());
        }
        matcher = DateP5_1.matcher(message);
        if(matcher.find()){
            return DateCalc(date,0,Integer.valueOf(matcher.group(1).trim()).intValue());
        }
        matcher = DateP6.matcher(message);
        if(matcher.find()){
            return year+"/"+month+"/"+matcher.group(1);
        }
        matcher = DateP6_1.matcher(message);
        if(matcher.find()){
            return year+"/"+month+"/"+matcher.group(1);
        }

        matcher = DateP7.matcher(message);
        if(matcher.find()) {
            dayofweek = DayofWeek_int(matcher.group(7));
            if (matcher.group(4).contains("첫")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 1, dayofweek);
            } else if (matcher.group(4).contains("둘")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 2, dayofweek);
            } else if (matcher.group(4).contains("셋")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 3, dayofweek);
            } else if (matcher.group(4).contains("넷")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 4, dayofweek);
            } else if (matcher.group(4).contains("다섯")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 5, dayofweek);
            }
        }

        matcher = DateP7_1.matcher(message);
        if(matcher.find()) {
            dayofweek = DayofWeek_int(matcher.group(7));
            if (matcher.group(4).contains("첫")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 1, dayofweek);
            } else if (matcher.group(4).contains("둘")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 2, dayofweek);
            } else if (matcher.group(4).contains("셋")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 3, dayofweek);
            } else if (matcher.group(4).contains("넷")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 4, dayofweek);
            } else if (matcher.group(4).contains("다섯")) {
                return DayofWeekCalk_month(date, Integer.valueOf(matcher.group(1).trim()).intValue(), 5, dayofweek);
            }
        }

        matcher = DateP8.matcher(message);
        if(matcher.find()){
            dayofweek = DayofWeek_int(matcher.group(6));
            if(matcher.group(1).equals("다음달")){
                if (matcher.group(3).contains("첫")) {
                    return DayofWeekCalk_month(date,month+1, 1, dayofweek);
                } else if (matcher.group(3).contains("둘")) {
                    return DayofWeekCalk_month(date,month+1, 2, dayofweek);
                } else if (matcher.group(3).contains("셋")) {
                    return DayofWeekCalk_month(date,month+1, 3, dayofweek);
                } else if (matcher.group(3).contains("넷")) {
                    return DayofWeekCalk_month(date,month+1, 4, dayofweek);
                } else if (matcher.group(3).contains("다섯")) {
                    return DayofWeekCalk_month(date,month+1, 5, dayofweek);
                }
            }
            else if(matcher.group(1).equals("다다음달")) {
                if (matcher.group(3).contains("첫")) {
                    return DayofWeekCalk_month(date, month + 2, 1, dayofweek);
                } else if (matcher.group(3).contains("둘")) {
                    return DayofWeekCalk_month(date, month + 2, 2, dayofweek);
                } else if (matcher.group(3).contains("셋")) {
                    return DayofWeekCalk_month(date, month + 2, 3, dayofweek);
                } else if (matcher.group(3).contains("넷")) {
                    return DayofWeekCalk_month(date, month + 2, 4, dayofweek);
                } else if (matcher.group(3).contains("다섯")) {
                    return DayofWeekCalk_month(date, month + 2, 5, dayofweek);
                }
            }
            else if(matcher.group(1).equals("") || matcher.group(1).equals("이번달")){
                if (matcher.group(3).contains("첫")) {
                    return DayofWeekCalk_month(date,month, 1, dayofweek);
                } else if (matcher.group(3).contains("둘")) {
                    return DayofWeekCalk_month(date,month, 2, dayofweek);
                } else if (matcher.group(3).contains("셋")) {
                    return DayofWeekCalk_month(date,month, 3, dayofweek);
                } else if (matcher.group(3).contains("넷")) {
                    return DayofWeekCalk_month(date,month, 4, dayofweek);
                } else if (matcher.group(3).contains("다섯")) {
                    return DayofWeekCalk_month(date,month, 5, dayofweek);
                }
            }
        }

        matcher = DateP9.matcher(message);
        if(matcher.find()){
            dayofweek = DayofWeek_int(matcher.group(3));
            if(matcher.group(1).equals("다음주") || matcher.group(1).equals("담주")){
                return DayofWeekCalk(date, 1, dayofweek);
            }
            else if(matcher.group(1).equals("다다음주") || matcher.group(1).equals("다담주")){
                return DayofWeekCalk(date, 2, dayofweek);
            }
            else if(matcher.group(1).equals("") || matcher.group(1).equals("이번주")){
                return DayofWeekCalk(date, 0, dayofweek);
            }
        }

        if(message.contains("모레") || message.contains("이튿") || message.contains("이틀 뒤") ||
                message.contains("이틀 후")){
            return DateCalc(date, 0, 2);
        }
        else if(message.contains("내일") || message.contains("낼") || message.contains("다음날") ||
                message.contains("담날")){
            return DateCalc(date, 0, 1);
        }
        else if(message.contains("글피") || message.contains("사흗") ||message.contains("사흘 뒤") ||
                message.contains("사흘 후")){
            return DateCalc(date, 0, 3);
        }
        else if(message.contains("나흗") ||message.contains("나흘 뒤") || message.contains("나흘 후")){
            return DateCalc(date, 0, 4);
        }
        else if(message.contains("닷샛") || message.contains("닷새 뒤") || message.contains("닷새 후")){
            return DateCalc(date, 0, 5);
        }
        else if(message.contains("엿샛") || message.contains("엿새 뒤") || message.contains("엿새 후")){
            return DateCalc(date, 0, 6);
        }

        matcher = Date10.matcher(message);
        if(matcher.find()){
            return DateCalc(date,Integer.valueOf(matcher.group(1).trim()).intValue(),0);
        }
        matcher = Date10_1.matcher(message);
        if(matcher.find()){
            return DateCalc(date,Integer.valueOf(matcher.group(1).trim()).intValue(),0);
        }

        matcher = Date11.matcher(message);
        if(matcher.find()){
            return DateCalc(date,0,Integer.valueOf(matcher.group(1).trim())*7);
        }

        return "N";

    }

    public int DayofWeek_int(String dayofweek){
        int index = 0;
        switch (dayofweek) {
            case "일":
                index = 1;
                break;
            case "월":
                index = 2;
                break;
            case "화":
                index = 3;
                break;
            case "수":
                index = 4;
                break;
            case "목":
                index = 5;
                break;
            case "금":
                index = 6;
                break;
            case "토":
                index = 7;
                break;
        }

        return index;
    }

    public String Time_Recognition(String message){
        int hour;
        Pattern TimeP1 = Pattern.compile("([A-a][M-m]|오전|아침|[P-p][M-m]|오후|낮|저녁)( |)(0[0-9]|1[0-1]|[0-9])[시:]");
        Pattern TimeP1_1 = Pattern.compile("([A-a][M-m]|오전|아침)( |)12[시:]");
        Pattern TimeP2 = Pattern.compile("(0[0-9]|1[0-9]|2[0-3]|[1-9])[시:]");
        Pattern TimeP2_1 = Pattern.compile("([1-9])[시:]");

        Matcher matcher = TimeP1.matcher(message);
        if(matcher.find()){
            if(matcher.group(1).equals("오전") || matcher.group(1).equals("아침") || matcher.group(1).equals("am") ||
                    matcher.group(1).equals("Am") || matcher.group(1).equals("AM")){
                hour = Integer.valueOf(matcher.group(3).trim()).intValue();
                return hour+":"+"00";
            }
            else if(matcher.group(1).equals("pm") || matcher.group(1).equals("Pm") || matcher.group(1).equals("PM") ||
                    matcher.group(1).equals("오후") || matcher.group(1).equals("낮") || matcher.group(1).equals("저녁")){
                hour = Integer.valueOf(matcher.group(3).trim()).intValue() + 12;
                return hour + ":" + "00";
            }
        }
        matcher = TimeP1_1.matcher(message);
        if(matcher.find()){
            return "12:00";
        }

        matcher = TimeP2.matcher(message);
        if(matcher.find()){
            hour = Integer.valueOf(matcher.group(1).trim()).intValue();
            return hour + ":" + "00";
        }

        matcher = TimeP2_1.matcher(message);
        if(matcher.find()){
            hour = Integer.valueOf(matcher.group(1).trim()).intValue();
            return hour + ":" + "00";
        }

        if(message.contains("아침")){
            return "07:00";
        }
        if(message.contains("정오") || message.contains("낮")){
            return "12:00";
        }
        if(message.contains("저녁")){
            return "19:00";
        }

        return "N";
    }

    public String Place_Recognition(String message){
        Pattern PlaceP1 = Pattern.compile("(\\S*)( |)(근처|인근|주변|가까이)");
        Pattern PlaceP2 = Pattern.compile("(\\S*\\S)(에서|[^에^\\s]서|로)");

        Matcher matcher = PlaceP1.matcher(message);
        if(matcher.find()){
            return matcher.group(1);
        }
        matcher = PlaceP2.matcher(message);
        if(matcher.find()){
            return matcher.group(1);
        }

        return "N";
    }


    //메시지 리스트를 실행시킨다.
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.ChangeFragment(R.id.ScheduleMSG);
    }

    //자신의 백키를 불러오게 등록
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
    }

}
