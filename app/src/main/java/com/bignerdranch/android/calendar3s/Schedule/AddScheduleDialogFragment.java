package com.bignerdranch.android.calendar3s.Schedule;


import android.Manifest;

import com.bignerdranch.android.calendar3s.BusProvider;

import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.calendar3s.Dialog.TimePickerFragment;
import com.bignerdranch.android.calendar3s.data.CalData;
import com.bignerdranch.android.calendar3s.data.EventData;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.bignerdranch.android.calendar3s.data.TagData;
import com.bignerdranch.android.calendar3s.database.SendToDB;

/**
 * Created by ieem5 on 2017-02-22.
 */

//test 0323
public class AddScheduleDialogFragment extends Fragment implements MainActivity.onKeyBackPressedListener {

 //CalendarProvider calendarProvider;

    String gmail="";


    long CalID;
/*
String gmail="";


long CalID;
    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };
*/

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private  static final  int REQUEST_READ_CALENDAR_PERMISSION = 4;
    private  static final  int REQUEST_WRITE_CALENDAR_PERMISSION = 5;
    private  static final  int REQUEST_READ_EXTERNAL_STORAGE =6;
    private  static final  int REQUEST_SELECT_TAG = 7;


    public static final String DIALOG_START_TIME = "DialogStartTime";
    public static final String DIALOG_FINISH_TIME = "DialogFinishTime";
    public static final int REQUEST_START_TIME = 6;
    public static final int REQUEST_FINISH_TIME = 7;
    public static final int REQUEST_MAP_DIALOG_FRAGMENT = 8;
    public static final String EXTRA_EVENTDATA = "com.bignerdranch.android.calendar3s.Schedule.addScheduleFragment.eventData";
    public static final String DIALOG_SELECT_TAG ="DialogSelectTag";
    AlertDialog.Builder  builder;

    private EditText ScheduleEditText;//스제줄명 입력

    private Button startTimeBtn;//시작 시간
    private Button finishTimeBtn;// 종료 시간
    private Button showMapBtn;//지도보기 버튼
    private EditText LocationEv;


    private Calendar selectedCal;//선택된 날짜 객체
    private  String selectedDate; // '2017년 1월 17일 일정'
    private  TextView addScheduleDateTv;
    private TextView startTimeTV;
    private  TextView finishTimeTv;
    private  Button addScheduleBtn;




    private SendToDB sendToDB;
    private  Button selectTagBtn;
    private  Button selectTagOKBtn;
    private ListView scheduleTagNameListView;
    private  LinearLayout tagListLayout;
    private TextView selectedTagTv;



    private  ArrayList<TagData>tagDatas;
    //태그  리스트뷰 어댑터

    private ScheduleTagListViewAdapterNoCBox scheduleTagListViewAdapter;




    //선택된 태그 이름
    private String selecetedTagName;
    //선택된 태그 id
    private String selectedTagId;


    //2017년 1월 17일 일정'   --> 선택한 날짜 받아와야 함.

    Date date;

   // long changedStartTime;
    //long changedFinishTime;
    int alarmTime;
    int hour ;
    int minute;
    String AM_PM ;
    String dateClean;
    String year;
    String month ;
    String day ;

    long startMillis = 0;
    long endMillis = 0;
    long calID=0;
    String eventId;
    String getEventId;
    //HashMap<Long,String>eventIdandTitleHMap;
    String schedeulEditTextStr;
    String eventLocation;
    String attendeeEmail;
    Calendar endTime  = Calendar.getInstance();
    Calendar beginTime = Calendar.getInstance();
    EventData eventDataToMain;
    CalData inputCalData;


    private MainActivity main;




    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Calendar getSelectedCal() {
        return selectedCal;
    }

    public void setSelectedCal(Calendar selectedCal) {
        this.selectedCal = selectedCal;
    }
    public ArrayList<TagData> getTagDatas() {
        return tagDatas;
    }

    public void setTagDatas(ArrayList<TagData> tagDatas) {
        this.tagDatas = tagDatas;
    }

