package com.lge.systraceapp;


import android.Manifest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.GuardedBy;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.lge.systraceapp.R;

import com.github.mikephil.charting.charts.Chart;

import static com.lge.systraceapp.MainActivity.mediaStorageDir_settings;

/**
 * Example of a dual axis {@link LineChart} with multiple data sets.
 *
 * @since 1.7.4
 * @version 3.1.0
 */
public class Systrace_chart extends DemoBase implements OnSeekBarChangeListener, OnChartValueSelectedListener, View.OnClickListener, View.OnTouchListener {

    private LineChart chart, chart2;
    //    private SeekBar seekBarX, seekBarY;
    private TextView char_data, char_data_2, tvY;
    private final int READ = 0;
    private final int WRITE = 1;
    private final int DELETE = 2;

    private final int DATA_START = 1;
    private final int DATA_STOP = 0;

    private final int NOT_CHECK_PROCESS = 0;
    private final int CHECK_PROCESS = 1;

    private final int UPDATE_CHART = 10;
    private final int PROGRESS = 12;
    private final int DATA_COPY = 13;
    private final int START = 0;
    private final int STOP = 1;

    private static final String TAG = "SystraceApp:Chart";
  //  static File mediaStorageDir;

    /* interval mode */
    private final int MODE_BOTH = 0;
    private final int MODE_ONE_TO_ONE = 1;
    private final int MODE_ONE_TO_ZERO = 2;

    private ArrayList<ChartData> mChartData = new ArrayList<>();
    private ChartData mChartDataClass;
    private DataInfo mDataInfoClass;
    private ArrayList<Listviewitem> mDrawingChartData;

    private int progress_status = 0;

    private ArrayList<String> mCheckProcess = new ArrayList<>();

    /* delivery Input*/
    private ArrayList<Entry> values7 = new ArrayList<>();

    private double start_time = 0.0d;
    private float max_wait_time = 33.2f; //4 frames

    private int enable_interval = 1;

    private int crop_mode = 0;

    @GuardedBy("mLock")
    private final Object mLock = new Object();

    private ReadData readData;
    static String intent_path = null;
    static String name = null;
    static String focus_path = null;
    private static ListviewAdapter listviewAdapter;
    private static ImageView list_button;
    private static ImageView convert_button;
    static Activity mActivity;
    private static int maxDisplayWidth;
    private static int maxDisplayHeight;
    Vibrator vibrator;

    private float event_x = 0;
    private float event_y = 0;
    private long time = 0;
    private FileCopyTask fileCopyTask;
    private SaveToGalleryTask saveToGalleryTask;
    private int focused_chart = 0;
    private static MainActivity mainActivity;

    private ProgressBar selectProgress;

    private CopyThread copyThread;

    private int selected = 0;
    private int long_click = 0;

    private double time_x1 = 0;
    private double time_x2 = 0;

    public class CopyThread extends Thread {
        private String status;

        public void run() {

            try {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                        PROGRESS,
                        START, 0), 0);

                if (crop_mode == 1) {



                    mainActivity.txt_file_rw(intent_path, mainActivity.DELETE, focus_path, name + "_crop.html", mainActivity.CROP_MODE);


                    status = mainActivity.txt_file_rw(focus_path, mainActivity.ASSETS_COPY, "source.html", name + "_crop.html", mainActivity.CROP_MODE);
                    if (status != null) {
                        if (status.equals("exist")) {
                            Log.d(TAG, "exist html file");
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                    PROGRESS,
                                    STOP, 0), 0);
                            try {
                                Intent intent = null;
                                intent = new Intent(mActivity, Web_View.class);
                                intent.putExtra("path", focus_path + "/" + name + "_crop.html");
                                if (intent != null) {
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }

                    mainActivity.txt_file_rw(intent_path, mainActivity.HTML_WRITE, focus_path, name + "_crop.html", mainActivity.CROP_MODE);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                            PROGRESS,
                            STOP, 0), 0);

                    try {
                        Intent intent = null;
                        intent = new Intent(mActivity, Web_View.class);
                        intent.putExtra("path", focus_path+"/"+name+"_crop.html");
                        if (intent != null) {
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                    crop_mode = 0;

                } else {
                    status = mainActivity.txt_file_rw(focus_path, mainActivity.ASSETS_COPY, "source.html", name + ".html", 0);
                    if (status != null) {
                        if (status.equals("exist")) {
                            Log.d(TAG, "exist html file");
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                    PROGRESS,
                                    STOP, 0), 0);
                            try {
                                Intent intent = null;
                                intent = new Intent(mActivity, Web_View.class);
                                intent.putExtra("path", focus_path + "/" + name + ".html");
                                if (intent != null) {
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }

                    mainActivity.txt_file_rw(intent_path, mainActivity.HTML_WRITE, focus_path, name + ".html", 0);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                            PROGRESS,
                            STOP, 0), 0);
                    try {
                        Intent intent = null;
                        intent = new Intent(mActivity, Web_View.class);
                        intent.putExtra("path", focus_path+"/"+name+".html");
                        if (intent != null) {
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class SaveToGalleryTask extends  AsyncTask {
        private Context mContext;
        private String mName;
        LineChart mChart;
        public SaveToGalleryTask(Context context, LineChart chart, String name) {
            mContext = context;
            mName = name;
            mChart = chart;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                saveToGallery(mChart, mName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Object o) {
        }
    }



    private class FileCopyTask extends  AsyncTask {
        private Context mContext;
        private String status;
        public FileCopyTask(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {

                status = mainActivity.txt_file_rw(focus_path, mainActivity.ASSETS_COPY, "source.html", name+".html",0);
                if (status.equals("exist")) {
                    Log.d(TAG, "exist html file");
                }

                mainActivity.txt_file_rw(intent_path, mainActivity.HTML_WRITE, focus_path, name+".html",0);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG,"Start FileCopy Task");
            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                    PROGRESS,
                    START, 0), 0);
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Object o) {
            Log.d(TAG,"End FileCopy Task");
            mHandler.sendMessageDelayed(mHandler.obtainMessage(
                    PROGRESS,
                    STOP, 0), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart);

        setTitle("Trace Interval");
        mActivity = this;
        mainActivity = new MainActivity();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Point displaySize = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(displaySize);
        maxDisplayWidth = displaySize.x;
        maxDisplayHeight = displaySize.y;

        final Intent intent = getIntent();
        intent_path = intent.getStringExtra("path");
        name = intent.getStringExtra("name");
        focus_path = intent.getStringExtra("focus_path");
        max_wait_time = Float.parseFloat(intent.getStringExtra("MaxWaitTime"));
        mDrawingChartData = (ArrayList<Listviewitem>)getIntent().getSerializableExtra("mDrawingChartData");
        selectProgress = (ProgressBar) findViewById(R.id.select_progress);
        selectProgress.setMax(maxDisplayWidth);

        Log.d(TAG, "path " + intent_path + " max_wait_time " + max_wait_time);
        list_button = (ImageView)findViewById(R.id.listview_btn);
        list_button.setOnClickListener(this);

        convert_button = (ImageView)findViewById(R.id.convert_html);
        convert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.equals("sample.txt")) {
                    Toast.makeText(mActivity, "Sample.txt is not support", Toast.LENGTH_SHORT);
                } else {
                    mHandler.sendEmptyMessageDelayed(DATA_COPY, 0);
                }
            }
        });




        //    mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/SystraceApp");
        char_data = findViewById(R.id.chart_data);
        char_data_2 = findViewById(R.id.chart_data_2);
        //   seekBarX = findViewById(R.id.seekBar1);
        //      seekBarX.setOnSeekBarChangeListener(this);

