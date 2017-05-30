package com.bignerdranch.android.calendar3s.Group;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleManager;
import com.bignerdranch.android.calendar3s.data.GroupData;
import com.bignerdranch.android.calendar3s.data.MessageData;
import com.bignerdranch.android.calendar3s.database.SendToDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ieem5 on 2017-03-25.
 */

public class RequestFragment extends Fragment {
    private ListView listView;

    private  RequestListViewAdapter requestListViewAdapter;
   private MainActivity main;
    // 요청 메시지 수락 거절 버튼  포지션
    private  int acceptPosition;
    private  int rejectPosition;

    private ArrayList<MessageData>messageDatas = new ArrayList<>();
    private ArrayList<GroupData>groupDatas = new ArrayList<>();
    public RequestFragment newInstance(){
        RequestFragment fragment = new RequestFragment();
        return fragment;
    }

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        main = ((MainActivity)getActivity());

      //  Log.d("GROUPANDREQ : ", "reqeust : " + messageDatas.toString());
      //  Log.d("GROUPANDREQ : ", "reqeust : " + groupDatas.toString());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        freshmessages();
        View v = inflater.inflate(R.layout.fragment_request,container,false);
        listView = (ListView)v.findViewById(R.id.requestListVIew);
        requestListViewAdapter = new RequestListViewAdapter();
        if(messageDatas!=null) {
            for (int i = 0; i < messageDatas.size(); i++) {
                if(messageDatas.get(i).getData(3).equals("groupInvite")){//그룹가입요청
                    requestListViewAdapter.addItem(messageDatas.get(i).getData(1),
                            messageDatas.get(i).getData(6), messageDatas.get(i).getData(4));
                }else if(messageDatas.get(i).getData(3).equals("groupSchedule")){//그룹스케줄참여
                    String []tok=messageDatas.get(i).getData(4).split("/");
                    String scheduleMsg ="[" + tok[0] + "] 스케줄에 참여하시겠습니까?";

                    requestListViewAdapter.addItem(messageDatas.get(i).getData(1),
                            messageDatas.get(i).getData(6), scheduleMsg);
                }
            }
        }
        listView.setAdapter(requestListViewAdapter);


        return v;
    }

    class RequestListViewItem{

       private String groupOrganizer;
        private String groupName;
        private String message;
        private Button okBtn;
        private Button rejectBtn;

        public String getGroupOrganizer() {
            return groupOrganizer;
        }

        public void setGroupOrganizer(String groupOrganizer) {
            this.groupOrganizer = groupOrganizer;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

   class RequestListViewAdapter extends BaseAdapter{


       private  ArrayList<RequestListViewItem>items = new ArrayList<>();
       //private ArrayList<MessageData> messages;
       private MessageData message;


       @Override
       public int getCount() {
           return items.size();
       }

       @Override
       public RequestListViewItem getItem(int position) {
           return items.get(position);
       }

       @Override
       public long getItemId(int position) {
           return 0;
       }

       @Override
       public View getView(final int position, View convertView, ViewGroup parent) {

           int pos = position;
           Context context = parent.getContext();

           if(convertView == null){
               LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               convertView = inflater.inflate(R.layout.request_list_item, parent, false);
           }
           TextView groupOrganizerTv = (TextView)convertView.findViewById(R.id.groupOrganizerTv);
           TextView groupNameTv = (TextView)convertView.findViewById(R.id.groupNameTv);
           TextView msgTv = (TextView)convertView.findViewById(R.id.groupMessageTv);
           Button okBtn = (Button)convertView.findViewById(R.id.okBtn);
           Button rejectBtn = (Button)convertView.findViewById(R.id.rejectBtn);

           //리스트에 뿌려줄 아이템 받아오기
           RequestListViewItem item = getItem(pos);
           groupOrganizerTv.setText(item.getGroupOrganizer());
            groupNameTv.setText(item.getGroupName());
            msgTv.setText(item.getMessage());

           //위젯에 대한 이벤트 리스너 지정
           okBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //수락하면 GroupListFragment로 그룹명과 그룹원 정보가 넘어가야함. 그룹 사진 설정 기능은 보류
                   //디비로 넘어가게 설정?
                   Toast.makeText(getActivity(),"요청 메시지를 수락!",Toast.LENGTH_SHORT).show();

                   message = messageDatas.get(position);

                    if(message.getData(3).equals("groupInvite")){
                        //그룹에 참가
                        JoinGroup(message);
                        //그룹 스케줄 태그 생성
                        InsertGroupTag(message);
                        main.freshGroup();
                        main.freshTag();
                    }else if(message.getData(3).equals("groupSchedule")){
                        //스케줄에 참가
                        main.messageFromJoin(message);
                        main.freshMySchedule();
                    }

                    //메세지를 삭제하고 새로고침
                   deleteMessage(message.getData(0));
                   freshmessages();

                   items.remove(position);
                   notifyDataSetChanged();
               }
           });



           //삭제 버튼
           rejectBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //거절
                   //디비에서도 요청 메시지 삭제해야 하나?
                   Toast.makeText(getActivity(),"요청 메시지를 거절!",Toast.LENGTH_SHORT).show();

                   message = messageDatas.get(position);

                   //메세지를 삭제
                   deleteMessage(message.getData(0));
                   freshmessages();

                   items.remove(position);
                   rejectPosition = position;
                   notifyDataSetChanged();

               }
           });

           return convertView;
       }

       public void addItem(String groupOrganizer,String groupName,String msg){//,ArrayList<MessageData> messages){

           RequestListViewItem item = new RequestListViewItem();

           item.setGroupOrganizer(groupOrganizer);
           item.setGroupName(groupName);
           item.setMessage(msg);
           //this.messages = messages;

           items.add(item);


       }
   }

    private void JoinGroup(MessageData m) {
        String url = "JoinGroup.php";
        String sql = "'" + m.getData(2) + "','" + m.getData(5) + "'";

        SendToDB stDB = new SendToDB(url, sql);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    private void deleteMessage(String Mid) {
        String url = "DeleteMessage.php";

        SendToDB stDB = new SendToDB(url, Mid);
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    public void InsertGroupTag(MessageData m) {
        String message = "'" + m.getData(2) + "','" + m.getData(6) + "','" + "#f6f9bd"
                + "','맑은고딕','15','" + m.getData(5) + "'";
        String url = "InsertTag.php";

        SendToDB stDB = new SendToDB(url, message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("insertTag", "fail :" + e.toString());
        }
    }

    public void freshmessages(){
        main.freshMessage();
        messageDatas = main.getMessages();//보낸 사람
    }

}
