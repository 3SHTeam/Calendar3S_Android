package com.bignerdranch.android.calendar3s.data;

import com.bignerdranch.android.calendar3s.database.JsonMaster;
import com.bignerdranch.android.calendar3s.database.SendToDB;

import java.util.ArrayList;

/**
 * Created by ieem5 on 2017-05-08.
 */

public class GroupData implements DataInfo {
    private String[] uData = new String[4];
    private ArrayList<String> userIds_Arr;// 회원 아이디

    public GroupData() {

    }
    public GroupData(String GId, String name, String comment, String gmaster) {
        setData(0, GId);
        setData(1, name);
        setData(2, comment);
        setData(3, gmaster);
    }


    @Override
    public String[] getData() {
        return uData;
    }

    @Override
    public String getData(int index) {
        return uData[index];
    }

    @Override
    public void setData(int index, String data) {
        uData[index] = data;
    }

    @Override
    public void setData(String[] uData) {
        this.uData = uData;
    }

    @Override
    public String getSendSQLString() {
        String sql = "'" + uData[1] + "','" + uData[2] + "','" + uData[3] + "'";
        return sql;
    }

    @Override
    public void showData() {
        System.out.println("GId : " + uData[0]);
        System.out.println("GroupName : " + uData[1]);
        System.out.println("comment : " + uData[2]);
        System.out.println("gmaster : " + uData[3]);
        System.out.println("userIds : " + uData[4]);
    }

    public ArrayList<String> getUserIds_Arr() {
        return userIds_Arr;
    }

    public void setUserIds_Arr() {
      /* DB에서 스케줄 가져오기 */
        String url = "http://113.198.84.66/Calendar3S/SelectGroupMember.php";

        SendToDB stDB = new SendToDB(url, getData(0));
        stDB.start();// DB연결 스레드 시작
        try {
            stDB.join();// DB연결이 완료될때까지 대기
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        String result = stDB.getResult();// Json형식의 String값 가져옴
        System.out.println(result);

        JsonMaster jm = new JsonMaster();
        jm.onPostExecute("SelectGroupMember", result);

        userIds_Arr = jm.getUserIds_Arr();

    }

}
