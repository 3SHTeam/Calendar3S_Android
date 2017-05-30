package com.bignerdranch.android.calendar3s.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.calendar3s.R;

/**
 * Created by ieem5 on 2017-05-27.
 */

public class InviteDialogFragment extends DialogFragment {

    public  static final String  EXTRA_INVEITEE = "INVITEE";
    public  static final String  EXTRA_MSG = "INVITATION_MSG";

    private EditText  inviteeEditText;
    private EditText msgEditText;
    private Button inviteBtn;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_invite_dialog,null);
        inviteeEditText = (EditText) v.findViewById(R.id.inviteeEv);
          msgEditText = (EditText) v.findViewById(R.id.msgEv);
          inviteBtn = (Button) v.findViewById(R.id.diaglogInviteBtn);

        //초대하기 버튼 누르면
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String invitee =inviteeEditText.getText().toString().trim();
                String msg = msgEditText.getText().toString().trim();
                if( (invitee.length()!=0) && (msg.length()!=0)) {

                    sendResult(Activity.RESULT_OK,invitee,msg);

                }else{
                    Toast.makeText(getActivity(),"다 입력하지 않음!",Toast.LENGTH_SHORT).show();
                }


                dismiss();
            }
        });


        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("초대하기")
                //.setItems()
                .setView(v);


        return builder.create();
    }



    private  void sendResult(int resultCode, String invitee, String msg){
        //사용자가 대화상자의 확인 버튼을 누르면
        //DatePicker로부터 날짜르 받아서  MonthCalendarFragment에 결과로 전달해야 한다.
        if( getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_INVEITEE,invitee);
        intent.putExtra(EXTRA_MSG,msg);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
