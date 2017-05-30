package com.bignerdranch.android.calendar3s.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.data.UserData;
import com.bignerdranch.android.calendar3s.database.JsonMaster;
import com.bignerdranch.android.calendar3s.database.SendToDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

/**
 * Created by ieem5 on 2017-03-27.
 */

public class SignUpFragment extends Fragment {
    //회원가입 데이터
    private String googleId;
    private String googlePw;
    private String name;

    private char gender;
    private int birthDate;
    private String phoneNumber;
    private String nickname;
    private String word;

    private SendToDB sendToDB;

    //
    EditText googleIdEv;
    EditText googlePwEv;
    EditText nameTv;
   RadioButton RadioBtnF;
    RadioButton RadioBtnM;

   EditText birthDateEv;
    EditText phoneNumEv;
    EditText nickNameEv;
    EditText wordEv;

    Button signUpBtn;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        googleIdEv = (EditText) v.findViewById(R.id.googleIdEv);
        googlePwEv = (EditText) v.findViewById(R.id.googlePwEv);
        nameTv = (EditText) v.findViewById(R.id.startUserNameEv);
        RadioBtnF = (RadioButton)v.findViewById(R.id.genderF);
        RadioBtnM = (RadioButton)v.findViewById(R.id.genderM);
        birthDateEv = (EditText)v.findViewById(R.id.birthDateEv);
        phoneNumEv = (EditText) v.findViewById(R.id.startUserPhoneNumEv);
        nickNameEv = (EditText) v.findViewById(R.id.startUsernicknameEv);
        wordEv = (EditText) v.findViewById(R.id.startUserIntroEv);


        signUpBtn = (Button)v.findViewById(R.id.SignUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Signup","onClick Button");
                readSignUpData();

            }
        });


        return v;
    }

    public void readSignUpData() {

        Log.d("Signup","start");

        //UI에서 정보 뽑기
        String googleId = googleIdEv.getText().toString(); //구글 id
        String googlePw = googlePwEv.getText().toString();//구글 pw
        String name = nameTv.getText().toString();
        char gender=' ';
        if( RadioBtnF.isChecked()==true ){
            gender = 'W';

        }
        else if(RadioBtnM.isChecked()==true ){
            gender='M';
        }
        else {
            Toast.makeText(getActivity(), "성별을 체크하세요!", Toast.LENGTH_SHORT).show();

        }

        Log.d("Signup","birthDate");
        int birthDate = Integer.parseInt(birthDateEv.getText().toString());
        String phoneNumber =phoneNumEv.getText().toString();
        //Integer.parseInt는 앞의 0이 사라짐.

        String nickname = nickNameEv.getText().toString();
        String word = wordEv.getText().toString();
        MainActivity main = ((MainActivity)this.getActivity());


        Log.d("Signup","idcheck = " + googleId);
        //DB에서 아이디 중복체트
        if(main.CheckUser("where Googleid = '" + googleId + "'")) {

            //파이어 베이스에사용자 추가
            main.SetFirebaseListener();
            main.signupFireBase(googleId, googlePw);

            // 파이어 베이스 로그인
            main.signinFireBase(googleId, googlePw);

            //DB에 사용자 추가
            String message = "'" + googleId + "','" + googlePw + "','" + name + "','" + gender + "','"
                    + nickname + "','" + birthDate + "','" + phoneNumber + "','" + word + "','"
                    + main.getFBid() + "','" + main.getToken() + "'";

            Log.d("Signup","token = " + main.getToken());
            Log.d("Signup","message = " + message);
            AddUser(message);
            AddFirestTag(googleId);

            //새로 만든 아이디로 로그인
            ((MainActivity)getActivity()).initDB(googleId,googlePw);

            //캘린더 화면으로 전환
            ((MainActivity)getActivity()).ChangeFragment(R.id.calendars);
        }
        else{
            //중복된 아이디가 있습니다. 회원가입 실패
            Log.e("SignUpFail","중복된 아이디입니다.");
        }

        Log.i("data","googleId: "+googleId);
        Log.i("data","googlePw: "+googlePw);
        Log.i("data","name: "+name);
        Log.i("data","gender: "+gender);
        Log.i("data","birthDate: "+birthDate);
        Log.i("data","phoneNumber: "+phoneNumber);
        Log.i("data","nickname: "+nickname);
        Log.i("data","word: "+word);


    }

    private void AddFirestTag(String name) {
        String message = "'" + name + "','" + name + "','" + "#82B926"
                +"','맑은고딕','15','NULL'";
        String url = "InsertTag.php";

        SendToDB stDB = new SendToDB(url,message);

        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            Log.e("insertTag", "fail :" +e.toString());
        }
    }

    private void AddUser(String message){
        //DB에 사용자 추가
        String php = "InsertUser.php";

        sendToDB = new SendToDB(php, message);
        sendToDB.start();// DB연결 스레드 시작
        try {
            sendToDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
