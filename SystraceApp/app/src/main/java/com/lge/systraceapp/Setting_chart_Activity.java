package com.lge.systraceapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Setting_chart_Activity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG = "SystraceApp:setting_chart";
    ArrayList<SettingsChartItem> data= new ArrayList<>();
    SettingsChartItem items;
    private ListView listView;
    private Settings_marker_Adapter adapter;
    private int selected_position = -1;
    private int checked_item;
    private int selected_cam = 0;
    Button back;
    Button store;
    Button list_btn;
    Vibrator vibrator;
    EditText editText1, editText2;
    private ArrayList<Listviewitem> savedDrawingChartData = new ArrayList<>();
    private static ListviewAdapter listviewAdapter;
    private Listviewitem item;

    private ArrayList<Listviewitem> mDrawingChartData;
    private ArrayList<Listviewitem> mTimeData;
    private ArrayList<Listviewitem> mAtraceData;
    private Activity mActivity;
    private static int maxDisplayWidth;
    private static int maxDisplayHeight;
    private ArrayList<Listviewitem> data_atrace = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Setting");
        setContentView(R.layout.setting_chart_activity);
        listView = (ListView) findViewById(R.id.settings_markerlist);
        final View header = getLayoutInflater().inflate(R.layout.settings_header, null, false);
        listView.addHeaderView(header);

        mActivity = this;
        mDrawingChartData = (ArrayList<Listviewitem>)getIntent().getSerializableExtra("mDrawingChartData");
        mTimeData = (ArrayList<Listviewitem>)getIntent().getSerializableExtra("mTimeData");
        mAtraceData = (ArrayList<Listviewitem>)getIntent().getSerializableExtra("mAtraceData");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        for (int i = 0; i < mDrawingChartData.size(); i++) {
            boolean status = false;

            if (Integer.parseInt(mDrawingChartData.get(i).getData()) == 1) {
                status = true;
            }

            items = new SettingsChartItem(R.drawable.pen, mDrawingChartData.get(i).getName(), status);
            data.add(items);
        }

        adapter = new Settings_marker_Adapter(this, R.layout.settings_chart_item, data);
        listView.setAdapter(adapter);
        Point displaySize = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(displaySize);
        maxDisplayWidth = displaySize.x;
        maxDisplayHeight = displaySize.y;


        /*
        selected_cam = intent.getIntExtra("map_choice", -1);

        //마커 설정에서 intent 날라 왔을때 처리
        if (selected_cam > 0) {
            selected_position = selected_cam - 1;
            //        Log.d(TAG, "selected_position" + selected_position);
            //need update
            items = new SettingsChartItem(R.drawable.pen, "기본", false);
            data.add(items);
            items = new SettingsChartItem(R.drawable.pen, "위성", false);
            data.add(items);
            items = new SettingsChartItem(R.drawable.pen, "terrain", false);
            data.add(items);
            //     items = new SettingsMarkerItem(R.drawable.toad_app_icon_v4, "하이브리드", false);
            //     data.add(items);

            adapter = new Settings_marker_Adapter(this, R.layout.settings_chart_item, data);
            listView.setAdapter(adapter);

        }
*/

        back = (Button) findViewById(R.id.button_marker_1);
        store = (Button) findViewById(R.id.button_marker_2);
        list_btn = (Button) findViewById(R.id.button_marker_3);
        editText1 = (EditText) findViewById(R.id.editText_1);
        editText2 = (EditText) findViewById(R.id.editText_2);
        back.setOnClickListener(this);
        store.setOnClickListener(this);
        list_btn.setOnClickListener(this);


        if (!mTimeData.isEmpty()) {
            for (int i = 0; i < mTimeData.size(); i++) {
                if (mTimeData.get(i).getName().equals("TraceTime")) {
                    editText1.setText(mTimeData.get(i).getData());
                }
                if (mTimeData.get(i).getName().equals("MaxWaitTime")) {
                    editText2.setText(mTimeData.get(i).getData());
                }
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + i);
                if (i > 0) {

                }
            }
        });

    }//end onCreate


    public class Settings_marker_Adapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<SettingsChartItem> data;
        private int layout;


        public Settings_marker_Adapter(Context context, int layout, ArrayList<SettingsChartItem> data){
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
      //      Log.d(TAG, "getview");
            if(convertView==null){
                convertView=inflater.inflate(layout, parent,false);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.check.setTag(position);

            if (data.get(position).getisChecked() == true) {
                holder.check.setChecked(true);
            } else {
                holder.check.setChecked(false);
            }
            /*
            for (int i = 0; i < data.size(); i++) {
                Log.d(TAG, "stats: " + data.get(i).getisChecked() + " pos " + position + " ");
            }




            if (selected_position == position) {
                holder.check.setChecked(true);
                checked_item = position + 1;
                           Log.d(TAG, "checked_item" + checked_item);
            } else {
                holder.check.setChecked(false);
            }
*/
            SettingsChartItem listviewitem=data.get(position);
            holder.image.setImageResource(listviewitem.getIcon());
            holder.name.setText(listviewitem.getName());

            holder.check.setOnClickListener(onStateChangedListener(holder.check, position));

            return convertView;
        }

    }



    private final int UPDATE_DATA = 0;



    private final int CHART_ITEM_TRUE = 0;
    private final int CHART_ITEM_FALSE = 1;

    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            //      Log.d(TAG, "mHandler " + msg.what);
            switch (msg.what) {
                case UPDATE_DATA:

                    switch (msg.arg1) {
                        case CHART_ITEM_TRUE:
                            items = new SettingsChartItem(R.drawable.pen, mDrawingChartData.get(msg.arg2).getName(), true);
                            data.set(msg.arg2, items);
                            break;
                        case CHART_ITEM_FALSE:
                            items = new SettingsChartItem(R.drawable.pen, mDrawingChartData.get(msg.arg2).getName(), false);
                            data.set(msg.arg2, items);
                            break;
                    }
           //         adapter.notifyDataSetChanged();
                break;
            }

            super.handleMessage(msg);
        }
    };

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "selected " + position + " check " + checkBox.isChecked());

                    if (checkBox.isChecked()) {
                        mHandler.sendMessage(mHandler.obtainMessage(
                                UPDATE_DATA,
                                CHART_ITEM_TRUE, position));

                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(
                                UPDATE_DATA,
                                CHART_ITEM_FALSE, position));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


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
                String edit1 = editText1.getText().toString();
                String edit2 = editText2.getText().toString();
                Log.d(TAG, "edit1: " + edit1 + " edit2: " + edit2);


                if (edit1.length() == 0 || edit2.length() ==0) {
                    Log.d(TAG, "need data");
                    Toast.makeText(this, "Null data", Toast.LENGTH_SHORT).show();
                } else {
                    int count = 0;
                    savedDrawingChartData.clear();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getisChecked() == true) {
                            item = new Listviewitem(data.get(i).getName(), "1");
                            count++;
                        } else {
                            item = new Listviewitem(data.get(i).getName(), "0");
                        }
                        savedDrawingChartData.add(item);
                    }

                    Log.d(TAG, "checked: " + count);

                    if (count > 4) {
                        Toast.makeText(this, "MAX Drawing chart 4", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();

                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 0; i < savedDrawingChartData.size(); i++) {
                            stringBuilder.append(savedDrawingChartData.get(i).getName() + "," + savedDrawingChartData.get(i).getData() + "/");
                        }


                        StringBuilder stringBuilder_a = new StringBuilder();

                        for (int i = 0; i < mAtraceData.size(); i++) {
                            stringBuilder_a.append(mAtraceData.get(i).getName() + "," + mAtraceData.get(i).getData() + "&");
             //               Log.d(TAG, stringBuilder_a.toString());
                        }

                        Intent intent = getIntent();
//                        intent.putExtra("atraceData", mAtraceData);
                        intent.putExtra("result", stringBuilder.toString());
                        intent.putExtra("result_time", "TraceTime," + edit1 + "/" + "MaxWaitTime," + edit2 + "/");

                        intent.putExtra("atraceData", stringBuilder_a.toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                break;
            case R.id.button_marker_3:
                Log.d(TAG, "view atrace lists");
                v = mActivity.getLayoutInflater().inflate(R.layout.listview_layout, null);

                final ListView listView=(ListView)v.findViewById(R.id.listviewbtn);
                Listviewitem items = null;
                data_atrace= new ArrayList<>();

                if (!mAtraceData.isEmpty()) {
                    for (int i = 0; i < mAtraceData.size(); i++) {
                        if (Integer.parseInt(mAtraceData.get(i).getData()) == 1) {
                            items = new Listviewitem(mAtraceData.get(i).getName(), "   V");
                        } else {
                            items = new Listviewitem(mAtraceData.get(i).getName(), "");
                        }
                        data_atrace.add(items);
                    }
                }

                listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data_atrace);
                listView.setAdapter(listviewAdapter);

                listviewAdapter.notifyDataSetChanged();

                final AlertDialog listDialog = new AlertDialog.Builder(mActivity).create();
                listDialog.setView(v);
                listDialog.setTitle("Atrace Categories");
                listDialog.setIcon(R.drawable.pen);

                listDialog.getWindow().setLayout(maxDisplayWidth  * 3/4, maxDisplayHeight * 3/4);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //          listDialog.dismiss();
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        vibrator.vibrate(100);

                        final String name = listviewAdapter.getItem(position);
                        Log.d(TAG, "list name " + name + " position " + position);

                        Listviewitem items = null;
                        if (!mAtraceData.isEmpty()) {
                            for (int i = 0; i < mAtraceData.size(); i++) {
                                if (name.contains(mAtraceData.get(i).getName())) {
                                    if (Integer.parseInt(mAtraceData.get(i).getData()) == 1) {
                                        items = new Listviewitem(mAtraceData.get(i).getName(), "");
                                        mAtraceData.set(i, new Listviewitem(mAtraceData.get(i).getName(), "0"));
                                    } else {
                                        items = new Listviewitem(mAtraceData.get(i).getName(), "   V");
                                        mAtraceData.set(i, new Listviewitem(mAtraceData.get(i).getName(), "1"));
                                    }
                                    data_atrace.set(position, items);
                                }
                            }
                        }
                        listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data_atrace);
                        listView.setAdapter(listviewAdapter);

                        listviewAdapter.notifyDataSetChanged();

                        for (int i = 0; i < mAtraceData.size(); i++) {
                            if (mAtraceData.get(i).getData().equals("0")) {
                                Log.d(TAG, mAtraceData.get(i).getName() + " / " + mAtraceData.get(i).getData());
                            }
                        }

                        return false;
                    }
                });

                listDialog.show();

                break;
        }
    }
}

