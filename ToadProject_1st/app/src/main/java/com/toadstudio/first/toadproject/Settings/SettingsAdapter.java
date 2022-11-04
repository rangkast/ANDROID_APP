package com.toadstudio.first.toadproject.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toadstudio.first.toadproject.R;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by rangkast.jeong on 2018-03-16.
 */

public class SettingsAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;

    private ArrayList<SettingsItem> mData = new ArrayList<SettingsItem>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public SettingsAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final SettingsItem item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final SettingsItem item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public SettingsItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.settings_item, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.text_1);
                    holder.txtValue = (TextView) convertView.findViewById(R.id.text_2);
                    break;
                case TYPE_HEADER:
                    convertView = mInflater.inflate(R.layout.settings_header, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.settings_header);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(rowType == TYPE_ITEM){
            holder.textView.setText(mData.get(position).getText_1());
            holder.txtValue.setText(""+mData.get(position).getText_2());
        }else if(rowType == TYPE_HEADER){
            holder.textView.setText(mData.get(position).getText_1());
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public TextView txtValue;
    }

}
