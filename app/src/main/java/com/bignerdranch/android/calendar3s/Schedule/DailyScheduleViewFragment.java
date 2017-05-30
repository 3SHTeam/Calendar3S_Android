package com.bignerdranch.android.calendar3s.Schedule;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.bignerdranch.android.calendar3s.data.EventData;
import com.bignerdranch.android.calendar3s.data.TagData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ieem5 on 2017-05-12.
 */

public class DailyScheduleViewFragment extends Fragment {
    private static final int REQUEST_DATE =0;
    public static final int REQUEST_START_TIME = 1;
    public static final int REQUEST_FINISH_TIME = 2;
    private static final String DIALOG_DATE ="dialogDate";
    private static final String  DIALOG_START_TIME="startTime";
    private static final String  DIALOG_FINISH_TIME="finishTime";

    private  MainActivity main;

    //ClickListFragment 로부터 선택한 하루 이벤트 데이터 받는다.
    private EventData eventData;



    //ClickListFragment calendar 객체 전달 받는다.
    private Calendar calendar;

    //MainActivity의 tagDatas 받아올 객체
    private ArrayList<TagData>tagDatas;

    //태그 이름 표시할 리스트 뷰
    private ListView scheduleTagNameListView ;
    private LinearLayout tagListLayout ;

    //태그  리스트뷰 어댑터
   // private ArrayAdapter<String> tagListViewAdapter;//밑의 것 쓰고 위의 것 지워야한다.
    private  ScheduleTagListViewAdapterNoCBox scheduleTagListViewAdapter ;

  //  AddScheduleDialogFragment d = new AddScheduleDialogFragment();
   // scheduleTagListViewAdapter =  d.ScheduleTagListViewAdapter();


    //선택된 태그 이름
    private String selecetedTagName;
    //선택된 태그 id
    private String selecetedTagId;

    private Button selectTagBtn ;
    private Button selectTagOKBtn ;

    private EditText ScheduleEditText;//스케줄명 입력

    private Button startTimeBtn;//시작 시간
    private Button finishTimeBtn;// 종료 시간
    private Button showMapBtn;//지도보기 버튼
    private EditText LocationEv;

    private Calendar selectedCal;//선택된 날짜 객체
    private  String selectedDate; // '2017년 1월 17일 일정'
    private TextView addScheduleDateTv;
    private TextView selectedTagTv;//태그 이름 텍스트뷰
    private Button DateBtn;//날짜 선택 버튼
    private TextView DateTv;

    private TextView startTimeTV;
    private  TextView finishTimeTv;
    private  Button modifyBtn;
    private  Button DeleteBtn;
   //db용 변수
    private String yyyyMMdd;
    private String startHHmm;
    private String endHHmm;

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_daily_schedule,null);
        addScheduleDateTv = (TextView)v.findViewById(R.id.addScheduleDate);

       Log.i("DELETE","eventId : "+getEventData().getData(0));
        ScheduleEditText = (EditText)v.findViewById(R.id.scheduleEditText);
        //selectedTagTv = (TextView)v.findViewById(R.id.selectedTagTv);

        main = ((MainActivity)getActivity());

        //태그 관련 뷰
        selectTagBtn = (Button)v.findViewById(R.id.selectTagBtn);
        selectTagOKBtn = (Button)v.findViewById(R.id.selectTagOkBtn);
        scheduleTagNameListView = (ListView)v.findViewById(R.id.tagListView);
        tagListLayout = (LinearLayout)v.findViewById(R.id.tagListViewLayout);
        selectedTagTv = (TextView)v.findViewById(R.id.selectedTagTv);
        DateBtn= (Button)v.findViewById(R.id.dateBtn);
        DateTv=(TextView)v.findViewById(R.id.dateTv);
        startTimeBtn= (Button)v.findViewById(R.id.startTimeBtn);
        finishTimeBtn =(Button)v.findViewById(R.id.finishTimeBtn);
        showMapBtn =(Button)v.findViewById(R.id.showMapBtn);
        LocationEv = (EditText)v.findViewById(R.id.LocationEv);

        startTimeTV=(TextView) v.findViewById(R.id.startTimeTv);
        finishTimeTv=(TextView) v.findViewById(R.id.finishTimeTv);
        modifyBtn = (Button)v.findViewById(R.id.modifyBtn);
           DeleteBtn = (Button)v.findViewById(R.id.deleteBtn);



        //DatePicker 불러오기
        DateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Date date = new Date();
                date.setTime(getCalendar().getTimeInMillis());
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(DailyScheduleViewFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);


            }
        });

        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Date date = setTimeToTimePickerDate(getEventData().getData(4));
                FragmentManager manager = getFragmentManager();
                TimePickerFragment startTimeDialog = TimePickerFragment.newInstance(date);

                startTimeDialog.setTargetFragment(DailyScheduleViewFragment.this,REQUEST_START_TIME);
                startTimeDialog.show(manager,DIALOG_START_TIME);


            }
        });
        finishTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Date date = setTimeToTimePickerDate(getEventData().getData(5));
                FragmentManager manager = getFragmentManager();
                TimePickerFragment startTimeDialog = TimePickerFragment.newInstance(date);

                startTimeDialog.setTargetFragment(DailyScheduleViewFragment.this,REQUEST_FINISH_TIME);
                startTimeDialog.show(manager,DIALOG_FINISH_TIME);




            }
        });
