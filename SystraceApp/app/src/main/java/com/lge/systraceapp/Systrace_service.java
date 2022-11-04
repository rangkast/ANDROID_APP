package com.lge.systraceapp;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.os.Trace;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.GuardedBy;
import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Systrace_service extends Service implements View.OnTouchListener {

    private static WindowManager wm;
    private static View mView;

    private static final String TAG = "SystraceApp:Service";
    private static ImageView bt;
    private static TextView textView;
    Vibrator vibrator;
    static File mediaStorageDir;

    private MainActivity mainActivity;

    private float downX;
    private float upX;
    private Context mContext;
    int tool_status = 0;
    static int palm_count;
    static private BroadcastReceiver mBroadcastReceiver;
    private CommandThread commandThread;
    @GuardedBy("mLock")
    private final Object mLock = new Object();
    private int READ = 0;
    private int WRITE = 1;
    private int DELETE = 2;

    private static int trace_time = 0;
    private static ArrayList<Listviewitem> mAtraceData = new ArrayList<>();
    private String txt_file_rw(String path, int rw, String data) {
        String str = null;

        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        String line;
        synchronized (mLock) {
            if (rw == READ) {
                StringBuffer stringBuffer = new StringBuffer("");
                //read test
                try {
                    Log.d(TAG, "try to read: " + path);
                    bufferedReader = new BufferedReader(new FileReader(path));
                    while ((line = bufferedReader.readLine()) != null) {
                        Log.d(TAG, "data: " + line);
                        stringBuffer = stringBuffer.append(line).append("\n");
                    }
                    str = stringBuffer.toString();
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (rw == WRITE) {
                File file = new File(path);

                try{
                    if (file.exists()) {
                        file.delete();
                        Log.d(TAG, file.toString() + " deleted");
                    }

                    FileOutputStream fos = new FileOutputStream(file, false);
                    byte[] contents = data.getBytes();
                    fos.write(contents);
                    fos.flush();
                    fos.close();

                }catch (IOException e){
                    e.printStackTrace();
                }
            } else if (rw == DELETE) {
                File file = new File(path);
                file.delete();
            }
        }

        return str;
    }

    public class CommandThread extends Thread {
        public void run() {

            try {
                Log.d(TAG, "try to Excommand in Thread");
                ExCommand();
          //      run_cmd(false, "echo 1 > sys/devices/virtual/input/lge_touch/aes_mode");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean run_cmd(boolean runAsRoot, String cmd) {

        String shell = runAsRoot ? "su" : "sh";

        int exitCode = 255;
        Process p;
        try {
            Log.d(TAG, "cmd " + cmd);
            p = Runtime.getRuntime().exec(shell);
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.flush();

            os.writeBytes("exit\n");
            os.flush();

            exitCode = p.waitFor();
            showToast(getApplication(), "cmd success");
        } catch (IOException e1) {
            showToast(getApplication(), "I/O Error");
            Log.d(TAG, e1.toString());
        } catch (InterruptedException e) {
            showToast(getApplication(), "Int Error");
            Log.d(TAG, e.toString());
        }
        return (exitCode != 255);
    }


    private final int UPDATE_TXT = 0;

    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            //      Log.d(TAG, "mHandler " + msg.what);
            switch (msg.what) {
                case UPDATE_TXT:
                    textView.setText(status_txt);

                    if (status_txt.equals("done")) {
   //                     mainActivity.update_list();
                    }

                    break;
            }

            super.handleMessage(msg);
        }
    };


    public static String TRACING_NODE = "/sys/kernel/debug/tracing/events/irq/enable";

    private int sysnode_control(String path, int rw, int value) {
        int ret = -1;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        String line;

        if (rw == READ) {
            //read test
            try {
                Log.d(TAG, "try to read: " + path);
                bufferedReader = new BufferedReader(new FileReader(path));
                while ((line = bufferedReader.readLine()) != null) {
                    Log.d(TAG, "data: " + line);
                    ret = Integer.parseInt(line);
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (rw == WRITE) {
            //write test
            try {
                Log.d(TAG, "try to write: " + path);
                bufferedWriter = new BufferedWriter(new FileWriter(path));
                bufferedWriter.write(Integer.toString(value));
                bufferedWriter.close();
                ret = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }






    String status_txt = "";





    private static final  Runtime RUNTIME = Runtime.getRuntime();

    public void ExCommand() {
        Process process;
        InputStreamReader isr;
        int count_line = 0;
        String line;
        String str="";
        StringBuffer stb  = new StringBuffer();
        try {


            trace_time = mainActivity.get_trace_time();
            mAtraceData = mainActivity.get_atrace();

            StringBuilder atrace_cmd = new StringBuilder();
            for (int i = 0; i < mAtraceData.size(); i++) {
                if (mAtraceData.get(i).getData().equals("1")) {
                    String[] parser = mAtraceData.get(i).getName().split("-");
          //          Log.d(TAG, parser[0]);
                    atrace_cmd.append(parser[0]).append(" ");
                }
           //     Log.d(TAG, mAtraceData.get(i).getName());
            }

            Log.d(TAG, "trace time: " + trace_time + " atrace cmd " + atrace_cmd);
        //    run_cmd (false , "cp -r /sdcard/SystraceApp/settings/test.txt /vendor/etc/init/");

         //   mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/SystraceApp");

            status_txt = "start";
            mHandler.sendEmptyMessageDelayed(UPDATE_TXT, 0);

            run_cmd (false , "setenforce 0");

            process = Runtime.getRuntime().exec("echo > /d/tracing/set_event");
            process.waitFor();
            Log.d(TAG, "set_event done");

            //Thread.sleep(100);

      //      process = Runtime.getRuntime().exec("echo > /d/tracing/trace");
       //     Log.d(TAG, "set trace null done");
        //    Thread.sleep(100);




            process = Runtime.getRuntime().exec("am trace-ipc start --no-dump");
            process.waitFor();
            Log.d(TAG, "trace-ipc start");

            //Thread.sleep(100);





                 //process = Runtime.getRuntime().exec("atrace --async_start -c -b 12800 -a '*' gfx input irq view sync");
      //          String tmpdir = null;
        //        String[] cmdarray = {"sh", "-c", "atrace -t 3 -b 12800 -a '*' gfx input irq view sync"};
       //         String[] cmdarray = {"sh", "-c", "gpiod"};
          //      String[] envp = {"TMPDIR=" + tmpdir};
           //     envp = tmpdir == null ? null : envp;

             //   Log.d(TAG, "exec: " + Arrays.toString(envp) + " " + Arrays.toString(cmdarray));

       //         RUNTIME.exec(cmdarray, envp);

      //         run_cmd (false , "gpiod");

              //  process = Runtime.getRuntime().exec(cmdarray, null);

           //     process.waitFor();


         //       process = Runtime.getRuntime().exec("echo 1 > d/tracing/events/irq/enable");
          //      process.waitFor();

            //    run_cmd (false , "echo 1 > sys/kernel/debug/tracing/events/irq/enable");
                sysnode_control(TRACING_NODE, WRITE, 1);
        //        atrace_cmd.setLength(0);
         //       atrace_cmd = atrace_cmd.append("workq regulators idle freq sched gfx input view rs audio am wm sm camera hal res dalvik webview power bionic video binder_driver pm ss adb binder_lock sync network database i2c vibrator aidl");
                process = Runtime.getRuntime().exec("atrace -t " + trace_time + " -b 12800 " + atrace_cmd.toString());

     //           process.waitFor();

//05-24 21:46:10.971 D       21992    SystraceApp:ServiceMain                                                     system clock 262389215/262389215025615 : 38604584
            Log.d(TAG, "system clock " + SystemClock.elapsedRealtime() + "/" + SystemClock.elapsedRealtimeNanos()
                    + " : " + SystemClock.uptimeMillis());

            float start_time_on_log = SystemClock.elapsedRealtime() / 1000;
            Log.d(TAG, "real start time: " + start_time_on_log);
            Thread.sleep(100);
            status_txt = "tracking...";
            mHandler.sendEmptyMessageDelayed(UPDATE_TXT, 0);

            long time_clock = System.currentTimeMillis();

            isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            int loop_cnt = 0;
            long currnt_time = 0;
            long prev_time = 0;

            int start_point = 0;

            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/SystraceApp", today);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    showToast(getApplication(), "Fail to create DIR");
                    return;
                } else {
                    Log.d(TAG, "create directory " + mediaStorageDir);
                }
            }
            String time = new SimpleDateFormat("HH_mm_ss").format(new Date());
            OutputStream out = new FileOutputStream(mediaStorageDir.toString() + "/" + time +".txt");
/*
            status_txt = "done";
            mHandler.sendEmptyMessageDelayed(UPDATE_TXT, 0);
*/
            while (true) {
                long time_spent = System.currentTimeMillis();

                if (count_line == 0) {
        //            stb = stb.append("START_TIME:" + start_time_on_log).append("\n");
                }

                line = br.readLine();
                count_line++;

                if (line == null) {
                    Log.d(TAG, "Capture break");
                    break;
                }


                currnt_time = (time_spent - time_clock) / 1000;

                if (currnt_time != prev_time) {
                    prev_time = currnt_time;
                    Log.d(TAG, "current time " + currnt_time);
                    status_txt = "dumping " + (currnt_time - trace_time) + "s";
                    mHandler.sendEmptyMessageDelayed(UPDATE_TXT, 0);
                }

                stb = stb.append(line).append("\n");

                if (stb.toString().contains("tracer: nop")) {
                    start_point = 1;
                }

                if (start_point == 1) {
                    byte[] content = stb.toString().getBytes();
                    out.write(content);
                }
                stb.setLength(0);
/*
                if (line.contains("VSYNC-sf") || line.contains("name=lge_touch") || line.contains("|iq|")
            || line.contains("deliverInputEvent") || line.contains("DrawFrame") ||
                line.contains("Choreographer#doFrame")) {
                    stb = stb.append(line).append("\n");

                    byte[] content = stb.toString().getBytes();
                    out.write(content);
                    stb.setLength(0);
                }
                */

            }
//		    str = stb.toString();
//		    Log.d(TAG, "result: " + str);
            br.close();
            isr.close();
            out.flush();
            out.close();
            /*
            status_txt = "dump files";
            mHandler.sendEmptyMessageDelayed(UPDATE_TXT, 0);

            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/SystraceApp", today);


            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                              Log.d(TAG, "failed to create directory");
                              showToast(getApplication(), "Fail to create DIR");
                              return;
                } else {
                    Log.d(TAG, "create directory " + mediaStorageDir);
                }
            }
            /*
            String time = new SimpleDateFormat("HH_mm_ss").format(new Date());
            txt_file_rw(mediaStorageDir.toString() + "/" + time +".txt", WRITE, stb.toString());
*/


            Log.d(TAG, "file writing done");
            showToast(getApplication(), "/" + today + "/" + time + ".txt " + "saved");
/*

            Log.d(TAG, "try to read: " + focusPath + "/test.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(focusPath + "/test.txt"));
            while ((line = bufferedReader.readLine()) != null) {
                Log.d(TAG, "data: " + line);
          //      stb.append(line);
            }
       //     str = stb.toString();
            bufferedReader.close();


 */
            status_txt = "done";
            mHandler.sendEmptyMessageDelayed(UPDATE_TXT, 0);
        } catch (Exception e) {
            Log.d(TAG,"ExCommand Function Error");
            e.printStackTrace();
        }
    }

    /*
    private class ExeCommand extends AsyncTask {
        private Context mContext;

        public ExeCommand(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Log.d(TAG, "try to Excommand");
                ExCommand();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG,"Start FileSearch Task");
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Object o) {
            Log.d(TAG,"End FileSearch Task");
        }
    }

*/

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG," START");
        mainActivity = new MainActivity();
        mContext = getApplicationContext();

        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                200,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.RIGHT | Gravity.CENTER;
        mView = inflate.inflate(R.layout.view_in_service_main, null);
        mAtraceData = mainActivity.get_atrace();
/*
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "x:" + event.getX() + " y:" + event.getY());




                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "Action down: " + event.getX());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "Action up: " + event.getX());
                        break;

                }

                return true;
            }
        });
*/
        trace_time = mainActivity.get_trace_time();
        Log.d(TAG ,"main WM: " + wm + " trace time " + trace_time);

        Point displaySize = new Point();
        wm.getDefaultDisplay().getSize(displaySize);

        bt =  (ImageView) mView.findViewById(R.id.drag_btn);
        textView = mView.findViewById(R.id.main_text);

        bt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            //    Log.d(TAG, "main event id: " + event.getToolType(event.getActionIndex()));
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "Action down: " + event.getX());
                        tool_status =  event.getToolType(event.getActionIndex());
                        downX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "Action up: " + event.getX());
                        upX = event.getX();

                        if (downX != 0 || upX != 0) {
                            if (Math.abs(Math.abs(downX) - Math.abs(upX)) < 30) {
                                if (tool_status == 1) {
                                    vibrator.vibrate(100);
                                    commandThread = new CommandThread();
                                    commandThread.start();
                                }
                            }
                        }

                        downX = 0;
                        upX = 0;

                        break;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        Log.d(TAG, "Button press: " + event.getX());
                        break;
                    case MotionEvent.ACTION_BUTTON_RELEASE:
                        Log.d(TAG, "Button release: " + event.getX());
                        break;
                }

                return true;
            }
        });

