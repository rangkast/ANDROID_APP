package com.lge.systraceapp;

import java.io.Serializable;

public class Listviewitem implements Serializable  {
    private String name;
    private String data;
    public String getData(){return data;}
    public String getName(){return name;}

    public Listviewitem(String name, String data){
        this.data=data;
        this.name=name;
    }
}
