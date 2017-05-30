package com.bignerdranch.android.calendar3s.data;

import android.app.usage.UsageEvents;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by ieem5
 * on 2017-04-26.
 */

public class EventData implements DataInfo,Parcelable{
    private String[] uData = new String[9];
    private String longStartTime,longEndTime;
    private String Tagid="null";

    public String getTagid() {return Tagid;}
    public void setTagid(String tagid) {Tagid = tagid;}

    public EventData(){

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uData[0]);//이벤트 아이디

        dest.writeString(uData[2]);//이벤트 Title
        dest.writeString(uData[3]);//이벤트 장소
        dest.writeString(uData[4]);//이벤트 시작시간
        dest.writeString(uData[5]);//이벤트 종료시간

    }

    public EventData(Parcel in){
        readFromParcel(in);
    }

    private  void readFromParcel(Parcel in){
        uData[0] = in.readString(); //이벤트 아이디
        uData[2] = in.readString();//이벤트 Title
        uData[3] = in.readString();//이벤트 장소
        uData[4] = in.readString();//이벤트 시작시간
        uData[5] = in.readString();//이벤트 종료시간
    }

    public  static final Parcelable.Creator<EventData>CREATOR = new Parcelable.Creator(){

        //writeToParcel() 메소드에서 썼던 순서대로 읽어 오는 것입니다.
        @Override
        public Object createFromParcel(Parcel source) {
            return new EventData(source);
        }
        @Override
        public Object[] newArray(int size) {
            return new EventData[size];
        }

    };


    public EventData(String Sid, String SMaster, String Sname, String Place,
                     String StartTime, String EndTime,String TagId,String GoogleSId,String GId){

        setData(0,Sid);
        setData(1,SMaster);
        setData(2,Sname);// 2. 이벤트 이름,
        setData(3,Place);// 6.이벤트 장소
        setData(4,StartTime);// 4.이벤트 시작시간
        setData(5,EndTime); // 5. 이벤트 종료시간
        setData(6,TagId);
        setData(7,GoogleSId);//이벤트 아이디
        setData(8,GId);

        this.longStartTime = StartTime;
        this.longEndTime = EndTime;
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
        String sql =  "'" + uData[1] + "','" + uData[2] + "','"+ uData[3] + "','"
                + uData[4] + "','" + uData[5]+ "'";
        return sql;
    }


    @Override
    public void showData() {
        System.out.println("Sid : " + uData[0]);
        System.out.println("SMaster : " + uData[1]);
        System.out.println("Sname : " + uData[2]);
        System.out.println("Place : " + uData[3]);
        System.out.println("StartTime : " + uData[4]);
        System.out.println("EndTime : " + uData[5]);
        System.out.println("TagId : " + uData[6]);
        System.out.println("GoogleSId : " + uData[7]);
        System.out.println("GId : " + uData[8]);
    }


    @Override
    public String toString() {
        String str = "eventId: "+uData[7]+" eventTitle: "+uData[2]+" eventLocation : "+uData[3]+
                "  eventStartTime :  "+uData[4]+ "  eventEndTime : "+uData[5];
        return str;
    }
}