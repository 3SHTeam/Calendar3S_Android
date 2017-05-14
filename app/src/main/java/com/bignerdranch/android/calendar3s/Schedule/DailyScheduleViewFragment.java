package com.bignerdranch.android.calendar3s.Schedule;

import android.gesture.GestureLibrary;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.data.EventData;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ieem5 on 2017-05-12.
 */

public class DailyScheduleViewFragment extends Fragment {



    //ClickListFragment 로부터 선택한 하루 이벤트 데이터 받는다.
    private EventData eventData;



    //ClickListFragment calendar 객체 전달 받는다.
    private Calendar calendar;


    private EditText ScheduleEditText;//스케줄명 입력

    private Button startTimeBtn;//시작 시간
    private Button finishTimeBtn;// 종료 시간
    private Button showMapBtn;//지도보기 버튼
    private EditText LocationEv;

    private Calendar selectedCal;//선택된 날짜 객체
    private  String selectedDate; // '2017년 1월 17일 일정'
    private TextView addScheduleDateTv;
    private TextView startTimeTV;
    private  TextView finishTimeTv;
    private  Button addScheduleBtn;


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


        ScheduleEditText = (EditText)v.findViewById(R.id.scheduleEditText);

        startTimeBtn= (Button)v.findViewById(R.id.startTimeBtn);
        finishTimeBtn =(Button)v.findViewById(R.id.finishTimeBtn);
        showMapBtn =(Button)v.findViewById(R.id.showMapBtn);
        LocationEv = (EditText)v.findViewById(R.id.LocationEv);

        startTimeTV=(TextView) v.findViewById(R.id.startTimeTv);
        finishTimeTv=(TextView) v.findViewById(R.id.finishTimeTv);
        addScheduleBtn = (Button)v.findViewById(R.id.addBtn);



       /* selectTagBtn = (Button)v.findViewById(R.id.selectTagBtn);
        selectTagOKBtn = (Button)v.findViewById(R.id.selectTagOkBtn);
        scheduleTagNameListView = (ListView)v.findViewById(R.id.tagListView);
        tagListLayout = (LinearLayout)v.findViewById(R.id.tagListViewLayout);
        selectedTagTv = (TextView)v.findViewById(R.id.selectedTagTv);*/


        ScheduleEditText.setText(getEventData().getData(2));
        startTimeTV.setText(getEventData().getData(4));
        finishTimeTv.setText(getEventData().getData(5));
        LocationEv.setText(getEventData().getData(3));

        return  v;
    }
}
