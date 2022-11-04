package com.lge.systraceapp;

import java.io.Serializable;
public class DataInfo implements Serializable {
    public String process = "";
    public String id = "";
    public double time;
    public int value;
    public int pos;
    public int interval_mode;
    public int check_process_mode;


    public DataInfo(String process, String id, double time, int value, int pos, int interval_mode, int check_process_mode) {
        this.process = process;
        this.id = id;
        this.time = time;
        this.value = value;
        this.pos = pos;
        this.interval_mode = interval_mode;
        this.check_process_mode = check_process_mode;
    }
    public String getProcess() { return  process; }
    public String getid() { return  id; }
    public double gettime() {
        return  time;
    }
    public int getvalue() {
        return  value;
    }
    public int getpos() {
        return  pos;
    }
    public int getIntervalmode() {
        return  interval_mode;
    }
    public int getCheck_process_mode() {
        return  check_process_mode;
    }
}
