package com.bignerdranch.android.calendar3s.Setting;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleManager;


public class SettingFragment extends Fragment implements MainActivity.onKeyBackPressedListener{
    private ScheduleManager SM;
    private LinearLayout Alarm,MyC,OtherC;

    public SettingFragment newInstance(){
        SettingFragment fragment = new SettingFragment();
        //SM을 부모로부터 받아올것
        return fragment;
    }

    public SettingFragment() {
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
        View v = inflater.inflate(R.layout.fragment_setting,container,false);

        Alarm = (LinearLayout)v.findViewById(R.id.alarmSetting);
        Alarm.setOnClickListener(SettingClickListener);

        MyC = (LinearLayout)v.findViewById(R.id.myCalendar);
        MyC.setOnClickListener(SettingClickListener);

        OtherC = (LinearLayout)v.findViewById(R.id.otherCalendar);
        OtherC.setOnClickListener(SettingClickListener);

        return v;
    }

    View.OnClickListener SettingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Log.i("settingLog","getId() = " + id);
            ((MainActivity)getActivity()).ChangeFragment(id);
        }
    };

    @Override
    public void onBack() {
        // if (mWebView.canGoBack()) { //다른 조건이 있을 경우에 사용하기
        //     mWebView.goBack();
        // } else { }
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
        Log.i("backKey","Setting onAttach");
    }

}
