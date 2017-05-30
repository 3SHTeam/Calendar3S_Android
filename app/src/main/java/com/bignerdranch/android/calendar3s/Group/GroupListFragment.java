package com.bignerdranch.android.calendar3s.Group;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;

import com.bignerdranch.android.calendar3s.data.GroupData;
import com.bignerdranch.android.calendar3s.data.MessageData;
import com.bignerdranch.android.calendar3s.database.SendToDB;

import java.util.ArrayList;
import java.util.Date;


public class GroupListFragment extends Fragment {

    public  static final String EXTRA_GROUP_NAME="kr.ac.hansung.a3scalendar.GroupName";
    public  static final String EXTRA_GROUP_INTRO="kr.ac.hansung.a3scalendar.GroupIntro";
    private static final int REQUEST_NEW_GROUP = 1;
    private static final int REQUEST_NEW_GROUP_MEMBER = 2;
    private static final int REQUEST_GROUP_MEMBER = 3;
    private static final String REQUEST_SHOW_ADD_GROUP_DIALLOG = "showAddNewGroupDialogFragment";
    private static final String REQUEST_SHOW_ADD_GROUP_DIALLOG_MEMBER = "showAddNewGroupMemberDialogFragment";

    private Button addGroupBtn;
    private ListView groupListView;
    private GroupListViewAdapter adapter;
    private ArrayList<GroupData> groups;

    //그룹 추가 다이얼로그로부터 받아올 그룹 이름과 그룹소개
    private  String newGroupName;
    private  String newGroupIntro;


    private View mainView;

    private MainActivity main;
    private ArrayList<GroupData>groupDatas = new ArrayList<>();
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
        main = ((MainActivity)getActivity());


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView= inflater.inflate(R.layout.fragment_group_list, container, false);


        groupDatas = main.getGroupDatas();
        //Log.d("GROUPANDREQ : ","group : "+ groupDatas.toString());

        return mainView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //여기서부터 ui변경 작업 가능
        super.onActivityCreated(savedInstanceState);
        setGroupData();
        adapter = new GroupListViewAdapter();
        if(groups!=null) {
            for (int i = 0; i < groups.size(); i++) {
                int membernum = groups.get(i).getUserIds_Arr().size();
                String members = null;
                if (membernum > 1)
                    members = groups.get(i).getData(3) + "\n외 " + (membernum - 1) + "명";
                else members = groups.get(i).getData(3);

                adapter.addItem(R.drawable.group, groups.get(i).getData(1), members);
            }
        }
        groupListView = (ListView)mainView.findViewById(R.id.groupListView);
        groupListView.setAdapter(adapter);

        //그룹 리스트 클릭해서 초대와 삭제 가능하게 만들어야 한다.
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GroupListViewItem item   = (GroupListViewItem)parent.getItemAtPosition(position);
                String clickedName = item.getGroupItemName();
                Log.d("CLICK"," position : "+position+" clickedName : "+clickedName+" is clicked");
              GroupData groupData = groupDatas.get(position);
                main.ChangeFragment(R.id.grouplistitem);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_NEW_GROUP) {

            newGroupName = data.getStringExtra(EXTRA_GROUP_NAME);
            newGroupIntro= data.getStringExtra(EXTRA_GROUP_INTRO);

            //그룹생성, 그룹가입, 그룹태그 생성
            main.makegroup(newGroupName,newGroupIntro);

            Log.d("TAG","newGroupName: "+newGroupName+"newGroupIntro : "+newGroupIntro);

        }
    }

    private void setGroupData() {
        //그룹을 새로고침하고 가져온다.
        groups = new ArrayList<>();
        main.freshGroup();
        groups = main.getGroupDatas();
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

       private Button addGroupBtn;
        private EditText groupNameEditText;
        private EditText groupIntroEditText;
        //그룹 이름
        private String groupName;
        //그룹 한 줄 소개
        private String groupIntro;





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

                    sendResult(Activity.RESULT_OK,groupName,groupIntro);

                  dismiss();
                    ((MainActivity)getActivity()).ChangeFragment(R.id.Community);



                }
            });


            return builder.create();
        }



        private  void sendResult(int resultCode, String groupName, String groupIntro){
            //사용자가 대화상자의 확인 버튼을 누르면
            //DatePicker로부터 날짜르 받아서  MonthCalendarFragment에 결과로 전달해야 한다.
            if( getTargetFragment() == null){
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(EXTRA_GROUP_NAME,groupName);
            intent.putExtra(EXTRA_GROUP_INTRO,groupIntro);

            getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
        }

    }

    public class GroupListViewAdapter extends BaseAdapter {

        //Adapter에 추가된 데이터를 저장하기 위한  ArrayList
        private ArrayList<GroupListViewItem>groupListViewItemList = new ArrayList<>();

        public GroupListViewAdapter() {

        }

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return groupListViewItemList.size();
        }


        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            int pos = position;
            Context context = parent.getContext();
            //listViewItem Layout을 infalte하여 convertView 참조 획득
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.group_list_item,parent,false);




            }

            ImageView iconImageView = (ImageView) convertView.findViewById(R.id.glItem_image) ;
            TextView titleTextView = (TextView) convertView.findViewById(R.id.glItem_name) ;
            TextView descTextView = (TextView) convertView.findViewById(R.id.glItem_member) ;
            Button memberBtn = (Button)convertView.findViewById(R.id.memberBtn);
            Button scheduleBtn = (Button)convertView.findViewById(R.id.scheduleBtn);


            GroupListViewItem groupListViewItem = groupListViewItemList.get(position);

            iconImageView.setImageResource(groupListViewItem.getImage());
            titleTextView.setText(groupListViewItem.getGroupItemName());
            descTextView.setText(groupListViewItem.getGroupItemMember());

            //그룹원 볼 수 있는 버튼 이거 누르면 invite 버튼과 delete 버튼과 그룹원들 이메일 주소가
            //쓰여진 프래그먼트가 뜬다.
            memberBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("CLICK","MEMBERBTN"+" position ["+position+"] is clicked!");
                    GroupMememberListFragment groupMememberListFragment = new GroupMememberListFragment();
                   GroupData groupData = groupDatas.get(position);
                   groupMememberListFragment.setGroupData(groupData);
                    groupMememberListFragment.setTargetFragment(GroupListFragment.this,REQUEST_GROUP_MEMBER);

                    main.ChangeFragment(groupMememberListFragment);

                }
            });

            //그룹 스케줄 버튼 - 그룹 스케줄 요청 메시지 보내기
            scheduleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    GroupScheduleFragment groupScheduleFragment  = new GroupScheduleFragment();
                    groupScheduleFragment.setGroupData(groupDatas.get(position));
                    main.ChangeFragment(groupScheduleFragment);

                }
            });
            return convertView;

        }


        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return groupListViewItemList.get(position);
        }

        public void addItem(int image,String groupItemName,String groupItemMember){

            GroupListViewItem item = new GroupListViewItem();
            item.setImage(image);
            item.setGroupItemName(groupItemName);
            item.setGroupItemMember(groupItemMember);

            groupListViewItemList.add(item);

        }
    }
}


