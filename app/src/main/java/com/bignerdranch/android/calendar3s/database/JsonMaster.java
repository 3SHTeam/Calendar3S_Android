package com.bignerdranch.android.calendar3s.database;

import com.bignerdranch.android.calendar3s.data.EventData;
import com.bignerdranch.android.calendar3s.data.GroupData;
import com.bignerdranch.android.calendar3s.data.MessageData;
import com.bignerdranch.android.calendar3s.data.TagData;
import com.bignerdranch.android.calendar3s.data.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ieem5 on 2017-05-02.
 */

public class JsonMaster {
    private ArrayList<EventData> events = new ArrayList<EventData>();
    private String result = "";
    private UserData user;
    private ArrayList<TagData> tags = new ArrayList<TagData>();
    private ArrayList<GroupData> Groups =new ArrayList<GroupData>();
    private ArrayList<String> userIds_Arr = new ArrayList<String>();// 회원 아이디
    public ArrayList<MessageData> messages = new ArrayList<MessageData>();


    public ArrayList<MessageData> getMessages() {return messages;}
    public void setMessages(ArrayList<MessageData> messages) {this.messages = messages;}

    public String getResult() {return result;}
    public void setResult(String result) {this.result = result;}

    public ArrayList<EventData> getEvents() {return events;}
    public void setEvents(ArrayList<EventData> events) {this.events = events;}

    public UserData getUser() {return user;}
    public void setUser(UserData user) {this.user = user;}

    public ArrayList<TagData> getTags() {return tags;}
    public void setTags(ArrayList<TagData> tags) {this.tags = tags;}

    public ArrayList<GroupData> getGroups() {return Groups;}
    public void setGroups(ArrayList<GroupData> groups) {Groups = groups;}

    public ArrayList<String> getUserIds_Arr() {return userIds_Arr;}
    public void setUserIds_Arr(ArrayList<String> userIds_Arr) {this.userIds_Arr = userIds_Arr;}


    public void onPostExecute(String php, String str){
        switch(php){
            case "SelectMySchedule":
                selectMySchedule(str);
                break;

            case "SelectMyProfile":
                selectMyProfile(str);
                break;

            case "SelectMyTag":
                selectMyTag(str);
                break;

            case "SelectMyGroup":
                selectMyGroup(str);
                break;

            case "SelectGroupMember":
                selectGroupMember(str);
                break;

            case "SelectMakeGroup":
                selectMakeGroup(str);
                break;

            case "SelectInsertSchedule":
                SelectInsertSchedule(str);
                break;

            case "SelectMyMessage":
                SelectMyMessage(str);
                break;

            case "SelectMyGroupTag":
                SelectMyGroupTag(str);
                break;
        }
    }