    //메인 액티비티로 데이터를 전달할 커스텀 리스너 연결
   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passEventDataToMainListener = (PassEventDataToMainListener)context;
    }*/

   /*public interface PassEventDataToMainListener{
       public void eventDataReceived(String eventId,String eventTitle,String eventStartTime,
                                     String eventEndTime,String location,String tagId);
   }*/

  // private PassEventDataToMainListener passEventDataToMainListener;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        main = ((MainActivity)getActivity());

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_add_schedule,null);
        addScheduleDateTv = (TextView)v.findViewById(R.id.addScheduleDate);


        ScheduleEditText = (EditText)v.findViewById(R.id.scheduleEditText);

        startTimeBtn= (Button)v.findViewById(R.id.startTimeBtn);
        finishTimeBtn =(Button)v.findViewById(R.id.finishTimeBtn);
        showMapBtn =(Button)v.findViewById(R.id.showMapBtn);
        LocationEv = (EditText)v.findViewById(R.id.LocationEv);

        startTimeTV=(TextView) v.findViewById(R.id.startTimeTv);
        finishTimeTv=(TextView) v.findViewById(R.id.finishTimeTv);
        addScheduleBtn = (Button)v.findViewById(R.id.addBtn);



        selectTagBtn = (Button)v.findViewById(R.id.selectTagBtn);
        selectTagOKBtn = (Button)v.findViewById(R.id.selectTagOkBtn);
        scheduleTagNameListView = (ListView)v.findViewById(R.id.tagListView);
        tagListLayout = (LinearLayout)v.findViewById(R.id.tagListViewLayout);
        selectedTagTv = (TextView)v.findViewById(R.id.selectedTagTv);




        selectedDate = selectedCal.get(Calendar.YEAR)+"년"+(selectedCal.get(Calendar.MONTH)+1)+"월"+
                selectedCal.get(Calendar.DATE)+"일";
        addScheduleDateTv.setText(selectedDate);
        gmail = main.getAccount();
        // Run query
        //calendar id 구하기
        calID = main.getCalID();
        Log.i("ottoL","FROM MAIN gmail: "+gmail+"calID : "+calID);






        tagDatas=((MainActivity)getActivity()).getTagDatas();

        int len = tagDatas.size();
        //태그 이름 리스트
        ArrayList<String>tagList = new ArrayList<>();

        for(int i=0;i<len;i++){
            tagList.add(tagDatas.get(i).getData(1));
            Log.i("AAA","tagId : "+tagDatas.get(i).getData(0)+"tagName : "+tagDatas.get(i).getData(1));
        }




        scheduleTagListViewAdapter =new ScheduleTagListViewAdapterNoCBox();
        //String tagId,String tagTitle, String tagColor,String groupId,boolean b
        for(int i=0;i<len;i++) {
            scheduleTagListViewAdapter.addItem(tagDatas.get(i).getData(0), tagDatas.get(i).getData(1), tagDatas.get(i).getData(2),
                    tagDatas.get(i).getData(5), tagDatas.get(i).isCheckbox());
        }

        scheduleTagListViewAdapter.notifyDataSetChanged();

        scheduleTagNameListView.setAdapter(scheduleTagListViewAdapter);


        scheduleTagNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;


                ScheduleTagListViewItemNoCBox item =   (ScheduleTagListViewItemNoCBox) listView.getItemAtPosition(position);
                selecetedTagName = item.getTagTitle();
                //사용자가 태그를 선택하지 않았으면 기본 태그로 설정
                if( item == null){
                    selecetedTagName = ((ScheduleTagListViewItemNoCBox)  listView.getItemAtPosition(0)).getTagTitle();

                }

                for( int i=0;i<tagDatas.size();i++){
                    if(tagDatas.get(i).getData(1).equals(selecetedTagName)){
                        //태그이름과 일치하는 태그 아이디 저장

                        selectedTagId = tagDatas.get(i).getData(0);
                        //아이디를 MAIN으로 보내야한다?
                        break;
                    }
                }//end of for


            }


        });
        //태그 버튼 눌렀을 때 태그리스트 보이게 하기

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
                                        //사용자가 태그 선택 하지 않았을 경우
                                        String defaultTag="";
                                        if(selecetedTagName ==null){

                    selecetedTagName =  getTagDatas().get(0).getData(1);
                    selectedTagId=getTagDatas().get(0).getData(0);
                    defaultTag="태그 선택 하지 않음!";
                }
                //선택된 리스트뷰이 이름으로 텍스트뷰 세팅하고 아이디 출력해야 한다.
                selectedTagTv.setText(selecetedTagName);
                Toast.makeText(getActivity(),defaultTag+"tagName : "+selecetedTagName+" , "+
                        "tagId : "+selectedTagId,Toast.LENGTH_SHORT).show();

              /*  //이거 부를 때 태그 이름 불러와서 뿌려줘야 함
               tagDatas =  ((MainActivity)getActivity()).getTagDatas();
                int len = tagDatas.size();
                ArrayList<String>tagNameList = new ArrayList<String>();
                for(int i=0;i<len;i++){

                      //tagName만 추출해서 SelectTagFromAddScheduleDialogFragment 로 보낸다.
                    tagNameList.add(tagDatas.get(i).getData(1));


                }


                SelectTagFromAddScheduleDialogFragment dialog = new SelectTagFromAddScheduleDialogFragment();
                dialog.setTagNameList(tagNameList);
                dialog.setTargetFragment(AddScheduleDialogFragment.this,REQUEST_SELECT_TAG);
                dialog.show(getFragmentManager(),DIALOG_SELECT_TAG);*/

            }
        });

        //시작 시간 TimePicker에서 고르기
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                FragmentManager manager = getFragmentManager();
               // TimePickerFragment startTimeDialog =  new TimePickerFragment();
                TimePickerFragment startTimeDialog = TimePickerFragment.newInstance(date);
                startTimeDialog.setTargetFragment(AddScheduleDialogFragment.this,REQUEST_START_TIME);
                startTimeDialog.show(manager,DIALOG_START_TIME);

            }
        });

        // 종료 시간 TimePicker에서 고르기
        finishTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                FragmentManager manager = getFragmentManager();
               // TimePickerFragment finishTimeDialog = new TimePickerFragment();
                TimePickerFragment finishTimeDialog = TimePickerFragment.newInstance(date);
                finishTimeDialog.setTargetFragment(AddScheduleDialogFragment.this,REQUEST_FINISH_TIME);
                finishTimeDialog.show(manager,DIALOG_FINISH_TIME);

            }
        });

        //장소선택 버튼 누르면 지도 볼 수 있는 창 나오고 선택하면 장소명만 가져오기
        //주소 입력 후 지도 버튼 클릭시 해당 위도 경도 값의 지도화면으로 이동

