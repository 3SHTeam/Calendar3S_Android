package com.bignerdranch.android.calendar3s.Group;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleManager;


public class GroupListFragment extends Fragment {
    private static final int REQUEST_NEW_GROUP = 1;
    private static final int REQUEST_NEW_GROUP_MEMBER = 2;
    private static final String REQUEST_SHOW_ADD_GROUP_DIALLOG = "showAddNewGroupDialogFragment";
    private static final String REQUEST_SHOW_ADD_GROUP_DIALLOG_MEMBER = "showAddNewGroupMemberDialogFragment";

    private     ScheduleManager SM;
    Button addGroupBtn;
    ListView groupListView;
    GroupListViewAdapter adapter;

    View mainView;

    public GroupListFragment newInstance() {
        GroupListFragment fragment = new GroupListFragment();
        //SM을 부모로부터 받아올것
        return fragment;
    }

    public GroupListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        SM = new ScheduleManager();//메인에서 받아오는 것으로 변경


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView= inflater.inflate(R.layout.fragment_group_list, container, false);



        return mainView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //여기서부터 ui변경 작업 가능
        super.onActivityCreated(savedInstanceState);
        adapter = new GroupListViewAdapter();
        adapter.addItem(R.drawable.group, "세모고", "Tomy외 5명");
        adapter.addItem(R.drawable.group, "초코방", "Choco외 4명");
        groupListView = (ListView)mainView.findViewById(R.id.groupListView);
        groupListView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        //Fragment가 화면에 완전히 그렸으며, 사용자의 Action과 상호 작용이 가능하다.
        super.onResume();
        addGroupBtn = (Button) mainView.findViewById(R.id.addGroupBtn);
        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Group추가 프래그먼트 띄우기

                AddNewGroupDialogFragment dialog = new AddNewGroupDialogFragment();
                dialog.setTargetFragment(GroupListFragment.this, REQUEST_NEW_GROUP);
                dialog.show(getFragmentManager(), REQUEST_SHOW_ADD_GROUP_DIALLOG);

            }
        });
    }

    public static class AddNewGroupDialogFragment extends DialogFragment {

        Button addGroupBtn;
         EditText groupNameEditText;
         EditText groupIntroEditText;
        //그룹 이름
         String groupName;
        //그룹 한 줄 소개
         String groupIntro;





        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.fragment_add_new_group,null);
            builder.setView(v);
            builder.setTitle("새 그룹 추가");

            //그룹만들기 버튼
            addGroupBtn = (Button)v.findViewById(R.id.checkMakingGroupBtn);

            //그룹명
            groupNameEditText = (EditText)v.findViewById(R.id.newGroupNameEditText);
            //그룹 한 줄 소개
            groupIntroEditText  = (EditText)v.findViewById(R.id.newGroupIntroEditText);

            addGroupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    groupName = groupNameEditText.getText().toString();
                     groupIntro = groupIntroEditText.getText().toString();
                    Log.i("data","groupName : "+groupName);
                    Log.i("data","groupIntro : "+groupIntro);


                }
            });


            return builder.create();
        }





    }
}


