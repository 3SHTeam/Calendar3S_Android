package com.bignerdranch.android.calendar3s.Schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.calendar3s.R;

/**
 * Created by ieem5 on 2017-05-12.
 */

public class ModifyScheduleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_modify_schedule,null);



   /*
   setData(0,Sid);// 1.이벤트 아이디,
        setData(1,SMaster);
        setData(2,Sname);// 2. 이벤트 이름,
        setData(3,Place);// 6.이벤트 장소
        setData(4,StartTime);// 4.이벤트 시작시간
        setData(5,EndTime); // 5. 이벤트 종료시간
        setData(6,TagId);
        setData(7,GoogleSId);
        setData(8,GId);
        */


   // 출력은 하지 않지만 불러와야 하는 것:이벤트 아이디, 태그 아이디 등
        //출력할 것은 이벤트 이름 이벤트 장소 이벤트 시작 ,종료시간

        return  v;
    }
}