///




        ///

        tagDatas = new ArrayList<>();
        tagDatas = ((MainActivity)getActivity()).getTagDatas();




      int len  =  tagDatas.size();

        scheduleTagListViewAdapter =new ScheduleTagListViewAdapterNoCBox();
        //String tagId,String tagTitle, String tagColor,String groupId,boolean b
        for(int i=0;i<len;i++) {
            scheduleTagListViewAdapter.addItem(tagDatas.get(i).getData(0), tagDatas.get(i).getData(1), tagDatas.get(i).getData(2),
                    tagDatas.get(i).getData(5), tagDatas.get(i).isCheckbox());
        }

        scheduleTagListViewAdapter.notifyDataSetChanged();

        scheduleTagNameListView.setAdapter(scheduleTagListViewAdapter);


//getEventData().getData(6)   --태그아이디 이것과 일치하는 태그명으로 텍스트뷰 세팅해야 함
        String tagId = getEventData().getData(6);
        String tagName = "";
        for(int i=0;i<tagDatas.size();i++){
            if(tagId.equals(tagDatas.get(i).getData(0))){
                tagName = tagDatas.get(i).getData(1);
                break;
            }
        }
        selectedTagTv.setText(tagName);
        ArrayList<String>tagList = new ArrayList<>();
    //int len =  tagDatas.size();
        for(int i=0;i<len;i++){
            tagList.add(tagDatas.get(i).getData(1));
            Log.i("DAILY","tagId : "+tagDatas.get(i).getData(0)+"tagName : "+tagDatas.get(i).getData(1));
        }
       // tagListViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,tagList);
      //  scheduleTagNameListView.setAdapter(tagListViewAdapter);
       // scheduleTagNameListView.setAdapter(scheduleTagListViewAdapter);
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
        //태그 버튼 누르면 태그 리스트뷰 보이게
        selectTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagListLayout.setVisibility(View.VISIBLE);
            }
        });

        //태그 선택하고 확인 누르면 리스트뷰 다시 안 보이게
        selectTagOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tagListLayout.setVisibility(View.GONE);

                //선택된 리스트뷰이 이름으로 텍스트뷰 세팅하고 아이디 출력해야 한다.
               selectedTagTv.setText(selecetedTagName);
                Toast.makeText(getActivity(),"tagName : "+selecetedTagName+" , "+
                        "tagId : "+selecetedTagId,Toast.LENGTH_SHORT).show();


            }


        });

        //수정 버튼 클릭  빈칸있을 때 처리하기!!!
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이벤트 아이디
                String eventId = getEventData().getData(0);
                //태그 아이디
                String tagId = selecetedTagId;
                //날짜,시간  DB:yyyyMMddMMmm 구글은 long 형식으로  calendat.getTime~~
                //시작시간
                String startHour = startTimeTV.getText().toString().substring(0,2);
                String  startMin = startTimeTV.getText().toString().substring(5,7);
                yyyyMMdd =  DateTv.getText().toString().replaceAll("[^0-9]", "");
                String startTimedB = yyyyMMdd+startHour+startMin;
                //이벤트 제목
                String eventTitle = ScheduleEditText.getText().toString();




                Calendar startGoogleCalendar = Calendar.getInstance();
                startGoogleCalendar = main.makeGoogleTimeFormat(yyyyMMdd,startHour,startMin);

                //종료시간
                String endHour = finishTimeTv.getText().toString().substring(0,2);
                  String endMin = finishTimeTv.getText().toString().substring(5,7);
                 String endTimedB = yyyyMMdd+endHour+endMin;
                Calendar endGoogleCalendar = Calendar.getInstance();
                endGoogleCalendar = main. makeGoogleTimeFormat(yyyyMMdd,endHour,endMin);


                //장소
                String location = LocationEv.getText().toString();

                //수정 버튼 눌렀을 때 변수 확인용
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy년MM월dd일HH시mm분");
                String startTimeGoogle = sdf.format(startGoogleCalendar.getTimeInMillis());
                String endTimeGoogle =  sdf.format(endGoogleCalendar.getTimeInMillis());
                String googleSid = getEventData().getData(7);

                System.out.println("수정 후 eventId : "+eventId+" tagId : "+tagId+" startTimedB : "+startTimedB+
                "startTimeGoogle : "+startTimeGoogle+"endTimeDB : "+endTimedB+" endTimeGoogle : "+endTimeGoogle+" location : "+
                        location);

                UpdateSchedule(eventId, eventTitle, tagId, startTimedB, endTimedB, location, startGoogleCalendar.getTimeInMillis(),
                        endGoogleCalendar.getTimeInMillis(), googleSid);


                main.ChangeFragment(R.id.calendars);


            }
        });

        //삭제 버튼 클릭
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DeleteSchedule","start");
                DeleteSchedule();
                Log.d("DeleteSchedule","end");
                main.ChangeFragment(R.id.calendars);
            }
        });

        //getEventData로 스케줄명,시작 종료 시간 위치 세팅해놓기
        ScheduleEditText.setText(getEventData().getData(2));


       /* String AM_PM="";
       String hour =  getEventData().getData(4).substring(8,9);
        String min = getEventData().getData(4).substring(10,11);
        if( Integer.parseInt(hour)<12) AM_PM = "AM";
        else  AM_PM = "PM";*/
       String year = getEventData().getData(4).substring(0,4);
        String month= getEventData().getData(4).substring(4,6);
        String date= getEventData().getData(4).substring(6,8);
        String ymd = year+" 년 "+month+" 월 "
                +date+" 일" ;
      //  yyyyMMdd = year+month+date; //yyyyMMdd
        DateTv.setText(ymd);
        startTimeTV.setText(setTime(getEventData().getData(4)));
        finishTimeTv.setText(setTime(getEventData().getData(5)));
      //  finishTimeTv.setText(getEventData().getData(5));
        LocationEv.setText(getEventData().getData(3));

        return  v;
    }

    private void UpdateSchedule(String eventId, String eventTitle, String tagId, String startTimedB,
                                String endTimedB, String location, long startTimeGoogle, long endTimeGoogle, String googleSid) {
        String Gid = getEventData().getData(8);
        if(Gid.equals("")) {
            main.UpdateSchedule(eventId, eventTitle, startTimedB, endTimedB, location);
            main.UpdateScheduleJoin(eventId,tagId,main.getUser().getData(0));
            //google 수정

             main.updateGoogleCalendar(googleSid,eventTitle,location,startTimeGoogle,endTimeGoogle);
            //updateGoogleCalendar(googleSid,eventTitle,location,startTimeGoogle,endTimeGoogle);

        }
        main.freshMySchedule();
    }

