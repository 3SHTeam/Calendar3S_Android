package com.bignerdranch.android.calendar3s.Group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.calendar3s.Calendar.MonthCalendarFragment;
import com.bignerdranch.android.calendar3s.Dialog.DatePickerFragment;
import com.bignerdranch.android.calendar3s.Dialog.TimePickerFragment;
import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.AddScheduleDialogFragment;
import com.bignerdranch.android.calendar3s.Schedule.DailyScheduleViewFragment;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleTagListViewAdapterNoCBox;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleTagListViewItemNoCBox;
import com.bignerdranch.android.calendar3s.data.EventData;
import com.bignerdranch.android.calendar3s.data.GroupData;
import com.bignerdranch.android.calendar3s.data.TagData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ieem5 on 2017-05-28.
 */

public class GroupScheduleFragment extends Fragment implements MainActivity.onKeyBackPressedListener{



    private static final int REQUEST_DATE = 0;
    public static final int REQUEST_START_TIME = 1;
    public static final int REQUEST_FINISH_TIME = 2;
    private static final String DIALOG_DATE="DIALOG_DATE";
    private static final String  DIALOG_START_TIME="startTime";
    private static final String  DIALOG_FINISH_TIME="finishTime";


    private GroupData groupData;

    public GroupData getGroupData() {
        return groupData;
    }

    public void setGroupData(GroupData groupData) {
        this.groupData = groupData;
    }




    private ScheduleTagListViewAdapterNoCBox scheduleTagListViewAdapter ;
    private LinearLayout tagListLinearLayout;
    //태그 이름 표시할 리스트 뷰
    private ListView scheduleTagNameListView ;


    //선택된 태그 이름
    private String selecetedTagName;
    //선택된 태그 id
    private String selecetedTagId;
    private TextView selecetedTagNameTv;
    private Button selectTagOkBtn;
    private MainActivity main;
    private EditText scheduleNameEt;
    private ArrayList<TagData>tagDatas ;
    private  Button tagSelectBtn;
    private Button dateSelectBtn;
    private TextView dateTv;
    private Button startTimeBtn;
    private TextView startTimeTv;
    private  Button endTimeBtn;
    private TextView endTimeTv;
    private  EditText locationEt;
    private Button showMapBtn;
    private  Button addBtn;


