package com.toadstudio.first.toadproject.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.toadstudio.first.toadproject.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by rangkast.jeong on 2018-03-19.
 */

public class Settings_map_Activity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG = "ToadPrj_SettingsMap";
    ArrayList<SettingsMarkerItem> data= new ArrayList<>();
    SettingsMarkerItem items;
    private ListView listView;
    private Settings_marker_Adapter adapter;
    private int selected_position = -1;
    private int checked_item;
    private int selected_cam = 0;
    Button back;
    Button store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_marker_activity);
        listView = (ListView) findViewById(R.id.settings_markerlist);
        final View header = getLayoutInflater().inflate(R.layout.settings_header, null, false);
        listView.addHeaderView(header);

        Intent intent = getIntent();

        selected_cam = intent.getIntExtra("map_choice", -1);

        //마커 설정에서 intent 날라 왔을때 처리
        if (selected_cam > 0) {
            selected_position = selected_cam - 1;
            //        Log.d(TAG, "selected_position" + selected_position);
            //need update
            items = new SettingsMarkerItem(R.drawable.toad_app_icon_v4, "기본", false);
            data.add(items);
            items = new SettingsMarkerItem(R.drawable.toad_app_icon_v4, "위성", false);
            data.add(items);
            items = new SettingsMarkerItem(R.drawable.toad_app_icon_v4, "terrain", false);
            data.add(items);
       //     items = new SettingsMarkerItem(R.drawable.toad_app_icon_v4, "하이브리드", false);
       //     data.add(items);

            adapter = new Settings_marker_Adapter(this, R.layout.settings_marker_item, data);
            listView.setAdapter(adapter);

        }


        back = (Button) findViewById(R.id.button_marker_1);
        store = (Button) findViewById(R.id.button_marker_2);
        back.setOnClickListener(this);
        store.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //        Log.d(TAG, "onItemClick: " + i);
                if (i > 0) {

                                      Intent intent = new Intent(getBaseContext(), InfoActivity.class);
                                      intent.putExtra("from",i + 3);
                                      getBaseContext().startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));

                }
            }
        });

    }//end onCreate


    public class Settings_marker_Adapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<SettingsMarkerItem> data;
        private int layout;


        public Settings_marker_Adapter(Context context, int layout, ArrayList<SettingsMarkerItem> data){
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
        public View getView(final int position, View convertView, final ViewGroup parent){
            ViewHolder holder = null;

            if(convertView==null){
                convertView=inflater.inflate(layout, parent,false);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.check.setTag(position);

            if (selected_position == position) {
                holder.check.setChecked(true);
                checked_item = position + 1;
            //           Log.d(TAG, "checked_item" + checked_item);
            } else {
                holder.check.setChecked(false);
            }

            SettingsMarkerItem listviewitem=data.get(position);
            holder.image.setImageResource(listviewitem.getIcon());
            holder.name.setText(listviewitem.getName());

            holder.check.setOnClickListener(onStateChangedListener(holder.check, position));

            return convertView;
        }

    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selected_position = position;
                } else {
                    selected_position = -1;
                }
                adapter.notifyDataSetChanged();
            }
        };
    }

    private static class ViewHolder {
        TextView name;
        ImageView image;
        CheckBox check;

        public ViewHolder(View v) {
            check = (CheckBox) v.findViewById(R.id.checkBox_marker);
            name = (TextView) v.findViewById(R.id.textview_marker);
            image = (ImageView)v.findViewById(R.id.imageview_marker);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_marker_1:
                //   Log.d(TAG, "canceled");
                Intent intent_cancel = getIntent();
                //       intent.putExtra("data", Integer.toString(checked_item));
                setResult(RESULT_CANCELED, intent_cancel);
                finish();
                break;
            case R.id.button_marker_2:
                //    Log.d(TAG, "stored");

                Toast.makeText(this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                intent.putExtra("data", Integer.toString(checked_item));
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
