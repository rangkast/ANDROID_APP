package com.lge.systraceapp;

public class SettingsMarkerItem  {
    private int icon;
    private String name;
    private String path;
    public int getIcon(){return icon;}
    public String getName(){return name;}
    public String getPath(){return path;}
    public SettingsMarkerItem(int icon,String name, String path){
        this.icon=icon;
        this.name=name;
        this.path=path;
    }
}