        //     seekBarY = findViewById(R.id.seekBar2);
        //    seekBarY.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        // no description text
        chart.getDescription().setEnabled(true);
        Description desc = new Description();
        desc.setText("Interval");
        chart.setDescription(desc);
        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);


        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);

        // add data
        //     seekBarX.setProgress(20);
        //     seekBarY.setProgress(30);

        chart.animateX(2000);

        /* To Do */
        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



     //           Log.d(TAG, "x: " + event.getX() + " y: " + event.getY() + " " + event.getPointerCount());

                if (event.getPointerCount() > 1) {
                    time = 0;
                    long_click = 0;
                    selected = 0;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (time == 0 && selected == 1) {
                            event_x = event.getX();
                            event_y = event.getY();
                            time =  System.currentTimeMillis();

                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, selected + " " + long_click);

                        time = 0;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (long_click == 1)
                            selectProgress.setProgress((int)event.getX());

                        break;
                }

                if (time != 0) {
                    if (Math.abs(event.getX() - event_x) < 50 &&
                            Math.abs(event.getY() - event_y) < 50 &&
                            (System.currentTimeMillis() - time) > 600) {
                        vibrator.vibrate(100);
                        Log.d(TAG, "long click");
                        long_click= 1;
                        time = 0;
                    }
                }
                return false;
            }
        });
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         //       Log.d(TAG, "chart clicked " + position_x + " " + position_y);

                try {
                    int detect_selected_chart_1 = -1;
                    String text_to_write_value_chart1 = null;

                    ArrayList<Entry> entries = null;
                    ArrayList<DataInfo> mDataInfo = null;
                    StringBuilder stringBuilder = new StringBuilder("");
                    if ((position_x + position_y) != 0) {
                        String name = chart.getData().getDataSetByIndex(data_set_index).toString();
                        //            Log.d(TAG, "name " + name);
                        for (int i = 0; i < mChartData.size(); i++) {
                            String chartdata = mChartData.get(i).getid();
                            //           Log.d(TAG, "try to read " + chartdata + " " + mChartData.size());
                            if (name.contains(chartdata)) {
                                if (name.contains("*")) {
                                    //          Log.d(TAG, "try to read " + chartdata);
                                    entries = mChartData.get(i).getIntervalEntries();
                                    for (int j = 0; j < entries.size(); j++) {
                                        if (entries.get(j).getX() == position_x && entries.get(j).getY() == position_y) {
                                            detect_selected_chart_1 = j;
                                        }
                                    }

                                    mDataInfo = mChartData.get(i).getArrayDataList();
                                    for (int j = 0; j < mDataInfo.size(); j++) {
                                        if (mDataInfo.get(j).getpos() == detect_selected_chart_1) {
                                            //           char_data.setText(" Label: [" + mDataInfo.get(j).getid() + "] Trace Time: [" + mDataInfo.get(j).gettime() + "] Value: [" + e.getY() + "]");
                                            stringBuilder.append(" Label: [" + mDataInfo.get(j).getid() + "*] Trace Time: [" + mDataInfo.get(j).gettime() + "] Value: [" + position_y + "]");
                                            text_to_write_value_chart1 = stringBuilder.toString();
                                            Log.d(TAG, "text chart1 " + text_to_write_value_chart1);

                                      //       chart2.centerViewToAnimated(position_x, position_y, chart2.getData().getDataSetByIndex(data_set_index).getAxisDependency(), 200);


                                            if (selected == 0) {
                                                time_x1 = (mDataInfo.get(j).gettime());
                                                selected = 1;
                                            }

                                            if (long_click == 1) {

                                                time_x2 = (mDataInfo.get(j).gettime());


                                                Log.d(TAG, "start crop mode");
                                                crop_mode = 1;

                                                long_click = 0;
                                                selected = 0;

                                                mainActivity.set_crop_time(time_x1, time_x2);

                                                mHandler.sendEmptyMessageDelayed(DATA_COPY, 10);

                                            }


                                         }
                                    }
                                }
                            }
                        }

                        char_data.setText(text_to_write_value_chart1);
                        focused_chart = 0;
                        position_x = 0;
                        position_y = 0;
                        data_set_index = -1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(LegendForm.LINE);
        l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(tfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(60f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(false);


        chart2 = findViewById(R.id.chart2);
        chart2.setOnChartValueSelectedListener(this);
        // no description text
        chart2.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("Sync");
        chart2.setDescription(description);
        // enable touch gestures
        chart2.setTouchEnabled(true);
        chart2.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart2.setDragEnabled(true);
        chart2.setScaleEnabled(true);
        chart2.setDrawGridBackground(false);
        chart2.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart2.setPinchZoom(true);

        // set an alternative background color
        chart2.setBackgroundColor(Color.WHITE);


        // add data
        //     seekBarX.setProgress(20);
        //     seekBarY.setProgress(30);

        chart2.animateX(2000);
        chart2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
   //             Log.d(TAG, "x: " + event.getX() + " y: " + event.getY());
                return false;
            }
        });
        /* To Do */
        chart2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
 //               Log.d(TAG, "x: " + event.getX() + " y: " + event.getY() + " " + event.getPointerCount());
                if (event.getPointerCount() > 1) {
                    time = 0;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (time == 0) {
                            event_x = event.getX();
                            event_y = event.getY();
                            time =  System.currentTimeMillis();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        time = 0;
                        break;
                }

                if (time != 0) {
                    if (Math.abs(event.getX() - event_x) < 50 &&
                            Math.abs(event.getY() - event_y) < 50 &&
                            (System.currentTimeMillis() - time) > 600) {
                        vibrator.vibrate(100);
                        Log.d(TAG, "long click");

                        time = 0;
                    }
                }
                return false;
            }
        });
        chart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       //         Log.d(TAG, "chart clicked " + position_x + " " + position_y);

                try {
                    int detect_selected_chart_2 = -1;
                    String text_to_write_value_chart2 = null;

                    ArrayList<Entry> entries = null;
                    ArrayList<DataInfo> mDataInfo = null;
                    StringBuilder stringBuilder = new StringBuilder("");
                    if ((position_x + position_y) != 0) {
                        String name = chart2.getData().getDataSetByIndex(data_set_index).toString();
                        //            Log.d(TAG, "name " + name);
                        for (int i = 0; i < mChartData.size(); i++) {
                            String chartdata = mChartData.get(i).getid();
                            //           Log.d(TAG, "try to read " + chartdata + " " + mChartData.size());
                            if (name.contains(chartdata)) {
                                if (!name.contains("*")) {
                                    //          Log.d(TAG, "try to read " + chartdata);
                                    entries = mChartData.get(i).getValueEntries();
                                    for (int j = 0; j < entries.size(); j++) {
                                        if (entries.get(j).getX() == position_x && entries.get(j).getY() == position_y) {
                                            detect_selected_chart_2 = j;
                                        }
                                    }

                                    mDataInfo = mChartData.get(i).getArrayDataList();
                                    for (int j = 0; j < mDataInfo.size(); j++) {
                                        if (mDataInfo.get(j).getpos() == detect_selected_chart_2) {
                                            //           char_data.setText(" Label: [" + mDataInfo.get(j).getid() + "] Trace Time: [" + mDataInfo.get(j).gettime() + "] Value: [" + e.getY() + "]");
                                            stringBuilder.append(" Label: [" + mDataInfo.get(j).getid() + "] Trace Time: [" + mDataInfo.get(j).gettime() + "] Value: [" + position_y + "]");
                                            text_to_write_value_chart2 = stringBuilder.toString();
                                            Log.d(TAG, "text chart2 " + text_to_write_value_chart2);



                                            chart.centerViewToAnimated(position_x, position_y, chart.getData().getDataSetByIndex(data_set_index)
                                                    .getAxisDependency(), 200);



                                        }
                                    }
                                }
                            }
                        }

                        char_data.setText(text_to_write_value_chart2);
                        focused_chart = 0;
                        position_x = 0;
                        position_y = 0;
                        data_set_index = -1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // get the legend (only possible after setting data)
        Legend l2 = chart2.getLegend();

        // modify the legend ...
        l2.setForm(LegendForm.LINE);
        l2.setTypeface(tfLight);
        l2.setTextSize(11f);
        l2.setTextColor(Color.BLACK);
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l2.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l2.setDrawInside(false);

        XAxis xAxis2 = chart2.getXAxis();
        xAxis2.setTypeface(tfLight);
        xAxis2.setTextSize(11f);
        xAxis2.setTextColor(Color.BLACK);
        xAxis2.setDrawGridLines(false);
        xAxis2.setDrawAxisLine(true);

        YAxis leftAxis2 = chart2.getAxisLeft();
        leftAxis2.setTypeface(tfLight);
        leftAxis2.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis2.setAxisMaximum(2f);
        leftAxis2.setAxisMinimum(0);
        leftAxis2.setDrawGridLines(true);
        leftAxis2.setGranularityEnabled(false);


        readData = new ReadData();
        readData.setPriority(10);
        readData.start();

    }



    private void calc_item (ArrayList<ChartData> arrayList) {
        float avg = 0.0f;
        float max = 0.0f;
        float min = max_wait_time;
        StringBuilder stringBuilder = new StringBuilder();
        if (!arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (enable_interval == 1) {
                    ArrayList<Entry> entries = arrayList.get(i).getIntervalEntries();
                    if (entries != null) {
                        for (int j = 0; j < entries.size(); j++) {
                            if (entries.get(j).getY() > 0) {
                                avg += entries.get(j).getY();
                                if (entries.get(j).getY() > max)
                                    max = entries.get(j).getY();
                                if (entries.get(j).getY() < min)
                                    min = entries.get(j).getY();
                            }
                        }


     //                   Log.d(TAG, "[" + arrayList.get(i).getid() + "] " + " avg " + avg / entries.size() + " max" + max + " min" + min);

                        stringBuilder.append("[" + arrayList.get(i).getid() + "] " + " avg: " + avg /entries.size() + " max: " + max + " min: " + min);
                        stringBuilder.append("\n");

                        avg = 0.0f;
                        max = 0.0f;
                        min = max_wait_time;
                    }
                }
            }

            char_data_2.setText(stringBuilder.toString());
        }


    }

    private ArrayList<String> restore_data(ArrayList<DataInfo> dataInfos) {
  //      String process_name = "";
        StringBuilder process_name = new StringBuilder();
        ArrayList<String> return_list = new ArrayList<>();
        int mode = 0;
        try {
                List<String> sort_list = new ArrayList<>();
                ArrayList<String> diff_process = new ArrayList<>();

                for (int j = 0; j < dataInfos.size(); j++) {

                        if (j == 0) {
                            process_name.append(dataInfos.get(j).getid());
                            if (dataInfos.get(j).getCheck_process_mode() == NOT_CHECK_PROCESS) {
                                process_name.append("  *");
                                mode = NOT_CHECK_PROCESS;
                            } else {
                                mode = CHECK_PROCESS;
                            }
                        }
                        if (dataInfos.get(j).getProcess() != null) {
                            sort_list.add(dataInfos.get(j).getProcess());
                        }


                }

                Collections.sort(sort_list);

                for (int t = 0; t < sort_list.size(); t++) {
                    //      Log.d(TAG, "sorting " + sort_list.get(t));
                    if (t == 0) {
                        diff_process.add(sort_list.get(t));
                    } else {
                        int size = diff_process.size();
                        if (!diff_process.get(size - 1).equals(sort_list.get(t))) {
                            diff_process.add(sort_list.get(t));
                        }
                    }
                }
            int counting_max = 0;

            for (int j = 0; j < diff_process.size(); j++) {
                //            Log.d(TAG, "sorted: " + diff_process.get(j));
                if (j == 0) {
                    return_list.add(process_name.toString());

                    // counting check
                    if (mode == CHECK_PROCESS) {

                        for (int k = 0; k < diff_process.size(); k++) {
                            int counting = counting_data(sort_list, diff_process.get(k));
                            if (counting > counting_max) {
                                counting_max = counting;
                            }
                        }
                    }
                }
                int count_process = counting_data(sort_list, diff_process.get(j));
                if (mode == NOT_CHECK_PROCESS) {
                    return_list.add("  - [" + diff_process.get(j) + "]   " + count_process);
                } else {
                    int detect = 0;
                    for (int i = 0; i< mCheckProcess.size(); i++) {
                        if (mCheckProcess.get(i).contains(diff_process.get(j))) {
                            return_list.add("  - [" + diff_process.get(j) + "]   " + count_process + "  *");
                            detect = 1;
                        }
                    }

                    if (detect == 0) {
                        return_list.add("  - [" + diff_process.get(j) + "]   " + count_process);
                    }
                    /*
                    if (count_process == counting_max) {
        //                return_list.add("  - [" + diff_process.get(j) + "]   " + count_process + "  *");
                        // change scene
                        return_list.add("  - [" + diff_process.get(j) + "]   " + count_process);
                    } else {
                        return_list.add("  - [" + diff_process.get(j) + "]   " + count_process);
                    }

                     */
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        for (int i = 0; i < return_list.size(); i++) {
            Log.d(TAG, "restore: " + return_list.get(i));
        }
*/
        return return_list;
    }

    private int counting_data(List<String> arrayList, String data) {
        int count = 0;

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(data)) {
                count++;
            }
        }
        return count;
    }

    /* set Datainfos convert to values for chart */
    private void setData_array(ArrayList<DataInfo> dataInfos, int pos) {
        ArrayList<Entry> value_interval = new ArrayList<>();
        ArrayList<Entry> value = new ArrayList<>();

        try {
            float acc_time = 0.0f;
            float cal_time = 0.0f;
            float start_to_finish = 0.0f;
            float firtst_cal_time = 0.0f;
            float cal_interval_time = 0.0f;
            int mode_count = 0;
            String check_process = null;
            int detect_first_data = 0;
            int detect_first_data_int = 0;
            int skip_data = 0;

            double preve_time = 0.0d;
            double prev_time_int = 0.0d;

            if (!dataInfos.isEmpty()) {
                Log.d(TAG, "try to add data for chart ID: " + dataInfos.get(0).getid());

                if (dataInfos.get(0).getCheck_process_mode() == CHECK_PROCESS) {
                    ArrayList<String> thread_list = restore_data(dataInfos);

                    for (int j = 0; j < thread_list.size(); j++) {
                        if (thread_list.get(j).contains("*")) {
                            check_process = thread_list.get(j);
                        }
                    }
                } else {
                    check_process = "NOT SUPPORT";
                }

                Log.d(TAG, "check_process " + check_process);

                for (int i = 0; i < dataInfos.size(); i++) {
                    String process = dataInfos.get(i).getProcess();
                    if (process == null)
                        process = "NULL";

                    if (dataInfos.get(i).getCheck_process_mode() == NOT_CHECK_PROCESS ||
                            ((dataInfos.get(i).getCheck_process_mode() == CHECK_PROCESS) &&
                    check_process.contains(process))) {


                        if (dataInfos.get(i).getCheck_process_mode() == CHECK_PROCESS && (detect_first_data == 0)) {
                            Log.d(TAG, "first detect: " + i + " process " + dataInfos.get(i).getProcess());
                            detect_first_data = 1;
                            Log.d(TAG, "first data for check process : " + dataInfos.get(i).gettime());
                            firtst_cal_time = (float) ((dataInfos.get(i).gettime() - start_time) * 1000);
                            acc_time += firtst_cal_time;
                            preve_time = dataInfos.get(i).gettime();
                        } else {
                            if (i == 0) {
                                Log.d(TAG, "first data: " + dataInfos.get(i).gettime());
                                firtst_cal_time = (float) ((dataInfos.get(i).gettime() - start_time) * 1000);
                                acc_time += firtst_cal_time;
                            }
                        }
                        if (i > 0 || detect_first_data == 1) {

                            if (dataInfos.get(i).getCheck_process_mode() == CHECK_PROCESS && (detect_first_data == 1)) {

                                if (preve_time > 0) {
                                    cal_time = (float) ((dataInfos.get(i).gettime() - preve_time) * 1000);
                                    preve_time = dataInfos.get(i).gettime();
                                }

                                /* pitch */

                            } else {
                                /* pitch */
                                cal_time = (float) ((dataInfos.get(i).gettime() - dataInfos.get(i - 1).gettime()) * 1000);
                            }
/*
                            if (dataInfos.get(i).getid().contains("Chore")) {
                                Log.d(TAG, "cal_time" + cal_time + " prev " + preve_time + " time " + dataInfos.get(i).gettime());
                            }
                            */

                            acc_time += cal_time;

                            if (value != null) {
                                value.add(new Entry(acc_time,
                                        dataInfos.get(i).getvalue()));
                            }


                            if (value_interval != null) {
                                int data_mode = dataInfos.get(i).getIntervalmode();
                                if (data_mode == MODE_BOTH) {
                                    /* add all data */

                                    if (cal_time <= max_wait_time) {
                                        value_interval.add(new Entry(acc_time,
                                                cal_time));

                                        mDataInfoClass = new DataInfo(
                                                dataInfos.get(i).getProcess(),
                                                dataInfos.get(i).getid(),
                                                dataInfos.get(i).gettime(),
                                                dataInfos.get(i).getvalue(),
                                                value_interval.size() - 1,
                                                dataInfos.get(i).getIntervalmode(),
                                                dataInfos.get(i).getCheck_process_mode()
                                        );
                                        dataInfos.set(i, mDataInfoClass);
                                    }

                                } else if (data_mode == MODE_ONE_TO_ONE) {
                                    if (dataInfos.get(i).getvalue() == DATA_START) {
                                        mode_count++;

                                        if (dataInfos.get(i).getCheck_process_mode() == CHECK_PROCESS) {

                                            if (prev_time_int == 0) {
                                                prev_time_int = dataInfos.get(i).gettime();
                                            }
                                        }


                                        if (mode_count > 1) {

                                            if (dataInfos.get(i).getCheck_process_mode() == CHECK_PROCESS) {



                                                    cal_interval_time = (float) ((dataInfos.get(i).gettime() - prev_time_int) * 1000);
                                                    prev_time_int = dataInfos.get(i).gettime();


                                            } else {
                                                cal_interval_time = (float) ((dataInfos.get(i).gettime() - dataInfos.get(i - 2).gettime()) * 1000);
                                            }

                                            if (cal_interval_time <= max_wait_time) {
                                                value_interval.add(new Entry(acc_time,
                                                        cal_interval_time));

                                                mDataInfoClass = new DataInfo(
                                                        dataInfos.get(i).getProcess(),
                                                        dataInfos.get(i).getid(),
                                                        dataInfos.get(i).gettime(),
                                                        dataInfos.get(i).getvalue(),
                                                        value_interval.size() - 1,
                                                        dataInfos.get(i).getIntervalmode(),
                                                        dataInfos.get(i).getCheck_process_mode()
                                                );
                                                dataInfos.set(i, mDataInfoClass);
                                            }
                                        }

                                    }
                                } else if (data_mode == MODE_ONE_TO_ZERO) {
                                    if (dataInfos.get(i).getvalue() == DATA_STOP) {
                                        mode_count++;

                                        if (dataInfos.get(i).getCheck_process_mode() == CHECK_PROCESS) {

                                            if (prev_time_int == 0) {
                                                prev_time_int = dataInfos.get(i).gettime();
                                            }
                                        }

                                        if (mode_count > 1) {

                                            if (dataInfos.get(i).getCheck_process_mode() == CHECK_PROCESS) {



                                                cal_interval_time = (float) ((dataInfos.get(i).gettime() - prev_time_int) * 1000);
                                                prev_time_int = dataInfos.get(i).gettime();


                                            } else {
                                                cal_interval_time = (float) ((dataInfos.get(i).gettime() - dataInfos.get(i - 1).gettime()) * 1000);
                                            }




                                            if (cal_interval_time <= max_wait_time) {
                                                value_interval.add(new Entry(acc_time,
                                                        cal_interval_time));

                                                mDataInfoClass = new DataInfo(
                                                        dataInfos.get(i).getProcess(),
                                                        dataInfos.get(i).getid(),
                                                        dataInfos.get(i).gettime(),
                                                        dataInfos.get(i).getvalue(),
                                                        value_interval.size() - 1,
                                                        dataInfos.get(i).getIntervalmode(),
                                                        dataInfos.get(i).getCheck_process_mode()
                                                );
                                                dataInfos.set(i, mDataInfoClass);
                                            }
                                        }

                                    }
                                }

                            }
                        }








                    } else {
         //               Log.d(TAG, "skip p " + dataInfos.get(i).getProcess() + " id " + dataInfos.get(i).getid() + " t " + dataInfos.get(i).gettime());
                        skip_data = 1;
                    }


                }

                /* chart data update*/
                Log.d(TAG, "set data in ChartData");
                mChartDataClass = new ChartData(
                        mChartData.get(pos).getid(),
                        dataInfos,
                        value,
                        value_interval
                );
                mChartData.set(pos, mChartDataClass);

            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    private void drawChart(ArrayList<ChartData> arrayList) {


        LineDataSet set1 = null;
        LineDataSet set2 = null;
        LineDataSet set3 = null;
        LineDataSet set4 = null;
        LineDataSet set5 = null;
        LineDataSet set6 = null;
        LineDataSet set7 = null;
        LineDataSet set8 = null;

            // create a dataset and give it a type
        int array_length = arrayList.size();


            for (int i = 0; i <  array_length; i++) {
       //         Log.d(TAG, "mChartData: " + arrayList.get(i).getid());

                if (i == 0) {
                    set1 = new LineDataSet(arrayList.get(i).getValueEntries(), arrayList.get(i).getid());

                    set1.setAxisDependency(AxisDependency.LEFT);
                    set1.setColor(Color.BLACK);
                    //        set1.setCircleColor(Color.WHITE);
                    set1.setLineWidth(1f);
                    //      set1.setCircleRadius(3f);
                    set1.setFillAlpha(65);
                    set1.setFillColor(ColorTemplate.getHoloBlue());
                    set1.setHighLightColor(Color.rgb(244, 117, 117));
                    set1.setDrawCircleHole(false);
                    set1.setDrawCircles(false);

                    if (enable_interval == 1) {
                        set2 = new LineDataSet(arrayList.get(i).getIntervalEntries(), arrayList.get(i).getid() + "*");

                        set2.setAxisDependency(AxisDependency.LEFT);
                        set2.setColor(Color.BLACK);
                        //        set1.setCircleColor(Color.WHITE);
                        set2.setLineWidth(1f);
                        //      set1.setCircleRadius(3f);
                        set2.setFillAlpha(65);
                        set2.setFillColor(ColorTemplate.getHoloBlue());
                        set2.setHighLightColor(Color.rgb(244, 117, 117));
                        set2.setDrawCircleHole(false);
                        set2.setDrawCircles(false);
                    }
                }

                if (i == 1) {
                    set3 = new LineDataSet(arrayList.get(i).getValueEntries(), arrayList.get(i).getid());

                    set3.setAxisDependency(AxisDependency.LEFT);
                    set3.setColor(Color.BLUE);
                    //        set1.setCircleColor(Color.WHITE);
                    set3.setLineWidth(1f);
                    //      set1.setCircleRadius(3f);
                    set3.setFillAlpha(65);
                    set3.setFillColor(ColorTemplate.getHoloBlue());
                    set3.setHighLightColor(Color.rgb(244, 117, 117));
                    set3.setDrawCircleHole(false);
                    set3.setDrawCircles(false);

                    if (enable_interval == 1) {
                        set4 = new LineDataSet(arrayList.get(i).getIntervalEntries(), arrayList.get(i).getid() + "*");

                        set4.setAxisDependency(AxisDependency.LEFT);
                        set4.setColor(Color.BLUE);
                        //        set1.setCircleColor(Color.WHITE);
                        set4.setLineWidth(1f);
                        //      set1.setCircleRadius(3f);
                        set4.setFillAlpha(65);
                        set4.setFillColor(ColorTemplate.getHoloBlue());
                        set4.setHighLightColor(Color.rgb(244, 117, 117));
                        set4.setDrawCircleHole(false);
                        set4.setDrawCircles(false);
                    }
                }

                if (i == 2) {
                    set5 = new LineDataSet(arrayList.get(i).getValueEntries(), arrayList.get(i).getid());

                    set5.setAxisDependency(AxisDependency.LEFT);
                    set5.setColor(Color.RED);
                    //        set1.setCircleColor(Color.WHITE);
                    set5.setLineWidth(1f);
                    //      set1.setCircleRadius(3f);
                    set5.setFillAlpha(65);
                    set5.setFillColor(ColorTemplate.getHoloBlue());
                    set5.setHighLightColor(Color.rgb(244, 117, 117));
                    set5.setDrawCircleHole(false);
                    set5.setDrawCircles(false);

                    if (enable_interval == 1) {
                        set6 = new LineDataSet(arrayList.get(i).getIntervalEntries(), arrayList.get(i).getid() + "*");

                        set6.setAxisDependency(AxisDependency.LEFT);
                        set6.setColor(Color.RED);
                        //        set1.setCircleColor(Color.WHITE);
                        set6.setLineWidth(1f);
                        //      set1.setCircleRadius(3f);
                        set6.setFillAlpha(65);
                        set6.setFillColor(ColorTemplate.getHoloBlue());
                        set6.setHighLightColor(Color.rgb(244, 117, 117));
                        set6.setDrawCircleHole(false);
                        set6.setDrawCircles(false);
                    }

                }

                if (i == 3) {
                    set7 = new LineDataSet(arrayList.get(i).getValueEntries(), arrayList.get(i).getid());

                    set7.setAxisDependency(AxisDependency.LEFT);
                    set7.setColor(Color.GREEN);
                    //        set1.setCircleColor(Color.WHITE);
                    set7.setLineWidth(1f);
                    //      set1.setCircleRadius(3f);
                    set7.setFillAlpha(65);
                    set7.setFillColor(ColorTemplate.getHoloBlue());
                    set7.setHighLightColor(Color.rgb(244, 117, 117));
                    set7.setDrawCircleHole(false);
                    set7.setDrawCircles(false);

                    if (enable_interval == 1) {
                        set8 = new LineDataSet(arrayList.get(i).getIntervalEntries(), arrayList.get(i).getid() + "*");

                        set8.setAxisDependency(AxisDependency.LEFT);
                        set8.setColor(Color.GREEN);
                        //        set1.setCircleColor(Color.WHITE);
                        set8.setLineWidth(1f);
                        //      set1.setCircleRadius(3f);
                        set8.setFillAlpha(65);
                        set8.setFillColor(ColorTemplate.getHoloBlue());
                        set8.setHighLightColor(Color.rgb(244, 117, 117));
                        set8.setDrawCircleHole(false);
                        set8.setDrawCircles(false);
                    }

                }

            }


            // create a data object with the data sets
            LineData data_sync = null;
            LineData data = null;

            if (!arrayList.isEmpty()) {

                if (enable_interval == 1) {
                    switch (array_length * 2) {
                        case 2:
                            data = new LineData(set2);
                            data_sync = new LineData(set1);
                            break;
                        case 4:
                            data = new LineData(set2, set4);
                            data_sync = new LineData(set1, set3);
                            break;
                        case 6:
                            data = new LineData(set2, set4, set6);
                            data_sync = new LineData(set1, set3, set5);
                            break;
                        case 8:
                            data = new LineData(set2,set4, set6, set8);
                            data_sync = new LineData(set1, set3, set5, set7);
                            break;
                    }

                } else {
                    switch (array_length) {
                        case 1:
                            data_sync = new LineData(set1);
                            break;
                        case 2:
                            data_sync = new LineData(set1, set3);
                            break;
                        case 3:
                            data_sync = new LineData(set1, set3, set5);
                            break;
                        case 4:
                            data_sync = new LineData(set1, set3, set5, set7);
                            break;
                    }
                }


            }
            if (data != null) {
                data.setValueTextColor(Color.BLACK);
                data.setValueTextSize(10f);
                // set data
                chart.setData(data);
            }

            if (data_sync != null) {
                data_sync.setValueTextColor(Color.BLACK);
                data_sync.setValueTextSize(10f);
                // set data
                chart2.setData(data_sync);
            }

            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }


    }



    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            //      Log.d(TAG, "mHandler " + msg.what);
            switch (msg.what) {
                case DATA_COPY:
                    copyThread = new CopyThread();
                    copyThread.setPriority(10);
                    copyThread.start();
/*
                    if (fileCopyTask != null) {
                        if (fileCopyTask.getStatus() == AsyncTask.Status.RUNNING) {
                            fileCopyTask.cancel(true);
                        }
                    }
                    fileCopyTask = new FileCopyTask(getBaseContext());
                    fileCopyTask.execute();

*/
                    break;

                case PROGRESS:

                    switch (msg.arg1) {
                        case START:
                            progress_status = START;
                            chartToProcessing();
                            break;
                        case STOP:
                            progress_status = STOP;
                            chartToProcessing();
                            break;
                    }
                    break;

                case UPDATE_CHART:
                    Log.d(TAG, "UPDATE CHART");

                    try {
                        if (!mChartData.isEmpty()) {
                            for (int i = 0; i < mChartData.size(); i++) {
                                setData_array(mChartData.get(i).getArrayDataList(), i);
                            }


                            drawChart(mChartData);

                            chart.invalidate();
                            chart2.invalidate();

                            calc_item(mChartData);
                        } else {
                            Log.d(TAG, "chart data is empty");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
            }

            super.handleMessage(msg);
        }
    };


    private void draw_list_view() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listview_btn:
                v = mActivity.getLayoutInflater().inflate(R.layout.listview_layout, null);

                final ListView listView=(ListView)v.findViewById(R.id.listviewbtn);
                Listviewitem items;
                final ArrayList<Listviewitem> data= new ArrayList<>();

                ArrayList<String> thread_list = new ArrayList<>();

                try {

                    if (!mChartData.isEmpty()) {
                        for (int i = 0; i < mChartData.size(); i++) {

                            thread_list = restore_data(mChartData.get(i).getArrayDataList());
/*
                            for (int j = 0; j < thread_list.size(); j++) {
                                Log.d(TAG, "result: " + thread_list.get(j));

                            }
                            */

                            for (int j = 0; j < thread_list.size(); j++) {
                //                Log.d(TAG, "sorted: " + thread_list.get(j));

                                items = new Listviewitem(thread_list.get(j), "");
                                data.add(items);
                            }

                        }
                    }


                    listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data);
                    listView.setAdapter(listviewAdapter);

                    listviewAdapter.notifyDataSetChanged();


                    final AlertDialog listDialog = new AlertDialog.Builder(mActivity).create();
                    listDialog.setView(v);
                    listDialog.setTitle("Thread List");
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
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Systrace_chart.this);
                            vibrator.vibrate(100);
                            ArrayList<String> thread_list = new ArrayList<>();
                            final String Thread_name = listviewAdapter.getItem(position);

                            if (!mChartData.isEmpty()) {
                                for (int i = 0; i < mChartData.size(); i++) {

                                    thread_list = restore_data(mChartData.get(i).getArrayDataList());

                                    for (int j = 0; j < thread_list.size(); j++) {
                                        //                Log.d(TAG, "sorted: " + thread_list.get(j));
                                            if (Thread_name.equals(thread_list.get(j))) {
                                                final int detect_data = i;

                                                alertDialog.setTitle("Thread Name");
                                                alertDialog.setMessage("\n"+Thread_name);


                                                alertDialog.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {
                                                            Listviewitem items;
                                                            final ArrayList<Listviewitem> data= new ArrayList<>();
                                                            ArrayList<String> thread_list = new ArrayList<>();

                                                            thread_list = restore_data(mChartData.get(detect_data).getArrayDataList());
                                                            int detect_pos = -1;
                                                            for (int i =0; i < thread_list.size(); i++) {
                                                                if (thread_list.get(i).contains("*")) {
                                                                    for (int j = 0; j < mCheckProcess.size(); j++) {
                                                                                    if (thread_list.get(i).contains(mCheckProcess.get(j))) {
                                                                                        detect_pos = j;
                                                                                    }
                                                                    }
                                                                }
                                                            }

                                                            if (detect_pos >= 0) {
                                                                Log.d(TAG, "remove " + mCheckProcess.get(detect_pos));
                                                                mCheckProcess.remove(detect_pos);
                                                            }

                                                            int detect_main = 0;
                                                            for (int i = 0; i < mDrawingChartData.size(); i++) {
                                                                if (mDrawingChartData.get(i).getName().equals(Thread_name)) {
                                                                    detect_main = 1;
                                                                }
                                                            }
                                                            ArrayList<DataInfo> dataInfos;
                                                            if (detect_main == 0) {
                                                                mCheckProcess.add(Thread_name);

                                                                dataInfos = mChartData.get(detect_data).getArrayDataList();
                                                                for (int i = 0; i < dataInfos.size(); i++) {
                                                                    mDataInfoClass = new DataInfo(
                                                                            dataInfos.get(i).getProcess(),
                                                                            dataInfos.get(i).getid(),
                                                                            dataInfos.get(i).gettime(),
                                                                            dataInfos.get(i).getvalue(),
                                                                            dataInfos.get(i).getpos(),
                                                                            dataInfos.get(i).getIntervalmode(),
                                                                            CHECK_PROCESS
                                                                    );
                                                                    dataInfos.set(i, mDataInfoClass);
                                                                }
                                                            } else {

                                                                dataInfos = mChartData.get(detect_data).getArrayDataList();
                                                                for (int i = 0; i < dataInfos.size(); i++) {
                                                                    mDataInfoClass = new DataInfo(
                                                                            dataInfos.get(i).getProcess(),
                                                                            dataInfos.get(i).getid(),
                                                                            dataInfos.get(i).gettime(),
                                                                            dataInfos.get(i).getvalue(),
                                                                            dataInfos.get(i).getpos(),
                                                                            dataInfos.get(i).getIntervalmode(),
                                                                            NOT_CHECK_PROCESS
                                                                    );
                                                                    dataInfos.set(i, mDataInfoClass);
                                                                }
                                                            }

                                                            mChartDataClass = new ChartData(
                                                                    mChartData.get(detect_data).getid(),
                                                                    dataInfos,
                                                                    mChartData.get(detect_data).getValueEntries(),
                                                                    mChartData.get(detect_data).getIntervalEntries()
                                                            );
                                                            mChartData.set(detect_data, mChartDataClass);


                                                            mHandler.sendEmptyMessageDelayed(UPDATE_CHART, 0);



                                                                if (!mChartData.isEmpty()) {
                                                                    for (int i = 0; i < mChartData.size(); i++) {

                                                                        thread_list = restore_data(mChartData.get(i).getArrayDataList());


                                                                        for (int j = 0; j < thread_list.size(); j++) {
                                                                            //                Log.d(TAG, "sorted: " + thread_list.get(j));

                                                                            items = new Listviewitem(thread_list.get(j), "");
                                                                            data.add(items);
                                                                        }

                                                                    }
                                                                }

                                                                listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data);
                                                                listView.setAdapter(listviewAdapter);

                                                                listviewAdapter.notifyDataSetChanged();

                                                                listDialog.dismiss();


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });

                                                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });

                                                alertDialog.show();


                                            }
                                    }
                                }
                            }


                            return false;
                        }
                    });
                    listDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "x: " + event.getX() + " y: " + event.getY());
        return false;
    }

    public class ReadData extends Thread {
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                }
            });

            try {
    //            Log.d(TAG, "try to read txt file in Thread");

                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                        PROGRESS,
                        START, 0), 0);

        //        Log.d(TAG, "mDrawingCharData size " + mDrawingChartData.size());
                for (int i = 0; i < mDrawingChartData.size(); i++) {
                    if (Integer.parseInt(mDrawingChartData.get(i).getData()) == 1)
                        add_datas(intent_path, READ, mDrawingChartData.get(i).getName());
                }
