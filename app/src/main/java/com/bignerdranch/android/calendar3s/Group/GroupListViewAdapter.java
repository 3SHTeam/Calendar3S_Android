package com.bignerdranch.android.calendar3s.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.calendar3s.R;

import java.util.ArrayList;

/**
 * Created by ieem5 on 2017-03-26.
 */

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
    public View getView(int position, View convertView, ViewGroup parent) {

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

        GroupListViewItem groupListViewItem = groupListViewItemList.get(position);

        iconImageView.setImageResource(groupListViewItem.getImage());
        titleTextView.setText(groupListViewItem.getGroupItemName());
        descTextView.setText(groupListViewItem.getGroupItemMember());

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
