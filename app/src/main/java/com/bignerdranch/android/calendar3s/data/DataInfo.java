package com.bignerdranch.android.calendar3s.data;

import java.io.Serializable;

/**
 * Created by ieem5 on 2017-04-26.
 */

public interface DataInfo {
    public String[] getData();
    public String getData(int index);
    public void setData(int index,String data);
    public void setData(String[] uData);

    public String getSendSQLString();
    public void showData();
}