/*
                if (!mChartData.isEmpty()) {
                    for (int i = 0; i < mChartData.size(); i++) {
                        ArrayList<String> thread_list = new ArrayList<>();
                        thread_list = restore_data(mChartData.get(i).getArrayDataList());
                    }
                }
*/
                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                        PROGRESS,
                        STOP, 0), 0);

                mHandler.sendEmptyMessageDelayed(UPDATE_CHART, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





    public String add_datas(String path, int rw, String data) {
        String str = null;

        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        String line = "";
        int count_line = 0;

        ArrayList<DataInfo> temp_array= new ArrayList<>();

        synchronized (mLock) {
            if (rw == READ) {
                //             StringBuffer stringBuffer = new StringBuffer("");
                //read test


                try {
       //             Log.d(TAG, "try to read: " + path);

                    bufferedReader = new BufferedReader(new FileReader(path));

                    /*
                    while ((line = bufferedReader.readLine()) != null) {
                        Log.d(TAG, "data: " + line);
                        stringBuffer = stringBuffer.append(line).append("\n");
                    }
                    str = stringBuffer.toString();
                    bufferedReader.close();
*/

                    while (true) {
                        line = bufferedReader.readLine();



                        if (line == null) {
                            Log.d(TAG, "Capture break");
                            break;
                        }


                //        if (line != null ) {
               //             if (line.contains("\\(") && line.contains("\\)")) {
                                if (count_line == 0) {
           //                         Log.d(TAG, line);
                                    if (line.contains("[") && line.contains("]")) {
                                        String[] parser_0 = line.split(":");
                                        for (int j = 0; j < parser_0.length; j++) {
                                            //           Log.d(TAG, "[" + j + "]" + parser_1[j]);
                                            if (j == 0) {
                                    /*
                                    Log.d(TAG, parser_1[0] + " length: " + parser_1[0].length() + " lastindexof " + parser_1[0].lastIndexOf(" ") +
                                            " substring: " + parser_1[0].substring(parser_1[0].lastIndexOf(" ")));
*/


                                                try {
                                                    start_time = Double.parseDouble(parser_0[0].substring(parser_0[0].lastIndexOf(" ")));
                                                    //                      start_time = start_time - (int)start_time;
                                                } catch (Exception e) {
                                                    start_time = Double.parseDouble(parser_0[1].substring(parser_0[1].lastIndexOf(" ")));
                                                    //             start_time = start_time - (int)start_time;
                                                    e.printStackTrace();
                                                }


                                            }
                                        }
                    //                    Log.d(TAG, "start time " + start_time);
                                        count_line++;
                                    }


                                }
            //                }
               //         }
                        /*
                        if (count_line == 0) {
                            String[] parser_0 = line.split(":");
                            for (int j = 0; j < parser_0.length; j++) {
                                //           Log.d(TAG, "[" + j + "]" + parser_1[j]);
                                if (j == 0) {
                                    /*
                                    Log.d(TAG, parser_1[0] + " length: " + parser_1[0].length() + " lastindexof " + parser_1[0].lastIndexOf(" ") +
                                            " substring: " + parser_1[0].substring(parser_1[0].lastIndexOf(" ")));



                                    try {
                                        start_time = Double.parseDouble(parser_0[0].substring(parser_0[0].lastIndexOf(" ")));
                  //                      start_time = start_time - (int)start_time;
                                    } catch (Exception e) {
                                        start_time = Double.parseDouble(parser_0[1].substring(parser_0[1].lastIndexOf(" ")));
                           //             start_time = start_time - (int)start_time;
                                        e.printStackTrace();
                                    }
          //                          Log.d(TAG, "start time " + start_time);

                                }
                            }

                        }


                        count_line++;
*/



                        /* start time detected */
                        if (count_line > 0) {

                            if (data.equals("Choreograph")) {
                                if (line.contains("Choreographer#doFrame")) {

                                    String[] parser_2 = line.split(": ");
                                    double time_ch_data = 0.0d;
                                    String process_name = null;
                                    for (int j = 0; j < parser_2.length; j++) {
                                        //         Log.d(TAG, "[" + j + "]" + parser_1[j]);
                                        if (j == 0) {
/*
                                    Log.d(TAG, parser_1[0] + " length: " + parser_1[0].length() + " lastindexof " + parser_1[0].lastIndexOf(" ") +
                                            " substring: " + parser_1[0].substring(parser_1[0].lastIndexOf(" ")));
*/
                                            time_ch_data = Double.parseDouble(parser_2[0].substring(parser_2[0].lastIndexOf(" ")));

                                            String[] process = parser_2[0].split(" \\(");
                                            if (process.length > 0) {
                                                process_name = process[0];
                                                //              Log.d(TAG, "CH Process " +  process_name);
                                            }
                                        /*
                                        for (int f = 0; f < process.length; f++) {
                                            if (process[f].length() > 0) {
                                                if (!process[f].contains(Double.toString(time_ch_data)) && process[f].length() > 2) {
                                                    process_name = process[f];
                                                                 Log.d(TAG, "CH Process [" + f + "] " + process[f]);
                                                }
                                            }
                                        }

                                         */

                                        }
                                    }
                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_ch_data - 0.000001,
                                            DATA_STOP,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);


                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_ch_data,
                                            DATA_START,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_ch_data + 0.000001,
                                            DATA_STOP,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                }
                            }

                            if (data.equals("DrawFrame")) {
                                if (line.contains("DrawFrame")) {
                                    String[] parser_2 = line.split(": ");
                                    double time_ch_data = 0.0d;
                                    String process_name = null;
                                    for (int j = 0; j < parser_2.length; j++) {
                                        //         Log.d(TAG, "[" + j + "]" + parser_1[j]);
                                        if (j == 0) {
/*
                                    Log.d(TAG, parser_1[0] + " length: " + parser_1[0].length() + " lastindexof " + parser_1[0].lastIndexOf(" ") +
                                            " substring: " + parser_1[0].substring(parser_1[0].lastIndexOf(" ")));
*/
                                            time_ch_data = Double.parseDouble(parser_2[0].substring(parser_2[0].lastIndexOf(" ")));

                                            String[] process = parser_2[0].split(" \\(");
                                            if (process.length > 0) {
                                                process_name = process[0];
                                            }


                                        }
                                    }
                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_ch_data - 0.000001,
                                            DATA_STOP,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_ch_data,
                                            DATA_START,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_ch_data + 0.000001,
                                            DATA_STOP,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);
                                }
                            }


                            if (data.equals("dInput")) {
                                if (line.contains("deliverInputEvent")) {

                                    String[] parser_1 = line.split(": ");
                                    double time_dinput_data = 0.0d;
                                    int vsync_value = 0;
                                    String process_name = null;
                                    for (int j = 0; j < parser_1.length; j++) {
                                        //           Log.d(TAG, "[" + j + "]" + parser_1[j]);
                                        if (j == 0) {
                                    /*
                                    Log.d(TAG, parser_1[0] + " length: " + parser_1[0].length() + " lastindexof " + parser_1[0].lastIndexOf(" ") +
                                            " substring: " + parser_1[0].substring(parser_1[0].lastIndexOf(" ")));
*/
                                            time_dinput_data = Double.parseDouble(parser_1[0].substring(parser_1[0].lastIndexOf(" ")));

                                            String[] process = parser_1[0].split(" \\(");
                                            if (process.length > 0) {
                                                process_name = process[0];
                                            }

                                        }
                                        if (parser_1[j].contains("deliverInputEvent")) {
                                            //              Log.d(TAG, "data: " + parser_1[j].substring(parser_1[j].length() - 1));
                                            if (parser_1[j].contains("F")) {
                                                vsync_value = DATA_STOP; //Finish
                                            } else if (parser_1[j].contains("S")) {
                                                vsync_value = DATA_START; //Start
                                            }
                                        }
                                    }
                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_dinput_data,
                                            vsync_value,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS


                                    );
                                    temp_array.add(mDataInfoClass);

                                }
                            }



                            if (data.equals("VSYNC")) {
                                if (line.contains("VSYNC-sf")) {
                                    //      Vsync_raw_array.add(line);

                                    String[] parser_1 = line.split(": ");
                                    double time_vsync_data = 0.0d;
                                    int vsync_value = 0;
                                    String process_name = null;
                                    for (int j = 0; j < parser_1.length; j++) {
                                        //           Log.d(TAG, "[" + j + "]" + parser_1[j]);
                                        if (j == 0) {
/*
                                    Log.d(TAG, parser_1[0] + " length: " + parser_1[0].length() + " lastindexof " + parser_1[0].lastIndexOf(" ") +
                                            " substring: " + parser_1[0].substring(parser_1[0].lastIndexOf(" ")));
*/
                                            time_vsync_data = Double.parseDouble(parser_1[0].substring(parser_1[0].lastIndexOf(" ")));

                                            String[] process = parser_1[0].split(" \\(");
                                            if (process.length > 0) {
                                                process_name = process[0];
                                            }


                                        }
                                        if (parser_1[j].contains("VSYNC-sf")) {
                                            //              Log.d(TAG, "data: " + parser_1[j].substring(parser_1[j].length() - 1));
                                            vsync_value = Integer.parseInt(parser_1[j].substring(parser_1[j].length() - 1));
                                        }
                                    }

                                    /*

                                    change MODE_BOTH to MODE_ONE_TO_ONE

                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_vsync_data,
                                            vsync_value,
                                            -1,
                                            MODE_BOTH,
                                            NOT_CHECK_PROCESS

                                    );
                                    temp_array.add(mDataInfoClass);
                                    */

                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_vsync_data - 0.000001,
                                            DATA_STOP,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_vsync_data,
                                            DATA_START,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_vsync_data + 0.000001,
                                            DATA_STOP,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);


                                }

                            }

                            if (data.equals("IRQ")) {
                                if (line.contains("name=lge_touch")) {
                                    String[] parser_2 = line.split(": irq_handler");
                                    double time_irq_data = 0.0d;
                                    String process_name = null;
                                    for (int j = 0; j < parser_2.length; j++) {
                                        //         Log.d(TAG, "[" + j + "]" + parser_1[j]);
                                        if (j == 0) {
/*
                                    Log.d(TAG, parser_1[0] + " length: " + parser_1[0].length() + " lastindexof " + parser_1[0].lastIndexOf(" ") +
                                            " substring: " + parser_1[0].substring(parser_1[0].lastIndexOf(" ")));
*/
                                            time_irq_data = Double.parseDouble(parser_2[0].substring(parser_2[0].lastIndexOf(" ")));

                                            String[] process = parser_2[0].split(" \\(");
                                            if (process.length > 0) {
                                                process_name = process[0];
                                            }

                                        }
                                    }
                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_irq_data - 0.000001,
                                            DATA_STOP,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_irq_data,
                                            DATA_START,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_irq_data + 0.000001,
                                            DATA_STOP,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                }

                            }

                            if (data.equals("iq")) {
                                if (line.contains("|iq|")) {
                                    String[] parser_3 = line.split(": ");
                                    double time_iq_data = 0.0d;
                                    int iq_value = 0;
                                    String process_name = null;
                                    for (int j = 0; j < parser_3.length; j++) {
                                        //                               Log.d(TAG, "[" + j + "]" + parser_3[j]);
                                        if (j == 0) {
/*
                                    Log.d(TAG, parser_3[0] + " length: " + parser_3[0].length() + " lastindexof " + parser_3[0].lastIndexOf(" ") +
                                            " substring: " + parser_3[0].substring(parser_3[0].lastIndexOf(" ")));
*/
                                            time_iq_data = Double.parseDouble(parser_3[0].substring(parser_3[0].lastIndexOf(" ")));

                                            String[] process = parser_3[0].split(" \\(");
                                            if (process.length > 0) {
                                                process_name = process[0];
                                            }

                                        }
                                        if (parser_3[j].contains("|iq|")) {
                                            //                                   Log.d(TAG, "data: " + parser_3[j].substring(parser_3[j].length() - 1));
                                            iq_value = Integer.parseInt(parser_3[j].substring(parser_3[j].length() - 1));
                                        }
                                    }
                                    mDataInfoClass = new DataInfo(
                                            process_name,
                                            data,
                                            time_iq_data,
                                            iq_value,
                                            -1,
                                            MODE_ONE_TO_ONE,
                                            NOT_CHECK_PROCESS
                                    );
                                    temp_array.add(mDataInfoClass);

                                    //                   Log.d(TAG, time_iq_data + " / " + iq_value + " added");
                                }

                                //                if (line.contains("VSYNC-sf") || line.contains("name=lge_touch") || line.contains("|iq|")) {
                                //            Log.d(TAG, "Line[" + count_line + "]" + line);
                                //                  stringBuffer = stringBuffer.append(line).append("\n");
                                //               }


                            }



                        } //end count_line check



                    }
                    //       str = stringBuffer.toString();
                    Thread.sleep(1);
                    bufferedReader.close();

                    Log.d(TAG, "read data done");



                    /* */

                    mChartDataClass = new ChartData(
                            data,
                            temp_array,
                            null,
                            null
                    );
                    mChartData.add(mChartDataClass);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (rw == WRITE) {
                File file = new File(path);

                try {

                    if (file.exists()) {
                        file.delete();
                        Log.d(TAG, file.toString() + "deleted");
                    }

                    FileOutputStream fos = new FileOutputStream(file, false);
                    byte[] contents = data.getBytes();
                    fos.write(contents);
                    fos.flush();
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (rw == DELETE) {
                File file = new File(path);
                file.delete();
            }



        }
        return str;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.actionSave_interval:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (saveToGalleryTask != null) {
                        if (saveToGalleryTask.getStatus() == AsyncTask.Status.RUNNING) {
                            saveToGalleryTask.cancel(true);
                        }
                    }
                    saveToGalleryTask = new SaveToGalleryTask(mActivity, chart, "Interval");
                    saveToGalleryTask.execute();

                } else {
                    requestStoragePermission(chart);
                }
                break;

            case R.id.actionSave_sync:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (saveToGalleryTask != null) {
                        if (saveToGalleryTask.getStatus() == AsyncTask.Status.RUNNING) {
                            saveToGalleryTask.cancel(true);
                        }
                    }
                    saveToGalleryTask = new SaveToGalleryTask(mActivity, chart2, "Sync");
                    saveToGalleryTask.execute();

                } else {
                    requestStoragePermission(chart2);
                }
                break;
