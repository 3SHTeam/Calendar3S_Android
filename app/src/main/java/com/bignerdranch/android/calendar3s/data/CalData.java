package com.bignerdranch.android.calendar3s.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Owner on 2017-01-31.
 */

public class CalData implements Parcelable{

    //private UUID mId;//고유한 식별자
    int dayofweek;//1.요일
    //int images;//날씨 정보 이미지
    private int year;//2.연도
    private int month;//3.월
    int day;//날짜 4.일
    String totalDate;//년월일형식의 날짜
    long totalDateLong; //CalData 정렬할 때 이 변수 기준으로 오름차순 정렬
    private Calendar calendar;//날짜 객체

    private  boolean isFreezen;//날짜 동결 여부 체크 변수

    //private int hour;//5.시간
   // private int min;//6.분
    private String txt;
    private ArrayList<String> scheduleArrList;//나중에 없앨것이다 가짜 스케줄이므로

    private ArrayList<EventData> eventDataList;//이벤트 데이터

    @Override
    public int describeContents() {
        return 0;
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     *
     * Parcel에 데이터를 쓰는 부분.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dayofweek);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeString(totalDate);
        dest.writeLong(totalDateLong);
        dest.writeValue(calendar);
        //요부분1
        dest.writeTypedList(eventDataList);
    }
public CalData(Parcel in){
    readFromParcel(in);
}
    private void readFromParcel(Parcel in) {
        dayofweek = in.readInt();
        year = in.readInt();
        month= in.readInt();
        day= in.readInt();
        totalDate = in.readString();
        totalDateLong = in.readLong();
        calendar  =(Calendar) in.readValue(null);
        //요부분 2
        eventDataList = new ArrayList<>();
    }

    /**
     * @author sungsik81
     *
     * Parcelable.Creator<T> 클래스는 createFromParcel() 과 newArray() 메소스가 필요하다.
     * Parcel로 부터 값을 읽어 오기 위해서는 Parcelable.Creator Interface 가 필요하다.
     */
    public static final Parcelable.Creator<CalData> CREATOR = new Parcelable.Creator() {
        //writeToParcel() 메소드에서 썼던 순서대로 읽어 오는 것입니다.
        @Override
        public Object createFromParcel(Parcel source) {
            return new CalData(source);
        }
        @Override
        public Object[] newArray(int size) {
            return new CalData[size];
        }

    };

    public CalData(){
        init();
    //mId = UUID.randomUUID();//고유한 식별자를 생성한다.
    }

    public CalData(int Y, int M, int D, int dayofweek){
        //MonthCalendar  용

        year = Y;
        month = M;
        day = D;
        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.YEAR,year);
        this.calendar.set(Calendar.MONTH,month);
        this.calendar.set(Calendar.DATE,day);
        this.totalDate = makeTotalDateYMD();
        setCalendarFormatDate();
        this.eventDataList = new ArrayList<>();

        totalDateLong = Long.parseLong(totalDate);
        this.dayofweek = dayofweek;
       // mId = UUID.randomUUID();//고유한 식별자를 생성한다.

        init();

    }

