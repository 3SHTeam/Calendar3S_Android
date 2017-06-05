package com.bignerdranch.android.calendar3s.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bignerdranch.android.calendar3s.Dialog.ClickDialogFragment;
import com.bignerdranch.android.calendar3s.Dialog.DatePickerFragment;
import com.bignerdranch.android.calendar3s.Dialog.LongClickDialogFragment;
import com.bignerdranch.android.calendar3s.Dialog.TagDIalogFragment;
import com.bignerdranch.android.calendar3s.Dialog.TagListDialogFragment;
import com.bignerdranch.android.calendar3s.MainActivity;
import com.bignerdranch.android.calendar3s.R;
import com.bignerdranch.android.calendar3s.Schedule.ClickListFragment;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleManager;
import com.bignerdranch.android.calendar3s.Schedule.ScheduleStr;
import com.bignerdranch.android.calendar3s.data.CalData;
import com.bignerdranch.android.calendar3s.data.EventData;
import com.bignerdranch.android.calendar3s.data.TagData;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.bignerdranch.android.calendar3s.MainActivity.context;

/**
 * Created by Owner on 2017-01-11.
 */

public class MonthCalendarFragment extends Fragment implements MainActivity.onKeyBackPressedListener{
    private static final String TAG_LIST_DIALOG = "DialogTagList";
    private static final String ADD_TAG_DAILOG = "AddTagDialog";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_SCHEDULE_TAG= "DialogScheduleTag";
    private static final String ARG_CALDATAS="caldata_id";

    private  static final String DIALOG_DAY_SCHEDULE = "DialogDaySchedule";

    private  static final int REQUEST_DAY_SCHDULE = 0;
    private  static final int REQUEST_DIALOG_OPTION = 1;
    private static final int REQUEST_DATE = 2;

    private static final int REQUEST_TAG_LIST_DIALOG = 3;
    private static final int REQUEST_ADD_TAG= 4;

    public  static final String DIALOG_OPTION = "AddModifyFreezeDelete";
    public  static final String ARG_TAGDATA ="tagData";


    //calData 관련


    private ArrayList<CalData> calDatas = new ArrayList<CalData>();
    public void setCalDatas(ArrayList<CalData> calDatas) {
        this.calDatas = calDatas;
    }
    public ArrayList<CalData> getCalDatas() {return calDatas;}
    public void addCalDatas(CalData calData){calDatas.add(calData);}

    private  ArrayList<EventData>eventDatas = new ArrayList<>();
    private  HashMap<String,Boolean> map = null;

    //년 월 출력 텍스트뷰와 날짜 선택 버튼
    private TextView yearTv;
    private TextView monthTv;
    private Button selectDateBtn;
    private Date date;

    private  Calendar mCalToday;
    private Calendar mCal;

    //private  ArrayList<CalData> arrData = new ArrayList<>();

    private ArrayList<ScheduleStr>arrSchedule;

    private GridView mGridView;

    public DateAdapter getDateAdapter() {
        return dateAdapter;
    }

    private DateAdapter dateAdapter;

    private MainActivity main;


    private  Button todayBtn;
    private Button addScheduleTagBtn;
    private  Button ScheduleTagListViewBtn;
    //private  AlertDialog.Builder alertDialogBuilder_Click;


    private int startday;//시작 요일 1:일요일 7:토요일
    private int endDay;
    private  int maximumdayOfMonth;//한달 마지막일
    private EventData eventDataFromMain;
    private String ymdFromMain;

    public void seteventDataFromMain(EventData e){
        this.eventDataFromMain = e;
    }

    public void setYmdFromMain( String ymdFromMain){
       this.ymdFromMain = ymdFromMain;

    }

    public EventData getEventDataFromMain(){
        return eventDataFromMain;
    }
    public String getYmdFromMain(){
        return ymdFromMain;
    }


    public void setArrSchedule(ArrayList<ScheduleStr> arrSchedule) {this.arrSchedule = arrSchedule;}

    //TagDialogFragment로 넘길 tagData



    private ArrayList<TagData> tagDatas = new ArrayList<>();
    public ArrayList<TagData> getTagDatas() { return tagDatas; }
    public void setTagDatas(ArrayList<TagData> tagDatas) { this.tagDatas = tagDatas; }


