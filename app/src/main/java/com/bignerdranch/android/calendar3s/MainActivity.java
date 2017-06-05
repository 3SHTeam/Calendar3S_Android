package com.bignerdranch.android.calendar3s;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bignerdranch.android.calendar3s.Calendar.MonthCalendarFragment;
import com.bignerdranch.android.calendar3s.Group.FriendGroupFragment;
import com.bignerdranch.android.calendar3s.Group.GroupMememberListFragment;
import com.bignerdranch.android.calendar3s.Login.LoginFragment;
import com.bignerdranch.android.calendar3s.Message.MsgRecognitionFragment;
import com.bignerdranch.android.calendar3s.Message.MyService;
import com.bignerdranch.android.calendar3s.Schedule.AddScheduleDialogFragment;

import com.bignerdranch.android.calendar3s.Schedule.ClickListFragment;
import com.bignerdranch.android.calendar3s.data.EventData;
import com.bignerdranch.android.calendar3s.data.GroupData;
import com.bignerdranch.android.calendar3s.data.MessageData;
import com.bignerdranch.android.calendar3s.data.TagData;
import com.bignerdranch.android.calendar3s.data.UserData;
import com.bignerdranch.android.calendar3s.database.JsonMaster;
import com.bignerdranch.android.calendar3s.database.SendToDB;
import com.bignerdranch.android.calendar3s.database.UpdateToDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;




