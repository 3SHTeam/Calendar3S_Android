package com.bignerdranch.android.calendar3s.Group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.bignerdranch.android.calendar3s.Dialog.InviteDialogFragment;
import com.bignerdranch.android.calendar3s.MainActivity;

import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.data.GroupData;

import java.util.ArrayList;

import static com.bignerdranch.android.calendar3s.Dialog.InviteDialogFragment.EXTRA_INVEITEE;
import static com.bignerdranch.android.calendar3s.Dialog.InviteDialogFragment.EXTRA_MSG;

/**
 * Created by ieem5 on 2017-05-27.
 */

public class GroupMememberListFragment extends Fragment implements MainActivity.onKeyBackPressedListener{

    private static final int  REQUEST_INVITE_DIALOG=0;


    private static final String    DIALOG_INVITE ="DIALOG_INVITE";
    private ListView GroupMemberListView;
    private Button inviteBtn;
    private Button deleteBtn;
    //화면에 표시할 그룹데이터 1개 단위로 받아오기 위한 변수
    private GroupData  groupData;

    private MainActivity main;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String>userIdarrList;
    public GroupMememberListFragment newInstance(GroupData groupData){
        GroupMememberListFragment fragment = new GroupMememberListFragment();
        this.groupData = groupData;
        return fragment;
    }

    public void setGroupData(GroupData groupData) {
        this.groupData = groupData;
        Log.d("TAG",groupData.toString());
    }
    public GroupData getGroupData() {
        return groupData;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = ((MainActivity)getActivity());
        Log.d("CLICK",getGroupData().toString());
        userIdarrList= getGroupData().getUserIds_Arr();
       // userIdarrList = getGroupData().getUserIds_Arr();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frgment_group_member_list,container,false);
        GroupMemberListView = (ListView)v.findViewById(R.id.groupMemberListView);


        arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,userIdarrList);


        GroupMemberListView.setAdapter(arrayAdapter);

        inviteBtn = (Button)v.findViewById(R.id.inviteBtn);
          deleteBtn = (Button)v.findViewById(R.id.deleteBtn);

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                InviteDialogFragment dialog = new InviteDialogFragment();
                dialog.setTargetFragment(GroupMememberListFragment.this, REQUEST_INVITE_DIALOG);
                dialog.show(manager, DIALOG_INVITE);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Gid = groupData.getData(0);
                main.DeleteGroup(Gid);
                main.ChangeFragment(R.id.Community);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //InviteDialogFragment에서 초대할 사람이랑 초대메시지 받아옴
        if (requestCode == REQUEST_INVITE_DIALOG) {
            String invitee = (String)data.getStringExtra(EXTRA_INVEITEE);
            String msg =(String) data.getStringExtra(EXTRA_MSG);
            Log.d("INVITE","INVITE "+"invitee : "+invitee+" |msg : "+msg);
            String type = "groupInvite";
            //sender,reciver,type,message,Gid,Gname
            String sql =    "'" + main.getUser().getData(0) + "'," +
                            "'" + invitee + "'," +
                            "'" + type + "'," +
                            "'" + msg + "'," +
                            "'" + groupData.getData(0) + "'," +
                            "'" + groupData.getData(1) + "'" ;
            main.InsertMessage(sql);
        }
    }

    //그룹를 실행시킨다.
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.ChangeFragment(R.id.Community);
    }

    //자신의 백키를 불러오게 등록
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
    }
}
