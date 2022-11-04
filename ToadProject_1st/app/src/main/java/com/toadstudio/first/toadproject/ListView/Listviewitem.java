package com.toadstudio.first.toadproject.ListView;

/**
 * Created by rangkast.jeong on 2018-03-15.
 */

public class Listviewitem {
    private String marker;
    private String name;
    private String pics;
    private String data;
    private int fav;
    private int storage;
    public String getMarker(){return marker;}
    public String getData(){return data;}
    public String getName(){return name;}
    public String getPics(){return  pics;}
    public int getFav(){return  fav;}
    public int getStorage(){return  storage;}
    public Listviewitem(String marker, String data,String name, String pics, int fav, int storage){
        this.marker=marker;
        this.data=data;
        this.name=name;
        this.pics=pics;
        this.fav=fav;
        this.storage = storage;
    }
}