final Geocoder geocoder = new Geocoder(getActivity());
        showMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address>list = null;
                String location = LocationEv.getText().toString();
               // double  lat =
                //지도 인텐트
                Uri uri = Uri.parse("geo:38.899533,-77.036476");

                Intent it = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);

            }
        });



        //스케줄 추가 버튼
        //추가 버튼 누르면 입력된 내용 모두가 저장되면서 대화창 종료


        addScheduleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //구글에 스케줄을 등록하고 구글 스케줄 아이디를 저장한다.
                eventDataToMain = new EventData();
                //eventDataToMain = main.addEventToCalendarandGetEventID(calID);

                schedeulEditTextStr = ScheduleEditText.getText().toString();

                eventLocation = LocationEv.getText().toString();
                if(startMillis ==0 || endMillis == 0 || schedeulEditTextStr.equals("") || eventLocation.equals("")){
                    Toast.makeText(getActivity(),"다 입력하지 않았습니다. 확인해보세요! "
                            ,Toast.LENGTH_LONG).show();
                    return;
                }

                //AddScheduleFragment ->MonthCalendarFragment로 eventData전달후 스케줄 추가
                eventDataToMain = main.addEventToCalendarandGetEventID(calID,startMillis,endMillis,schedeulEditTextStr,eventLocation);
                Log.i("ottoL","확인 calID : "+calID);
                //스케줄 입력 창에 빈칸 ㅇ있으면 여기서 에러남!!!
                Log.i("ottoL","확인 eventDataToMain : "+eventDataToMain.toString());


                if(eventDataToMain == null){
                    Toast.makeText(getActivity(),"입력한 값이 없어 스케줄 등록되지 않음! "
                            ,Toast.LENGTH_LONG).show();

                      //빈스케줄 등록시 스케줄 등록되지 않음 메시지 띄우기!!


                    //스케줄 등록하지 않아도 내가 조회했던 캘린더로 돌아가야 하므로
                    //내가 선택한 날짜 정보만 넘겨준다. 스케줄 추가 창 종료하면 오늘날짜 캘린더가 아니라
                    // 내가 조회했던 캘린더 보일 수 있도록
                   /* passEventDataToMainListener.eventDataReceived("", "",
                            "",String.valueOf(selectedCal.getTimeInMillis()),"","");*/

                    ( (MainActivity)getActivity()).ChangeFragment(R.id.calendars);

                }
                //MainActivity로 데이터 전달

                 else{


                    //여기서 스케줄 태그 아이디 보내야한다???
                   /* passEventDataToMainListener.eventDataReceived(eventDataToMain.getData(7), eventDataToMain.getData(2),
                            eventDataToMain.getData(3),eventDataToMain.getData(4),eventDataToMain.getData(5),selectedTagId);*/

                    Log.i("ottoL"," 확인 FROM ADD 메인으로 이벤트 데이터  : "+eventDataToMain.toString());


                    eventDataToMain.setTagid(selectedTagId);
                    Log.d("TAG","selectedTagId : "+selectedTagId);
                    BusProvider.getInstance().post(eventDataToMain);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
                    String stime =  sdf.format(Long.parseLong(eventDataToMain.getData(4)));
                    String etime = sdf.format(Long.parseLong(eventDataToMain.getData(5)));
                    Toast.makeText(getActivity(),"eventId: "+eventDataToMain.getData(7)+"Added Successfully! Title : "+
                                    eventDataToMain.getData(2)+"시작시간 : "+
                                    stime+"종료시간 : "+etime+"장소 : "+eventDataToMain.getData(3)
                            ,Toast.LENGTH_LONG).show();

                    Log.i("ttt","eventId: "+eventDataToMain.getData(7)+"Added Successfully! Title : "+
                            eventDataToMain.getData(2)+"시작시간 : "+
                            stime+"종료시간 : "+etime+"장소 : "+eventDataToMain.getData(3));

                    Log.i("tata","tataAdd eventId: "+eventDataToMain.getData(7)+"Added Successfully! Title : "+
                            eventDataToMain.getData(2)+"시작시간 : "+
                            stime+"종료시간 : "+etime+"장소 : "+eventDataToMain.getData(3));


                    //화면전환
                    ((MainActivity)getActivity()).ChangeFragment(R.id.calendars);

                }

          //  }










               // getActivity().onBackPressed();

                //스케줄 추가 fragment 뜨기 이전 월 달력화면 그대로 보임
                //추가버튼 누르면 달력 데이터가 바뀌었으므로 이전 달력화면이 그대로 보이면 안됨
                //이전 화면이 갱신되어서 보여져야 함.
                //이전 프래그먼트의 onResume()에 notifyDataSetChanged();하면 됨?!?!?!?!

            }
        });



        return  v;

    }



    //TimePicker에서 선택한 시간 받아오는 코드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       /* int hour ;
        int minute;
        String AM_PM ;*/
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_START_TIME) {
           // Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // mCrime.setDate(date);
            // updateDate();
            //TimePicker에서 사용자가 선택한 날짜
            //SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
           // String getDataString = format.format(date);
             hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
             minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);

            //long형인 시간을 Event로 처리할 수 있음
            //yyyyMMddHHmmss
            //'2017년1월17일'에서 년월일 뽑아오기
              String startHour="";
            String startMin="";
             if(hour<10)startHour = "0"+hour;
            else startHour=hour+"";
            if(minute<10)  startMin="0"+minute;
            else startMin=minute+"";
                startTimeTV.setText(startHour+" : "+startMin);

            //String dateClean= selectedDate.replaceAll("[^0-9]", "");//20170227 이런형식
            String dateClean= ""+selectedCal.get(Calendar.YEAR)+(selectedCal.get(Calendar.MONTH)+1)+
                    selectedCal.get(Calendar.DATE);// 20170227 이런형식
            Log.i("tag"," startTime : dateClean : "+dateClean);
            //long형으로..

            year = dateClean.substring(0,4);
            month = dateClean.substring(4,6);
            day = dateClean.substring(6);


            beginTime = selectedCal;
            beginTime.set(Calendar.HOUR_OF_DAY,hour);
            beginTime.set(Calendar.MINUTE,minute);
            //beginTime.set(Integer.parseInt(year), (Integer.parseInt(month)-1), Integer.parseInt(day), hour, minute);
            startMillis = beginTime.getTimeInMillis();


            //년 월 일 정수로 분리해서 long형?
            //

            //changedStartTime =  changeTime(dateClean);
            //사용자가 선택한 시간을 long형으로 바꾸기
            //Date.getTime()은 long 반환함.


        }
        else if (requestCode == REQUEST_FINISH_TIME) {
            // Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // mCrime.setDate(date);
            // updateDate();
            //TimePicker에서 사용자가 선택한 날짜
            //SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            // String getDataString = format.format(date);
             hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
             minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);


            String endHour="";
            String endMin="";
            if(hour<10)endHour = "0"+hour;
            else endHour=hour+"";
            if(minute<10)  endMin="0"+minute;
            else endMin=minute+"";

             finishTimeTv.setText(endHour+" : "+endMin);

          //  dateClean= selectedDate.replaceAll("[^0-9]", "");//20170227 이런형식
            String dateClean= ""+selectedCal.get(Calendar.YEAR)+(selectedCal.get(Calendar.MONTH)+1)+
                    selectedCal.get(Calendar.DATE);// 20170227 이런형식
            //changedFinishTime =  changeTime(dateClean);
            year = dateClean.substring(0,4);
            month = dateClean.substring(4,6);
            day = dateClean.substring(6);


            endTime = selectedCal;
            endTime.set(Calendar.HOUR_OF_DAY,hour);
            endTime.set(Calendar.MINUTE,minute);

            //endTime .set(Integer.parseInt(year), (Integer.parseInt(month)-1), Integer.parseInt(day), hour, minute);
            endMillis = endTime .getTimeInMillis();
            Log.i("tag","finishTime : dateClean : "+dateClean);
        }


    }





 /*   public String getAccount() {

        final int REQUEST_GET_ACCOUNT = 0;
        //안드로이드 계정 불러오기


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_GET_ACCOUNT);
        }
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        Account account = null;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].type.equals("com.google")) {

                account = accounts[i];
                break;

            }


        }
        return account.name;


    }*/

    public long changeTime(String time){

        String []yMd = new String[9];

        //dateClean을 Date로 바꿔야 long으로 바꿀수 있음
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        Date changedDate = null;
        try{

            changedDate = originalFormat.parse(time);
        }catch (Exception e){
            e.printStackTrace();
        }
        //date->long으로
        return changedDate.getTime();


    }


   /* public long getCalID() {

        String[] EVENT_PROJECTION = new String[]{
                //SQL의 SELECT 문에 해당. 열 리턴, null 사용시 모든 열 리턴
                CalendarContract.Calendars._ID,                           // 0

        };

        final int PROJECTION_ID_INDEX = 0;

        final int REQUEST_READ_CALENDAR_PERMISSION = 5;
        Cursor cur = null;
        ContentResolver cr = getActivity().getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        //selection :SQL의 WHERE 조건에 해당 NULL일 경우 모든 데이터 반환
        // String selection ="(("+Calendars.ACCOUNT_NAME+" =?))";

        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " =?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " =?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " =?))";

        //selectionArgs :  selection을 지정하였을 경우 where절에 해당하는 값들을 배열로 적어줘야 한다.
        //String []selectionArgs = new String[]{gmail};
        String[] selectionArgs = new String[]{gmail, "com.google", gmail};
        //Submit the query and get a Cursor object back
        if (ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.READ_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    ( getActivity(), new String[]{Manifest.permission.READ_CALENDAR}
                            , REQUEST_READ_CALENDAR_PERMISSION);
        }
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        *//*String displayName = null;
        String accountName = null;
        String ownerName = null;*//*
        long calID = 0;

        while (cur.moveToNext()) {
            calID = 0;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);//캘린더 id
            // displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            // accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            // ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

        }

        return calID;


    }*/

