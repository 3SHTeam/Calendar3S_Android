package com.bignerdranch.android.calendar3s.Group;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleManager;


public class FriendGroupFragment extends Fragment {
    private ScheduleManager SM;

    private FragmentTabHost tabHost;

    public FriendGroupFragment newInstance(){
        FriendGroupFragment fragment = new FriendGroupFragment();
        //SM을 부모로부터 받아올것
        return fragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        SM = new ScheduleManager();//메인에서 받아오는 것으로 변경
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend_group,container,false);

        tabHost = (FragmentTabHost)v.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(),getChildFragmentManager(),android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("Friend List").setIndicator("Friend List"),
                FriendListFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("Group List").setIndicator("Group List"),
                GroupListFragment.class,null);
        ///0325
        tabHost.addTab(tabHost.newTabSpec("Request List").setIndicator("Request List"),
                RequestFragment.class,null);


        return v;
    }

}