/*    private  void updateGoogleCalendar(String googleSid,String eventTitle,String location,long startTimeGoogle,
                                       long endTimeGoogle){

        Uri updateUri = null;
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE,eventTitle);
        values.put(CalendarContract.Events.EVENT_LOCATION,location);
        values.put(CalendarContract.Events.DTSTART,startTimeGoogle);
        values.put(CalendarContract.Events.DTEND,endTimeGoogle);
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,Long.parseLong(googleSid));
        int rows = getActivity().getContentResolver().update(updateUri,values,null,null);
        Log.i("tata"," tata Rows updated : "+rows);
        Log.i("tata","수정 후  "+"eventTitle : "+eventTitle+ "startTimeGoogle : "+startTimeGoogle+ " endTimeGoogle : "+
                endTimeGoogle+" location : "+ location);



    }*/
    private void DeleteSchedule() {
        String sid = getEventData().getData(0);
        String GoogleSid = getEventData().getData(7);
        String Gid = getEventData().getData(8);

        Log.d("DeleteSchedule","sid="+sid);

        //Sid로 스케줄 참여를 해제.
        main.DeleteScheduleJoin(sid,main.getUser().getData(0));
        //개인 스케줄일 경우에는 스케줄도 삭제  그룹 스케줄일 경우에는 아무것도 안한다.
        if(Gid.equals("")){
            main.DeleteMySchedule(sid);
        }
        //구글캘린더에서 스케줄을 삭제 GoogleSid사용

          main.deleteGoogleCalendar(GoogleSid);
        //deleteGoogleCalendar(GoogleSid);


        main.freshMySchedule();
    }
   /* private  void deleteGoogleCalendar(String googleSid){
        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(googleSid));
        Log.i("DELETE", "GoogleSid " + Long.parseLong(googleSid));

        int rows = getActivity().getContentResolver().delete(deleteUri, null, null);
        Log.i("DELETE", "Rows deleted: " + rows);

    }*/


    public Date setTimeToTimePickerDate(String eventData){

        Date date = new Date();
        int hour = Integer.parseInt(eventData.substring(8,10));
        int min = Integer.parseInt(eventData.substring(10,12));

        Calendar calendar;
        calendar =getCalendar();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        date.setTime(calendar.getTimeInMillis());

        return date;
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


            DateTv.setText(getDataString);



        }
        if (requestCode == REQUEST_START_TIME) {

            int  hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
            int  minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);

            String hourStr=""+hour;
            String minStr =""+minute;
           if(hour < 10) hourStr = "0"+hour;
            if( minute<10)minStr = "0"+minute;
            startTimeTV.setText(hourStr+" : "+minStr);




        }
        if (requestCode == REQUEST_FINISH_TIME) {

            int  hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
            int  minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);

            String hourStr=""+hour;
            String minStr =""+minute;
            if(hour < 10) hourStr = "0"+hour;
            if( minute<10)minStr = "0"+minute;
            finishTimeTv.setText(hourStr+" : "+minStr);






        }
    }

    public String setTime(String eventTime){

        String hour = eventTime.substring(8,10);
        String min = eventTime.substring(10,12);



        return hour+" : "+min;

    }
}