    public MonthCalendarFragment newInstance(){
        MonthCalendarFragment fragment = new MonthCalendarFragment();
        return fragment;
    }


    //메인의 백키를 실행시킨다.
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
    }

    //자신의 백키를 불러오게 등록
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
            super.onCreate(saveInstanceState);

        main = ((MainActivity)getActivity());

        dateAdapter = new DateAdapter(getActivity(),calDatas);
        initSchedule();

        //처음에 로그인버튼 누르고 캘린더뷰가 나오면 날짜만 메인에서 와있음.
        //아직 스케줄 추가 안한 상태인데 이벤트리스트 읽으려 하면 에러남
        //스케줄 추가를 한 다음에 이벤트 리스트에 접근해야 함...
        for(int i=startday-1;i<startday+endDay-1;i++){

            Log.i("tata","i ,fromMain : 날짜"+"["+i+"] "+calDatas.get(i).getTotalDate());

            if(calDatas.get(i).getEventDataList()!=null ){
                Log.i("tata"," FromMainEvent : "+
                        calDatas.get(i).getEventDataList().toString());
            }

        }

        String a="";
        for(int i=startday-1;i<startday+endDay-1;i++){

            Log.i("ttt"," i ,fromMain : 날짜"+" ["+i+"] "+calDatas.get(i).getTotalDate());

            if(calDatas.get(i).getEventDataList()!=null ){

                a+= "[i : "+i +" ]"+calDatas.get(i).getEventDataList().toString()+" ";
                Log.i("ttt"," ALL FromMainEvent : "+a
                      );
            }

        }

        Log.i("ttt","LEN : "+getCalDatas().size());


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar_month,container,false);

       // getTitleTv = (TextView)v.findViewById(R.id.getTitle);

//오늘 버튼
        todayBtn = (Button)v.findViewById(R.id.todayBtn);

        yearTv = (TextView)v.findViewById(R.id.yearTv);
        monthTv = (TextView)v.findViewById(R.id.monthTv);
        selectDateBtn = (Button)v.findViewById(R.id.selectDateBtn);
        addScheduleTagBtn= (Button)v.findViewById(R.id.ScheduleTagBtn);
        ScheduleTagListViewBtn = (Button)v.findViewById(R.id.ScheduleTagListBtn);

        addScheduleTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager manager = getFragmentManager();
                TagDIalogFragment dialog = new TagDIalogFragment();

                dialog.setTargetFragment(MonthCalendarFragment.this, REQUEST_ADD_TAG);


                dialog.show(manager, ADD_TAG_DAILOG);


            }
        });

       ScheduleTagListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //태그 데이터 보여주는 버튼

                //태그 버튼 누를 때 태그 데이터 넘겨주기

