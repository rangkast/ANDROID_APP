package com.toadstudio.first.toadproject.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toadstudio.first.toadproject.R;
import com.toadstudio.first.toadproject.Settings.Settings_GridActivity;

import java.util.ArrayList;

/**
 * Created by rangkast.jeong on 2018-03-15.
 */

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
        Settings_GridActivity settings_gridActivity = new Settings_GridActivity();

        Listviewitem listviewitem=data.get(position);
        imageView = (ImageView) convertView.findViewById(R.id.imageview);
        try {
            custom_marker = settings_gridActivity.get_data(Integer.parseInt(listviewitem.getMarker()));
            imageView.setImageResource(custom_marker.get(Integer.parseInt(listviewitem.getData())));
            TextView name=(TextView)convertView.findViewById(R.id.textview);
            str.append(listviewitem.getName() + "  ");
            str.append(listviewitem.getPics() + " pictures");
            name.setText(str.toString());
        } catch (Exception e) {
        //    e.printStackTrace();
            imageView.setImageResource(R.drawable.folder3);
            TextView name=(TextView)convertView.findViewById(R.id.textview);
            str.append(listviewitem.getName() + "");
            name.setText(str.toString());
        }

        ImageView favorite=(ImageView)convertView.findViewById(R.id.imageview_2);
        if (listviewitem.getFav() == 0) {
            favorite.setImageResource(R.color.transparent);
        } else {
            favorite.setImageResource(R.drawable.star1);
        }
        ImageView storage=(ImageView)convertView.findViewById(R.id.imageview_3);
        if (listviewitem.getStorage() == 0) {
            storage.setImageResource(R.drawable.internal50);
        } else if (listviewitem.getStorage() == 1) {
            storage.setImageResource(R.drawable.external50);
        } else if (listviewitem.getStorage() == 2) {
            storage.setImageResource(R.drawable.both50);
        } else {
            storage.setImageResource(R.color.transparent);
        }
        return convertView;
    }

}