public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_CALDATA = "com.bignerdranch.android.a3scal.calData_id";



    private String gmail = "";

    private String eventId = "";




    //문자메시지 서비스 관련


    //문자 메시지 리스트
    private  ArrayList<HashMap<String, String>> smsList;
    public void clearSmsList(){
        smsList.clear();
    }
    public ArrayList<HashMap<String, String>> getSmsList() {
        return smsList;
    }



    private ArrayList<String> SmsMsgArry;

    public void setSmsMsgArry(ArrayList<String> smsMsgArry) {
        SmsMsgArry = smsMsgArry;
    }


    public ArrayList<String> getSmsMsgArry() {
        return SmsMsgArry;
    }




    private ArrayList<String> PhNumArry;
    public void setPhNumArry(ArrayList<String> phNumArry) {
        PhNumArry = phNumArry;
    }
    public ArrayList<String> getPhNumArry() {
        return PhNumArry;
    }
    public static  SimpleAdapter adapter;

    public SimpleAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(SimpleAdapter adapter) {
        this.adapter = adapter;
    }

    private ListView ListViewSMS;

    public ListView getListViewSMS() {
        return ListViewSMS;
    }

    public void setListViewSMS(ListView listViewSMS) {
        ListViewSMS = listViewSMS;
    }

    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public static Context context;
    private Iterator<String> iterator;
    private String received;

    public String getReceived() {
        return received;
    }

    private String ListInfo;

    public String getListInfo() {
        return ListInfo;
    }


    private long CalID;
    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    //permission
    public static final int REQUEST_READ_CALENDAR_PERMISSION = 1;
    public static final int REQUEST_WRITE_CALENDAR_PERMISSION = 2;
    public static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 3;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 4;
    public static MainActivity Root;

    FragmentManager fm;
    Fragment fragment;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    long calendarId /*= getCalID()*/;

    int startday;  //달력 시작 요일
    int endday;//달력 마지막 요일

    public int getStartday() {
        return startday;
    }

    public int getEndday() {
        return endday;
    }

    //이벤트 객체 - 이벤트 아이디, 타이틀 ,시작 종료 시간, 장소 등
    private ArrayList<EventData> eventDatas = new ArrayList<>();

    public ArrayList<EventData> getEventDatas() {
        return eventDatas;
    }

    public void setEventDatas(ArrayList<EventData> eventDatas) {
        this.eventDatas = eventDatas;
    }

    //태그 데이터
    private ArrayList<TagData> tagDatas = new ArrayList<>();

    public ArrayList<TagData> getTagDatas() {
        return tagDatas;
    }

    public void setTagDatas(ArrayList<TagData> tagDatas) {
        this.tagDatas = tagDatas;
        for (int i = 0; i < tagDatas.size(); i++)
            tagDatas.get(i).showData();
    }

    private HashMap<String, Boolean> tagHash = null;

    public HashMap<String, Boolean> getTagHash() {
        return tagHash;
    }

    public void setTagHash(HashMap<String, Boolean> tagHash) {
        this.tagHash = tagHash;
    }

    //그룹 데이터
    private ArrayList<GroupData> groupDatas = new ArrayList<>();

    public ArrayList<GroupData> getGroupDatas() {
        return groupDatas;
    }

    public void setGroupDatas(ArrayList<GroupData> groupDatas) {
        this.groupDatas = groupDatas;
    }


    //캘린더 객체. 메인은 MonthCalendarFragment가 몇월을 뿌리고 있는지 알고 있다.
    //DatePicker로 날짜 이동시에도 마찬가지이다.
    private Calendar calendar = Calendar.getInstance();

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity FireBase";
    //FireBae Notification 자동추가 스케줄 변수
    EventData autoAddEventFromNoti;

    //UserData
    private UserData user;

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }


    //AddSCheduleFragment에서 스케줄 추가할 때 받은 정보로 만든 EventData
    EventData receivedNewEventData;

    public EventData getReceivedNewEventData() {
        return receivedNewEventData;
    }

    public void setReceivedNewEventData(EventData receivedNewEventData) {
        this.receivedNewEventData = receivedNewEventData;
    }


    private String msgbdoyFromFBNoti = "";
    private Intent intentFromFB;

    //REQUEST LIST DATA
    public ArrayList<MessageData> messages = new ArrayList<MessageData>();

    public ArrayList<MessageData> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageData> messages) {
        this.messages = messages;
    }


    //문자메시지 관련
    private SharedPreferences pref;

    public SharedPreferences getPref() {
        return pref;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (intentFromFB != null) {

            msgbdoyFromFBNoti = intentFromFB.getStringExtra("msgFromMyPC");
            if (msgbdoyFromFBNoti != null) {
                Log.i("msgTEST", msgbdoyFromFBNoti);
//                EventData autoAddEventFromNoti = new EventData();
//                long now = System.currentTimeMillis();
//                long now2 =  System.currentTimeMillis();
//                gmail = getAccount(); calendarId = getCalID();
//                autoAddEventFromNoti = addEventToCalendarandGetEventID(calendarId,now,now2,"auto5","autoLoca5");
//                Log.i("msgTEST","자동추가?"+autoAddEventFromNoti.toString()+" sql: "+autoAddEventFromNoti.getSendSQLString());

                String type;
                String SMaster, Sname, Place, StartTime, EndTime, Tagid, GoogleSid;
                String Sid, Gid, receiver;

                if (msgbdoyFromFBNoti.length() != 0) {
                    try {

                        Log.d("ottoL", "originM  : " + msgbdoyFromFBNoti);
                        JSONObject root = new JSONObject(msgbdoyFromFBNoti);
                        type = root.getString("type").toString();
                        Log.d("msgTEST", type);
                        if (type.equals("createSchedule")) {
                            SMaster = root.getString("SMaster").toString();
                            Place = root.getString("Place").toString();
                            StartTime = root.getString("StartTime").toString();
                            EndTime = root.getString("EndTime").toString();
                            Tagid = root.getString("Tagid").toString();
                            Sname = root.getString("Sname").toString();


                            Log.d("ottoL", "SMaster : " + SMaster + ", " + "Place: " + Place + ", " + "Sname: " + Sname + ", " +
                                    "StartTime : " + StartTime + ", " + "EndTime: " + EndTime + ", " + "Tagid : " + Tagid);

                            String sql = "'" + SMaster + "','" + Sname + "','" + Place + "','"
                                    + StartTime + "','" + EndTime + "'";
                            insertScheduleDB(sql);//DB스케줄 삽입 성공

                            //구글용으로 시간 포멧 변경
                            Calendar startCal = makeStringDateToGoogleTypeDate(StartTime);
                            Calendar endCal = makeStringDateToGoogleTypeDate(EndTime);

                            long startTime = startCal.getTimeInMillis();
                            long endTime = endCal.getTimeInMillis();
                            SimpleDateFormat sdftest = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
                            String ss = sdftest.format(startCal.getTime());
                            String ss2 = sdftest.format(endCal.getTime());
                            Log.i("ottoL", "NOW startTime : " + ss);
                            Log.i("ottoL", " NOW endTime : " + ss2);
                            autoAddEventFromNoti = new EventData();

                            //addEventToCalendarandGetEventID(long calID,long startMillis,
                            // long endMillis,String scheduleEt, String locationEt)
                            gmail = getAccount();
                            calendarId = getCalID();
                            //구글에 추가하고 구글 스케줄 아이디 받아오기
                            autoAddEventFromNoti = addEventToCalendarandGetEventID(calendarId, startTime, endTime, Sname, Place);
                            Log.i("ottoL", " NOW calendarId : " + calendarId + "gmail : " + gmail);
                            Log.i("ottoL", "NOW autoAddEventFromNoti : " + autoAddEventFromNoti.toString());

                            //방금 삽입한 스케줄 아이디 가져오기
                            String message = "SMaster='" + SMaster +
                                    "'and SName='" + Sname + "'";
                            Log.i("msgTEST", "select1 " + message);
                            Sid = SelectInsertSchedule(message);
                            //Googldid, Sid, Tagid, Gid, GoogleSid
                            message = "'" + SMaster + "','" + Sid + "','" + Tagid
                                    + "','" + autoAddEventFromNoti.getData(7) + "'";// event.getData(7):구글 이벤트 아이디 넣기
                            Log.i("msgTEST", "join1 " + message);

                            joinScheduleDB(message);
                        } else if (type.equals("deleteSchedule")) {
                            Sid = root.getString("Sid").toString();
                            Tagid = root.getString("Tagid").toString();
                            SMaster = root.getString("SMaster").toString();
                            GoogleSid = root.getString("GoogleSid").toString();
                            Log.i("msgTEST", Sid + ", " + Tagid);


                            //  String GoogleSid = getEventData().getData(7);

                            Log.d("DeleteSchedule", "sid=" + Sid);

                            DeleteScheduleJoin(Sid, SMaster);
                            DeleteMySchedule(Sid);

                            deleteGoogleCalendar(GoogleSid);

                        } else if (type.equals("updateSchedule")) {
                            SMaster = root.getString("SMaster").toString();
                            Place = root.getString("Place").toString();
                            StartTime = root.getString("StartTime").toString();
                            EndTime = root.getString("EndTime").toString();
                            Tagid = root.getString("Tagid").toString();
                            Sname = root.getString("Sname").toString();
                            Sid = root.getString("Sid").toString();
                            GoogleSid = root.getString("GoogleSid").toString();
                            Log.d("msgTEST", SMaster + ", " + Place + ", " + Sname + ", " + StartTime
                                    + ", " + EndTime + ", " + Tagid + ", " + Sid + ", " + GoogleSid);

                            UpdateSchedule(Sid, Sname, StartTime, EndTime, Place);
                            UpdateScheduleJoin(Sid, Tagid, SMaster);
                            //google 수정


                            //구글용으로 시간 포멧 변경
                            Calendar startCal = makeStringDateToGoogleTypeDate(StartTime);
                            Calendar endCal = makeStringDateToGoogleTypeDate(EndTime);
                            long startTimeLong = startCal.getTimeInMillis();
                            long endTimeLong = endCal.getTimeInMillis();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
                            String test1 = sdf.format(startCal.getTime());
                            String test2 = sdf.format(endCal.getTime());

                            updateGoogleCalendar(GoogleSid, Sname, Place, startTimeLong, endTimeLong);
                            Log.d("msgTEST", " 구글 수정 후 GoogleSid : " + GoogleSid + ", " + "Place : " + Place + ", " + "Sname : " + Sname + ", " +
                                    "StartTime : " + StartTime + ", " + "EndTime : " + EndTime + ", " + "Tagid : " + Tagid + ", " + "Sid : " + Sid);
                            Log.d("msgTEST", " 구글 수정 후 시간 : " + "StartTime : " + test1 + ", " + "EndTime : " + test2);
                        }else if (type.equals("createGroupSchedule")) {
                            SMaster = root.getString("SMaster").toString();
                            Sname = root.getString("Sname").toString();
                            Place = root.getString("Place").toString();
                            StartTime = root.getString("StartTime").toString();
                            EndTime = root.getString("EndTime").toString();
                            Tagid = root.getString("Tagid").toString();
                            Gid = root.getString("Gid").toString();


                            Log.d("ottoL", "SMaster : " + SMaster + ", " + "Place: " + Place + ", " + "Sname: " + Sname + ", " +
                                    "StartTime : " + StartTime + ", " + "EndTime: " + EndTime + ", " + "Tagid : " + Tagid + "Gid : " + Gid);

                            String sql = "'" + SMaster + "','" + Sname + "','" + Place + "','"
                                    + StartTime + "','" + EndTime + "'";
                            insertScheduleDB(sql);//DB스케줄 삽입 성공

                            //구글용으로 시간 포멧 변경
                            Calendar startCal = makeStringDateToGoogleTypeDate(StartTime);
                            Calendar endCal = makeStringDateToGoogleTypeDate(EndTime);

                            long startTime = startCal.getTimeInMillis();
                            long endTime = endCal.getTimeInMillis();
                            SimpleDateFormat sdftest = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
                            String ss = sdftest.format(startCal.getTime());
                            String ss2 = sdftest.format(endCal.getTime());
                            Log.i("ottoL", "NOW startTime : " + ss);
                            Log.i("ottoL", " NOW endTime : " + ss2);
                            autoAddEventFromNoti = new EventData();

                            //addEventToCalendarandGetEventID(long calID,long startMillis,
                            // long endMillis,String scheduleEt, String locationEt)
                            gmail = getAccount();
                            calendarId = getCalID();
                            //구글에 추가하고 구글 스케줄 아이디 받아오기
                            autoAddEventFromNoti = addEventToCalendarandGetEventID(calendarId, startTime, endTime, Sname, Place);
                            Log.i("ottoL", " NOW calendarId : " + calendarId + "gmail : " + gmail);
                            Log.i("ottoL", "NOW autoAddEventFromNoti : " + autoAddEventFromNoti.toString());

                            //방금 삽입한 스케줄 아이디 가져오기
                            String message = "SMaster='" + SMaster +
                                    "'and SName='" + Sname + "'";
                            Log.i("msgTEST", "select1 " + message);
                            Sid = SelectInsertSchedule(message);
                            //Googldid, Sid, Tagid, Gid, GoogleSid
                            message = "'" + SMaster + "','" + Sid + "','" + Tagid + "','" + Gid
                                    + "','" + autoAddEventFromNoti.getData(7) + "'";// event.getData(7):구글 이벤트 아이디 넣기
                            Log.i("msgTEST", "join1 " + message);

                            joinGroupSchedule(message);
                        }else if (type.equals("joinGroupSchedule")) {
                            SMaster = root.getString("SMaster").toString();
                            Place = root.getString("Place").toString();
                            StartTime = root.getString("StartTime").toString();
                            EndTime = root.getString("EndTime").toString();
                            Sname = root.getString("Sname").toString();
                            Gid = root.getString("Gid").toString();
                            receiver = root.getString("receiver").toString();

                            Log.d("joingroupnoti",SMaster + " " + Place+ " " +StartTime+ " " +
                                        EndTime+ " " +Sname+ " " +Gid+ " " +receiver);


                            //구글용으로 시간 포멧 변경
                            Calendar startCal = makeStringDateToGoogleTypeDate(StartTime);
                            Calendar endCal = makeStringDateToGoogleTypeDate(EndTime);

                            long startTime = startCal.getTimeInMillis();
                            long endTime = endCal.getTimeInMillis();
                            SimpleDateFormat sdftest = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
                            String ss = sdftest.format(startCal.getTime());
                            String ss2 = sdftest.format(endCal.getTime());
                            Log.i("ottoL", "NOW startTime : " + ss);
                            Log.i("ottoL", " NOW endTime : " + ss2);
                            autoAddEventFromNoti = new EventData();

                            //addEventToCalendarandGetEventID(long calID,long startMillis,
                            // long endMillis,String scheduleEt, String locationEt)
                            gmail = getAccount();
                            calendarId = getCalID();
                            //구글에 추가하고 구글 스케줄 아이디 받아오기
                            autoAddEventFromNoti = addEventToCalendarandGetEventID(calendarId, startTime, endTime, Sname, Place);
                            Log.i("ottoL", " NOW calendarId : " + calendarId + "gmail : " + gmail);
                            Log.i("ottoL", "NOW autoAddEventFromNoti : " + autoAddEventFromNoti.toString());

                            //방금 삽입한 스케줄 아이디 가져오기
                            String message = "SMaster='" + SMaster +
                                    "'and SName='" + Sname + "'";
                            Log.i("msgTEST", "select1 " + message);
                            Sid = SelectInsertSchedule(message);
                            Tagid = selectMyGroupTag("Googleid='" + receiver + "' and Gid='" + Gid + "'");

                            Log.d("joingroupnoti",Sid);
                            Log.d("joingroupnoti",Tagid);

                            //Googldid, Sid, Tagid, Gid, GoogleSid
                            message = "'" + receiver + "','" + Sid + "','" + Tagid + "','" + Gid
                                    + "','" + autoAddEventFromNoti.getData(7) + "'";// event.getData(7):구글 이벤트 아이디 넣기
                            Log.i("msgTEST", "join1 " + message);

                            joinGroupSchedule(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                Log.i("msgTEST", "msgbdoyFromFBNoti : " + msgbdoyFromFBNoti);
            }

            msgbdoyFromFBNoti = "";
            intentFromFB = null;
        }
    }


    public Calendar makeStringDateToGoogleTypeDate(String time) {


        String Year = time.substring(0, 4);
        String Month = time.substring(4, 6);
        String Day = time.substring(6, 8);
        String Hour = time.substring(8, 10);
        String Min = time.substring(10, 12);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(Year));
        c.set(Calendar.MONTH, (Integer.parseInt(Month) - 1));
        c.set(Calendar.DATE, Integer.parseInt(Day));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Hour));
        c.set(Calendar.MINUTE, Integer.parseInt(Min));

        return c;

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("acceptSMS", Activity.MODE_PRIVATE);
        Map<String, ?> values = pref.getAll();
        iterator = values.keySet().iterator();

        received = pref.getString("SMS", "");
        context = this;


        ListInfo = pref.getString("ListSelect", "");


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(0);
        }

        intentFromFB = getIntent();

        mAuth = FirebaseAuth.getInstance();
        Log.d("firebase", "FirebaseAuth getInstance");


        //이벤트를 감지하고자하는 Activity 혹은 Fragment에서 register를 수행합니다
        BusProvider.getInstance().register(this);
        calendar = Calendar.getInstance();


        Root = this;


        smsList = new ArrayList<HashMap<String, String>>();

        //SharedPreference에서 가져오는 부분 -문자데이터와 폰 데이터
        //  Map<String, ?> values = pref.getAll();
        // iterator = values.keySet().iterator();

        // String received = pref.getString("SMS", "");
       // String received =getReceived();
        Log.d("HASHMAP","received : "+received);
        String[] smss = received.split("@@@@@@@");

        SmsMsgArry = new ArrayList<String>();
        PhNumArry = new ArrayList<String>();
        for (int i = 0; i < smss.length; i++) {
            String asms = smss[i];
            Log.d("HASHMAP","asms : "+asms);
            String[] messageAndPhone = smss[i].split("@,@,@,@");
            if (messageAndPhone.length != 2) continue;
            SmsMsgArry.add(messageAndPhone[0]);
            PhNumArry.add(messageAndPhone[1]);
        }

        if (SmsMsgArry != null) {

            for (int i = SmsMsgArry.size()-1; i >= 0; i--) {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("item1", SmsMsgArry.get(i));
                //Log.d("HASHMAP","SmsMsgArry : "+SmsMsgArry.toString());
                item.put("item2", PhNumArry.get(i));
                // Log.d("HASHMAP","PhNumArry : "+PhNumArry.toString());
                smsList.add(item);
            }

            // for(int i=0;i<)
        }


        // 권한 요청
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.WRITE_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                        this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS) !=
                        PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) !=
                        PackageManager.PERMISSION_GRANTED


                ) {
            //주소록 접근ㅎ권한 추가하기
            ActivityCompat.requestPermissions
                    (this,
                            new String[]{
                                    android.Manifest.permission.READ_CALENDAR,
                                    android.Manifest.permission.WRITE_CALENDAR,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.GET_ACCOUNTS,
                                    android.Manifest.permission.RECEIVE_SMS
                            }
                            , REQUEST_READ_CALENDAR_PERMISSION);

        }


        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new LoginFragment().newInstance();
            fm.beginTransaction().replace(R.id.container, fragment).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    public void ChangeActivity(View v) {
        Intent intent = null;
        switch (v.getId()) {
            default:
                //case R.id.alarmSetting:
                //intent = new Intent(MainActivity.this,MainActivity.class);
                //   break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


    //AddScheduleDialogFragment로부터 추가한 스케줄 정보를 EventData에 필요한 String객체로 가져온다.
   /* @Override
    public void eventDataReceived(String eventId,String eventTitle,String location,String eventStartTime,
                                  String eventEndTime,String tagId) {
        //구글에 스케줄 삽입 완료
       //CalData에 추가할 날짜 받아오기
        String receivedTagId = tagId;
        Log.i("AAA","receivedTagId : "+receivedTagId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String eventDate =   sdf.format(Long.parseLong(eventStartTime));
        String Googleid = this.user.getData(0);
        //Sid, SMaster, Sname, Place, StartTime, EndTime, TagId, GoogleSId, GId
         receivedNewEventData  = new EventData(null, Googleid ,eventTitle,location,
                                                eventStartTime,eventEndTime, null, eventId, null);

        System.out.println(" CHECK!"+"eventTitle : "+eventTitle);
        Log.i("ttt","CHECK!"+"eventId : "+eventId+"eventTitle : "+eventTitle+"eventStartTime : "+eventStartTime+
                "eventEndTime : "+eventEndTime+ "location : "+location+"totalDate : " +eventDate);

        //DB에 스케줄 삽입.
        insertScheduleDB(receivedNewEventData.getSendSQLString());
        //방금 삽입한 스케줄 아이디 가져오기
        String message = "SMaster='" + receivedNewEventData.getData(1) +
                "'and SName='" + receivedNewEventData.getData(2) + "'";
        String Sid = SelectInsertSchedule(message);
        //Googldid, Sid, Tagid, Gid, GoogleSid
        message = "'" + Googleid + "','" + Sid +  "','" + receivedTagId + "','" + receivedNewEventData.getData(7) + "'" ;
        joinScheduleDB(message);


        //Main의 스케줄 데이터 최신화
        freshMySchedule();

        Log.i("ttt","FROMDB!"+"receivedNewEventData : "+receivedNewEventData.getData(0));

    }*/

    @Subscribe
    public void FinishLoad(EventData eventData) {

// 이벤트가 발생한뒤 수행할 작업
        EventData receivedNewEventData = new EventData();
        receivedNewEventData = eventData;
        if (receivedNewEventData != null) {
            Log.i("ottoL", "MainActivity sqlString : " + receivedNewEventData.getSendSQLString());
            Log.i("ottoL", "MainActivity eventData toString : " + receivedNewEventData.toString());

           /* //여기서 구글에 추가해야 함.
            gmail = getAccount(); calendarId = getCalID();
           // addEventToCalendarandGetEventID(calendarId);*/
        } else
            Log.i("ottoL", "MainActivity : " + "eventData is null");


        String receivedTagId = receivedNewEventData.getData(6);
        Log.i("AAA", "receivedTagId : " + receivedTagId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String Googleid = this.user.getData(0);


        //DB에 스케줄 삽입.
        receivedNewEventData.setData(1, Googleid);
        insertScheduleDB(receivedNewEventData.getSendSQLString());

        //방금 삽입한 스케줄 아이디 가져오기
        String message = "SMaster='" + receivedNewEventData.getData(1) +
                "'and SName='" + receivedNewEventData.getData(2) + "'";

        Log.i("ottoL", "select1 " + message);

        String Sid = SelectInsertSchedule(message);
        //Googldid, Sid, Tagid, Gid, GoogleSid
        message = "'" + Googleid + "','" + Sid + "','" + receivedNewEventData.getTagid()
                + "','" + receivedNewEventData.getData(7) + "'";


        Log.i("ottoL", "join1 " + message);

        joinScheduleDB(message);


        //Main의 스케줄 데이터 최신화
        freshMySchedule();

        Log.i("ttt", "FROMDB!" + "receivedNewEventData : " + receivedNewEventData.getData(0));


    }


    public String SelectInsertSchedule(String message) {
        String url = "SelectSchedule.php";
        SendToDB stDB = new SendToDB(url, message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("signinDB", "fail :" + e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        Log.d("signinDB", "json result : " + result);

        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectInsertSchedule", result);

        return jm.getResult();
    }

    public void insertScheduleDB(String message) {
        String url = "InsertSchedule.php";

        SendToDB stDB = new SendToDB(url, message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("signinDB", "fail :" + e.toString());
        }
    }

    private void joinScheduleDB(String message) {
        String url = "JoinSchedule.php";
        SendToDB stDB = new SendToDB(url, message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("signinDB", "fail :" + e.toString());
        }
    }

    public void joinGroupSchedule(String message) {
        String url = "JoinGroupSchedule.php";
        SendToDB stDB = new SendToDB(url, message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("signinDB", "fail :" + e.toString());
        }
    }


    public void signupFireBase(String googleId, String googlePw) {
        Log.d("FirebaseSignup", "email,pw : " + googleId + ", " + googlePw);

        mAuth.createUserWithEmailAndPassword(googleId, googlePw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {//회원가입 실패
                            Log.d("FirebaseSignup", "Failed : " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void signinFireBase(String googleId, String googlePw) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(googleId, googlePw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {//로그인 실패
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                        }

                    }
                });

        this.mAuth = auth;
    }

    public void signinDB(String id, String pw) {
        String message = "where Googleid = '" + id + "' and Googlepw = '" + pw + "'";
        String url = "SelectMyProfile.php";

        SendToDB stDB = new SendToDB(url, message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("signinDB", "fail :" + e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        Log.d("signinDB", "json result : " + result);

        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectMyProfile", result);

        this.user = jm.getUser();
        this.user.showData();
    }


    public boolean CheckUser(String message){
        //DB에 사용자 추가
        String php = "SelectMyProfile.php";

        SendToDB sendToDB = new SendToDB(php, message);
        sendToDB.start();// DB연결 스레드 시작
        try {
            sendToDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

        JsonMaster jsonMaster = new JsonMaster();
        jsonMaster.onPostExecute("SelectMyProfile", sendToDB.getResult());
        UserData userData = jsonMaster.getUser();

        if(userData == null) return true;
        else return false;
    }


    public String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public String getFBid() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            return uid;
        }
        return null;
    }

    public void InsertTag(String tagname, String color) {
        String message = "'" + user.getData(0) + "','" + tagname + "','" + color
                + "','맑은고딕','15','NULL'";
        String url = "InsertTag.php";

        SendToDB stDB = new SendToDB(url, message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("insertTag", "fail :" + e.toString());
        }

        freshTag();
    }


    public void freshMySchedule() {
      /* DB에서 스케줄 가져오기 */
        String url = "SelectMySchedule.php";
        String Googleid = user.getData(0);

        SendToDB stDB = new SendToDB(url, Googleid);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);

        JsonMaster jm = new JsonMaster();
        ///////////////////////////////////////////////EventDatas 계정에 따라 디비에서 가져오는 부분???
        //계정따라 가져오는 이벤트 데이터면 시작 종료일이 다른 이벤트 데이터도 들어있겠네
        //이 이벤트 데이터를 날짜별로 분류해서 caldatas에 넣어줘야겠네...
        jm.onPostExecute("SelectMySchedule", result);
        this.eventDatas = jm.getEvents();

        if (this.eventDatas != null) {
            for (int i = 0; i < eventDatas.size(); i++) {
                eventDatas.get(i).showData();
            }
        }

    }

    private String selectMyGroupTag(String sql) {
     /* DB에서 스케줄 태그가져오기 */
        String url = "SelectMyGroupTag.php";

        SendToDB stDB = new SendToDB(url, sql);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);

        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectMyGroupTag", result);
        return jm.getResult();
    }

    public void freshTag() {
      /* DB에서 스케줄 태그가져오기 */
        String url = "SelectMyTag.php";
        String Googleid = user.getData(0);

        SendToDB stDB = new SendToDB(url, Googleid);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);

        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectMyTag", result);
        this.tagDatas = jm.getTags();

    }

    public void freshGroup() {
      /* DB에서 내 그룹들 가져오기 */
        String url = "SelectMyGroup.php";
        String Googleid = this.user.getData(0);

        SendToDB stDB = new SendToDB(url, Googleid);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);

        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectMyGroup", result);
        this.groupDatas = jm.getGroups();

        if (this.groupDatas != null) {
            for (int i = 0; i < groupDatas.size(); i++) {
                //그룹멤버들의 이름을 Data엔set
                groupDatas.get(i).setUserIds_Arr();
            }
        }
    }

    public void freshMessage() {
        String url = "SelectMyMessage.php";
        String sql = "receiver='" + this.user.getData(0) + "'";

        SendToDB stDB = new SendToDB(url, sql);
        stDB.start();
        try {
            stDB.join();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);

        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectMyMessage", result);
        this.messages = jm.getMessages();
    }


    //모든 데이터 최신화
    public void initDB(String id, String pw) {
        //유저정보 갱신
        signinDB(id, pw);
        //스케줄 갱신
        freshMySchedule();
        //스케줄 태그 갱
        freshTag();
        //그룹 목록 갱신
        freshGroup();
        //요청 메세지 갱신
        freshMessage();
    }

    public void updateToken() {
        String url = "UpdateToken.php";
        String Googleid = this.getUser().getData(0);
        String Googlepw = this.getUser().getData(1);
        String id = "Googleid='" + Googleid + "' and Googlepw='" + Googlepw + "'";

        UpdateToDB stDB = new UpdateToDB(url, this.getToken(), id);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    public void DeleteScheduleJoin(String sid, String Googleid) {

        Log.d("DeleteSchedule", "sid=" + sid);

        String url = "DeleteScheduleSjoin.php";
        String message = "Googleid='" + Googleid + "' and Sid='" + sid + "'";

        Log.d("DeleteSchedule", "message=" + message);

        SendToDB stDB = new SendToDB(url, message);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);
    }

    public void DeleteMySchedule(String sid) {
        String url = "DeleteSchedule.php";
        String sql = "Sid='" + sid + "'";

        SendToDB stDB = new SendToDB(url, sql);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);
    }

    public void UpdateSchedule(String eventId, String eventTitle, String startTimedB, String endTimedB, String location) {
        String url = "UpdateSchedule.php";
        String message = "Sname='" + eventTitle + "',Place='" + location + "',StartTime='"
                + startTimedB + "',EndTime='" + endTimedB + "'";

        UpdateToDB stDB = new UpdateToDB(url, message, eventId);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);
    }

    public void UpdateScheduleJoin(String eventId, String tagId, String Googleid) {
        String url = "UpdateScheduleJoin.php";
        String message = "Tagid='" + tagId + "'";
        String search = "Googleid='" + Googleid + "'and Sid='" + eventId + "'";

        UpdateToDB stDB = new UpdateToDB(url, message, search);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mOnKeyBackPressedListener != null) { //백키 리스너 있을 경우
            mOnKeyBackPressedListener.onBack();
        } else {
            super.onBackPressed();
        }
    }


    /////
    public void setListItemInfo(int position) {


        setStr(smsList.get(position).get("item1") + "@@@@@@@" + smsList.get(position).get("item2")
                + "@,@,@,@" + position);
    }

    @Override
    protected void onDestroy() {
        // Always unregister when an object no longer should be on the bus.
        BusProvider.getInstance().unregister(this);
        super.onDestroy();

    }

    public void makegroup(String newGroupName, String newGroupIntro) {
        String id = this.getUser().getData(0);
        //그룹생성
        String sql = "'" + newGroupName + "','" + newGroupIntro + "','"
                + id + "'";
        InsertGroup(sql);

        //그룹에 가입
        String Gid = selectMaakeGroup(id, newGroupName);
        sql = "'" + id + "','" + Gid + "'";
        JoinGroup(sql);

        //그룹태그 생성
        sql = "'" + id + "','" + newGroupName + "','" + "#f6f9bd"
                                        + "','맑은고딕','15','" + Gid + "'";
        InsertGroupTag(sql);

        //새로고침하기
        freshGroup();
        freshTag();
    }

    public void InsertGroupTag(String sql) {
        String url = "InsertTag.php";

        SendToDB stDB = new SendToDB(url, sql);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("insertTag", "fail :" + e.toString());
        }
    }

    public void JoinGroup(String sql) {
        String url = "JoinGroup.php";

        SendToDB stDB = new SendToDB(url, sql);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    private String selectMaakeGroup(String id, String newGroupName) {
        String url = "SelectMakeGroup.php";
        String sql = "GMaster='" + id + "' and Group_name='" + newGroupName + "'";

        SendToDB stDB = new SendToDB(url, sql);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("insertTag", "fail :" + e.toString());
        }
        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectMakeGroup", stDB.getResult());

        return jm.getResult();
    }

    public void InsertGroup(String sql) {
        String url = "InsertGroup.php";

        SendToDB stDB = new SendToDB(url, sql);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("insertTag", "fail :" + e.toString());
        }
    }

    public void InsertMessage(String sql) {
        String url = "InsertMessage.php";

        SendToDB stDB = new SendToDB(url, sql);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("insertTag", "fail :" + e.toString());
        }
    }

    public void messageFromJoin(MessageData message) {
        String[] tok = message.getData(4).split("/");
        String SMaster = message.getData(1);
        String Place = tok[1];
        String StartTime = tok[2];
        String EndTime = tok[3];
        String Sname = tok[0];
        String Gid = message.getData(5);
        String receiver = message.getData(2);

        Log.d("joingroupnoti",SMaster + " " + Place+ " " +StartTime+ " " +
                EndTime+ " " +Sname+ " " +Gid+ " " +receiver);


        //구글용으로 시간 포멧 변경
        Calendar startCal = makeStringDateToGoogleTypeDate(StartTime);
        Calendar endCal = makeStringDateToGoogleTypeDate(EndTime);

        long startTime = startCal.getTimeInMillis();
        long endTime = endCal.getTimeInMillis();
        SimpleDateFormat sdftest = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
        String ss = sdftest.format(startCal.getTime());
        String ss2 = sdftest.format(endCal.getTime());
        Log.i("ottoL", "NOW startTime : " + ss);
        Log.i("ottoL", " NOW endTime : " + ss2);
        autoAddEventFromNoti = new EventData();

        //addEventToCalendarandGetEventID(long calID,long startMillis,
        // long endMillis,String scheduleEt, String locationEt)
        gmail = getAccount();
        calendarId = getCalID();
        //구글에 추가하고 구글 스케줄 아이디 받아오기
        autoAddEventFromNoti = addEventToCalendarandGetEventID(calendarId, startTime, endTime, Sname, Place);
        Log.i("ottoL", " NOW calendarId : " + calendarId + "gmail : " + gmail);
        Log.i("ottoL", "NOW autoAddEventFromNoti : " + autoAddEventFromNoti.toString());

        //방금 삽입한 스케줄 아이디 가져오기
        String msg = "SMaster='" + SMaster +
                "'and SName='" + Sname + "'";
        Log.i("msgTEST", "select1 " + msg);
        String Sid = SelectInsertSchedule(msg);
        String Tagid = selectMyGroupTag("Googleid='" + receiver + "' and Gid='" + Gid + "'");

        Log.d("joingroupnoti",Sid);
        Log.d("joingroupnoti",Tagid);

        //Googldid, Sid, Tagid, Gid, GoogleSid
        msg = "'" + receiver + "','" + Sid + "','" + Tagid + "','" + Gid
                + "','" + autoAddEventFromNoti.getData(7) + "'";// event.getData(7):구글 이벤트 아이디 넣기
        Log.i("msgTEST", "join1 " + message);

        joinGroupSchedule(msg);
    }

    public void DeleteGroup(String gid) {
        String url = "DeleteGroup.php";

        SendToDB stDB = new SendToDB(url, gid);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);
    }


    //프래그먼트 백키 관련
    public interface onKeyBackPressedListener {
        public void onBack();
    }

    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
        Log.i("backKey", "setOnKeyBackPressedListener");
    }

    public String getAccount() {

        final int REQUEST_GET_ACCOUNT = 0;
        //안드로이드 계정 불러오기


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_GET_ACCOUNT);
        }
        Account[] accounts = AccountManager.get(MainActivity.this).getAccounts();
        Account account = null;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].type.equals("com.google")) {

                account = accounts[i];
                break;

            }


        }
        return account.name;


    }

    public long getCalID() {

        gmail = getAccount();


        String[] EVENT_PROJECTION = new String[]{
                //SQL의 SELECT 문에 해당. 열 리턴, null 사용시 모든 열 리턴
                CalendarContract.Calendars._ID,                           // 0

        };

        final int PROJECTION_ID_INDEX = 0;

        final int REQUEST_READ_CALENDAR_PERMISSION = 5;
        Cursor cur = null;
        ContentResolver cr = MainActivity.this.getContentResolver();
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (this, new String[]{Manifest.permission.READ_CALENDAR}
                            , REQUEST_READ_CALENDAR_PERMISSION);
        }
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        /*String displayName = null;
        String accountName = null;
        String ownerName = null;*/
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


    }

    public EventData addEventToCalendarandGetEventID(long calID, long startMillis, long endMillis, String scheduleEt, String locationEt) {


        final int REQUEST_WRITE_CALENDAR_PERMISSION = 1;
        ContentResolver insertCr = MainActivity.this.getContentResolver();
        ContentValues values = new ContentValues();


        //values.put(Events._ID,"123");
        if (startMillis == 0 || endMillis == 0 || scheduleEt.length() == 0
                || locationEt.length() == 0
                ) {
            //스케줄 등록 요건 중 하나라도 비어 있으면 eventData를 생성하지도 않고 이벤트를 구글 캘린더에 추가하지도 않는다.
            Log.d("ttt", "스케줄 등록 요건 불만족.");
            Log.d("ttt", "startMillis : " + startMillis + "endMillis : " + endMillis + "ScheduleEditText : " + scheduleEt +
                    "LocationEv : " + locationEt);
            return null;


        }
        //201705231535

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        //  schedeulEditTextStr = ScheduleEditText.getText().toString();
        values.put(CalendarContract.Events.TITLE, scheduleEt);
   /* if(SummaryScheduleEditText.getText().toString().length()==0){
        SummaryScheduleEditText.setText("");

    }*/
        /// values.put(Events.DESCRIPTION,   SummaryScheduleEditText.getText().toString());
        //calID = getCalID();

        values.put(CalendarContract.Events.CALENDAR_ID, calID);


        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Seoul");
        values.put(CalendarContract.Events.EVENT_LOCATION, locationEt);
        EventData insertEventData = new EventData();


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (MainActivity.this, new String[]{Manifest.permission.READ_CALENDAR}
                            , REQUEST_WRITE_CALENDAR_PERMISSION);
        }

        Uri insertUri = insertCr.insert(CalendarContract.Events.CONTENT_URI, values);
        eventId = insertUri.getLastPathSegment();
        Log.i("ottoL", "gmail : " + gmail + " CalID : " + calID + "EVENTID: " + eventId);


        //스케줄 데이터 객체에 넣고 이벤트 아이디도 EventData 객체에 넣는다.
        //insertEventData.setEventId(eventId);
        insertEventData.setData(7, eventId);
        insertEventData.setData(2, scheduleEt);
        insertEventData.setData(3, locationEt);
        //String.valueOf --->long형의 시간을 String 타입으로 바꿔서 EventData객체에 저장한다.
        insertEventData.setData(4, getYMDHM(startMillis));
        insertEventData.setData(5, getYMDHM(endMillis));


        //이벤트 시작시간 (long형)에서 년월일만 뽑아서 년월일이 일치하는 CalData의 EventDataList에 스케줄을 대입한다.
        //String clanSelectedDate = selectedDate.replaceAll("[^0-9]", "");

        //eventData객체를 MonthCalendarFragment로 전달해서서  CalData의 eventDataList에 추가해야 함

        // Toast.makeText(getActivity(),"eventData : "+insertEventData.getTitle(),Toast.LENGTH_SHORT).show();
        /*Toast.makeText(MainActivity.this, "eventId: " + insertEventData.getData(7) + " eventData : " +
                insertEventData.getData(2), Toast.LENGTH_SHORT).show();*/

        Toast.makeText(MainActivity.this, "스케줄이 추가되었습니다!", Toast.LENGTH_SHORT).show();
        //

        return insertEventData;


    }

    public String getYMDHM(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Log.e("AAA", "바꾼 시간: " + sdf.format(time));
        return sdf.format(time);
    }

    public void deleteGoogleCalendar(String googleSid) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(googleSid));
        Log.i("DELETE", "GoogleSid " + Long.parseLong(googleSid));

        int rows = getContentResolver().delete(deleteUri, null, null);
        Log.i("DELETE", "Rows deleted: " + rows);

    }

    public void updateGoogleCalendar(String googleSid, String eventTitle, String location, long startTimeGoogle,
                                     long endTimeGoogle) {

        Uri updateUri = null;
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, eventTitle);
        values.put(CalendarContract.Events.EVENT_LOCATION, location);
        values.put(CalendarContract.Events.DTSTART, startTimeGoogle);
        values.put(CalendarContract.Events.DTEND, endTimeGoogle);
        Log.d("tata", " googleSid : " + googleSid);
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(googleSid));

        int rows = getContentResolver().update(updateUri, values, null, null);
        Log.d("msgTEST", " 구글수정함수 tata Rows updated : " + rows);
        Log.d("msgTEST", "구글수정함수 수정 후  " + "eventTitle : " + eventTitle + "startTimeGoogle : " + startTimeGoogle + " endTimeGoogle : " +
                endTimeGoogle + " location : " + location);


    }

    public void SetFirebaseListener() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    //네비게이션 드로어 코드
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        ChangeFragment(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    public void ChangeFragment(int id) {
        Fragment cf = null;
        switch (id) {
            case R.id.calendars:
                //calDatas MonthCalendarFragment 로 전달함.
                cf = new MonthCalendarFragment().newInstance();
                break;
            case R.id.Community:
                cf = new FriendGroupFragment().newInstance();
                break;
            case R.id.memberBtn:
                break;
            case R.id.scheduleBtn:
                break;
            case R.id.ScheduleMSG:
                cf = new MsgRecognitionFragment().newInstance();
                break;
            case R.id.Logout:
                cf = new LoginFragment().newInstance();
                //로그인 창으로 이동하게 만들기
                break;
        }
        if (cf != null) {
            Log.d("ddd", "setfm");
            fragment = cf;
            fm.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void ChangeFragment(Fragment cf) {
        if (cf != null) {
            Log.d("ddd", "setfm");
            fragment = cf;
            fm.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }


    public void deleteListItem(String info0, String info1, String position) {

        String SMS = pref.getString("SMS", "");
        String a = info1.replace("@,@,@,@" + position, "");
        Log.d("ssh", SMS);
        SMS = SMS.replace("@@@@@@@" + info0 + "@,@,@,@" + a, "");
        Log.d("ssh", "@@@@@@@" + info0 + "@,@,@,@" + a);
        Log.d("ssh", SMS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SMS", SMS);
        editor.commit();


    }

    public void deleteSelcetedListItem(int position) {

        smsList.remove(position);
        Log.d("ssh", "position : " + position);

        //ListViewSMS.clearChoices();
        //리스트뷰 삭제가 왜 바로 반영이 안되지.....

        adapter.notifyDataSetChanged();
        //ListViewSMS.setAdapter(adapter);
    }

    public String toGroupListFragment() {
        return "group";
    }

    public String toRequestListFragment() {
        return "request";

    }

    public void serviceOn(){
        Intent intent = new Intent(MainActivity.this,MyService.class);
       startService(intent);

    }

    public void serviceOff(){
        Intent intent = new Intent(MainActivity.this,MyService.class);
        stopService(intent);

    }
    //String 형식 년월일시분 Calendar 형식으로 만들기
    public Calendar makeGoogleTimeFormat(String yyyyMMdd,String hour, String min){
        String year = yyyyMMdd.substring(0,4);
        String month = yyyyMMdd.substring(4,6);
        int monthInt = Integer.parseInt(month);
        String date = yyyyMMdd.substring(6,8);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR,Integer.parseInt(year));
        c.set(Calendar.MONTH,(monthInt-1));//month는 꼭 -1해줘서 set해야 함.
        c.set(Calendar.DATE,Integer.parseInt(date));
        c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hour));
        c.set(Calendar.MINUTE,Integer.parseInt(min));

        return  c;

    }
}
