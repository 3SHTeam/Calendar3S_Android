package com.bignerdranch.android.calendar3s.Schedule;

/**
 * Created by Owner on 2017-01-31.
 */

public class ScheduleStr {
    String schedule1;
    String schedule2;
    String schedule3;

    public ScheduleStr(String schedule1, String schedule2, String schedule3) {
        this.schedule1 = schedule1;
        this.schedule2 = schedule2;
        this.schedule3 = schedule3;
    }

    public String getSchedule1() {
        return schedule1;
    }

    public void setSchedule1(String schedule1) {
        this.schedule1 = schedule1;
    }

    public String getSchedule2() {
        return schedule2;
    }

    public void setSchedule2(String schedule2) {
        this.schedule2 = schedule2;
    }

    public String getSchedule3() {
        return schedule3;
    }

    public void setSchedule3(String schedule3) {
        this.schedule3 = schedule3;
    }
}