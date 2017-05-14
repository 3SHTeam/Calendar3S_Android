package com.bignerdranch.android.calendar3s.Schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.data.EventData;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ieem5 on 2017-05-12.
 */

public class ClickListFragment extends Fragment {
    private  static final int   REQUEST_DAILY_SCHDULE = 0;

private  ListView listView;

private  ArrayAdapter<String>eventTitleAdapter;
    private Calendar calendar;
    private ArrayList<String>eventTitleList;
    private ArrayList<EventData> eventDataArrayList;

    private TextView dateTv;
    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
    public ArrayList<EventData> getEventDataArrayList() {
        return eventDataArrayList;
    }

    public void setEventDataArrayList(ArrayList<EventData> eventDataArrayList) {
        this.eventDataArrayList = eventDataArrayList;
    }
    String dateStr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_schedule_list,null);
        dateTv = (TextView)v.findViewById(R.id.dateTv);
        dateStr =  getCalendar().get(Calendar.YEAR)  +"년"+(getCalendar().get(Calendar.MONTH)+1)+"월"+
               getCalendar().get(Calendar.DATE)+"일 일정";
        Log.i("listClick",dateStr);
        //dateTv.setText(dateStr);
        eventTitleList = new ArrayList<>();
        int len = eventDataArrayList.size();
        //이벤트 제목만 따로 빼서 arrList 만든다.
        for(int i=0;i<len;i++){
            eventTitleList.add(eventDataArrayList.get(i).getData(2)) ;

        }
        for(int i=0;i<len;i++){

            Log.i("listClick","eventTitle : "+eventDataArrayList.get(i).getData(2)+"|"+
            "eventStartTIME : "+eventDataArrayList.get(i).getData(4)+"|");

        }
        for(int i=0;i<eventTitleList.size();i++){

            Log.i("listClick",eventTitleList.get(i)+"|");
        }

        eventTitleAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,eventTitleList);

        listView = (ListView)v.findViewById(R.id.listView);
        listView.setAdapter(eventTitleAdapter);
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dateTv.setText(dateStr);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String title = (String)(((ListView)parent).getItemAtPosition(position));
                showDailyEventDataDetail(title,getCalendar());
                Log.i("listClick","title : "+title+"  "+position+" is Clicked!");
            }
        });
    }

    public void showDailyEventDataDetail(String eventTitle,Calendar calendar){

        DailyScheduleViewFragment dailyScheduleViewFragment  = new DailyScheduleViewFragment();
        //하루 일정 리스트 전부다 보여주어야 하므로 날짜 정보 갖고 있는  calendar를 세팅
        dailyScheduleViewFragment.setCalendar(calendar);
       // Log.i("listClick",arrCalDataList.get(position).getTotalDate());
        //이벤트 타이틀 클릭하면 해당 이벤트 정보가 뿌려지는 화면이 나와서 수정 또는 삭제를 해야 하므로
        //이벤트 데이터 객체 세팅
                   /* clickListFragment.setEventDataArrayList( ((MainActivity)getActivity()).getEventDatas());*/
                   int idx = 0;
                   for(int i=0;i<getEventDataArrayList().size();i++){

                       if( eventTitle.equals(getEventDataArrayList().get(i).getData(2))){
                           idx = i;
                           break;
                       }
                   }


       //EventData  보내기
       dailyScheduleViewFragment.setEventData(getEventDataArrayList().get(idx));
        FragmentTransaction ft = getFragmentManager().beginTransaction().
                replace(R.id.container,dailyScheduleViewFragment);
        ft.addToBackStack(null);
        dailyScheduleViewFragment.
                setTargetFragment(getFragmentManager().findFragmentById(R.id.calendars),REQUEST_DAILY_SCHDULE);
        ft.commit();


    }
}
