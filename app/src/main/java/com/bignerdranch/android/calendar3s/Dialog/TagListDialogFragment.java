package com.bignerdranch.android.calendar3s.Dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.bignerdranch.android.calendar3s.Calendar.MonthCalendarFragment;
import com.bignerdranch.android.calendar3s.R;

import com.bignerdranch.android.calendar3s.data.TagData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ieem5 on 2017-05-11.
 */

public class TagListDialogFragment extends DialogFragment {
    public  static final String EXTRA_TAG_HASHMAP="tagIdAndTagIsCheckedOrNot";
    private ArrayList<TagData> tagDatas = new ArrayList<>();
    private Button okBtn;

    //스케줄 태그 리스트뷰
    private ListView scheduleTagListView;
    private ScheduleTagListViewAdapter scheduleTagListViewAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_tag_listview,null);

        tagDatas =  getArguments().getParcelableArrayList(MonthCalendarFragment.ARG_TAGDATA);

        scheduleTagListView = (ListView)v.findViewById(R.id.scheduleTagListView);
        okBtn = (Button)v.findViewById(R.id.tagDialogOkBtn) ;

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //태그 선택하고 확인 버튼 누르면

                //확인 버튼 누르면 표시된 태그데이터가 MAIN으로 전달되고 그 태그데이터가 표시된
                //MonthCalendarFragment가 표시되어야 함.



                    // 확인 버튼 누르면 체크된 스케줄 태그를 MonthCalendarFragment에 넘겨주어야 함.
                    int len =  scheduleTagListViewAdapter.getScheduleTagListViewItemList().size();

                    HashMap<String,Boolean> IdAndCheckedMap = new HashMap<String, Boolean>();
                    for(int i=0;i<len;i++){


                        IdAndCheckedMap.put(scheduleTagListViewAdapter.getScheduleTagListViewItemList().get(i).getTagId(),
                                scheduleTagListViewAdapter.getScheduleTagListViewItemList().get(i).isCheckedOrNot() );
                        System.out.println("from TagList id: "+scheduleTagListViewAdapter.getScheduleTagListViewItemList().get(i).getTagId()+
                                "tagName: "+scheduleTagListViewAdapter.getScheduleTagListViewItemList().get(i).getTagTitle()+
                        " checkedOrNot : "+scheduleTagListViewAdapter.getScheduleTagListViewItemList().get(i).isCheckedOrNot());
                    }
                    sendResult(Activity.RESULT_OK,IdAndCheckedMap);

                    //여기서 바로 Main의 tagData 셋하든가 아니면 AddScheduleFragment처럼 listener interface 달아주던가...




                dismiss();


            }
        });

        scheduleTagListViewAdapter = new ScheduleTagListViewAdapter();
        ///DB에서 받아온 태그 어댑터에 넣기
        for(int i=0;i<tagDatas.size();i++){


            System.out.println("["+i+"] : "+"tagId : "+tagDatas.get(i).getData(0)+"tagName : "+tagDatas.get(i).getData(1)+
                    "tagColor : "+tagDatas.get(i).getData(2)+"GroupId : "+tagDatas.get(i).getData(5) + "checkes : "
                    + tagDatas.get(i).isCheckbox());

            //String tagId,String tagTitle, String tagColor,String groupId,boolean b
            scheduleTagListViewAdapter.addItem(tagDatas.get(i).getData(0),tagDatas.get(i).getData(1),tagDatas.get(i).getData(2),
                    tagDatas.get(i).getData(5),tagDatas.get(i).isCheckbox());

           // scheduleTagListViewAdapter.notifyDataSetChanged();

           /*Log.i("AAA",(scheduleTagListView.getItemAtPosition(0))*/

        }
        scheduleTagListViewAdapter.notifyDataSetChanged();
        scheduleTagListView.setAdapter(scheduleTagListViewAdapter);


        //태그 리스트뷰의 태그를 선택했을 때

        scheduleTagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                ScheduleTagListViewItem item   = (ScheduleTagListViewItem)parent.getItemAtPosition(position);
               /* boolean b = false;
                b=!b;
                item.setCheckedOrNot(b);*/
                item.setCheckedOrNot(!item.isCheckedOrNot());
                scheduleTagListViewAdapter.notifyDataSetChanged();
                //item.setBooleanCheckBox(b);

                //hexColor = String.format("#%06X", (0xFFFFFF & color));
                Log.d("AAA",position  +" is Clicked!! ");
                Log.d("AAA","["+position+"]"+"tagId : "+item.getTagId()+", tagName: "+item.getTagTitle()+
                        " , "+" , tagColor : "+
                        String.format("#%06X", (0xFFFFFF & item.getTagTvColor()))+", GroupId : "+
                        item.getGroupId()+", Checked?! : "+item.isCheckedOrNot()+
                        " , "+" "  +" is Clicked!! ");
            }
        });



        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("스케줄 태그 목록")
                //.setItems()
                .setView(v);


        return builder.create();
    }


    private  void sendResult(int resultCode,HashMap<String,Boolean>map){
        //사용자가 대화상자의 확인 버튼을 누르면
        //태그아이디와 태그체크여부가 담긴 hashMap을  MonthCalendarFragment에 결과로 전달해야 한다.
        if( getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        //intent.putExtra(EXTRA_DATE,date);

        intent.putExtra(EXTRA_TAG_HASHMAP,map);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
  class ScheduleTagListViewItem{
        //태그ID, , 태그이름, 태그 색깔,그룹ID, checkedOrNot

        private String tagId;
        private String tagTitle;
        private int tagTvColor;
        private String groupId;

        private  boolean checkedOrNot;

        public boolean isCheckedOrNot() {
            return checkedOrNot;
        }
        public void setCheckedOrNot(boolean checkedOrNot) {
            this.checkedOrNot = checkedOrNot;
            // this.checkBox.setChecked(this.checkedOrNot);
        }

        public String getTagTitle() {
            return tagTitle;
        }
        public void setTagTitle(String tagTitle) {
            this.tagTitle = tagTitle;
        }

        public int getTagTvColor() {
            return tagTvColor;
        }
        public void setTagTvColor(int tagTvColor) {
            this.tagTvColor = tagTvColor;
        }

        public String getTagId() { return tagId; }
        public void setTagId(String tagId) { this.tagId = tagId; }

        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }






    }



  class ScheduleTagListViewAdapter  extends BaseAdapter {

        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        private ArrayList<ScheduleTagListViewItem> ScheduleTagListViewItemList = new ArrayList<ScheduleTagListViewItem>() ;

        // ListViewAdapter의 생성자
        public ScheduleTagListViewAdapter() {

        }

        public ArrayList<ScheduleTagListViewItem> getScheduleTagListViewItemList() {
            return ScheduleTagListViewItemList;
        }

        public void setScheduleTagListViewItemList(ArrayList<ScheduleTagListViewItem> scheduleTagListViewItemList) {
            ScheduleTagListViewItemList = scheduleTagListViewItemList;
        }
        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return ScheduleTagListViewItemList.size() ;
        }

        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            final int pos = position;
            final Context context = parent.getContext();

            // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다
            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.schedule_tag_list_item, parent, false);

            }
            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득


            CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.tagCheckBox);
            TextView tagNameTv=(TextView)convertView.findViewById(R.id.scheduleTagTv);
            TextView tagColorTv=(TextView)convertView.findViewById(R.id.scheduleTagColorTv);

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

            ScheduleTagListViewItem listViewItem =ScheduleTagListViewItemList.get(position);

            // 아이템 내 각 위젯에 데이터 반영
            checkBox.setChecked(listViewItem.isCheckedOrNot());
            tagNameTv.setText(listViewItem.getTagTitle());
            tagColorTv.setBackgroundColor(listViewItem.getTagTvColor());
            Log.i("AAA","MATCHING..."+listViewItem.isCheckedOrNot()+","+listViewItem.getTagTitle()+","+listViewItem.getTagTvColor());


            return convertView;
        }

        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position ;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return ScheduleTagListViewItemList.get(position) ;
        }



        // 아이템 데이터 추가를 위한 함수.
        public void addItem( String tagId,String tagTitle, String tagColor,String groupId,boolean b) {
            ScheduleTagListViewItem item = new ScheduleTagListViewItem();

            item.setTagId(tagId);
            item.setTagTitle(tagTitle);
            //hex String color -> int color
            int intColor = Color.parseColor(tagColor);
            item.setTagTvColor(intColor);
            item.setGroupId(groupId);
            item.setCheckedOrNot(b);

            // item.getCheckBox().setChecked(item.isCheckedOrNot());
            // item.setCheckedOrNot(b);

            ScheduleTagListViewItemList.add(item);
        }





    }




}