    String dateOnlyNumber="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = ((MainActivity)getActivity());
          tagDatas  = main.getTagDatas();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_group_schedule, container, false);

        tagListLinearLayout = (LinearLayout)v.findViewById(R.id.tagListViewLayoutInGroupSche);
        selecetedTagNameTv = (TextView)v.findViewById(R.id.selectedTagTv);
        scheduleTagNameListView = (ListView)v.findViewById(R.id.tagListViewInGroupSche);
       // tagListView.setAdapter();
        scheduleNameEt = (EditText)v.findViewById(R.id.scheduleEditText);
        tagSelectBtn= (Button)v.findViewById(R.id.selectTagBtn);
        selectTagOkBtn = (Button)v.findViewById(R.id.selectTagOkBtnInGroupSche);
          dateSelectBtn= (Button)v.findViewById(R.id.dateBtn);
        dateTv = (TextView)v.findViewById(R.id.dateTv);
          startTimeBtn= (Button)v.findViewById(R.id.startTimeBtn);
        startTimeTv= (TextView)v.findViewById(R.id.startTimeTv);
        endTimeBtn= (Button)v.findViewById(R.id.finishTimeBtn);
        endTimeTv = (TextView)v.findViewById(R.id.finishTimeTv);
        locationEt= (EditText)v.findViewById(R.id.LocationEv);
          showMapBtn= (Button)v.findViewById(R.id.showMapBtn);
           addBtn= (Button)v.findViewById(R.id.addBtnInGroupSche);





        int len  =  tagDatas.size();

        scheduleTagListViewAdapter =new ScheduleTagListViewAdapterNoCBox();
        //String tagId,String tagTitle, String tagColor,String groupId,boolean b
        for(int i=0;i<len;i++) {
            scheduleTagListViewAdapter.addItem(tagDatas.get(i).getData(0), tagDatas.get(i).getData(1), tagDatas.get(i).getData(2),
                    tagDatas.get(i).getData(5), tagDatas.get(i).isCheckbox());
        }

        scheduleTagListViewAdapter.notifyDataSetChanged();

        scheduleTagNameListView.setAdapter(scheduleTagListViewAdapter);
        // GroupMemberListView = (ListView) v.findViewById(R.id.groupMemberListView);

        scheduleTagNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                //  selecetedTagName = (String)listView.getItemAtPosition(position);

                ScheduleTagListViewItemNoCBox item =   (ScheduleTagListViewItemNoCBox) listView.getItemAtPosition(position);
                selecetedTagName = item.getTagTitle();
                Log.d("TAGTAG","selecetedTagName : "+selecetedTagName);
                //사용자가 태그를 선택하지 않았으면 기본 태그로 설정

                for( int i=0;i<tagDatas.size();i++){
                    if(tagDatas.get(i).getData(1).equals(selecetedTagName)){
                        //태그이름과 일치하는 태그 아이디 저장

                        selecetedTagId = tagDatas.get(i).getData(0);
                        //아이디를 MAIN으로 보내야한다!!!
                        Log.d("TAGTAG","selecetedTagId : "+selecetedTagId);
                        break;
                    }
                }//end of for
            }


        });


        //태그 선택하고 확인 누르면 리스트뷰 다시 안 보이게
        selectTagOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tagListLinearLayout.setVisibility(View.GONE);

                //선택된 리스트뷰이 이름으로 텍스트뷰 세팅하고 아이디 출력해야 한다.
                selecetedTagNameTv.setText(selecetedTagName);
                Toast.makeText(getActivity(),"tagName : "+selecetedTagName+" , "+
                        "tagId : "+selecetedTagId,Toast.LENGTH_SHORT).show();


            }


        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tagSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagListLinearLayout.setVisibility(View.VISIBLE);

            }
        });
        //날짜 선택 버튼
        dateSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Date date = new Date();
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(GroupScheduleFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        //시작 시간 TimePicker에서 고르기
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Date  date = new Date();
                FragmentManager manager = getFragmentManager();
                // TimePickerFragment startTimeDialog =  new TimePickerFragment();
                TimePickerFragment startTimeDialog = TimePickerFragment.newInstance(date);
                startTimeDialog.setTargetFragment(GroupScheduleFragment.this,REQUEST_START_TIME);
                startTimeDialog.show(manager,DIALOG_START_TIME);

            }
        });

        // 종료 시간 TimePicker에서 고르기
        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date  date = new Date();
                FragmentManager manager = getFragmentManager();
                // TimePickerFragment finishTimeDialog = new TimePickerFragment();
                TimePickerFragment finishTimeDialog = TimePickerFragment.newInstance(date);
                finishTimeDialog.setTargetFragment(GroupScheduleFragment.this,REQUEST_FINISH_TIME);
                finishTimeDialog.show(manager,DIALOG_FINISH_TIME);

            }
        });
        //추가 버튼. 그룹 스케줄 요청 메시지 보내기
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //사용자가 입력한 값을 가져오기
                String scheduleName = scheduleNameEt.getText().toString();
                String date = dateTv.getText().toString();
                String etstartTime =  startTimeTv.getText().toString();
                String etendTime = endTimeTv.getText().toString();
                String location= locationEt.getText().toString();

                dateOnlyNumber = date.replaceAll("[^0-9]", "");
                Log.d("TAGTAG","dateOnlyNumber : "+dateOnlyNumber);
                String startTimeOnlyNumber =etstartTime.replaceAll("[^0-9]", "");
                String endTimeOnlyNumber =etendTime.replaceAll("[^0-9]", "");

                String startTime = dateOnlyNumber+startTimeOnlyNumber;
                String endTime =dateOnlyNumber+endTimeOnlyNumber;


                //내부에서 추가해오기
                String type = "groupSchedule";
                String sender = main.getUser().getData(0);
                String sql="";
                String Gid = getGroupData().getData(0);

                //스케줄을 등록하고 참여

                sql = "'" + sender + "','" + scheduleName + "','" + location + "','"
                        + startTime + "','" + endTime + "'";
                main.insertScheduleDB(sql);//DB스케줄 삽입 성공


                //구글용으로 시간 포멧 변경
                Calendar startCal = main.makeStringDateToGoogleTypeDate(startTime);
                Calendar endCal = main.makeStringDateToGoogleTypeDate(endTime);

                long gstartTime = startCal.getTimeInMillis();
                long gendTime = endCal.getTimeInMillis();
                SimpleDateFormat sdftest = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
                String ss = sdftest.format(startCal.getTime());
                String ss2 = sdftest.format(endCal.getTime());
                Log.i("ottoL", "NOW startTime : " + ss);
                Log.i("ottoL", " NOW endTime : " + ss2);
                EventData autoAddEventFromNoti = new EventData();

                //addEventToCalendarandGetEventID(long calID,long startMillis,
                // long endMillis,String scheduleEt, String locationEt)
                String gmail = main.getAccount();
                long calendarId = main.getCalID();
                //구글에 추가하고 구글 스케줄 아이디 받아오기
                autoAddEventFromNoti = main.addEventToCalendarandGetEventID(calendarId, gstartTime, gendTime, scheduleName, location);
                Log.i("ottoL", " NOW calendarId : " + calendarId + "gmail : " + gmail);
                Log.i("ottoL", "NOW autoAddEventFromNoti : " + autoAddEventFromNoti.toString());

                //방금 삽입한 스케줄 아이디 가져오기
                String message = "SMaster='" + sender +
                        "'and SName='" + scheduleName + "'";
                Log.i("msgTEST", "select1 " + message);
                String Sid = main.SelectInsertSchedule(message);
                //Googldid, Sid, Tagid, Gid, GoogleSid
                message = "'" + sender + "','" + Sid + "','" + selecetedTagId + "','" + Gid
                        + "','" + autoAddEventFromNoti.getData(7) + "'";// event.getData(7):구글 이벤트 아이디 넣기
                Log.i("msgTEST", "join1 " + message);

                main.joinGroupSchedule(message);


                //그룹 스케줄 참가 메세지를 전송

                //스케줄 정보를 메세지로 묶기
                String msg = scheduleName+"/"+location+"/"+startTime+
                        "/"+endTime;


                Log.d("sendMessage",getGroupData().getUserIds_Arr().toString());

                for(int i=0;i<getGroupData().getUserIds_Arr().size();i++){
                    if(!getGroupData().getUserIds_Arr().get(i).equals(sender)){
                        sql =
                                "'"+ sender + "'," +
                                        "'"+ getGroupData().getUserIds_Arr().get(i) + "'," +
                                        "'"+ type + "'," +
                                        "'"+ msg + "'," +
                                        "'"+ getGroupData().getData(0) + "'," +
                                        "'"+ getGroupData().getData(1) + "'";
                        Log.d("sendMessage",sql);
                        main.InsertMessage(sql);
                        main.ChangeFragment(R.id.Community);
                    }
                }


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


            dateTv.setText(getDataString);



        }
        if (requestCode == REQUEST_START_TIME) {

            int  hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
            int  minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);

            String hourStr=""+hour;
            String minStr =""+minute;
            if(hour < 10) hourStr = "0"+hour;
            if( minute<10)minStr = "0"+minute;
            startTimeTv.setText(hourStr+" : "+minStr+" ");




        }
        if (requestCode == REQUEST_FINISH_TIME) {

            int  hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
            int  minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);

            String hourStr=""+hour;
            String minStr =""+minute;
            if(hour < 10) hourStr = "0"+hour;
            if( minute<10)minStr = "0"+minute;
            endTimeTv.setText(hourStr+" : "+minStr+" ");

        }
    }

    //그룹를 실행시킨다.
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.ChangeFragment(R.id.Community);
    }

    //자신의 백키를 불러오게 등록
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
    }
}