/*
            case R.id.chartSetting:
                Intent intent = null;
                intent = new Intent(getBaseContext(), Setting_chart_Activity.class);
                if (intent != null) {
                    startActivity(intent);
                }
                break;
*/

        }
        return true;
    }

    @Override
    protected void onStop() {
        if (readData != null) {
            readData.interrupt();
            readData = null;
        }
        if (copyThread != null) {
            copyThread.interrupt();
            copyThread = null;
        }
        super.onStop();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
/*
        tvX.setText(String.valueOf(seekBarX.getProgress()));
        tvY.setText(String.valueOf(seekBarY.getProgress()));

        setData(seekBarX.getProgress(), seekBarY.getProgress());

        // redraw
        chart.invalidate();
*/

    }

    @Override
    protected void saveToGallery() {
    }


    private float position_x = 0;
    private float position_y = 0;
    private int data_set_index = -1;
    @Override
    public void onValueSelected(Entry e, Highlight h) {
      //  Log.d(TAG, "x " + e.getX() + " y " +  e.getY() + " info chart " +  h.getDataSetIndex());

        position_x = e.getX();
        position_y = e.getY();
        data_set_index = h.getDataSetIndex();


/*
        ArrayList<Entry> entries = null;
        ArrayList<DataInfo> mDataInfo = null;
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < mChartData.size(); i++) {
            String name = chart.getData().getDataSetByIndex(h.getDataSetIndex()).toString();
            String name_2 = chart2.getData().getDataSetByIndex(h.getDataSetIndex()).toString();
            if (name.contains(mChartData.get(i).getid())) {

                    entries = mChartData.get(i).getIntervalEntries();

                    for (int j = 0; j < entries.size(); j++) {
                        if (entries.get(j).getX() == e.getX() && entries.get(j).getY() == e.getY()) {
                            detect_selected_chart_1 = j;
                        }
                    }



                mDataInfo = mChartData.get(i).getArrayDataList();
                for (int j = 0; j < mDataInfo.size(); j++) {
                    if (mDataInfo.get(j).getpos() == detect_selected_chart_1) {
             //           char_data.setText(" Label: [" + mDataInfo.get(j).getid() + "] Trace Time: [" + mDataInfo.get(j).gettime() + "] Value: [" + e.getY() + "]");
                          stringBuilder.append(" Label: [" + mDataInfo.get(j).getid() + "] Trace Time: [" + mDataInfo.get(j).gettime() + "] Value: [" + e.getY() + "]");
                          text_to_write_value_chart1 = stringBuilder.toString();
                        Log.d(TAG, "text chart1 " + text_to_write_value_chart1);
                    }
                }

            }

            if (name_2.contains(mChartData.get(i).getid())) {
                entries = mChartData.get(i).getValueEntries();
                for (int j = 0; j < entries.size(); j++) {
                    if (entries.get(j).getX() == e.getX() && entries.get(j).getY() == e.getY()) {
                        detect_selected_chart_2 = j;
                    }
                }

                mDataInfo = mChartData.get(i).getArrayDataList();
                stringBuilder.setLength(0); //clear
                for (int j = 0; j < mDataInfo.size(); j++) {
                    if (mDataInfo.get(j).getpos() == detect_selected_chart_2) {
                        //           char_data.setText(" Label: [" + mDataInfo.get(j).getid() + "] Trace Time: [" + mDataInfo.get(j).gettime() + "] Value: [" + e.getY() + "]");
                        stringBuilder.append(" Label: [" + mDataInfo.get(j).getid() + "] Trace Time: [" + mDataInfo.get(j).gettime() + "] Value: [" + e.getY() + "]");
                        text_to_write_value_chart2 = stringBuilder.toString();
                        Log.d(TAG, "text chart2 " + text_to_write_value_chart2);
                    }
                }

            }

        }

        chart.centerViewToAnimated(e.getX(), e.getY(), chart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 100);
*/



    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }


    @Override
    protected void chartToProcessing() {
        chartToProcessing(progress_status);
    }




    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}


}

