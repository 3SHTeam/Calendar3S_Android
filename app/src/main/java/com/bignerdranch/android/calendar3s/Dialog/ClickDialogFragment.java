package com.bignerdranch.android.calendar3s.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ProviderInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.data.EventData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Owner on 2017-02-07.
 */

public class ClickDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener{
    ListView listView;
    ArrayAdapter<String> dayScheduleAdapter;//ListView 세팅 어댑터
    View v;


    //이벤트 데이터 arrList 받아와서 저장해둘 변수
    private ArrayList<EventData> eventDataArrayList;


    public ArrayList<EventData> getEventDataArrayList() {
        return eventDataArrayList;
    }

    public void setEventDataArrayList(ArrayList<EventData> eventDataArrayList) {
        this.eventDataArrayList = eventDataArrayList;
    }

    public Calendar getSelectedCal() {
        return selectedCal;
    }

    public void setSelectedCal(Calendar selectedCal) {
        this.selectedCal = selectedCal;
    }

    //2017년 1월 17일 일정'   --> 선택한 날짜 받아와야 함.
    private Calendar selectedCal;//선택된 날짜 객체

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy년MM월dd일 일정");
        String selectedDate = s.format(selectedCal.getTime());

        //2017년 1월 17일 일정'   --> 선택한 날짜 받아와야 함.
        String dialogmentTitle = selectedDate;
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        v = layoutInflater.inflate(R.layout.dialog_click, null);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().setTitle(dialogmentTitle);


        return  v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int len = eventDataArrayList.size();
        final String[] eventDataTitleArr = new String[len];
        for (int i = 0; i < len; i++) {
            eventDataTitleArr[i] = eventDataArrayList.get(i).getData(2);
        }
        dayScheduleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventDataTitleArr);

        listView = (ListView)v.findViewById(R.id.DayScheduleList);
        listView.setAdapter(dayScheduleAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        Log.i("listClick",position+" 스케줄 클릭");
    }

 /*   @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy년MM월dd일 일정");
        String selectedDate = s.format(selectedCal.getTime());

        //2017년 1월 17일 일정'   --> 선택한 날짜 받아와야 함.
        String dialogmentTitle = selectedDate;

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        v = layoutInflater.inflate(R.layout.dialog_click, null);

        int len = eventDataArrayList.size();
        final String[] eventDataTitleArr = new String[len];
        for (int i = 0; i < len; i++) {
            eventDataTitleArr[i] = eventDataArrayList.get(i).getData(2);
        }
        dayScheduleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventDataTitleArr);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setTitle(dialogmentTitle)


                .setView(v);


        return builder.create();

    }*/


/*    @Override
    public void onStart() {
        super.onStart();
        listView = (ListView)v.findViewById(R.id.DayScheduleList);
        listView.setAdapter(dayScheduleAdapter);

        //listView.setSelector(new ColorDrawable(0x0000ff));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                *//*String eventTitle = (String)((ListView)parent).getItemAtPosition(position);
                Log.i("listClick",eventTitle+" 스케줄 클릭");*//*
                Log.i("listClick",position+" 스케줄 클릭");
            }
        });
    }*/


}