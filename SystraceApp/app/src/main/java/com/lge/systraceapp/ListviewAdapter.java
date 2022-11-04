package com.lge.systraceapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Listviewitem> data;
    private ArrayList<Integer> custom_marker;
    private int layout;
    private ImageView imageView;
    public ListviewAdapter(Context context, int layout, ArrayList<Listviewitem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getName();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }

        StringBuilder str = new StringBuilder("");

        Listviewitem listviewitem=data.get(position);
        try {
            TextView name=(TextView)convertView.findViewById(R.id.textview);
            str.append(listviewitem.getName() + " ");
            str.append(listviewitem.getData() + " ");
            name.setText(str.toString());
        } catch (Exception e) {
            //    e.printStackTrace();
            TextView name=(TextView)convertView.findViewById(R.id.textview);
            str.append(listviewitem.getName() + "");
            name.setText(str.toString());
        }

        return convertView;
    }

}