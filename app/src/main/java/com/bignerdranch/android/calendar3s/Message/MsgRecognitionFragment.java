package com.bignerdranch.android.calendar3s.Message;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.AddScheduleDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.bignerdranch.android.calendar3s.MainActivity.adapter;

/**
 * Created by ieem5 on 2017-05-26.
 */

public class MsgRecognitionFragment extends Fragment implements MainActivity.onKeyBackPressedListener{
    private MainActivity main;


   // public static ListView ListViewSMS;
    private  ListView ListViewSMS;
    /*public static ArrayList<HashMap<String, String>> smsList;*/
    private SharedPreferences pref;
/*    private ArrayList<String> SmsMsgArry;
    private ArrayList<String> PhNumArry;*/
    private Button ReSet;
    private Button Service_Bt;
    private Button ServiceOff_Bt;

    private Iterator<String> iterator;
    public static Context context;
   // public static Activity activity;
    public static int ItemPosition;

    public static MsgRecognitionFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MsgRecognitionFragment fragment = new MsgRecognitionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = ((MainActivity)getActivity());
        pref = ((MainActivity)getActivity()).getSharedPreferences("accpetSMS",Activity.MODE_PRIVATE);
        ListViewSMS = ((MainActivity)getActivity()).getListViewSMS();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_msg_recognition,null);




        //pref = getActivity().getSharedPreferences("accpetSMS",Activity.);
       /* smsList = new ArrayList<HashMap<String, String>>();

        //SharedPreference에서 가져오는 부분 -문자데이터와 폰 데이터
      //  Map<String, ?> values = pref.getAll();
       // iterator = values.keySet().iterator();

       // String received = pref.getString("SMS", "");
        String received =((MainActivity)getActivity()).getReceived();
        Log.d("HASHMAP","received : "+received);
        String[] smss = received.split("@@@@@@@");

        SmsMsgArry = new ArrayList<String>();
        PhNumArry = new ArrayList<String>();
        for (int i = 0; i < smss.length; i++) {
            String asms = smss[i];
            Log.d("HASHMAP","asms : "+asms);
            String[] messageAndPhone = smss[i].split("@,@,@,@");
            if (messageAndPhone.length != 2) continue;
            SmsMsgArry.add(messageAndPhone[0]);
            PhNumArry.add(messageAndPhone[1]);
        }

        if (SmsMsgArry != null) {

            for (int i = SmsMsgArry.size()-1; i >= 0; i--) {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("item1", SmsMsgArry.get(i));
                //Log.d("HASHMAP","SmsMsgArry : "+SmsMsgArry.toString());
                item.put("item2", PhNumArry.get(i));
               // Log.d("HASHMAP","PhNumArry : "+PhNumArry.toString());
                smsList.add(item);
            }

           // for(int i=0;i<)
        }
*/

        ListViewSMS = (ListView) v.findViewById(R.id.ListViewSMS);
       adapter = new SimpleAdapter(getActivity(), main.getSmsList(), android.R.layout.simple_list_item_2, new String[]{"item1", "item2"},
                new int[]{android.R.id.text1, android.R.id.text2});
        ListViewSMS.setAdapter(adapter);

        ListViewSMS.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //리스트 아이템을 클릭했을 경우 InputCalendar로 이동.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  //테이블 아이템 클릭 리스너
               /* SharedPreferences.Editor editor = pref.edit();
                Log.d("ssh", "아이템 번호"+position);
                ItemPosition = position;
                editor.putString("ListSelect", smsList.get(position).get("item1") + "@@@@@@@" + smsList.get(position).get("item2")
                + "@,@,@,@" + position);
                Log.d("HASHMAP","ListSelect" +smsList.get(position).get("item1") + "@@@@@@@" + smsList.get(position).get("item2")
                        + "@,@,@,@" + position);
                editor.commit();*/
                ((MainActivity)getActivity()).setListItemInfo(position);
                //캘린더뷰로 이동
                AddMessageScheduleFragment fragment = new AddMessageScheduleFragment();
                //메시지 내용 저 프래그먼트 변수 값으로 전달해줘야 함
                fragment.setMsgContentsStr(main.getSmsList().get(position).get("item1"));
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

            }
        });

        //리스트랑 SharedPreferences 내용 모두 지우는 버튼
        ReSet = (Button)v.findViewById(R.id.button);  //리셋버튼
        ReSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (main.getSmsMsgArry() == null) {
                    return;
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                main.setSmsMsgArry(null);
                main.setPhNumArry(null);
               main.clearSmsList();
                adapter.notifyDataSetChanged();
            }
        });



        Service_Bt = (Button)v.findViewById(R.id.Service_button);
        Service_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main.serviceOn();

             /*  Intent intent = new Intent(getActivity(),MyService.class);
                getActivity().startService(intent);*/

                ServiceOff_Bt.setEnabled(true);
                Service_Bt.setEnabled(false);
            }
        });

        ServiceOff_Bt = (Button)v.findViewById(R.id.ServiceOff_Button);
        ServiceOff_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.serviceOff();
             /*  Intent intent = new Intent(  getActivity(),MyService.class);
                getActivity().stopService(intent);*/
                Service_Bt.setEnabled(true);
                ServiceOff_Bt.setEnabled(false);
            }
        });

        if(isServiceRunningCheck()){
            Service_Bt.setEnabled(false);
        }
        else{
            ServiceOff_Bt.setEnabled(false);
        }
        return v;

    }


    public boolean isServiceRunningCheck() {

        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.bignerdranch.android.calendar3s.Message.MyService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //캘린더를 실행시킨다.
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
