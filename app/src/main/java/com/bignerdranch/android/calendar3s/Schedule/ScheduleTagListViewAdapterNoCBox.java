package com.bignerdranch.android.calendar3s.Schedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bignerdranch.android.calendar3s.R;

import java.util.ArrayList;

/**
 * Created by ieem5 on 2017-05-18.
 */

public class ScheduleTagListViewAdapterNoCBox extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ScheduleTagListViewItemNoCBox> ScheduleTagListViewItemList = new ArrayList<ScheduleTagListViewItemNoCBox>() ;

    // ListViewAdapter의 생성자
    public ScheduleTagListViewAdapterNoCBox() {

    }

    public ArrayList<ScheduleTagListViewItemNoCBox> getScheduleTagListViewItemList() {
        return ScheduleTagListViewItemList;
    }

    public void setScheduleTagListViewItemList(ArrayList<ScheduleTagListViewItemNoCBox> scheduleTagListViewItemList) {
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
            convertView = inflater.inflate(R.layout.tag_name_and_color, parent, false);

        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득


        // CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.tagCheckBox);
        TextView tagNameTv=(TextView)convertView.findViewById(R.id.scheduleTagTv);
        TextView tagColorTv=(TextView)convertView.findViewById(R.id.scheduleTagColorTv);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

        ScheduleTagListViewItemNoCBox listViewItem =ScheduleTagListViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        //checkBox.setChecked(listViewItem.isCheckedOrNot());
        tagNameTv.setText(listViewItem.getTagTitle());
        tagColorTv.setBackgroundColor(listViewItem.getTagTvColor());
        // Log.i("AAA","MATCHING..."+listViewItem.isCheckedOrNot()+","+listViewItem.getTagTitle()+","+listViewItem.getTagTvColor());


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
        ScheduleTagListViewItemNoCBox item = new ScheduleTagListViewItemNoCBox();

        item.setTagId(tagId);
        item.setTagTitle(tagTitle);
        //hex String color -> int color
        int intColor = Color.parseColor(tagColor);
        item.setTagTvColor(intColor);
        item.setGroupId(groupId);
        // item.setCheckedOrNot(b);

        // item.getCheckBox().setChecked(item.isCheckedOrNot());
        // item.setCheckedOrNot(b);

        ScheduleTagListViewItemList.add(item);
    }



}
