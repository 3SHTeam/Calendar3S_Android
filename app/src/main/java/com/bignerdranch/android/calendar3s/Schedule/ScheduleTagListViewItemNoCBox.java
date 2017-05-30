package com.bignerdranch.android.calendar3s.Schedule;

/**
 * Created by ieem5 on 2017-05-18.
 */

public class ScheduleTagListViewItemNoCBox {
    //태그ID, , 태그이름, 태그 색깔,그룹ID, checkedOrNot

    private String tagId;
    private String tagTitle;
    private int tagTvColor;
    private String groupId;

    //private  boolean checkedOrNot;

   /* public boolean isCheckedOrNot() {
        return checkedOrNot;
    }
    public void setCheckedOrNot(boolean checkedOrNot) {
        this.checkedOrNot = checkedOrNot;
        // this.checkBox.setChecked(this.checkedOrNot);
    }*/

    public String getTagTitle() {
        return tagTitle;
    }
    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public int getTagTvColor() {
        return tagTvColor;
    }
    public void setTagTvColor(int tagTvColor) {
        this.tagTvColor = tagTvColor;
    }

    public String getTagId() { return tagId; }
    public void setTagId(String tagId) { this.tagId = tagId; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }




}
