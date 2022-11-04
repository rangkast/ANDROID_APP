package com.toadstudio.first.toadproject.GMap;

import java.io.Serializable;

/**
 * Created by rangkast.jeong on 2018-02-27.
 */

public class SettingInfo implements Serializable {
    public String id = "";
    public String path = "";
    public String latitude = "";
    public String longitude = "";
    public String xcord = "";
    public String ycord = "";
    public String marker = "";
    public String level ="";
    public String storage_status ="";
    public String map_status ="";


    public SettingInfo(String id, String path, String lat, String lon, String xcord, String ycord, String marker, String level, String storage_status, String map_status) {
        this.id = id;
        this.path = path;
        this.latitude = lat;
        this.longitude =lon;
        this.xcord = xcord;
        this.ycord = ycord;
        this.marker = marker;
        this.level = level;
        this.storage_status = storage_status;
        this.map_status = map_status;
    }

    public String getid() { return  id; }
    public String getPath() {
        return  path;
    }
    public String getLatitude() {
        return  latitude;
    }
    public String getLongitude() {
        return  longitude;
    }
    public String getXcord() {
        return  xcord;
    }
    public String getYcord() {
        return  ycord;
    }
    public String getMarker() {
        return  marker;
    }
    public String getLevel() {return  level; }
    public String getStorage_status() {return  storage_status; }
    public String getMap_status() {return  map_status; }

}