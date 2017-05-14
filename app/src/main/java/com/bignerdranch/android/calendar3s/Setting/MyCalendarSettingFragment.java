package com.bignerdranch.android.calendar3s.Setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleManager;

public class MyCalendarSettingFragment extends Fragment implements MainActivity.onKeyBackPressedListener{
    private ScheduleManager SM;

    public MyCalendarSettingFragment newInstance(){
        MyCalendarSettingFragment fragment = new MyCalendarSettingFragment();
        //SM을 부모로부터 받아올것
        return fragment;
    }

    public MyCalendarSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        SM = new ScheduleManager();//메인에서 받아오는 것으로 변경
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_calendar,container,false);

        return v;
    }

    @Override
    public void onBack() {
        // if (mWebView.canGoBack()) { //다른 조건이 있을 경우에 사용하기
        //     mWebView.goBack();
        // } else { }
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.ChangeFragment(R.id.Settings);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
        Log.i("backKey","MyCalendar onAttach");
    }

}