/*
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                if (penTestMain.get_service_status(penTestMain.get_service_status(penTestMain.CALL_FROM_MAIN)) == 1) {
                    Log.d(TAG, "Main button click " + penTestMain.extern_sysnode_control(penTestMain.CALL_FROM_MAIN));
                    Toast.makeText(mView.getContext(), "Switching...잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mView.getContext(), "Sub process 실행 해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
/*
        bt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.vibrate(100);
                Toast.makeText(mView.getContext(), "LongClick", Toast.LENGTH_SHORT).show();
      //          stopService(new Intent(PenService_Main.this, PenService_Main.class));
                return false;
            }
        });
        */
        wm.addView(mView, params);

        //  penTestMain.set_service_status(penTestMain.CALL_FROM_MAIN, 1);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "main event id: " + event.getToolType(event.getDeviceId()));
        return true;
    }


    private Thread mainThread;
    public static Intent serviceIntent = null;

    public static int mode = 0;
    public static int time_to_trace = 0;

    public Systrace_service() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
        showToast(getApplication(), "Start Service");

        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("aa hh:mm");
                boolean run = true;
                while (run) {
                    try {
                        Thread.sleep(1000 * 60 * 1); // 1 minute
                        Date date = new Date();
                        //showToast(getApplication(), sdf.format(date));
                        sendNotification(sdf.format(date));
                    } catch (InterruptedException e) {
                        run = false;
                        e.printStackTrace();
                    }
                }
            }
        });
        mainThread.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
        showToast(getApplication(), "Stop Service");
        Log.d(TAG, "service destroyed");

        if(wm != null) {
            if(mView != null) {
                wm.removeView(mView);

                mView = null;
            }
            wm = null;
        }

            serviceIntent = null;

/*
            setAlarmTimer();
            Thread.currentThread().interrupt();
*/

            if (mainThread != null) {
                mainThread.interrupt();
                mainThread = null;
            }
/*
            if (mBroadcastReceiver != null) {
                mContext.unregisterReceiver(mBroadcastReceiver);
                mBroadcastReceiver = null;
            }
*/
            if (commandThread != null) {
                commandThread.interrupt();
                commandThread = null;
            }

            Log.d(TAG, "service stop end");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void showToast(final Application application, final String msg) {
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                        .setContentTitle("SystraceApp Service")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}