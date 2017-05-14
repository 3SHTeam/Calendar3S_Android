package com.bignerdranch.android.calendar3s;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import com.bignerdranch.android.calendar3s.Calendar.MonthCalendarFragment;
import com.bignerdranch.android.calendar3s.Group.FriendGroupFragment;
import com.bignerdranch.android.calendar3s.Login.LoginFragment;
import com.bignerdranch.android.calendar3s.Schedule.AddScheduleDialogFragment;
import com.bignerdranch.android.calendar3s.Setting.AlarmSettingFragment;
import com.bignerdranch.android.calendar3s.Setting.MyCalendarSettingFragment;
import com.bignerdranch.android.calendar3s.Setting.OtherCalendarSettingFragment;
import com.bignerdranch.android.calendar3s.Setting.SettingFragment;

import com.bignerdranch.android.calendar3s.data.EventData;
import com.bignerdranch.android.calendar3s.data.GroupData;
import com.bignerdranch.android.calendar3s.data.TagData;
import com.bignerdranch.android.calendar3s.data.UserData;
import com.bignerdranch.android.calendar3s.database.JsonMaster;
import com.bignerdranch.android.calendar3s.database.SendToDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AddScheduleDialogFragment.PassEventDataToMainListener {
    public static final String EXTRA_CALDATA="com.bignerdranch.android.a3scal.calData_id";

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

    ToggleButton MWTBtn;



    int startday;  //달력 시작 요일
    int endday;//달력 마지막 요일
    public int getStartday() {
        return startday;
    }
    public int getEndday() {
        return endday;
    }

    //이벤트 객체 - 이벤트 아이디, 타이틀 ,시작 종료 시간, 장소 등
    private ArrayList<EventData>eventDatas = new ArrayList<>();
    public ArrayList<EventData> getEventDatas() {return eventDatas;}
    public void setEventDatas(ArrayList<EventData> eventDatas) {this.eventDatas = eventDatas;}

    //태그 데이터
    private ArrayList<TagData> tagDatas = new ArrayList<>();
    public ArrayList<TagData> getTagDatas() {return tagDatas;}
    public void setTagDatas(ArrayList<TagData> tagDatas) {this.tagDatas = tagDatas;
        for(int i = 0; i< tagDatas.size();i++)
            tagDatas.get(i).showData();
    }

    private HashMap<String,Boolean> tagHash = null;
    public HashMap<String, Boolean> getTagHash() {return tagHash;}
    public void setTagHash(HashMap<String,Boolean> tagHash) {this.tagHash = tagHash;}

    //그룹 데이터
    private ArrayList<GroupData> groupDatas = new ArrayList<>();
    public ArrayList<GroupData> getGroupDatas() {return groupDatas;}
    public void setGroupDatas(ArrayList<GroupData> groupDatas) {this.groupDatas = groupDatas;}


    //캘린더 객체. 메인은 MonthCalendarFragment가 몇월을 뿌리고 있는지 알고 있다.
    //DatePicker로 날짜 이동시에도 마찬가지이다.
    private Calendar calendar = Calendar.getInstance();
    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
    }
    public Calendar getCalendar() {
        return calendar;
    }

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity FireBase";

    //UserData
    private UserData user;

    public UserData getUser() {return user;}
    public void setUser(UserData user) {this.user = user;}


    //AddSCheduleFragment에서 스케줄 추가할 때 받은 정보로 만든 EventData
    EventData receivedNewEventData;
    public EventData getReceivedNewEventData() { return receivedNewEventData;   }

    public void setReceivedNewEventData(EventData receivedNewEventData) {  this.receivedNewEventData = receivedNewEventData; }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        Log.d("firebase","FirebaseAuth getInstance");

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

        calendar = Calendar.getInstance();





        Root = this;

        // 권한 요청
      if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.READ_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED    || ContextCompat.checkSelfPermission(
               this, android.Manifest.permission.WRITE_CALENDAR) !=
               PackageManager.PERMISSION_GRANTED ||
               ContextCompat.checkSelfPermission(
              this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
              PackageManager.PERMISSION_GRANTED||
        ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
              ContextCompat.checkSelfPermission(
                      this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                      PackageManager.PERMISSION_GRANTED

              ) {
          ActivityCompat.requestPermissions
                   ( this,
                           new String[]{
                                   android.Manifest.permission.READ_CALENDAR,
                                   android.Manifest.permission.WRITE_CALENDAR,
                                   android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                   android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                   android.Manifest.permission.ACCESS_FINE_LOCATION
                           }
                            , REQUEST_READ_CALENDAR_PERMISSION);

       }






        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.container);
        if(fragment==null){
            fragment = new LoginFragment().newInstance();
            fm.beginTransaction().replace(R.id.container,fragment).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    public void ChangeFragment(int id){
        Fragment cf = null;
        switch (id){
            case R.id.calendars:
               //calDatas MonthCalendarFragment 로 전달함.
              cf = new MonthCalendarFragment().newInstance();

                Log.d("ddd","Calendars");
                break;
            case R.id.Community:
                cf = new FriendGroupFragment().newInstance();
                Log.d("ddd","WeekFragment");
                break;
            case R.id.Settings:
 cf = new SettingFragment().newInstance();
               break;
          case R.id.Logout:
               cf = new LoginFragment().newInstance();
                //로그인 창으로 이동하게 만들기
                Log.d("ddd","Logout");
                break;case R.id.alarmSetting:
              cf = new AlarmSettingFragment().newInstance();
                Log.d("ddd","AlarmSettingFragment");
              break;
            case R.id.myCalendar:
               cf = new MyCalendarSettingFragment().newInstance();
               Log.d("ddd","MyCalendarSettingFragment");
                break;
           case R.id.otherCalendar:
                cf = new OtherCalendarSettingFragment().newInstance();
                Log.d("ddd","OtherCalendarSettingFragment");
               break;
        }
        if(cf!=null) {
            Log.d("ddd","setfm");
            fragment = cf;
            fm.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void ChangeActivity(View v){
        Intent intent = null;
        switch (v.getId()){
            default:
                //case R.id.alarmSetting:
                //intent = new Intent(MainActivity.this,MainActivity.class);
                //   break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }


    //AddScheduleDialogFragment로부터 추가한 스케줄 정보를 EventData에 필요한 String객체로 가져온다.
    @Override
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

    }

    private String SelectInsertSchedule(String message) {
        String url = "SelectSchedule.php";
        SendToDB stDB = new SendToDB(url,message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("signinDB", "fail :" +e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        Log.d("signinDB","json result : " + result);

        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectInsertSchedule", result);

        return jm.getResult();
    }

    private void insertScheduleDB(String message) {
        String url = "InsertSchedule.php";

        SendToDB stDB = new SendToDB(url,message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("signinDB", "fail :" +e.toString());
        }
    }

    private void joinScheduleDB(String message) {
        String url = "JoinSchedule.php";
        SendToDB stDB = new SendToDB(url,message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("signinDB", "fail :" +e.toString());
        }
    }

    public void signupFireBase(String googleId, String googlePw){
    Log.d("FirebaseSignup","email,pw : "+ googleId + ", " + googlePw);

        mAuth.createUserWithEmailAndPassword(googleId, googlePw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {//회원가입 실패
                            Log.d("FirebaseSignup","Failed");
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
        String message = "where Googleid = '" + id + "' and Googlepw = '" + pw +"'";
        String url = "SelectMyProfile.php";

        SendToDB stDB = new SendToDB(url,message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("signinDB", "fail :" +e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        Log.d("signinDB","json result : " + result);

        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectMyProfile", result);

        this.user = jm.getUser();
        this.user.showData();
    }


    public String getToken(){return FirebaseInstanceId.getInstance().getToken();}

    public String getFBid() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            return uid;
        }
        return null;
    }

    public void InsertTag(String tagname, String color){
        String message = "'" + user.getData(0) + "','" + tagname + "','" + color
                +"','맑은고딕','15'";
        String url = "InsertTag.php";

        SendToDB stDB = new SendToDB(url,message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("insertTag", "fail :" +e.toString());
        }

        freshTag();
    }



    private void freshMySchedule() {
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

        if(this.eventDatas != null) {
            for (int i = 0; i < eventDatas.size(); i++) {
                eventDatas.get(i).showData();
            }
        }

    }

    private void freshTag() {
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

    private void freshGroup() {
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

        if(this.groupDatas != null) {
            for (int i = 0; i < groupDatas.size(); i++) {
                //그룹멤버들의 이름을 Data엔set
                groupDatas.get(i).setUserIds_Arr();
            }
        }
    }


    //모든 데이터 최신화
    public void initDB(String id, String pw) {
        //유저정보 갱신
        signinDB(id,pw);
        //스케줄 갱신
        freshMySchedule();
        //스케줄 태그 갱신
        freshTag();
        //그룹 목록 갱신
        freshGroup();
    }

    //프래그먼트 백키 관련
    public interface onKeyBackPressedListener {
        public void onBack();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
        Log.i("backKey","setOnKeyBackPressedListener");
    }


}
