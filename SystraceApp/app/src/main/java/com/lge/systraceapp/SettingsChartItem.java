package com.lge.systraceapp;

public class SettingsChartItem  {
    private int icon;
    private String name;
    private boolean isChecked;
    public int getIcon(){return icon;}
    public String getName(){return name;}
    public boolean getisChecked(){return isChecked;}
    public SettingsChartItem(int icon,String name, boolean isChecked){
        this.icon=icon;
        this.name=name;
        this.isChecked=isChecked;
    }
}
