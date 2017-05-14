package com.bignerdranch.android.calendar3s.data;

/**
 * Created by ieem5 on 2017-04-26.
 */

public class UserData implements DataInfo{
    private String[] uData = new String[9];

    public UserData(String Googleid, String name, String gender, String nickname,
                    String birth, String phone ,String comment ,String FBuId, String FBToken){
        setData(0,Googleid);
        setData(1,name);
        setData(2,gender);
        setData(3,nickname);
        setData(4,birth);
        setData(5,phone);
        setData(6,comment);
        setData(7,FBuId);
        setData(8,FBToken);
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
        return null;
    }


    @Override
    public void showData() {
        System.out.println("Googleid : " + uData[0]);
        System.out.println("name : " + uData[1]);
        System.out.println("gender : " + uData[2]);
        System.out.println("nickname : " + uData[3]);
        System.out.println("birth : " + uData[4]);
        System.out.println("phone : " + uData[5]);
        System.out.println("comment : " + uData[6]);
        System.out.println("FBuId : " + uData[7]);
        System.out.println("FBToken : " + uData[8]);
    }

}