///Month ->TagListDialogFragment
              tagDatas = ((MainActivity)getActivity()).getTagDatas();

               Bundle args =  new Bundle();
                args.putParcelableArrayList(ARG_TAGDATA,tagDatas);

                FragmentManager manager = getFragmentManager();
                TagListDialogFragment dialog = new TagListDialogFragment();

                dialog.setTargetFragment(MonthCalendarFragment.this,REQUEST_TAG_LIST_DIALOG);


                dialog.setArguments(args);






              dialog.show(manager,TAG_LIST_DIALOG);



            }
        });


        //Calendar객체 생성
        mCalToday = Calendar.getInstance();
        mCal = Calendar.getInstance();


        //달력 세팅
       // setCalendarDate(0,mCal.get(Calendar.MONTH)+1,v);
        //dateAdapter와 arrData 연결해야 함.
     mGridView = (GridView)v.findViewById(R.id.calGrid);
      /* dateAdapter = new DateAdapter(getActivity(),arrData);*/

        mGridView.setAdapter(dateAdapter);
       //main에서 calendar객체 읽어와서 년 월 텍스트 뷰 출력하기
        yearTv.setText(main.getCalendar().get(Calendar.YEAR)+"년");
        monthTv.setText((main.getCalendar().get(Calendar.MONTH)+1)+"월");


        //이동할 날짜 고르는 버튼 . 클릭하면 datePicker가 뜬다!!
        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = new Date();
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(MonthCalendarFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);

            }
        });
        //오늘 버튼
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                //오늘 날짜로 이동
                updateDate(calendar);

            }
        });



        return v;
    }





    //DatePicker에서 선택한 날짜 받아오는 코드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            //DatePicker에서 사용자가 선택한 날짜
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            // updateDate();
            //DatePicker에서 사용자가 선택한 날짜 변환
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            String getDataString = format.format(date);
            Log.i("ttt","날짜 : "+getDataString);

            //DatePicker에서 선택한 날짜를 MainActivity로 보내서 다시 날짜 세팅해야 함.
            Calendar testCal = Calendar.getInstance();
           testCal.setTime(date);
            main.setCalendar(testCal);
            Log.i("ttt","mainFromCalendar : "+main.getCalendar().get(Calendar.YEAR)+"년"+
                    ( main.getCalendar().get(Calendar.MONTH)+1)+"월"+main.getCalendar().get(Calendar.DATE)+"일");

            //((MainActivity)getActivity()).setmCal(testCal);
            //datePicker에서 선택한 날짜로 날짜 다시 세팅
          //  ((MainActivity)getActivity()).setCalendarDate(testCal);

            updateDate(testCal);

            Log.i("ttt","변환 날짜 : "+testCal.get(Calendar.YEAR)+"년"+(testCal.get(Calendar.MONTH)+1)+"월"+
                    testCal.get(Calendar.DATE)+"일");
           // testTv.setText(getDataString);
            //updateDate(date);

        }

        else if( requestCode == REQUEST_TAG_LIST_DIALOG){
            //tag hashmap(id,booelan isChecked)

            map = (HashMap<String,Boolean>)data.getSerializableExtra(TagListDialogFragment.EXTRA_TAG_HASHMAP);

            Iterator<String>iterator = map.keySet().iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                for(int j=0;j<tagDatas.size();j++){
                    if(tagDatas.get(j).getData(0).equals(key)){
                        tagDatas.get(j).setCheckbox(map.get(key));
                        System.out.println("key="+key+" / value="+map.get(key));  // 출력
                        break;
                    }
                }//end of for
            }
            main.setTagDatas(tagDatas);
            main.setTagHash(map);
            this.initSchedule();
        }

        else if (requestCode == REQUEST_ADD_TAG){

            String tagName = (String) data.getStringExtra(TagDIalogFragment.EXTRA_TAG_NAME);
            String tagColor = (String) data.getStringExtra(TagDIalogFragment.EXTRA_TAG_COLOR);
            Log.i("AAA"," INSERTTAGCHECK : tagName="+tagName+" / tagColor="+tagColor);
            System.out.print(" INSERTTAGCHECK : tagName="+tagName+" / tagColor="+tagColor);  // 출력
            InsertTAg(tagName,tagColor);

        }

    }

    public void InsertTAg(String Tagname, String Color){
        main.InsertTag(Tagname, Color);
    }

    public void updateDate(Calendar calendar){

        int year = calendar.get(Calendar.YEAR);
        int month  =calendar.get(Calendar.MONTH)+1;
        yearTv.setText(year+"년");
        monthTv.setText(month+"월");
        setCalendarDate(calendar,eventDatas);
        dateAdapter.notifyDataSetChanged();
    }




    class DateAdapter extends BaseAdapter

    {
        //MainActivity의   private ArrayList<CalData> calDatas 의 데이터 저장해야 함...

        // private ImageView imageView;
        private  TextView schedule1Tv;
        private TextView schedule2Tv;
        private  TextView schedule3Tv;
        private  TextView schedule4Tv;
        private Context context;
        private ArrayList<CalData> arrCalDataList;
        private LayoutInflater inflater;

        public DateAdapter(Context c, ArrayList<CalData>arr){
            this.context = c;
            this.arrCalDataList = arr;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrCalDataList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return arrCalDataList.get(position);
        }

        public String matchColorWithId(String tagId){

            String tagColor="#FF0000";
            for(int i=0;i<tagDatas.size();i++){
                if( tagId.equals(tagDatas.get(i).getData(0))){
                    tagColor = tagDatas.get(i).getData(2);
                    break;
                }
            }
            return tagColor;

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                //  view_item은 날짜 표시하는 뷰이다 GridView의 한 칸 담당 뷰
                convertView = inflater.inflate(R.layout.view_item, parent, false);
                //holder= new ViewHolder();
                //날짜 표시 TextView
                //holder.viewText = (TextView)convertView.findViewById(R.id.ViewText);
                // convertView.setTag(holder);
            }

            //holder = (ViewHolder)convertView.getTag();
            //날짜표시 textView
            TextView ViewText = (TextView)convertView.findViewById(R.id.ViewText);

            //스케줄 표시 TextView

            schedule1Tv = (TextView)convertView.findViewById(R.id.schedule1);
            schedule2Tv =  (TextView)convertView.findViewById(R.id.schedule2);
            schedule3Tv =  (TextView)convertView.findViewById(R.id.schedule3);
            schedule4Tv =  (TextView)convertView.findViewById(R.id.schedule4);

            if(arrCalDataList.get(position) == null){
                ViewText.setText("");
                initDate();
            }

            else
            {
                initDate();
                //앱 실행 기준 오늘 날짜 달력에 표시
                Calendar today = Calendar.getInstance();
                int yearOfToday=today.get(Calendar.YEAR);
                int monthOfToday=today.get(Calendar.MONTH);
                int dateOfToday =today.get(Calendar.DATE);

                int date = arrCalDataList.get(position).getDay();

                String y="";
                for(int i = 0 ; i < yearTv.getText().toString().length(); i ++)
                {
                    // 48 ~ 57은 아스키 코드로 0~9이다.
                    if(48 <= yearTv.getText().toString().charAt(i) && yearTv.getText().toString().charAt(i) <= 57)
                        y += yearTv.getText().toString().charAt(i);
                }
                String m="";
                for(int i = 0 ; i < monthTv.getText().toString().length(); i ++)
                {
                    // 48 ~ 57은 아스키 코드로 0~9이다.
                    if(48 <= monthTv.getText().toString().charAt(i) && monthTv.getText().toString().charAt(i) <= 57)
                        m += monthTv.getText().toString().charAt(i);
                }


                ViewText.setText(arrCalDataList.get(position).getDay()+"");

               int  comparedYear=  Integer.parseInt(y);
                int  comparedMonth=  Integer.parseInt(m);
                if(yearOfToday == comparedYear && monthOfToday == (comparedMonth-1) &&
                        dateOfToday ==  arrCalDataList.get(position).getDay()   ){
                    ViewText.setBackgroundColor(Color.GREEN);
                }
                else{
                    ViewText.setBackgroundColor(Color.WHITE);

                }

                int index=0;
                //calDatas에 이벤트 데이터가 없을 때. 즉 스케줄이 하나도 없을 때
                if(  arrCalDataList.get(position).getEventDataList().size()==0){
                    initDate();
                }

                else {
                    ArrayList<EventData> eDatas = new ArrayList<>();
                    eDatas = (ArrayList)arrCalDataList.get(position).getEventDataList();

                    int eDataLen = arrCalDataList.get(position).getEventDataList().size();
/*
            if(tagid == tagDatas.get(i).getData(0))
                color = tagDatas.get(i).getData(2);*/


                   if( eDataLen ==1 ){

                         //EventData객체에 들어있는 tag id
                         String eventTagId = eDatas.get(0).getData(6);
                        String tagColor = matchColorWithId(eventTagId);
                       schedule1Tv.setBackgroundColor(Color.parseColor(tagColor));
                        schedule1Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                        schedule1Tv.setText(eDatas.get(0).getData(2));
                    }
                     if(eDataLen==2){
                         //EventData객체에 들어있는 tag id
                         String eventTagId1 = eDatas.get(0).getData(6);
                         String eventTagId2 = eDatas.get(1).getData(6);
                         String tagColor1 = matchColorWithId(eventTagId1);
                         String tagColor2 = matchColorWithId(eventTagId2);

                         schedule1Tv.setBackgroundColor(Color.parseColor(tagColor1));
                         schedule1Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                         schedule1Tv.setText(eDatas.get(0).getData(2));

                         schedule2Tv.setBackgroundColor(Color.parseColor(tagColor2));
                        schedule2Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                        schedule2Tv.setText(eDatas.get(1).getData(2));
                    }
                    else if(eDataLen ==3 ){

                         String eventTagId1 = eDatas.get(0).getData(6);
                         String eventTagId2 = eDatas.get(1).getData(6);
                         String eventTagId3 = eDatas.get(2).getData(6);
                         String tagColor1 = matchColorWithId(eventTagId1);
                         String tagColor2 = matchColorWithId(eventTagId2);
                         String tagColor3 = matchColorWithId(eventTagId3);

                         schedule1Tv.setBackgroundColor(Color.parseColor(tagColor1));
                         schedule1Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                         schedule1Tv.setText(eDatas.get(0).getData(2));
                         schedule2Tv.setBackgroundColor(Color.parseColor(tagColor2));
                        schedule2Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                        schedule2Tv.setText(eDatas.get(1).getData(2));
                         schedule3Tv.setBackgroundColor(Color.parseColor(tagColor3));
                        schedule3Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                        schedule3Tv.setText(eDatas.get(2).getData(2));
                    }
                    else if(eDataLen >= 4){

                         String eventTagId1 = eDatas.get(0).getData(6);
                         String eventTagId2 = eDatas.get(1).getData(6);
                         String eventTagId3 = eDatas.get(2).getData(6);
                         String tagColor1 = matchColorWithId(eventTagId1);
                         String tagColor2 = matchColorWithId(eventTagId2);
                         String tagColor3 = matchColorWithId(eventTagId3);


                         schedule1Tv.setBackgroundColor(Color.parseColor(tagColor1));
                         schedule1Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                         schedule1Tv.setText(eDatas.get(0).getData(2));
                         schedule2Tv.setBackgroundColor(Color.parseColor(tagColor2));
                        schedule2Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                        schedule2Tv.setText(eDatas.get(1).getData(2));
                         schedule3Tv.setBackgroundColor(Color.parseColor(tagColor3));
                        schedule3Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                        schedule3Tv.setText(eDatas.get(2).getData(2));
                        schedule4Tv.setWidth( (mGridView.getWidth()/mGridView.getNumColumns()));
                        schedule4Tv.setText("...");
                    }

                }





                if(arrCalDataList.get(position).getDayofweek() == 1)
                {   //일요일 빨간색
                    ViewText.setTextColor(Color.RED);
                }
                else if(arrCalDataList.get(position).getDayofweek() == 7)
                { //토요일 파란색
                    ViewText.setTextColor(Color.BLUE);
                }
                else
                {
                    ViewText.setTextColor(Color.BLACK);
                }
            }



            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(MainActivity.this,position+"번째",Toast.LENGTH_SHORT).show();

                    //Log.d("tag",arrCalDataList.get(position).getDay()+" 일"+"\n 오늘날짜"+c.get(Calendar.DAY_OF_MONTH));
                    //arrCalDataList.get(position).getDay() null 에러???!!!

                    //날짜가 없는 빈 칸 클릭했을 때는 아무 반응 없어야 함.
                    if(  arrCalDataList.get(position) == null  ){
                        return;
                    }





                    //FragmentManager manager = getFragmentManager();

                  /*  ClickDialogFragment dialog = new ClickDialogFragment();


                    dialog.setSelectedCal(arrCalDataList.get(position).getCalendar());
                    ArrayList<EventData>eventDataArrayList = new ArrayList<EventData>();
                    eventDataArrayList= arrCalDataList.get(position).getEventDataList();

                    dialog.setEventDataArrayList(eventDataArrayList);
                    dialog.setTargetFragment(MonthCalendarFragment.this,REQUEST_DAY_SCHDULE);
                    dialog.show(manager,DIALOG_DAY_SCHEDULE);

                    Log.i("AAA",arrCalDataList.get(position).getTotalDate()+"이벤트"+
                            arrCalDataList.get(position).getEventDataList().toString());*/
                    ClickListFragment clickListFragment  = new ClickListFragment();
                    //하루 일정 리스트 전부다 보여주어야 하므로 날짜 정보 갖고 있는  calendar를 세팅
                    clickListFragment.setCalendar(arrCalDataList.get(position).getCalendar());
                    Log.i("listClick",arrCalDataList.get(position).getTotalDate());
                    //이벤트 타이틀 클릭하면 해당 이벤트 정보가 뿌려지는 화면이 나와서 수정 또는 삭제를 해야 하므로
                    //이벤트 데이터 객체 세팅
                   /* clickListFragment.setEventDataArrayList( ((MainActivity)getActivity()).getEventDatas());*/
                    clickListFragment.setEventDataArrayList( getCalDatas().get(position).getEventDataList());
                    FragmentTransaction ft = getFragmentManager().beginTransaction().
                            replace(R.id.container,clickListFragment);
                    //ft.addToBackStack(null);
                    clickListFragment.
                            setTargetFragment(getFragmentManager().findFragmentById(R.id.calendars),REQUEST_DAY_SCHDULE);
                    ft.commit();


                }
            });

            mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    //날짜가 없는 빈 칸 클릭했을 때는 아무 반응 없어야 함.
                    if(  arrCalDataList.get(position) == null  ){
                        return true;
                    }
                    //월,일이 한자리수일 경우 0을 붙여준다
                   /* String monthClean= monthStr.replaceAll("[^0-9]", "");
                    Log.i("tag","monthStr:"+monthStr);
                    if(Integer.parseInt(monthClean) <10){
                        monthClean = "0"+monthClean;
                    }
                    Log.i("tag","monthClean:"+monthClean);*/
                    //String dayClean= arrCalDataList.get(position).getDay().replaceAll("[^0-9]", "");
                    // Log.i("tag","dayClean:"+dayClean);
                    //  if( Integer.parseInt(dayClean) <10){
                    //dayClean = "0"+dayClean;
                    // }
                    //Log.i("tag","dayClean:"+dayClean);
                    //String date = yearStr+" "+monthStr+" "+arrCalDataList.get(position).getDay()+"일";
                    int month = arrCalDataList.get(position).getMonth();
                    String monthStr = null;
                    if( month<10){
                        monthStr="0"+month;
                    }
                    int day = arrCalDataList.get(position).getDay();
                    String dayStr=day+"";
                    if( day<10) dayStr="0"+day;
                    String date = arrCalDataList.get(position).getYear()+" "+monthStr+"월"+dayStr+"일";
                    Log.i("tag","date:"+date);
                    FragmentManager manager = getFragmentManager();
                    ///
                    FragmentTransaction transaction = manager.beginTransaction();
                    //  ClickDialogFragment dialog = ClickDialogFragment.newInstance(date,scheduleArr);
                    LongClickDialogFragment dialog = new LongClickDialogFragment();
                    //날짜는 Calendar로 하자.
                    //dialog.setSelectedDate(date);


                    //날짜칸에 해당하는 날짜 LongClickDialogFragment로 전달.
                    //date말고 calendar로 전달했으므로
                    dialog.setSelectedCal(arrCalDataList.get(position).getCalendar());
                    dialog.setTargetFragment(MonthCalendarFragment.this,REQUEST_DIALOG_OPTION);
                    dialog.show(manager,DIALOG_OPTION);
                   // transaction.addToBackStack(null);
                    //dialog.getCalData().getEventDataList()
                    //arrCalDataList.get(position).getEventDataList().add(dialog.getCalData().getEventDataList().get())??

                    return true;
                }
            });
            return convertView;
        }

        public void initDate(){
            schedule1Tv.setText("");
            schedule2Tv.setText("");
            schedule3Tv.setText("");
            schedule4Tv.setText("");
            schedule1Tv.setBackgroundColor(Color.WHITE);
            schedule2Tv.setBackgroundColor(Color.WHITE);
            schedule3Tv.setBackgroundColor(Color.WHITE);
            schedule4Tv.setBackgroundColor(Color.WHITE);
        }


    }


    private void initSchedule(){
        //메인에서 내 스케줄 데이터와 태그 데이터를 가져온다.
        eventDatas = main.getEventDatas();

        if(eventDatas != null) {
            for (int i = 0; i < eventDatas.size(); i++) {
                Log.i("AAA", "from메인이벤트확인" + "[" + i + "] " + eventDatas.get(i).toString());
            }
        }

        tagDatas = main.getTagDatas();
        map = main.getTagHash();

        if(map!=null){
            Iterator<String>iterator = map.keySet().iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                for(int j=0;j<tagDatas.size();j++){
                    if(tagDatas.get(j).getData(0).equals(key)){
                        tagDatas.get(j).setCheckbox(map.get(key));
                        System.out.println("key="+key+" / value="+map.get(key));  // 출력
                        break;
                    }
                }//end of for
            }
        }

        Calendar calendar = main.getCalendar();

        //데이터를 토대로 CalDatas를 작성
        setCalendarDate(calendar,eventDatas);

        //어뎁터 갱신
        dateAdapter.notifyDataSetChanged();
    }



    public void setCalendarDate(Calendar calendar,ArrayList<EventData>eventDatas){
        //앱 실행시 날짜로 세팅 calData ArrayList ->calDatas
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1);
        startday = mCalendar.get(Calendar.DAY_OF_WEEK); //시작 요일을(1일) 리턴

        //기존에 있던 데이터 지우고 그렇지 않으면 달력이 계속 이어서 출력됨
       calDatas.clear();
        if(startday != 1){
            for(int i=0;i<startday-1;i++){
                addCalDatas(null);
            }
        }
        //요일은 +1 해야 되기 때문에 달력에 요일을 세팅할 때는 -1 해준다.
       this.eventDatas =eventDatas;
        //this.eventDatas =null;

        endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//마지막 날짜
        //캘린더 날짜 세팅
        for(int i=0;i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH);i++){
            mCalendar.set(Calendar.DATE,(i+1));
           /* addCalDatas(new CalData(mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH),(i+1),mCalendar.get(Calendar.DAY_OF_WEEK),eventDatas));*/

            addCalDatas(new CalData(mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH),(i+1),mCalendar.get(Calendar.DAY_OF_WEEK)));
        }

        //캘린더 날짜 세팅 후 이벤트 날짜와 일치하는 캘린더에 eventDatas 삽입

          // eventStartTime = this.eventDatas.get

     //yyyyMMddHHmm
        //yyyyMMdd로 바꾸려면 0~7까지 잘라야함

        for(int i=startday-1;i<startday+endDay-1;i++) {

            if (eventDatas != null){
                for (int j = 0; j < this.eventDatas.size(); j++) {
                    if (calDatas.get(i).getTotalDate().equals((this.eventDatas.get(j).getData(4)).substring(0, 8))) {
                        String tagid = eventDatas.get(j).getData(6);

                        for (int k = 0; k < tagDatas.size(); k++) {
                            if (tagDatas.get(k).getData(0).equals(tagid) && tagDatas.get(k).isCheckbox()) {

                                Log.i("AAA", "totalDate : " + calDatas.get(i).getTotalDate());
                                Log.i("AAA", "eventDate : " + (this.eventDatas.get(j).getData(4)).substring(0, 8));

                                // yyyyMMdd 형식의 날짜가 같으면 그 EventData 객체를 EventDataList에 대입한다.
                                if (calDatas.get(i).getEventDataList() == null) {
                                    calDatas.get(i).addFirstEventData(this.eventDatas.get(j));
                                } else {
                                    calDatas.get(i).getEventDataList().add(this.eventDatas.get(j));
                                }
                            }
                        }
                    }
                }

        }
        }//end of for

 //eventDatas

        if(eventDatas!=null) {
            for (int i = 0; i < eventDatas.size(); i++) {

                Log.i("AAA", "event날짜: " + (this.eventDatas.get(i).getData(4)).substring(0, 8));

                Log.i("AAA", "추가이벤트확인" + "[" + i + "] " + eventDatas.get(i).toString());


            }
        }
        //날짜별 스케줄
        String a="";
        for(int i=startday-1;i<startday+endDay-1;i++){

            Log.i("AAA"," i ,totalDate  : 날짜"+" ["+i+"] "+calDatas.get(i).getTotalDate());

            if(calDatas.get(i).getEventDataList()!=null ){

                a+= "[i : "+i +" ]"+calDatas.get(i).getEventDataList().toString()+" ";
                Log.i("AAA"," ||calDataEventList : "+a
                );
            }

        }//END OF for



    }

}
