package com.toadstudio.first.toadproject.Settings;

/**
 * Created by rangkast.jeong on 2018-03-19.
 */

public class SettingsMarkerItem  {
    private int icon;
    private String name;
    private boolean isChecked;
    public int getIcon(){return icon;}
    public String getName(){return name;}
    public boolean isChecked(){return isChecked;}
    public SettingsMarkerItem(int icon,String name, boolean isChecked){
        this.icon=icon;
        this.name=name;
        this.isChecked=isChecked;
    }
}