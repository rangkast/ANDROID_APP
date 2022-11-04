package com.lge.systraceapp;

import com.github.mikephil.charting.data.Entry;

import org.w3c.dom.Entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ChartData implements Serializable {
    public String id = "";
    ArrayList<DataInfo> arrayDataList;
    ArrayList<Entry> entries_value;
    ArrayList<Entry> entries_interval;

    public ChartData(String id, ArrayList<DataInfo> arrayDataList, ArrayList<Entry> entries_value, ArrayList<Entry> entries_interval) {
        this.id = id;
        this.arrayDataList = arrayDataList;
        this.entries_value = entries_value;
        this.entries_interval = entries_interval;
    }

    public String getid() { return  id; }
    public ArrayList<DataInfo> getArrayDataList() { return  arrayDataList; }
    public ArrayList<Entry> getValueEntries() { return  entries_value; }
    public ArrayList<Entry> getIntervalEntries() { return  entries_interval; }
}

