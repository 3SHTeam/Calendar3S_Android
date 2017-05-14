package com.bignerdranch.android.calendar3s.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ieem5 on 2017-04-26.
 */

public class TagData implements DataInfo,Parcelable{
    private String[] uData = new String[8];
    private boolean checkbox;

    public boolean isCheckbox() {return checkbox;}
    public void setCheckbox(boolean checkbox) {this.checkbox = checkbox;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uData[0]);//태그 아이디

        dest.writeString(uData[1]);//태그 이름
        dest.writeString(uData[2]);// 태그 칼라
        dest.writeString(uData[5]);//그룹 아이디

    }

    public TagData(Parcel in){
        readFromParcel(in);
    }

    private  void readFromParcel(Parcel in){
        uData[0] = in.readString(); //태그 아이디
        uData[1] = in.readString();//태그 Title
        uData[2] = in.readString();//태그 칼라
        uData[5] = in.readString();//그룹 아이디

    }

    public  static final Parcelable.Creator<TagData>CREATOR = new Parcelable.Creator(){

        //writeToParcel() 메소드에서 썼던 순서대로 읽어 오는 것입니다.
        @Override
        public Object createFromParcel(Parcel source) {
            return new TagData(source);
        }
        @Override
        public Object[] newArray(int size) {
            return new TagData[size];
        }

    };
    public TagData(String Tagid, String Tag_name, String Color,/* String Font, String Size,*/ String Gid) {
        setData(0, Tagid);
        setData(1, Tag_name);
        setData(2, Color);
        setData(3, "맑은고딕");
        setData(4, "15");
        setData(5, Gid);
       setCheckbox(true);
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
        System.out.println("Tagid : " + uData[0]);
        System.out.println("Tag_name : " + uData[1]);
        System.out.println("Color : " + uData[2]);
        System.out.println("Font : " + uData[3]);
        System.out.println("Size : " + uData[4]);
        System.out.println("Gid : " + uData[5]);
        System.out.println("checked : " + isCheckbox());

    }
}


