package com.lge.systraceapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.GuardedBy;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ClickTestActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "SystraceApp:ClickTest";
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private Canvas mCanvas;
    private Path mPath;
    LinearLayout mLinearLayout;
    Bundle extraBundle;

    @GuardedBy("mLock")
    private final Object mLock = new Object();

    ChartView mChartView;

    Paint mLineRect;
    Paint mPaint;
    Paint mPaint1;

    boolean drawFlags = true;
    boolean isDrawing = false;
    int Width = 0;
    int Height = 0;

    int viewWidth = 0;
    int viewHeight = 0;

    private ChartDrawThread mChartDrawThread;
    public boolean mChartDrawThreadRunning = false;

    float pos_x;
    float pos_y;
    float prev_x = 0;
    float prev_y = 0;
    float start_x = 0;
    float start_y = 0;
    float end_x = 0;
    float end_y = 0;
    private long time = 0;

    int detect_start_pos = 0;
    int detect_end_pos = 0;

    int end_data_search = 0;

    private final int READ = 0;
    private final int WRITE = 1;
    private final int DELETE = 2;

    private final int UPDATE_CHART = 10;
    private final int PROGRESS = 12;
    private final int DATA_COPY = 13;
    private final int START = 0;
    private final int STOP = 1;

    private ArrayList<Listviewitem> mClickChartData;
    ArrayList<DataInfo> parse_data = new ArrayList<>();
    private DataInfo mDataInfoClass;
    static String intent_path = null;
    private final int DATA_START = 1;
    private final int DATA_STOP = 0;

    private ReadData readData;

    private DataInfo mClickDataClass;

    private int onfocused = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Click Response");
        try {
            final Intent intent = getIntent();
            intent_path = intent.getStringExtra("path");
            mClickChartData = (ArrayList<Listviewitem>)getIntent().getSerializableExtra("mClickChartData");

            Log.d(TAG, "mClickChartData size " + mClickChartData.size());
            readData = new ReadData();
            readData.setPriority(10);
            readData.start();

            initUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        viewWidth = Width - mLinearLayout.getWidth();
        viewHeight = Height - mLinearLayout.getHeight();

        Log.d(TAG, "layout size X " + mLinearLayout.getWidth() + " Y " + mLinearLayout.getHeight() +
                " vW " + viewWidth + " vH" + viewHeight);

        onfocused = 1;
    }

    private void initUI() {
        setContentView(R.layout.click_chart_layout);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


        Point displaySize = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(displaySize);
        Width = displaySize.x;
        Height = displaySize.y;

        Log.d(TAG, "displayID: " + display.getDisplayId() + " " + display.getName());

        mLinearLayout = (LinearLayout) findViewById(R.id.click_chart_main);

        mChartView = new ChartView(this);
        mLinearLayout.addView(mChartView);

        mLinearLayout.setBackgroundColor(Color.BLACK);



        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(7);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);


        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setDither(true);
        mPaint1.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeWidth(7);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);


        mLineRect = new Paint();
        mLineRect.setColor(0xff87cefa);
        mLineRect.setStyle(Paint.Style.STROKE);
    }


    public class ChartView extends View {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        public ChartView(Context c) {
            super(c);
            mBitmap = Bitmap.createBitmap(Width, Height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        protected void onDraw(Canvas canvas) {

            canvas.drawColor(getResources().getColor(R.color.gray_overlay));
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);

            if (drawFlags) {
                mBitmap = Bitmap.createBitmap(Width, Height, Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mBitmap);
                mPath = new Path();
                mBitmapPaint = new Paint(Paint.DITHER_FLAG);
                drawFlags = false;

                if (detect_start_pos == 1) {
                        mPaint1.setColor(Color.WHITE);
                        mCanvas.drawLine(0, start_y, (float)Width, start_y, mPaint1);
                        mCanvas.drawLine(pos_x, start_y, pos_x, pos_y, mPaint1);

                        Log.d(TAG, "diff ms " + (start_y - pos_y));



                }
                if (detect_end_pos == 1) {
                    mPaint1.setColor(Color.WHITE);
                    mCanvas.drawLine(0, end_y, (float)Width, end_y, mPaint1);
                }

                if (end_data_search > 0 && onfocused == 1) {

                    int toggle_cnt = 0;
                    double start_time = 0;
                    float end_time = 0;
                    int data_cnt = 1;
                    int first_touch_detection = 0;
                    float main_position_time = 0;

                    float startx,starty,endx,endy = 0;

                    for (int i = 0; i < parse_data.size(); i++) {
                        if (parse_data.get(i).getvalue() == DATA_START) {
                            start_time = (float)parse_data.get(i).gettime();

                            if (parse_data.get(i).getid().equals("main_touch")) {
                                mPaint1.setColor(Color.YELLOW);
                                if (first_touch_detection == 0) {
                                    main_position_time = (float) Math.floor(start_time * 1000);
                                    first_touch_detection = 1;
                             //       Log.d(TAG, "main_position_time " + main_position_time);
                                }
                            } else if (parse_data.get(i).getid().equals("com.android.mms")) {
                                mPaint1.setColor(Color.BLUE);
                            }

                        } else {
                            end_time = (float)parse_data.get(i).gettime();
                            toggle_cnt = 1;
                        }
                        if (toggle_cnt == 1) {
                            startx = 10 * data_cnt;
                            starty = (Height + main_position_time - viewHeight) - (float)start_time * 1000 - 100; //add 100 pixel
                            endx = 10 * data_cnt;
                            endy = (Height + main_position_time - viewHeight) - end_time * 1000 - 100; //add 100 pixel
                            mCanvas.drawLine(startx, starty,
                                    endx, endy,
                                    mPaint1);

                            Log.d(TAG,"< " + startx + " " +starty + " " +endx + " " + endy + " >");
                            toggle_cnt = 0;
                            data_cnt++;
                        }
                    }
                    /*
                    Log.d(TAG, "draw data " + (Height - start_time * 1000) + " / " + (Height - end_time * 1000) + " " + data_cnt
                    + " " + parse_data.size());
                    */

                }


                invalidate();
            }
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        public boolean onTouchEvent(MotionEvent event) {




            pos_x = event.getX();
            pos_y = event.getY();
            Log.d(TAG, "X: " + pos_x + " Y: " + pos_y);

            if (event.getPointerCount() > 1) {
                time = 0;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                        detect_start_pos = 0;
                        detect_end_pos = 0;
                        mChartView.invalidate();
                        time =  System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (time != 0) {
                        if (Math.abs(pos_y - prev_y) > 3) {
                            time =  System.currentTimeMillis();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    end_x = pos_x;
                    end_y = pos_y;
                    detect_end_pos = 1;
                    mChartView.invalidate();
                    time = 0;
                    break;
                default:
                    break;
            }

            if (time != 0) {
                if ((System.currentTimeMillis() - time) > 1000) {
                    vibrator.vibrate(100);
                    time = 0;
                    Log.d(TAG, "long click");
                    start_x = pos_x;
                    start_y = pos_y;
                    detect_start_pos = 1;
                    mChartView.invalidate();
                }
            }

            prev_x = event.getX();
            prev_y = event.getY();

            return true;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "onLongClick");
        return false;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        drawFlags = true;
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_CHART:
                    startThread();
                    break;
            }
        };
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (readData != null) {
            readData.interrupt();
            readData = null;
        }
        killThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (readData != null) {
            readData.interrupt();
            readData = null;
        }
        killThread();
    }

    public void killThread() {
        if (mChartDrawThread != null) {
            mChartDrawThread.interrupt();
            mChartDrawThread = null;
            mChartDrawThreadRunning = false;
        }
    }

    public void startThread() {
        Log.d(TAG, "thread start");
        mChartDrawThread = new ChartDrawThread();
        mChartDrawThread.start();
    }

    public class ChartDrawThread extends Thread {
        int thread_cnt = 0;
        public void run() {
            mChartDrawThreadRunning = true;
            mHandler.post(new Runnable() {
                public void run() {
                }
            });
            while (mChartDrawThreadRunning) {
                mHandler.post(new Runnable() {
                    public void run() {
                        drawUI();
                    }
                });
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    Log.d(TAG, "thread end");
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public void drawUI() {
        if (mChartDrawThreadRunning != false) {
            mCanvas.drawLine(0, pos_y, (float)Width, pos_y, mPaint);
            mChartView.invalidate();
            drawFlags = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.click_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.package_setting:
                break;
        }
        return true;
    }


    public class ReadData extends Thread {
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                }
            });

            try {
                Log.d(TAG, "try to read txt file in Thread");

                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                        PROGRESS,
                        START, 0), 0);

                //        Log.d(TAG, "mDrawingCharData size " + mDrawingChartData.size());
                for (int i = 0; i < mClickChartData.size(); i++) {
                    if (Integer.parseInt(mClickChartData.get(i).getData()) == 1) {
                        Log.d(TAG, "parse data : " + mClickChartData.get(i).getName());
                        add_datas(intent_path, READ, mClickChartData.get(i).getName());
                    }
                }

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
        double time = 0.0d;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        String line = "";

        int start_activity = 0;
        int end_of_BE = 0;

        int check_first_detection = 0;
        float first_time = 0;

        synchronized (mLock) {
            if (rw == READ) {
                try {
                    Log.d(TAG, "try to read: " + path);
                    bufferedReader = new BufferedReader(new FileReader(path));

                    while (true) {
                        line = bufferedReader.readLine();

                        if (line == null) {
                            Log.d(TAG, "Capture break");
                            break;
                        }

                        // 1. treat main_touch
                            if (data.equals("main_touch")) {
                                if (line.contains(data)) {
                                    String[] parser_0 = line.split(":");
                                    if (line.contains("B|")) {
                                        for (int j = 0; j < parser_0.length; j++) {
                                            //                                     Log.d(TAG, "[" + j + "]" + parser_0[j]);
                                            if (j == 0) {
/*
                                            Log.d(TAG, parser_0[0] + " length: " + parser_0[0].length() + " lastindexof " + parser_0[0].lastIndexOf(" ") +
                                                    " substring: " + parser_0[0].substring(parser_0[0].lastIndexOf(" ")));
*/


                                                try {
                                                    time = Double.parseDouble(parser_0[0].substring(parser_0[0].lastIndexOf(" ")));

                                                } catch (Exception e) {
                                                    time = Double.parseDouble(parser_0[1].substring(parser_0[1].lastIndexOf(" ")));

                                                    e.printStackTrace();
                                                }



                                                if (check_first_detection == 0) {
                                                    first_time = (float)Math.floor(time);
                                                    check_first_detection = 1;
                                                    Log.d(TAG, "start_time: " + time + " first_time: " + first_time);
                                                }
                                                float time_data = Float.parseFloat(String.format("%,.6f", time - first_time));
                                                Log.d(TAG, data + " stime: " + time_data);
                                                mDataInfoClass = new DataInfo(
                                                        "",
                                                        data,
                                                        time_data,
                                                        DATA_START,
                                                        0,
                                                        0,
                                                        0
                                                );
                                                parse_data.add(mDataInfoClass);


                                            }

                                        }




                                    } else if (line.contains("E|")) {
                                        for (int j = 0; j < parser_0.length; j++) {
//                                        Log.d(TAG, "[" + j + "]" + parser_0[j]);
                                            if (j == 0) {
/*
                                            Log.d(TAG, parser_0[0] + " length: " + parser_0[0].length() + " lastindexof " + parser_0[0].lastIndexOf(" ") +
                                                    " substring: " + parser_0[0].substring(parser_0[0].lastIndexOf(" ")));
*/
                                                try {
                                                    time = Double.parseDouble(parser_0[0].substring(parser_0[0].lastIndexOf(" ")));

                                                } catch (Exception e) {
                                                    time = Double.parseDouble(parser_0[1].substring(parser_0[1].lastIndexOf(" ")));

                                                    e.printStackTrace();
                                                }
                                                float time_data = Float.parseFloat(String.format("%,.6f", time - first_time));
                                                Log.d(TAG, data + " etime: " + time_data);
                                                mDataInfoClass = new DataInfo(
                                                        "",
                                                        data,
                                                        time_data,
                                                        DATA_STOP,
                                                        0,
                                                        0,
                                                        0
                                                );
                                                parse_data.add(mDataInfoClass);

                                            }

                                        }
                                    }

                                }

                                // check release event
                                end_data_search++;
                            } else if (data.equals("com.android.mms")) {
                                String[] parser_0 = line.split(":");
                                if (parser_0[0] != null) {
                                    if (parser_0[0].contains(data) && line.contains("tracing_mark_write")) {
                    //                    Log.d(TAG,"line " + line);
                                        if (line.contains("B|")) {
                                            if (line.contains("activityStart")) {
                                                for (int j = 0; j < parser_0.length; j++) {
                                                    //                                     Log.d(TAG, "[" + j + "]" + parser_0[j]);
                                                    if (j == 0) {
    /*
                                                Log.d(TAG, parser_0[0] + " length: " + parser_0[0].length() + " lastindexof " + parser_0[0].lastIndexOf(" ") +
                                                        " substring: " + parser_0[0].substring(parser_0[0].lastIndexOf(" ")));
    */


                                                        try {
                                                            time = Double.parseDouble(parser_0[0].substring(parser_0[0].lastIndexOf(" ")));

                                                        } catch (Exception e) {
                                                            time = Double.parseDouble(parser_0[1].substring(parser_0[1].lastIndexOf(" ")));

                                                            e.printStackTrace();
                                                        }

                                                        if (check_first_detection == 0) {
                                                            first_time = (float) Math.floor(time);
                                                            check_first_detection = 1;
                                                            Log.d(TAG, "start_time: " + time + " first_time: " + first_time);
                                                        }
                                                        float time_data = Float.parseFloat(String.format("%,.6f", time - first_time));
                                                        Log.d(TAG, data + " stime: " + time_data);
                                                        mDataInfoClass = new DataInfo(
                                                                "",
                                                                data,
                                                                time_data,
                                                                DATA_START,
                                                                0,
                                                                0,
                                                                0
                                                        );
                                                        parse_data.add(mDataInfoClass);
                                                        start_activity = 1;

                                                    }

                                                }

                                            }

                                            if (start_activity > 0) {

                                                end_of_BE++;
                                            }


                                        } else if (line.contains("E|")) {
                                            if (start_activity > 0) {

                                                if (end_of_BE > 0) {
                                                    end_of_BE--;
                                                }

                                                if (end_of_BE == 0) {
                                                    for (int j = 0; j < parser_0.length; j++) {
//                                        Log.d(TAG, "[" + j + "]" + parser_0[j]);
                                                        if (j == 0) {
/*
                                            Log.d(TAG, parser_0[0] + " length: " + parser_0[0].length() + " lastindexof " + parser_0[0].lastIndexOf(" ") +
                                                    " substring: " + parser_0[0].substring(parser_0[0].lastIndexOf(" ")));
*/
                                                            try {
                                                                time = Double.parseDouble(parser_0[0].substring(parser_0[0].lastIndexOf(" ")));

                                                            } catch (Exception e) {
                                                                time = Double.parseDouble(parser_0[1].substring(parser_0[1].lastIndexOf(" ")));

                                                                e.printStackTrace();
                                                            }
                                                            float time_data = Float.parseFloat(String.format("%,.6f", time - first_time));
                                                            Log.d(TAG, data + " etime: " + time_data);
                                                            mDataInfoClass = new DataInfo(
                                                                    "",
                                                                    data,
                                                                    time_data,
                                                                    DATA_STOP,
                                                                    0,
                                                                    0,
                                                                    0
                                                            );
                                                            parse_data.add(mDataInfoClass);
                                                            start_activity = 0;
                                                        }

                                                    }
                                                }

                                            }
                                        }

                          //              Log.d(TAG, "eof_EB " + end_of_BE);
                                    }
                                }


                            }
                    }

                    Thread.sleep(1);
                    bufferedReader.close();
                    Log.d(TAG, "read data done");

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


}
