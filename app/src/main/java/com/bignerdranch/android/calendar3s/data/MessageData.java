package com.bignerdranch.android.calendar3s.data;

/**
 * Created by ieem5 on 2017-05-25.
 */

public class MessageData implements DataInfo{
    private String[] uData = new String[7];

    public MessageData(){

    }

    public MessageData(String Mid, String sender, String receiver,
                       String type, String message, String Gid, String Gname){
        setData(0,Mid);
        setData(1,sender);
        setData(2,receiver);
        setData(3,type);
        setData(4,message);
        setData(5,Gid);
        setData(6,Gname);
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
        String sql = "'" + uData[1] + "','" + uData[2] + "','"+ uData[3] + "','"
                + uData[4] + "','" + uData[5] + "','" + uData[6] +  "'";
        return sql;
    }


    @Override
    public void showData() {
        System.out.println("Mid : " + uData[0]);
        System.out.println("sender : " + uData[1]);
        System.out.println("receiver : " + uData[2]);
        System.out.println("type : " + uData[3]);
        System.out.println("message : " + uData[4]);
        System.out.println("GId : " + uData[5]);
        System.out.println("Gname : " + uData[6]);
    }

    @Override
    public String toString() {
        String str = "| Mid : "+uData[0]+" | sender : "+uData[1]+" | receiver : "+uData[2]+
                " | type :  "+uData[3]+"| message : "+uData[4]+" |Gid : "+uData[5]+"|Gname : "+uData[6];
        return str;
    }


}