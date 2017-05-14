package com.bignerdranch.android.calendar3s.Group;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleManager;


public class FriendListFragment extends Fragment {
    private ScheduleManager SM;
    private ListView listView;

    public FriendListFragment newInstance(){
        FriendListFragment fragment = new FriendListFragment();
        //SM을 부모로부터 받아올것
        return fragment;
    }

    public FriendListFragment() {
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
        View v = inflater.inflate(R.layout.fragment_friend_list,container,false);

        return v;
    }

}
