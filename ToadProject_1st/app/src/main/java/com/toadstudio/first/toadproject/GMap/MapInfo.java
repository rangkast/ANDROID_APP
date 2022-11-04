package com.toadstudio.first.toadproject.GMap;

import java.io.Serializable;

/**
 * Created by rangkast.jeong on 2018-02-27.
 */

public class MapInfo implements Serializable {
    public String id = "";
    public String folder = "";
    public String latitude = "";
    public String longitude = "";
    public String reserved = ""; //not reserved, this is for pictures counting
    public String fav = ""; //즐겨찾기
    public String data = ""; //마커종류
    public String level ="";
    public String storage ="";


    public MapInfo(String id, String folder, String lat, String lon, String reserved, String fav, String data, String level, String storage) {
        this.id = id;
        this.folder = folder;
        this.latitude = lat;
        this.longitude =lon;
        this.reserved = reserved;
        this.fav = fav;
        this.data = data;
        this.level = level;
        this.storage = storage;
    }

    public String getid() { return  id; }
    public String getfolder() {
        return  folder;
    }
    public String getlatitude() {
        return  latitude;
    }
    public String getlongitude() {
        return  longitude;
    }
    public String getreserved() {
        return  reserved;
    }
    public String getFav() { return  fav; }
    public String getdata() {
        return  data;
    }
    public String getlevel() {return  level; }
    public String getStorage() {return  storage; }

}
