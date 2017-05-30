package com.bignerdranch.android.calendar3s.Group;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleManager;


public class FriendGroupFragment extends Fragment implements MainActivity.onKeyBackPressedListener{

    private FragmentTabHost tabHost;

    public FriendGroupFragment newInstance(){
        FriendGroupFragment fragment = new FriendGroupFragment();
        //SM을 부모로부터 받아올것
        return fragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend_group,container,false);

        tabHost = (FragmentTabHost)v.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(),getChildFragmentManager(),android.R.id.tabcontent);

//        tabHost.addTab(tabHost.newTabSpec("Friend List").setIndicator("Friend List"),
//                FriendListFragment.class,null);

        tabHost.addTab(tabHost.newTabSpec("Group List").setIndicator("Group List"),
                GroupListFragment.class,null);
        ///0325
        tabHost.addTab(tabHost.newTabSpec("Request List").setIndicator("Request List"),
                RequestFragment.class,null);


        return v;
    }

    //달력화면으로 돌아간다.
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