    private void SelectMyGroupTag(String str) {

        try{
            JSONObject root = new JSONObject(str);
            if(root.get("rownum").equals("0")) {
                this.result = "";
                System.out.println("그룹태그가 없음!");
                return;
            }

            JSONArray ja = root.getJSONArray("result");

            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                this.result = jo.getString("Tagid");
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    private void SelectMyMessage(String str) {
        String Mid;
        String sender;
        String receiver;
        String type;
        String message;
        String Gid;
        String Gname;

        MessageData mdata;
        try{
            JSONObject root = new JSONObject(str);
            if(root.get("rownum").equals("0")) {
                this.messages = null;
                System.out.println("메세지가 없음!");
                return;
            }

            JSONArray ja = root.getJSONArray("result");

            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                Mid = jo.getString("Mid");
                sender= jo.getString("sender");
                receiver = jo.getString("receiver");
                type = jo.getString("type");
                message = jo.getString("message");
                Gid = jo.getString("Gid");
                Gname = jo.getString("Gname");

                System.out.println(Mid + " , " + sender + " , " + receiver
                        + " , " + type + " , " + message + " , " + Gid + " , " + Gname);

                mdata= new MessageData(Mid, sender, receiver, type, message, Gid, Gname);
                this.messages.add(mdata);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    private void SelectInsertSchedule(String str) {
        String Sid;
        String SMaster;
        String Sname;
        String Place;
        String StartTime;
        String EndTime;

        try{
            JSONObject root = new JSONObject(str);
            if(root.get("rownum").equals("0")) {
                this.result = null;
                System.out.println("스케줄이 없음!");
                return;
            }

            this.result = "0";
            JSONArray ja = root.getJSONArray("result");
            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                Sid = jo.getString("Sid");
                SMaster = jo.getString("SMaster");
                Sname = jo.getString("Sname");
                Place = jo.getString("Place");
                StartTime = jo.getString("StartTime");
                EndTime = jo.getString("EndTime");

                System.out.println(Sid + " , " + SMaster + " , " + Sname
                        + " , " + Place + " , " + StartTime + " , " + EndTime);

               // if(Integer.parseInt(this.result) < Integer.parseInt(Sid))
                this.result = Sid;
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void selectMakeGroup(String str) {
        String Gid;
        try{
            JSONObject root = new JSONObject(str);
            if(root.get("rownum").equals("0")) {
                this.result = null;
                System.out.println("그룹 없음!");
                return;
            }

            JSONArray ja = root.getJSONArray("result");

            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                Gid = jo.getString("Gid");

                System.out.println(Gid);

                this.result = Gid;
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    private void selectGroupMember(String str) {
        String GroupMemberIds;

        try{
            JSONObject root = new JSONObject(str);
            if(root.get("rownum").equals("0")) {
                this.userIds_Arr = null;
                System.out.println("그룹 없음!");
                return;
            }

            JSONArray ja = root.getJSONArray("result");

            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                GroupMemberIds = jo.getString("Googleid");

                System.out.println(GroupMemberIds);

                this.userIds_Arr.add(GroupMemberIds);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }


    }
    private void selectMyGroup(String str) {
        String gid;
        String name;
        String comment;
        String GMaster;

        GroupData group;
        try{
            JSONObject root = new JSONObject(str);
            if(root.get("rownum").equals("0")) {
                this.Groups = null;
                System.out.println("그룹 없음!");
                return;
            }

            JSONArray ja = root.getJSONArray("result");

            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                gid = jo.getString("Gid");
                name= jo.getString("Group_name");
                comment = jo.getString("Group_comment");
                GMaster = jo.getString("GMaster");

                System.out.println(gid + " , " + name + " , " + comment
                        + " , " + GMaster);

                group= new GroupData(gid, name, comment, GMaster);
                this.Groups.add(group);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }


    private void selectMyTag(String str) {
        String Tagid;
        String Tag_name;
        String Color;
        String Font;
        String Size;
        String Gid;

        TagData tag;
        try{
            JSONObject root = new JSONObject(str);
            if(root.get("rownum").equals("0")) {
                this.tags = null;
                System.out.println("태그가 없음!");
                return;
            }

            JSONArray ja = root.getJSONArray("result");

            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                Tagid = jo.getString("Tagid");
                Tag_name= jo.getString("Tag_name");
                Color = jo.getString("Color");
                Font = jo.getString("Font");
                Size = jo.getString("Size");
                Gid = jo.getString("Gid");

                System.out.println(Tagid + " , " + Tag_name + " , " + Color
                        + " , " + Font + " , " + Size + " , " + Gid);

                tag = new TagData(Tagid, Tag_name, Color, /*Font, Size,*/ Gid);
                this.tags.add(tag);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }
    private void selectMyProfile(String str) {
        String Googleid;
        String name;
        String gender;
        String nickname;
        String birth;
        String phone;
        String comment;
        String FBuId;
        String FBToken;

        EventData event;
        try{
            JSONObject root = new JSONObject(str);
            if(root.get("rownum").equals("0")) {
                this.user = null;
                System.out.println("로그인 실패!");
                return;
            }

            JSONArray ja = root.getJSONArray("result");

            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                Googleid = jo.getString("Googleid");
                name= jo.getString("name");
                gender = jo.getString("gender");
                nickname = jo.getString("nickname");
                birth = jo.getString("birth");
                phone = jo.getString("phone");
                comment = jo.getString("comment");
                FBuId = jo.getString("FBuId");
                FBToken = jo.getString("FBToken");

                System.out.println(Googleid + " , " + name + " , " + gender
                        + " , " + nickname + " , " + birth + " , " + phone
                        + " , " + comment + " , " + FBuId + " , " + FBToken );

                this.user = new UserData(Googleid, name, gender, nickname, birth, phone, comment, FBuId, FBToken);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }


    private void selectMySchedule(String str) {
        String Sid;
        String SMaster;
        String Sname;
        String Place;
        String StartTime;
        String EndTime;
        String Gid;
        String GoogleSid;
        String TagId;

        EventData event;
        try{
            JSONObject root = new JSONObject(str);
            if(root.get("rownum").equals("0")) {
                this.events = null;
                System.out.println("태그가 없음!");
                return;
            }

            JSONArray ja = root.getJSONArray("result");
            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                Sid = jo.getString("Sid");
                SMaster = jo.getString("SMaster");
                Sname = jo.getString("Sname");
                Place = jo.getString("Place");
                StartTime = jo.getString("StartTime");
                EndTime = jo.getString("EndTime");
                Gid = jo.getString("Gid");
                GoogleSid = jo.getString("GoogleSid");
                TagId = jo.getString("Tagid");

                System.out.println(Sid + " , " + SMaster + " , " + Sname
                        + " , " + Place + " , " + StartTime + " , " + EndTime
                        + " , " + Gid + " , " + GoogleSid + " , " + TagId);

                //String Sid, String SMaster, String Sname, String Place,
                //String StartTime, String EndTime, String date
                event = new EventData(Sid,SMaster,Sname,Place,StartTime,EndTime,TagId,GoogleSid,Gid);
                this.events.add(event);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }



}