/*    public void  addAttendeeToEvent(String eventId){
        if(attendeeEv.getText().toString().length()!=0){
            attendeeEmail = attendeeEv.getText().toString();


        }
        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        //values.put(Attendees.ATTENDEE_NAME, "Trevor");
        values.put(Attendees.ATTENDEE_EMAIL, attendeeEmail);
        //values.put(Attendees.ATTENDEE_RELATIONSHIP, Attendees.RELATIONSHIP_ATTENDEE);
        //  values.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_OPTIONAL);
        // values.put(Attendees.ATTENDEE_STATUS, Attendees.ATTENDEE_STATUS_INVITED);
        values.put(Attendees.EVENT_ID, getEventId);
        if (ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.WRITE_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    ( getActivity(), new String[]{android.Manifest.permission.READ_CALENDAR}
                            , REQUEST_WRITE_CALENDAR_PERMISSION);
        }
        Uri uri = cr.insert(Attendees.CONTENT_URI, values);


    }*/
/*public EventData addEventToCalendarandGetEventID(long calID){

    final int  REQUEST_WRITE_CALENDAR_PERMISSION = 1;
    ContentResolver insertCr= getActivity().getContentResolver();
    ContentValues values = new ContentValues();
    //values.put(Events._ID,"123");
    if( startMillis == 0 || endMillis ==0 || ScheduleEditText.getText().toString().length() ==0
        || LocationEv.getText().toString().length()==0
             ){
        //스케줄 등록 요건 중 하나라도 비어 있으면 eventData를 생성하지도 않고 이벤트를 구글 캘린더에 추가하지도 않는다.
        Log.d("ttt","스케줄 등록 요건 불만족.");
        Log.d("ttt","startMillis : "+startMillis+"endMillis : "+endMillis+"ScheduleEditText : "+ScheduleEditText.getText().toString()+
                "LocationEv : "+ LocationEv.getText().toString());
        return  null;


    }
    values.put(Events.DTSTART, startMillis);
    values.put(Events.DTEND, endMillis);
    //  schedeulEditTextStr = ScheduleEditText.getText().toString();
    values.put(Events.TITLE,  ScheduleEditText.getText().toString() );
   *//* if(SummaryScheduleEditText.getText().toString().length()==0){
        SummaryScheduleEditText.setText("");

    }*//*
   /// values.put(Events.DESCRIPTION,   SummaryScheduleEditText.getText().toString());
    values.put(Events.CALENDAR_ID, calID);
    values.put(Events.EVENT_TIMEZONE, "Asia/Seoul");
    values.put(Events.EVENT_LOCATION,LocationEv.getText().toString());
    EventData insertEventData = new EventData();


    if (ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.WRITE_CALENDAR) !=
            PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions
                ( getActivity(), new String[]{Manifest.permission.READ_CALENDAR}
                        , REQUEST_WRITE_CALENDAR_PERMISSION);
    }

    Uri insertUri = insertCr.insert(Events.CONTENT_URI, values);
    eventId = insertUri.getLastPathSegment();



    //스케줄 데이터 객체에 넣고 이벤트 아이디도 EventData 객체에 넣는다.
    //insertEventData.setEventId(eventId);
    insertEventData.setData(7,eventId);
    insertEventData.setData(2,ScheduleEditText.getText().toString() );
    insertEventData.setData(3,LocationEv.getText().toString() );
    //String.valueOf --->long형의 시간을 String 타입으로 바꿔서 EventData객체에 저장한다.
    insertEventData.setData(4,getYMDHM(startMillis));
    insertEventData.setData(5,getYMDHM(endMillis));


    //이벤트 시작시간 (long형)에서 년월일만 뽑아서 년월일이 일치하는 CalData의 EventDataList에 스케줄을 대입한다.
    //String clanSelectedDate = selectedDate.replaceAll("[^0-9]", "");

    //eventData객체를 MonthCalendarFragment로 전달해서서  CalData의 eventDataList에 추가해야 함

   // Toast.makeText(getActivity(),"eventData : "+insertEventData.getTitle(),Toast.LENGTH_SHORT).show();
    Toast.makeText(getActivity(),"eventData : "+insertEventData.getData(2),Toast.LENGTH_SHORT).show();
    //

    return  insertEventData;



}*/

   /* public String  addEventToCalendarandGetEventID(long calID){

        final int  REQUEST_WRITE_CALENDAR_PERMISSION = 1;
        ContentResolver insertCr= getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        //values.put(Events._ID,"123");
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
      //  schedeulEditTextStr = ScheduleEditText.getText().toString();
        values.put(Events.TITLE,  ScheduleEditText.getText().toString() );
        values.put(Events.DESCRIPTION,   SummaryScheduleEditText.getText().toString());
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, "Asia/Seoul");
        values.put(Events.EVENT_LOCATION,eventLocation);

        EventData eventData = new EventData(ScheduleEditText.getText().toString(),SummaryScheduleEditText.getText().toString(),
                startMillis,  endMillis,eventLocation);

        if (ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.WRITE_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    ( getActivity(), new String[]{android.Manifest.permission.READ_CALENDAR}
                            , REQUEST_WRITE_CALENDAR_PERMISSION);
        }

        Uri insertUri = insertCr.insert(Events.CONTENT_URI, values);
        eventId = insertUri.getLastPathSegment();
        //스케줄 데이터 객체에 넣고 이벤트 아이디도 EventData 객체에 넣는다.
        eventData.setEventId(eventId);
        //이벤트 시작시간 (long형)에서 년월일만 뽑아서 년월일이 일치하는 CalData의 EventDataList에 스케줄을 대입한다.
        String clanSelectedDate = selectedDate.replaceAll("[^0-9]", "");

      //eventData객체를 MonthCalendarFragment로 전달해서서  CalData의 eventDataList에 추가해야 함



        return  eventId;



    }
*/


    public void addAlarm(String eventId){

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Reminders.MINUTES, alarmTime);
        values.put(CalendarContract.Reminders.EVENT_ID, getEventId);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

        if (ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.WRITE_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    ( getActivity(), new String[]{Manifest.permission.READ_CALENDAR}
                            , REQUEST_WRITE_CALENDAR_PERMISSION);
        }
       // Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);

    }

   /* public String getYMDHM(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Log.e("AAA","바꾼 시간: "+sdf.format(time));
        return sdf.format(time);
    }*/


    //메인의 백키를 실행시킨다.
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.ChangeFragment(R.id.calendars);
    }

    //자신의 백키를 불러오게 등록
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
    }

}



//CalData ArrayList 시간순으로 오름차순 정렬하기위해
class CalDataComparator implements Comparator<CalData> {
    @Override
    public int compare(CalData first, CalData second) {
        long firstValue = first.getTotalDateLong();
        long secondValue = second.getTotalDateLong();

        //order by ascending
        if (firstValue > secondValue) {
            return 1;
        } else if (firstValue < secondValue) {
            return -1;
        } else {
            return 0;
        }
    }
}