/////main에서쓰는 생성자
    public CalData(Calendar calendar, EventData eventData){
        //MonthCalendar  용
        this.calendar = Calendar.getInstance();
         this.calendar = calendar;
        this.year = calendar.get(Calendar.YEAR);
        this.month =calendar.get(Calendar.MONTH);
        this. day =calendar.get(Calendar.DATE);
        this.totalDate =makeTotalDateYMD();
        this.dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        this.eventDataList =new ArrayList<>();

        this.eventDataList.add(eventData);
        setCalendarFormatDate();
        init();
       // mId = UUID.randomUUID();//고유한 식별자를 생성한다.

    }

    public CalData(String totalDate, EventData eventData){
        //MonthCalendar  용
        this.calendar = Calendar.getInstance();
        this.calendar = calendar;
        this.year = calendar.get(Calendar.YEAR);
        this.month =calendar.get(Calendar.MONTH);
        this. day =calendar.get(Calendar.DATE);
        this.totalDate =totalDate;
        this.dayofweek = calendar.get(Calendar.DAY_OF_WEEK);



        setCalendarFormatDate();
        init();
        // mId = UUID.randomUUID();//고유한 식별자를 생성한다.

    }

    public CalData(int Y, int M, int D) {

        //WeekCalendar 용
        year = Y;
        month = M;
        day = D;
        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.YEAR,year);
        this.calendar.set(Calendar.MONTH,month);
        this.calendar.set(Calendar.DATE,day);
        this.totalDate =makeTotalDateYMD();
        this.eventDataList = new ArrayList<>();

        totalDateLong = Long.parseLong(totalDate);
        setCalendarFormatDate();
        init();
       // mId = UUID.randomUUID();//고유한 식별자를 생성한다.

    }

    public CalData(int Y, int M, int D, int dayofweek, ArrayList<EventData> eventDataList) {

        //WeekCalendar 용
        this.year = Y;
        this.month = M;
        this.day = D;
       // this.eventDataList = new ArrayList<>();

        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.YEAR,year);
        this.calendar.set(Calendar.MONTH,month);
        this.calendar.set(Calendar.DATE,day);
        this.totalDate =makeTotalDateYMD();

         this.eventDataList = new ArrayList<>();
        this.eventDataList = eventDataList;
        setCalendarFormatDate();
       // mId = UUID.randomUUID();//고유한 식별자를 생성한다.

    }
   /*
    public CalData(int d, int h) {
        //MonthCalendar  용
        day = d;
        dayofweek = h;
        init();
    }

    public CalData(int d){
        //WeekCalendar 용
        day = d;
        init();
    }
*/

    public void init(){
       // hour = 0;
       // min = 0;
        txt = "";
        isFreezen=false;

       // eventDataList = new ArrayList<>();
    }

   /* public UUID getId(){
        return  mId;
    }*/
    public boolean isFreezen() {
        return isFreezen;
    }

    public void setFreezen(boolean freezen) {
        isFreezen = freezen;
    }

    public ArrayList<String> getScheduleArrList() {
        return scheduleArrList;
    }

    public void setScheduleArrList(ArrayList<String> scheduleArrList) {
        this.scheduleArrList = scheduleArrList;
    }

    public void setYear(int Y){
        year = Y;
    }

    public void setMonth(int M){
        month = M;
    }

    public void setDay(int D){
        day = D;
    }

    public int getYear(){
        return year;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    public void setString(String T){
        txt = T;
    }

    public void setTime(int H, int M){
       // hour = H;
       // min = M;
    }

    public String getString(){
        return txt;
    }
/*
    public String getTime(){
        //return ""+hour+" : "+min;

    }*/

    public String getSchedule(){
        return  txt;
    }





    public int getDayofweek() {
        return dayofweek;
    }

    public ArrayList<EventData> getEventDataList() {
        return eventDataList;
    }


    public void addFirstEventData(EventData e){
        this.eventDataList = new ArrayList<>();
        this.eventDataList.add(e);
    }

    public void setEventDataList(ArrayList<EventData> eventDataList) {
        this.eventDataList = eventDataList;
    }


    public String getTotalDate() {
        return totalDate;
    }

    public void setTotalDate(String totalDate) {
        this.totalDate = totalDate;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setCalendarFormatDate(){
       this.calendar = Calendar.getInstance();
        this.calendar.set(year,month,day);
    }

    public long getTotalDateLong() {
        return totalDateLong;
    }

    public void setTotalDateLong(long totalDateLong) {
        this.totalDateLong = totalDateLong;
    }

    public String makeTotalDateYMD(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String res =  sdf.format(this.calendar.getTime());//20170420 이런 형식으로

        return  res;

    }


    @Override
    public String toString() {


        String str= "totalDate : "+totalDate+" ";
        String strArr="";
        for(EventData e: eventDataList){
            strArr+="  eventId : "+e.getData(0)+" eventTitle: "+e.getData(2)+"eventLocation : "+e.getData(3)
            +" eventStartTime : "+e.getData(4)+ " eventEndTime : "+e.getData(5)+" ";
        }
        return  str+strArr;
    }


}
