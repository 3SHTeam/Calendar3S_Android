package com.bignerdranch.android.calendar3s.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bignerdranch.android.calendar3s.R;

import java.util.ArrayList;

/**
 * Created by ieem5 on 2017-05-11.
 */

public class SelectTagFromAddScheduleDialogFragment extends DialogFragment {
private ListView listView;
    private ArrayAdapter<String>arrayAdapter;



    //db에서 받아올 태그 이름 리스트  AddScheduleDialogFragment에서 이 다이얼로그 부를 때 set해준다.
    private ArrayList<String>tagNameList;
    public ArrayList<String> getTagNameList() {return tagNameList;}

    public void setTagNameList(ArrayList<String> tagNameList) {this.tagNameList = tagNameList;}
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_select_tag_from_add_schedule,null);

        listView = (ListView)v.findViewById(R.id.tagNameListView) ;
        tagNameList = new ArrayList<>();
        /*tagNameList.add("one");
        tagNameList.add("two");
        tagNameList.add("three");*/
        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,tagNameList);
        listView.setAdapter(arrayAdapter);
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("태그를 선택하세요")
                //.setItems()
                .setView(v)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        dismiss();

                    }
                });



        return builder.create();


    }
}
