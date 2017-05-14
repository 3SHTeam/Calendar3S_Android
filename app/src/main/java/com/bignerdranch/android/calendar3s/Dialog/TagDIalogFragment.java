package com.bignerdranch.android.calendar3s.Dialog;

import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.bignerdranch.android.calendar3s.Calendar.MonthCalendarFragment;

import com.bignerdranch.android.calendar3s.R;

import com.bignerdranch.android.calendar3s.data.TagData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * Created by ieem5 on 2017-05-01.
 */

public class TagDIalogFragment extends DialogFragment {

    public  static final String EXTRA_TAG_NAME = "tagName";
    public  static final String EXTRA_TAG_COLOR ="tagColor";
    public  static final String EXTRA_IS_CHECKED="tagIsChecked";
    private  Bundle bundle=null;
    private ArrayList<TagData> tagDatas = new ArrayList<>();
    private String hexColor;
    //태그명, 태그 색상
   // HashMap<String,Integer> tagNameColorMap;
    private CheckBox checkBox;
     //태그명
     private EditText tagName;

    //태그 추가 버튼 눌러야만 태그 추가 유아이 보이도록

    //스케줄 태그 선택 다이얼로그
    private Button colorSelectBtn;
    private Button addTagOKBtn;
    private Button addTagCancelBtn;
  //  private Button tagDialogOkBtn;
    //태그 추가 레이아웃
    private LinearLayout addTagLayout;
    //칼라피커에서 고른 태그 색깔
    private int tagColor;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_add_tag_dialog,null);

//        tagDatas =  getArguments().getParcelableArrayList(MonthCalendarFragment.ARG_TAGDATA);

        tagName = (EditText)v.findViewById(R.id.tagNameEt) ;
        checkBox = (CheckBox)v.findViewById(R.id.tagCheckBox) ;

        addTagOKBtn=  (Button)v.findViewById(R.id.addTagOKBtn);
         addTagCancelBtn=  (Button)v.findViewById(R.id.addTagCancelBtn);
         addTagLayout = (LinearLayout)v.findViewById(R.id.addTagLayout);

       // tagDialogOkBtn =  (Button)v.findViewById(R.id.tagDialogOkBtn);

    // tagDatas = main.getTagDatas();
        //태그이름






        //scheduleTagListView.setItemsCanFocus(false);
       // scheduleTagListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);





        //태그 추가 - 추가/취소 버튼 누르면 태그 추가 레이아웃 보이지 않게
        //태그 추가- 추가버튼
        addTagOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력한 태그와 고른 색깔 리스트뷰에 추가해야 함
                if(tagName.getText().length()!=0){
/*

                    //String tagId,String tagTitle, String tagColor,String groupId,boolean b
                    scheduleTagListViewAdapter.addItem(null,tagName.getText().toString(),hexColor,null,false);
                    scheduleTagListViewAdapter.notifyDataSetChanged();*/

                   // tagName.getText().toString(),hexColor
                    InsertTag(Activity.RESULT_OK,tagName.getText().toString(),hexColor);

                }

                dismiss();



            }
        });
        //태그 추가- 취소 버튼
        addTagCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이얼로그 창 종료
                dismiss();
            }
        });
        ////////

        colorSelectBtn = (Button)v.findViewById(R.id.ColorSelectBtn);
      colorSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ColorPicker colorPicker = new ColorPicker(getActivity());
                ArrayList<String> colors = new ArrayList<>();


                colors.add("#82B926");
                colors.add("#a276eb");
                colors.add("#6a3ab2");
                colors.add("#666666");
                colors.add("#FFFF00");
                colors.add("#3C8D2F");
                colors.add("#FA9F00");
                colors.add("#FF0000");

                colorPicker.setColors(colors).setDefaultColorButton(Color.parseColor("#f84c44")).setColumns(5).
                        setOnChooseColorListener
                                (new ColorPicker.OnChooseColorListener() {
                                    @Override
                                    public void onChooseColor(int position, int color) {
                                        Log.d("position",""+position);// will be fired only when OK button was tapped
                                        tagColor = color;
                                       // tagName.getText().toString();
                                        //int형 칼라  hex String으로 바꾸기
                                        hexColor = String.format("#%06X", (0xFFFFFF & color));

                                        Log.d("AAA","선택한칼라 : "+hexColor);




                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                }).addListenerButton("newButton", new ColorPicker.OnButtonListener() {
                    @Override
                    public void onClick(View v, int position, int color) {
                        Log.d("position",""+position);
                    }
                }).setRoundColorButton(true).show();
            }
        });


       /* tagDialogOkBtn.setOnClickListener(new View.OnClickListener() {
            //확인 버튼 누르면 표시된 태그데이터가 MAIN으로 전달되고 그 태그데이터가 표시된
            //MonthCalendarFragment가 표시되어야 함.
            @Override
            public void onClick(View v) {



                // 확인 버튼 누르면 체크된 스케줄 태그를 MonthCalendarFragment에 넘겨주어야 함.
               int len =  scheduleTagListViewAdapter.getScheduleTagListViewItemList().size();

                HashMap<String,Boolean>IdAndCheckedMap = new HashMap<String, Boolean>();
                for(int i=0;i<len;i++){
                        IdAndCheckedMap.put(scheduleTagListViewAdapter.getScheduleTagListViewItemList().get(i).getTagId(),
                                scheduleTagListViewAdapter.getScheduleTagListViewItemList().get(i).isCheckedOrNot() );
                }
                sendResult(Activity.RESULT_OK,IdAndCheckedMap);

                //여기서 바로 Main의 tagData 셋하든가 아니면 AddScheduleFragment처럼 listener interface 달아주던가...


                dismiss();

            }





        }



        );*/
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("스케줄 태그 지정")
                //.setItems()
                .setView(v);


        return builder.create();
    }

    private  void sendResult(int resultCode,HashMap<String,Boolean>map){
        //사용자가 대화상자의 확인 버튼을 누르면
        //DatePicker로부터 날짜르 받아서  MonthCalendarFragment에 결과로 전달해야 한다.
        if( getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        //intent.putExtra(EXTRA_DATE,date);

        intent.putExtra(EXTRA_IS_CHECKED,map);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
    private  void InsertTag(int resultCode,String tagName,String tagColor){
        //사용자가 스케줄 태그 추가 대화상자의 추가 버튼을 누르면
        //태그 이름과 색깔을 MonthCalendarFragment로 전달하고 여기 통해서 MainActivity로 데이터 전달
        if( getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        //intent.putExtra(EXTRA_DATE,date);

        intent.putExtra(EXTRA_TAG_NAME,tagName);
        intent.putExtra(EXTRA_TAG_COLOR,tagColor);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }




}
