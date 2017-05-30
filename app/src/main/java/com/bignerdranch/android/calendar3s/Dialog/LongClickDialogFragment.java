package com.bignerdranch.android.calendar3s.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.AddScheduleDialogFragment;
import com.bignerdranch.android.calendar3s.data.CalData;
import com.bignerdranch.android.calendar3s.data.EventData;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Owner on 2017-02-07.
 */

public class LongClickDialogFragment extends DialogFragment {

    public static final int REQUEST_EVENTDATA =1;
    public static final String EXTRA_EVENTDATA="kr.ac.hansung.a3scalendar.eventData";
    private CalData calData;

    private Calendar selectedCal;//선택된 날짜 객체
    private EventData eventDataFromAddSchedule;




    public CalData getCalData() {
        return calData;
    }

    public void setCalData(CalData calData) {
        this.calData = calData;
    }

    public EventData getEventData() {
        return eventDataFromAddSchedule;
    }

    public void setEventData(EventData eventDataFromAddSchedule) {
        this.eventDataFromAddSchedule = eventDataFromAddSchedule;
    }

    public Calendar getSelectedCal() {
        return selectedCal;
    }

    public void setSelectedCal(Calendar selectedCal) {
        this.selectedCal = selectedCal;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//simpledataformat으로 변환해야 캘린더 객체 월이 실제 월이랑 1 차이 나는 것을 신경쓸 필요가 없다.
      SimpleDateFormat s = new SimpleDateFormat("yyyy년MM월dd일");
        String selectedDate = s.format(selectedCal.getTime());

        //2017년 1월 17일 일정'   --> 선택한 날짜 받아와야 함.
String dialogmentTitle =selectedDate ;
        final String []DialogItem={"추가","수정","동결","삭제"};
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialogmentTitle)
                .setItems(DialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),DialogItem[which]+"선택!",Toast.LENGTH_SHORT).show();


                          if( DialogItem[which].equals("추가")){
                              //프래그먼트 화면


                              AddScheduleDialogFragment addScheduleDialogFragment = new  AddScheduleDialogFragment();
                              //addScheduleDialogFragment.setSelectedDate(selectedDate);
                              //날짜 Calendar 객체로 전달
                              addScheduleDialogFragment.setSelectedCal(selectedCal);
                              //FragmentManager manager = getFragmentManager();
                              FragmentTransaction ft = getFragmentManager().beginTransaction().
                                      replace(R.id.container,addScheduleDialogFragment);
                               ft.addToBackStack(null);
                           /*   addScheduleDialogFragment.
                                      setTargetFragment(getFragmentManager().findFragmentById(R.id.calendars),REQUEST_EVENTDATA);*/



                             ft.commit();

                              //AddScheduleDialogFragment addScheduleDialogFragment = new AddScheduleDialogFragment();


                             // addScheduleDialogFragment.setTargetFragmeN
                             // addScheduleDialogFragment.setTargetFragment(LongClickDialogFragment.this,REQUEST_EVENTDATA);
                              //eventData를 addScheduleFragment에서 받아온다음에
                              //이 eventData를 MonthCalendarFragment에서 받을 수 있으면 된것이다.


                           //  addScheduleDialogFragment.show(manager,"requestAddScheduleFragment ");






                          }else if(DialogItem[which].equals("수정")){





                          }else if(DialogItem[which].equals("동결")){
                              calData.setFreezen(true);//해당 날자 동결시키기

                          }else{//삭제

                          }
                    }



                });

        return builder.create();

    }





}