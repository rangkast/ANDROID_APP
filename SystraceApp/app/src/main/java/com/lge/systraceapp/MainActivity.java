package com.lge.systraceapp;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.EntryXComparator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SystraceApp:Main";

    @GuardedBy("mLock")
    private static final Object mLock = new Object();
    public String Appdir = "/SystraceApp";
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList hasPermissions = new ArrayList();
    ArrayList permissonsRationale = new ArrayList();
    ArrayList folderDepth = new ArrayList();
    private static List<String> fileNameList;
    private static File[] filesPath;
    Vibrator vibrator;

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2003;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE= 2004;

    private  final int KILL_ACTIVITY = 0;
    private  final int START_ACTIVITY = 1;
    private  final int SEARCH_START = 2;
    private  final int SEARCH_END= 3;
    private  final int CLOSE_APP = 4;
    private  final int ERROR_LOG = 5;
    private  final int UPDATE_PROGRESS = 6;
    private  final int FLASH_DONE = 7;
    private  final int START_SERVICE = 8;

    public static final int FAILED_WRITE = 1;
    public static final int FAILED_READ = 2;
    public static final int FAILED_DELETE = 3;
    public static int READ = 0;
    public static int WRITE = 1;
    public static int DELETE = 2;
    public static int DELETE_ALL = 4;
    public static int ASSETS_COPY = 3;
    public static int HTML_WRITE = 5;
    public static int CROP_MODE = 6;
    private  final int ISFILE = 0;
    private  final int ISDIR = 1;
    private  final int ISNOTCONTROLFILE = 2;
    private  final int UNKNOWN = -1;

    private static String focusPath;
    private static String[] arrayPermissions;
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    private static  AssetManager am;

    private static Activity mActivity;

    private WebView webView;

    private View mView;

    private static double time_1, time_2 = 0;

    private static ListviewAdapter listviewAdapter;
    private ArrayList<Listviewitem> data_btn = new ArrayList<>();
    private static int maxDisplayWidth;
    private static int maxDisplayHeight;

    /* test */
    String MAIN_SYSFSPATH = "/sys/devices/virtual/input/lge_touch/fw_upgrade";
    String Active_SYSFSPATH = "/sys/devices/virtual/input/lge_touch/active_pen_status";
    String WRITE_TO_KERNEL_SYSFSPATH = "/sys/devices/virtual/input/lge_touch/app_fw_upgrade";

    static File mediaStorageDir;
    static File mediaStorageDir_settings;
    ArrayList<SettingsMarkerItem> data= new ArrayList<>();

    SettingsMarkerItem items;
    private ListView listView;
    private Settings_marker_Adapter adapter;
    private ProgressBar numberProgressBar;
    private FileSearchTask fileSearchTask;
    private FileCopyTask fileCopyTask;
    static int file_control_status = 0;
    /* TEST FEATURE */
    private static int LOCAL_TEST = 0;
    private static int KERNEL_TEST = 1;
    private static ProgressDialog progressDialog;

    ImageButton refresh;
    Button start;
    Button stop;
    Button setting;
    private String file_to_kernel = null;
    private String progress_message = null;
    private static String upgrade_status = null;

    private TextView textView;
    private TextView textView_folder_depth;

    private static ArrayList<Listviewitem> mAtraceData = new ArrayList<>();
    private static ArrayList<Listviewitem> mDrawingChartData = new ArrayList<>();
    private static ArrayList<Listviewitem> mClickData = new ArrayList<>();
    private static ArrayList<Listviewitem> mTimeData = new ArrayList<>();
    private Listviewitem item;


    static SettingDBHelper settingDBHelper = null;
    private static SQLiteDatabase sqliteDB_setting;

    private int check_permission(Context context, String strPermission) {
        if (context.checkCallingOrSelfPermission(strPermission) != PackageManager.PERMISSION_GRANTED) {
            String msg = "Permission Denial: pid="
                    + Binder.getCallingPid() + ", uid="
                    + Binder.getCallingUid()
                    + strPermission;
            Log.d(TAG, msg);
            throw new SecurityException(msg);
        }
        return 1;
    }

    public void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {

                    if (Systrace_service.serviceIntent==null) {
                        serviceIntent = new Intent(this, Systrace_service.class);
                        startService(serviceIntent);
                    } else {
                        serviceIntent = Systrace_service.serviceIntent;//getInstance().getApplication();
                        Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
                    }

                    /*
                    startService(new Intent(PenTestMain.this, PenService_Main.class));
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(
                            UPDATE,
                            RESULT, main_aes_status), 10);
                            */


            }
        } else {

                if (Systrace_service.serviceIntent==null) {
                    serviceIntent = new Intent(this, Systrace_service.class);
                    startService(serviceIntent);
                } else {
                    serviceIntent = Systrace_service.serviceIntent;//getInstance().getApplication();
                    Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
                }
                /*
                startService(new Intent(PenTestMain.this, PenService_Main.class));
                mHandler.sendMessageDelayed(mHandler.obtainMessage(
                        UPDATE,
                        RESULT, main_aes_status), 10);
                        */

        }
    }

    public String size2String(long filesize) {
        Integer unit = 1024;
        if (filesize < unit){
            return String.format("%d B", filesize);
        }
        int exp = (int) (Math.log(filesize) / Math.log(unit));

        return String.format("%.0f %s", filesize / Math.pow(unit, exp), "KMGTPE".charAt(exp-1));
    }

    private List<String> check_files_list (String path) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + path);
        focusPath = mediaStorageDir.getPath();
//        Log.d(TAG, "focusPath: " + focusPath);
        filesPath = mediaStorageDir.listFiles();
        List<String> arrayName = new ArrayList<>();
        if (filesPath != null) {
            for (int i = 0; i < filesPath.length; i++) {
        //        Log.d(TAG, Appdir + ": " + filesPath[i].getName() + " (" + size2String(filesPath[i].length()) + ")");
                int ret = file_format_check(filesPath[i].getName());
                if (ret == ISFILE) {
                    arrayName.add(filesPath[i].getName() + " (" + size2String(filesPath[i].length()) + ")");
                } else {
                    arrayName.add(filesPath[i].getName());
                }
            }
        } else {
            Log.d(TAG, "filespath is null");
        }
        return arrayName;
    }



    private void addItems(List<String> files) {

        data.clear();
        int position = -1;
        for (int i = 0; i < files.size(); i++) {
            int ret = file_format_check(files.get(i));
            for (int j = 0; j < filesPath.length; j++) {
  //              Log.d(TAG, "filepath " + filesPath[j] + " file " + files.get(i));
                String[] parser = files.get(i).split(" \\(");
                if (filesPath[j].toString().contains(parser[0])) {
                    position = j;
                    break;
                }
            }
            if (position > -1) {
                if (ret == ISFILE) {
                    if (files.get(i).contains(".html")) {
                        items = new SettingsMarkerItem(R.drawable.check, files.get(i), filesPath[position].toString());
                    } else {
                        items = new SettingsMarkerItem(R.drawable.pen, files.get(i), filesPath[position].toString());
                    }
                    data.add(items);
                } else if (ret == ISDIR) {
                    items = new SettingsMarkerItem(R.drawable.folder3, files.get(i), filesPath[position].toString());
                    data.add(items);
                } else {
        //            Log.d(TAG, "??");
                }
            }
        }

        adapter = new Settings_marker_Adapter(this, R.layout.settings_marker_item, data);
        listView.setAdapter(adapter);

        Log.d(TAG, "notifydatasetChanged");
        adapter.notifyDataSetChanged();
    }


    public int uncontrolable_file_check (String data) {
        int detect = UNKNOWN;
        if (data.contains("Guide.txt") || data.contains(".rc") || data.contains(".sh") ||
            data.contains(".7z") ||
            data.contains("atrace")) {
            detect = ISNOTCONTROLFILE;
        }
        return  detect;
    }

    public int file_format_check (String data) {
        int detect = UNKNOWN;

        if (data.contains(".")) {
            detect = ISFILE;
            /*
            if (data.contains("Guide.txt") || data.contains(".rc") || data.contains("sample.txt")) {
                detect = ISNOTCONTROLFILE;
            }

             */
        } else if (data.contains("atrace"))  {
            detect = ISFILE;
        } else {
            detect = ISDIR;
        }

        return detect;
    }

    private class FileCopyTask extends  AsyncTask {
        private Context mContext;

        public FileCopyTask(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Log.d(TAG, "create settings directory");


                //       Log.d(TAG, "settings file");
                mediaStorageDir_settings = new File(Environment.getExternalStorageDirectory() + Appdir + "/samples");
                if (!mediaStorageDir_settings.exists()) {
                    if (!mediaStorageDir_settings.mkdirs()) {
                        Log.d(TAG, "failed to create directory");
                    } else {
                        progress_message = "copy Guide.txt";
                        runOnUiThread(changeText);
                        txt_file_rw(mediaStorageDir_settings.toString(), ASSETS_COPY, "Guide.txt",null, 0);
                        progress_message = "copy sample.txt";
                        runOnUiThread(changeText);
                        txt_file_rw(mediaStorageDir_settings.toString(), ASSETS_COPY, "sample.txt",null,0);
                        progress_message = "copy test_view.html";
                        runOnUiThread(changeText);
                        txt_file_rw(mediaStorageDir_settings.toString(), ASSETS_COPY, "test_view.html", null,0);

                        mediaStorageDir_settings = new File(Environment.getExternalStorageDirectory() + Appdir + "/samples/data_files");
                        if (!mediaStorageDir_settings.exists()) {
                            if (!mediaStorageDir_settings.mkdirs()) {
                                Log.d(TAG, "failed to create directory");
                            } else {
                                progress_message = "copy .rc file";
                                runOnUiThread(changeText);
                                txt_file_rw(mediaStorageDir_settings.toString(), ASSETS_COPY, "init.lge.vendor.on_boot.rc",null,0);
                                progress_message = "copy atrace file";
                                runOnUiThread(changeText);

                                txt_file_rw(mediaStorageDir_settings.toString(), ASSETS_COPY, "atrace",null,0);
                                /* test for sh */
                                txt_file_rw(mediaStorageDir_settings.toString(), ASSETS_COPY, "test.sh", null,0);
                                /* copy */
                                /*
                                txt_file_rw(mediaStorageDir_settings.toString(), ASSETS_COPY, "dummy.7z", null,0);
                                */
                                progress_message = "copy done";
                                runOnUiThread(changeText);
                            }
                        }

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG,"Start FileCopy Task");
            mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 0);
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Object o) {
            Log.d(TAG,"End FileCopy Task");
            mHandler.sendEmptyMessageDelayed(SEARCH_START, 0);
        }
    }



    private class FileSearchTask extends AsyncTask {
        private Context mContext;

        public FileSearchTask(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                back_key_pressed = 0;
                StringBuilder path = new StringBuilder("");
                for (int i = 0; i < folderDepth.size(); i++) {
                    Log.d(TAG, "folderDepth: " + folderDepth.get(i));
                    if (i == 0) {
                        path.append(folderDepth.get(i));
                    } else {
                        path.append("/");
                        path.append(folderDepth.get(i));
                    }
                }

                fileNameList = new ArrayList<>();
                fileNameList = check_files_list(path.toString());


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


            mHandler.sendEmptyMessageDelayed(SEARCH_END, 0);
        }
    }

    private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //test code
        /*
        try {
            Log.d(TAG, "add delay 2000ms");
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        try {
            Log.d(TAG, "onCreate Start");
            textView = (TextView)  findViewById(R.id.textview_info);
            textView_folder_depth = (TextView) findViewById(R.id.textview_folder);
 //           textView.setMovementMethod(new ScrollingMovementMethod());
//            numberProgressBar = (ProgressBar) findViewById(R.id.number_progress_bar);
            progressDialog = new ProgressDialog(MainActivity.this);

            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            listView = (ListView) findViewById(R.id.settings_markerlist);
            final View header = getLayoutInflater().inflate(R.layout.settings_header, null, false);
            listView.addHeaderView(header);

            mActivity = MainActivity.this;


            refresh = (ImageButton) findViewById(R.id.button_1);

            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Refresh Btn clicked");
                    mHandler.sendEmptyMessageDelayed(SEARCH_START, 0);
                }
            });

            start = (Button) findViewById(R.id.button_3);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "START Btn clicked");
                    try {
                        checkPermission();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            stop = (Button) findViewById(R.id.button_2);

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "STOP Btn clicked");
                    try {
                        if (Systrace_service.serviceIntent != null) {
                            serviceIntent = Systrace_service.serviceIntent;//getInstance().getApplication();
                            stopService(serviceIntent);
                            serviceIntent = null;
                        }

                        if (serviceIntent!= null) {
                            stopService(serviceIntent);
                            serviceIntent = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            setting = (Button)  findViewById(R.id.button_4);
            setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Setting Btn clicked");
                    try {
                        Intent intent = null;
                        intent = new Intent(getBaseContext(), Setting_chart_Activity.class);
                        intent.putExtra("mDrawingChartData", mDrawingChartData);
                        intent.putExtra("mTimeData", mTimeData);
                        intent.putExtra("mAtraceData", mAtraceData);
                        if (intent != null) {
                            startActivityForResult(intent, SETTING_CHART);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //add Permission to ArrayList
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if (position != 0) {
                        String pickPath = data.get(position - 1).getPath();
                        String pickName = data.get(position - 1).getName();
                        Log.d(TAG, "onItemClick: " + position + " name: " + pickName + " path: " + pickPath);
                        if (file_format_check(pickName) == ISDIR) {
                            Log.d(TAG, pickName + " is Dir");
                            folderDepth.add(pickName);
                            mHandler.sendEmptyMessageDelayed(SEARCH_START, 0);
                        } else {
              //              Log.d(TAG, pickName + " is File");
                            if (pickName.contains(".txt")) {
                                if (pickName.contains("Guide.txt")) {
                                    //클릭 했을 때 동작
                                    textView.setText("");
                                    textView.setText(txt_file_rw(pickPath, READ, "",null,0));
                                } else {
                                    /* Test code for JvmStatic annotation */
                      //              Person person = Person.makePerson("fromJava" , 18);

                                    PersonDatabase.saveData(Person.makePerson("fromJava", 18));

                                    Intent intent = null;
                                    intent = new Intent(view.getContext(), TraceFileViewer.class);
                                    intent.putExtra("path", pickPath);
                                    if (intent != null) {
                                        //To Do
                                     //   startActivity(intent);
                                    }

                                }
                            } else {
                                if(pickName.contains(".html")) {

                                } else if (pickName.contains(".sh")) {
                                    Log.d(TAG, ".sh file clicked");
                                    try {
                                        Process root = Runtime.getRuntime().exec("sh");
                                        DataOutputStream os = new DataOutputStream(root.getOutputStream());
                                        os.writeBytes("sh " + pickPath + " \n");
                                        os.flush();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                    if (position != 0) {

                        String[] parser = data.get(position - 1).getPath().split(" \\(");
                        final String pickPath = parser[0];
                        String[] parser_1 = data.get(position - 1).getName().split(" \\(");
                        final String pickName = parser_1[0];
            //            Log.d(TAG, "onItemLongClick: " + position + " name: " + pickName + " path: " + pickPath);
                        if (file_format_check(pickName) == ISFILE &&
                                uncontrolable_file_check(pickName) != ISNOTCONTROLFILE) {
                     //       Log.d(TAG, pickName + " is File");
                            vibrator.vibrate(100);
    //
                            //                        numberProgressBar.setProgress(0); //clear

                            // test
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                            alertDialog.setTitle(focusPath);

                            // Setting Dialog Message

                            alertDialog.setMessage("\n     " + pickName);

                            // Setting Icon to Dialog

                            if (file_format_check(pickName) == ISFILE && pickName.contains(".html")) {
                                alertDialog.setIcon(R.drawable.check);
                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("View", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            Intent intent = null;
                                            intent = new Intent(view.getContext(), Web_View.class);
                                            intent.putExtra("path", pickPath);
                                            if (intent != null) {
                                                startActivity(intent);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                alertDialog.setIcon(R.drawable.pen);
                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("ANALYSIS", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {

                                            mView = mActivity.getLayoutInflater().inflate(R.layout.listview_layout, null);

                                            final ListView listView=(ListView)mView.findViewById(R.id.listviewbtn);

                                            Listviewitem items = null;
                                            data_btn= new ArrayList<>();

                                            items = new Listviewitem("    CLICK", "");
                                            data_btn.add(items);

                                            items = new Listviewitem("    DRAG", "");
                                            data_btn.add(items);


                                            listviewAdapter = new ListviewAdapter(getBaseContext(), R.layout.listview_item, data_btn);
                                            listView.setAdapter(listviewAdapter);

                                            listviewAdapter.notifyDataSetChanged();

                                            final AlertDialog listDialog = new AlertDialog.Builder(mActivity).create();
                                            listDialog.setView(mView);
                                            listDialog.setTitle("Analysis List");
                                            listDialog.setIcon(R.drawable.pen);

                                            listDialog.getWindow().setLayout(maxDisplayWidth * 3 / 4, maxDisplayHeight * 3 / 4);

                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    vibrator.vibrate(100);

                                                    final String name = listviewAdapter.getItem(i);
                                                    Log.d(TAG, "list name " + name + " position " + i);

                                                    if (name.contains("CLICK")) {
                                                            Intent intent = null;
                                                            intent = new Intent(view.getContext(), ClickTestActivity.class);
                                                            intent.putExtra("mClickChartData", mClickData);
                                                            intent.putExtra("path", pickPath);


                                                            if (intent != null) {
                                                                startActivity(intent);
                                                            }
                                                            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

                                                    } else if (name.contains("DRAG")) {

                                                            Intent intent = null;
                                                            intent = new Intent(view.getContext(), Systrace_chart.class);
                                                            intent.putExtra("mDrawingChartData", mDrawingChartData);
                                                            intent.putExtra("path", pickPath);
                                                            intent.putExtra("name", pickName);
                                                            intent.putExtra("focus_path", focusPath);
                                                            intent.putExtra("MaxWaitTime", Float.toString(get_max_time()));
                                                            if (intent != null) {
                                                                startActivity(intent);
                                                            }

                                                            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);

                                                    }

                                                    listDialog.dismiss();
                                                }
                                            });
                                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                @Override
                                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                    vibrator.vibrate(100);
                                                    return false;
                                                }
                                            });

                                            listDialog.show();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }





/*
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                        alertDialog.setTitle("Check Analysis Mode");
                                        // Setting Icon to Dialog
                                        alertDialog.setIcon(R.drawable.systrace_icon);
                                        // Setting Positive "Yes" Button
                                        alertDialog.setPositiveButton("Drag", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                try {
                                                    //롱클릭 했을 때 동작
                                                    Intent intent = null;
                                                    intent = new Intent(view.getContext(), Systrace_chart.class);
                                                    intent.putExtra("mDrawingChartData", mDrawingChartData);
                                                    intent.putExtra("path", pickPath);
                                                    intent.putExtra("name", pickName);
                                                    intent.putExtra("focus_path", focusPath);
                                                    intent.putExtra("MaxWaitTime", Float.toString(get_max_time()));
                                                    if (intent != null) {
                                                        startActivity(intent);
                                                    }

                                                    overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });

                                        // Setting Negative "NO" Button
                                        alertDialog.setNeutralButton("Click", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                try {
                                                    //롱클릭 했을 때 동작
                                                    Intent intent = null;
                                                    intent = new Intent(view.getContext(), StackedBarActivity.class);
                                                    if (intent != null) {
                                                        startActivity(intent);
                                                    }
                                                    overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        alertDialog.show();
*/
                                    }
                                });
                            }



                            if (!pickName.equals("sample.txt") && !pickName.equals("test_view.html")) {

                                // Setting Negative "NO" Button
                                alertDialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        txt_file_rw(pickPath, DELETE, "",null,0);
                                        mHandler.sendEmptyMessageDelayed(SEARCH_START, 0);
                                    }
                                });


                                alertDialog.setNeutralButton("DELTE ALL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        txt_file_rw(focusPath, DELETE_ALL, "",null,0);
                                        mHandler.sendEmptyMessageDelayed(SEARCH_START, 0);
                                    }
                                });
                                //                if (!pickName.contains(".txt"))
                            }
                            alertDialog.show();


                        }  else {

                        }
                    }
                    return true;
                }
            });

            if (checkPermissions() == false) {
                mHandler.sendEmptyMessageDelayed(START_ACTIVITY, 0);
            }


            Point displaySize = new Point();
            mActivity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            maxDisplayWidth = displaySize.x;
            maxDisplayHeight = displaySize.y;

            /*
            try {
                Log.d(TAG, "setenforce 0");
                Runtime.getRuntime().exec("setenforce 0");
                Runtime.getRuntime().exec("echo 1 > sys/devices/virtual/input/lge_touch/aes_mode");
            } catch (Exception e) {
                Toast.makeText(this, "Fail to set permissive", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
*/

            run_cmd("setenforce 0");
            String atrace_list = run_cmd("atrace --list_categories");
            String build_type = run_cmd("getprop ro.build.type");
            Log.d(TAG,"atrace categories: " + atrace_list + " build_type: " + build_type);
/*
            if (atrace_list.contains("error")) {
               Toast.makeText(this, "Need to setenforce 0", Toast.LENGTH_LONG).show();
               finish();
            } else {
*/
                String[] atrace_categories = atrace_list.split("\n");

                for (int i = 0; i < atrace_categories.length; i++) {
                    //        Log.d(TAG, atrace_categories[i]);

                    /* default */
                    if (    atrace_categories[i].contains("gfx") ||
                            atrace_categories[i].contains("input") ||
                            atrace_categories[i].contains("irq") ||
                            atrace_categories[i].contains("view")
                    ) {
                        item = new Listviewitem(atrace_categories[i], "1");
                    } else {
                        item = new Listviewitem(atrace_categories[i], "0");
                    }
                    mAtraceData.add(item);
                }

                if (Systrace_service.serviceIntent == null) {
                    serviceIntent = new Intent(this, Systrace_service.class);
                }

                /* Default setting */
                item = new Listviewitem("VSYNC", "1");
                mDrawingChartData.add(item);
                item = new Listviewitem("iq", "0");
                mDrawingChartData.add(item);
                item = new Listviewitem("IRQ", "1");
                mDrawingChartData.add(item);
                item = new Listviewitem("dInput", "0");
                mDrawingChartData.add(item);
                item = new Listviewitem("Choreograph", "0");
                mDrawingChartData.add(item);
                item = new Listviewitem("DrawFrame", "0");
                mDrawingChartData.add(item);

                item = new Listviewitem("TraceTime", "3");
                mTimeData.add(item);
                item = new Listviewitem("MaxWaitTime", "33.2");
                mTimeData.add(item);


                //set click data here
                item = new Listviewitem("main_touch", "1");
                mClickData.add(item);

                item = new Listviewitem("com.android.mms", "1");
                mClickData.add(item);







                if (init_database() == 0) {
                    init_tables();
                    String status = readDB("setting");
                    if (status == null) {
                        //         Log.d(TAG, "failed to read setting DB");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < mDrawingChartData.size(); i++) {
                            stringBuilder.append(mDrawingChartData.get(i).getName() + "," + mDrawingChartData.get(i).getData() + "/");
                        }
                        writeDB("setting", stringBuilder.toString());

                    } else {
                        //        Log.d(TAG, "Success to read setting DB: " + status);

                        mDrawingChartData.clear();

                        String[] parser = status.split("/");

                        for (int i = 0; i < parser.length; i++) {
                            String[] parser_1 = parser[i].split(",");
                            //         Log.d(TAG, "parser: " + parser[i]);
                            item = new Listviewitem(parser_1[0], parser_1[1]);
                            mDrawingChartData.add(item);

                        }
/*
                    for (int i = 0; i < mDrawingChartData.size(); i++) {
                        Log.d(TAG, "read ChartData: " + mDrawingChartData.get(i).getName() + " data: " +mDrawingChartData.get(i).getData());
                    }

 */
                    }


                    status = readDB("setting_time");
                    if (status == null) {
                        //        Log.d(TAG, "failed to read setting time DB");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < mTimeData.size(); i++) {
                            stringBuilder.append(mTimeData.get(i).getName() + "," + mTimeData.get(i).getData() + "/");
                        }
                        writeDB("setting_time", stringBuilder.toString());

                    } else {
                        //     Log.d(TAG, "Success to read setting DB: " + status);

                        mTimeData.clear();

                        String[] parser = status.split("/");

                        for (int i = 0; i < parser.length; i++) {
                            String[] parser_1 = parser[i].split(",");
                            //        Log.d(TAG, "parser: " + parser[i]);
                            item = new Listviewitem(parser_1[0], parser_1[1]);
                            mTimeData.add(item);

                        }
/*
                    for (int i = 0; i < mTimeData.size(); i++) {
                        Log.d(TAG, "read mTimeData: " + mTimeData.get(i).getName() + " data: " +mTimeData.get(i).getData());
                    }

 */
                    }


                    status = readDB("atrace_data");
                    if (status == null) {
                        Log.d(TAG, "failed to read atrace DB");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < mAtraceData.size(); i++) {
                            stringBuilder.append(mAtraceData.get(i).getName() + "," + mAtraceData.get(i).getData() + "&");
                        }

                        if (atrace_list.length() > 0) {
                            writeDB("atrace_data", stringBuilder.toString());
                        } else {
                            Toast.makeText(this, "Fail to read atrace list(setenforce 0)", Toast.LENGTH_LONG).show();
                        }

                    } else {
//                        Log.d(TAG, "Success to read atrace DB: " + status);

                        mAtraceData.clear();

                        String[] parser = status.split("&");

                        for (int i = 0; i < parser.length; i++) {
                            String[] parser_1 = parser[i].split(",");
                         //   Log.d(TAG, "parser: " + parser[i]);
                            if (parser_1.length > 1) {
                                item = new Listviewitem(parser_1[0], parser_1[1]);
                                mAtraceData.add(item);
                            } else {

                            }

                        }
                    }
/*
                    for (int i = 0; i < mAtraceData.size(); i++) {
                        Log.d(TAG, "read mAtraceData: " + mAtraceData.get(i).getName() + " data: " + mAtraceData.get(i).getData());
                    }
*/

                    am = getResources().getAssets();
                } else {
                    Log.d(TAG, "Failed to make DB");
                }

  /*     here here
            }
*/

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Abnormal status", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private Runnable changeText = new Runnable() {
        @Override
        public void run() {
            progressDialog.setMessage(progress_message);
        }
    };

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

            SettingsMarkerItem listviewitem=data.get(position);
            holder.image.setImageResource(listviewitem.getIcon());
            holder.name.setText(listviewitem.getName());

            return convertView;
        }

    }

    private static class ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.textview_marker);
            image = (ImageView)v.findViewById(R.id.imageview_marker);
        }
    }

    private String run_cmd(String cmd) {

        Process process;
        InputStreamReader isr;

        String line;
        String str = "";
        StringBuffer stb = new StringBuffer();


        try {
            process = Runtime.getRuntime().exec(cmd);
            isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            textView.setText("");
   //         stb.append("Touch Systrace App V0.1").append("\n\n");

            while (true) {
                line = br.readLine();
                if (line == null) {
      //              Log.d(TAG, "read break");
                    break;
                }

                stb = stb.append(line).append("\n");

            }
/*
            if (!stb.toString().contains("version")) {
                stb.append("\nCheck root and permissive status").append("\n");
            }

 */
            br.close();
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
            stb.append("error");
        }

        return stb.toString();
    }


    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            //      Log.d(TAG, "mHandler " + msg.what);
            switch (msg.what) {
                case KILL_ACTIVITY:
                //    Log.d(TAG, "finish");
                    finish();
                    break;
                case START_ACTIVITY:
                    mediaStorageDir = new File(Environment.getExternalStorageDirectory() + Appdir);
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.d(TAG, "failed to create directory");
                            mHandler.sendEmptyMessageDelayed(KILL_ACTIVITY, 0);
                        } else {
                            Log.d(TAG, "created directory");
                        }
                    }

                    folderDepth.add(Appdir);


                    if (fileCopyTask != null) {
                        if (fileCopyTask.getStatus() == AsyncTask.Status.RUNNING) {
                            fileCopyTask.cancel(true);
                        }
                    }
                    fileCopyTask = new FileCopyTask(getBaseContext());
                    fileCopyTask.execute();

                    break;
                case SEARCH_START:
                    if (fileSearchTask != null) {
                        if (fileSearchTask.getStatus() == AsyncTask.Status.RUNNING) {
                            fileSearchTask.cancel(true);
                        }
                    }
                    fileSearchTask = new FileSearchTask(getBaseContext());
                    fileSearchTask.execute();
                    progress_message = "Done";
                    runOnUiThread(changeText);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    try {
                        StringBuffer stb = new StringBuffer();
                        textView.setText("");
                        stb.append(run_cmd("cat sys/devices/virtual/input/lge_touch/version") + "\n");
                        stb.append("BuildType: " + run_cmd("getprop ro.build.type"));
                        stb.append("SDK: " + run_cmd("getprop ro.build.version.sdk"));
                        stb.append("SWVersion: " + run_cmd("getprop ro.vendor.lge.swversion"));
                        textView.setText(stb.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case SEARCH_END:
                    Collections.sort(fileNameList, comparable);
                    addItems(fileNameList);

                    StringBuilder depth = new StringBuilder("");
                    if (folderDepth != null) {
                        for (int i = 0; i < folderDepth.size(); i++) {
                            depth.append(folderDepth.get(i) + "/");

                        }
                    }

                    textView_folder_depth.setText(depth.toString());

                    break;
                case CLOSE_APP:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("Touch Systrace App");
                    // Setting Dialog Message
                    alertDialog.setMessage("\n     Do you want to quit?");
                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.systrace_icon);
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onStop();
                            finish();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            back_key_pressed = 0;
                        }
                    });

                    alertDialog.show();
                    break;
                case ERROR_LOG:
                    switch (msg.arg1) {
                        case FAILED_WRITE:
                            Toast.makeText(MainActivity.this, "Failed to Write", Toast.LENGTH_SHORT).show();
                            break;
                        case FAILED_READ:
                            Toast.makeText(MainActivity.this, "Failed to READ", Toast.LENGTH_SHORT).show();
                            break;
                        case FAILED_DELETE:
                            Toast.makeText(MainActivity.this, "Failed to DELETE", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    upgrade_status = null;
                    break;
                case UPDATE_PROGRESS:
                    try {
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setIcon(R.drawable.pen);
                        progressDialog.setCanceledOnTouchOutside(false);
         //               progressDialog.setTitle(file_to_kernel);
                        progressDialog.setMessage("Init Start");
                        progressDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case FLASH_DONE:
                    if (!upgrade_status.contains("ERROR")) {
                        Toast.makeText(MainActivity.this, "Firmware Update Complete", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case START_SERVICE:
                    if (Systrace_service.serviceIntent == null) {
                        startService(serviceIntent);
                    } else {
                        serviceIntent = Systrace_service.serviceIntent;//getInstance().getApplication();
                        Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private Comparator<String> comparable = new Comparator<String>() {

        public int compare(String s1, String s2) {
            int s1_status = file_format_check(s1);
            int s2_status = file_format_check(s2);

            if (s2_status > s1_status)
                return 1;

            return -1;
        }
    };

    static String txt_file_rw(String path, int rw, String data, String data_1, int mode) {
        String str = null;

        String line;
        Log.d(TAG, "txt_file_rw " + rw);
        synchronized (mLock) {
            if (rw == READ) {
                StringBuffer stringBuffer = new StringBuffer("");
                //read test
                try {
                    BufferedReader bufferedReader;
                    Log.d(TAG, "try to read: " + path);
                    bufferedReader = new BufferedReader(new FileReader(path));
                    while ((line = bufferedReader.readLine()) != null) {
    //                    Log.d(TAG, "data: " + line);
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
                        Log.d(TAG, file.toString() + "deleted");
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

                File file;
                if (mode == CROP_MODE) {
                    file = new File(data + "/" + data_1);
                    Log.d(TAG, "delete " + file);
                } else {
                    file = new File(path);
                    Log.d(TAG, "delete " + file);
                }
                if (file.exists()) {
                    file.delete();
                    return "success";
                }
            } else if (rw == ASSETS_COPY) {

            try {
                Log.d(TAG, "ASSETS_COPY" + path + " data: " + data + " data_1: " + data_1);
     //           Log.d(TAG, "try to write " + path + " data: " + data);

                InputStream in = null;

                in = am.open(data);
                File write_file;
                if (data_1 != null) {
                    write_file = new File(path + "/" + data_1);
                    if (write_file.exists()) {
                        Log.d(TAG, write_file.toString() + " exist");
                        return "exist";
                    }
                } else {
                    write_file = new File(path + "/" + data);
                    if (write_file.exists()) {
                        Log.d(TAG, write_file.toString() + " exist");
                        return "exist";
                    }
                }

                OutputStream out = new FileOutputStream(write_file, false);
                int data_size = (64 * 1024);
                byte[] buffer = new byte[data_size];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                    try {
                        Thread.sleep(3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                in.close();

                // write the output file (You have now copied the file)
                out.flush();
                out.close();
                Log.d(TAG, "copy done");


            } catch (Exception e) {
                e.printStackTrace();
            }



            } else if (rw == DELETE_ALL) {
                for (int i = 0; i < fileNameList.size(); i++) {
                    String[] parser = fileNameList.get(i).split(" \\(");
                    File file = new File(path + "/" + parser[0]);
                    file.delete();
                }
            } else if (rw == HTML_WRITE) {
                Log.d(TAG, "HTML_WRITE " + path + " data: " + data + " data_1: " + data_1);

                try {

                    String line_r;
                    StringBuffer stb = new StringBuffer();
                    BufferedReader  bufferedReader= new BufferedReader(new FileReader(path));
                    int count_line = 0;

                    File write_file = new File(data + "/" + data_1);

                    OutputStream out = new FileOutputStream(write_file, true);

                    /* start add trace to HTML */
                    stb.append("\n");
                    byte[] content_n = stb.toString().getBytes();
                    out.write(content_n);
                    stb.setLength(0);

                    while (true) {

                        int pass = 0;
                        double time_ch_data = 0.0d;
                        line_r = bufferedReader.readLine();

                        if (line_r == null) {
                            Log.d(TAG, "read break " + count_line);
                            break;
                        }

                        if (mode == CROP_MODE) {

                            if (line_r.contains("# ")) {
                                pass = 1;
                            }

                            if (pass == 0) {
                                String[] parser = line_r.split(": ");

                                for (int j = 0; j < parser.length; j++) {
                            //        Log.d(TAG, "[" + j + "]" + parser[j]);
                                    if (j == 0) {
                                        try {
                                            time_ch_data = Double.parseDouble(parser[0].substring(parser[0].lastIndexOf(" ")));

                                            if ((time_ch_data > time_1 && time_ch_data < time_2)) {
                                                stb = stb.append(line_r).append("\n");

                                                byte[] content = stb.toString().getBytes();
                                                count_line++;

                                                if (count_line > 10000) {
                                                    Log.d(TAG, "writing...");
                                                    count_line = 0;
                                                }

                                                out.write(content);
                                                stb.setLength(0);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.d(TAG, "error " + line_r);
                                        }
                                    }
                                }
                            }

                            if (pass == 1) {
                                stb = stb.append(line_r).append("\n");

                                byte[] content = stb.toString().getBytes();
                                count_line++;

                                if (count_line > 10000) {
                                    Log.d(TAG, "writing...");
                                    count_line = 0;
                                }

                                out.write(content);
                                stb.setLength(0);
                            }

                        } else {


                            stb = stb.append(line_r).append("\n");

                            byte[] content = stb.toString().getBytes();
                            count_line++;

                            if (count_line > 10000) {
                                Log.d(TAG, "writing...");
                                count_line = 0;
                            }

                            out.write(content);
                            stb.setLength(0);

                        }

                    }

                    stb.setLength(0);
                    stb.append("  </script>").append("\n");
                    stb.append("<!-- END TRACE -->").append("\n");
                    stb.append("</body>").append("\n");
                    stb.append("</html>").append("\n");

                    byte[] content = stb.toString().getBytes();
                    out.write(content);

                    bufferedReader.close();
                    out.flush();
                    out.close();
                    Log.d(TAG, "copy done");


                } catch (Exception e) {
                    e.printStackTrace();
                }








                /*


                try {
                    FileInputStream in = new FileInputStream(path);
                    File write_file = new File(data + "/" + data_1);

                    OutputStream out = new FileOutputStream(write_file, true);
                    int data_size = (8 * 1024); //8KB writing
                    byte[] buffer = new byte[data_size];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    StringBuilder stb = new StringBuilder("\n");
                    stb.append("  </script>").append("\n");
                    stb.append("<!-- END TRACE -->").append("\n");
                    stb.append("</body>").append("\n");
                    stb.append("</html>").append("\n");

                    byte[] content = stb.toString().getBytes();
                    out.write(content);

                    in.close();
                    out.flush();
                    out.close();
                    Log.d(TAG, "copy done");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                 */
            }
        }

        return str;
    }

    public static String byteArrayToBinaryString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; ++i) {
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    public String write(byte b[], int off, int len) throws IOException {
        StringBuilder ret_str = new StringBuilder("");

        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return null;
        }
        for (int i = 0 ; i < len ; i++) {
            Log.d(TAG, Byte.toString(b[off + i]));
            ret_str.append(Byte.toString(b[off + i]));
        }
        Log.d(TAG, ret_str.toString());
        return ret_str.toString();
    }

    private void file_control(String from_path, String to_path, int rw) {

        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        String line;
        synchronized (mLock) {
            if (rw == READ) {
                //read test
                try {
                    Log.d(TAG, "try to read: " + to_path);
                    bufferedReader = new BufferedReader(new FileReader(to_path));
                    while ((line = bufferedReader.readLine()) != null) {
                        Log.d(TAG, "data: " + line);
                        upgrade_status = line;
                    }
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (rw == WRITE) {
                //write test
                mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 0);
                try {
                    File file = new File(from_path);
                    int data_size = (4 * 1024); //4KB writing
                    int progress_max = ((int) (long) file.length()) / data_size;
                    if (progress_max == 0)
                        progress_max = 1;
                    numberProgressBar.setMax(progress_max);
                    progress_message = "Copy to Kernel";
                    runOnUiThread(changeText);

                    Log.d(TAG, "from: " + from_path + " to: " + to_path);
                    Log.d(TAG, "progress_max: " + progress_max);
                    InputStream in = new FileInputStream(from_path);
                    OutputStream out = new FileOutputStream(to_path);


                    byte[] buffer = new byte[data_size];
                    int read;
                    int progress_status = 0;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (progress_status <= progress_max) {
                            progress_status++;
      //                      numberProgressBar.setProgress(progress_status);
                        }
                    }
                    in.close();

                    // write the output file (You have now copied the file)
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    upgrade_status = "ERROR";
                    mHandler.sendMessage(mHandler.obtainMessage(
                            ERROR_LOG,
                            FAILED_WRITE, 0));
                }

                file_control_status = 0;

            } else if (rw == DELETE) {
                try {
                    Log.d(TAG, "try to delete" + from_path);
                    File file = new File(from_path);
                    file.delete();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<Byte> byteArrayToList(byte[] bytes) {
        ArrayList<Byte> list = new ArrayList<Byte>();
        for (byte b : bytes)
            list.add(new Byte(b));
        return list;
    }

    public ArrayList<Integer> intArrayToList(int[] ints) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int b : ints)
            list.add(new Integer(b));
        return list;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        int result = -2;
    //    Log.d(TAG, "permissionResult");

        for  (int i = 0; i < grantResults.length; i++) {
     //       Log.d(TAG, "results[" +i +"]: " + grantResults[i] + " RequseCode:" + permsRequestCode +" String[" + i +"]:" + permissions[i]);
            if (permissions[i].toString().contains(arrayPermissions[i]) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                result++;
            }
        }

        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission accepted");
            mHandler.sendEmptyMessageDelayed(START_ACTIVITY, 0);
            Toast.makeText(this, "Permission Get Success", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "permission denied");
            Toast.makeText(this, "Permission Get Failed", Toast.LENGTH_LONG).show();
            mHandler.sendEmptyMessageDelayed(KILL_ACTIVITY, 0);
        }
    }
    private ArrayList<Listviewitem> returnChartData;
    private final int SETTING_CHART = 1;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case SETTING_CHART:
                if (resultCode == RESULT_OK) {


                    try {
                                                /*
                        mAtraceData.clear();
                        mAtraceData = (ArrayList<Listviewitem>) getIntent().getSerializableExtra("atraceData");
                        for (int i = 0; i < mAtraceData.size(); i++) {
                            Log.d(TAG, "read mAtraceData: " + mAtraceData.get(i).getName() + " data: " +mAtraceData.get(i).getData());
                        }

                    returnChartData = (ArrayList<Listviewitem>) getIntent().getSerializableExtra("atraceData");

                    for (int i = 0; i < returnChartData.size(); i++) {
                        Log.d(TAG, "read ChartData: " + returnChartData.get(i).getName() + " data: " +returnChartData.get(i).getData());
                    }
*/
                        String status = data.getStringExtra("result");
              //          Log.d(TAG, " " + status);


                        mDrawingChartData.clear();

                        String[] parser = status.split("/");

                        for (int i = 0; i < parser.length; i++) {
                            String[] parser_1 = parser[i].split(",");
                 //           Log.d(TAG, "parser: " + parser[i]);
                            item = new Listviewitem(parser_1[0], parser_1[1]);
                            mDrawingChartData.add(item);

                        }

                        writeDB("setting", status);


                        status = data.getStringExtra("atraceData");

                        mAtraceData.clear();

                        String[] parser_a = status.split("&");

                        for (int i = 0; i < parser_a.length; i++) {
                            String[] parser_1 = parser_a[i].split(",");
               //             Log.d(TAG, "parser: " + parser_a[i]);
                            item = new Listviewitem(parser_1[0], parser_1[1]);
                            mAtraceData.add(item);

                        }

                        writeDB("atrace_data", status);

                        Log.d(TAG, " atarce data size: " + mAtraceData.size());
/*
                        for (int i = 0; i < mAtraceData.size(); i++) {
                            Log.d(TAG, mAtraceData.get(i).getName() + " / " + mAtraceData.get(i).getData());
                        }
*/
                        status = data.getStringExtra("result_time");
                 //       Log.d(TAG, " " + status);

                        mTimeData.clear();

                        String[] parser_t = status.split("/");

                        for (int i = 0; i < parser_t.length; i++) {
                            String[] parser_1 = parser_t[i].split(",");
                     //       Log.d(TAG, "parser: " + parser_t[i]);
                            item = new Listviewitem(parser_1[0], parser_1[1]);
                            mTimeData.add(item);

                        }

                        writeDB("setting_time", status);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    //     Log.d(TAG, "canceled");
                }
                break;

        }

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermissions() {
        int permissonCnt = 0;
        Boolean needPermissionRequest = false;
        Boolean needRationaleRequest = false;

        permissonCnt = permissions.size();

        arrayPermissions = permissions.toArray(new String[permissonCnt]);

        for (int i = 0; i < permissonCnt; i++) {
            Object obj = permissions.get(i);
            if (obj instanceof String) {
                String str = (String) obj;
                permissonsRationale.add(new Boolean(ActivityCompat.shouldShowRequestPermissionRationale(this, str)));
                hasPermissions.add(new Integer(ContextCompat.checkSelfPermission(this, str)));
                      Log.d(TAG, " " + str + ": " + hasPermissions.get(i) + ", " + permissonsRationale.get(i));

                if ((Integer) hasPermissions.get(i) == PackageManager.PERMISSION_DENIED) {
                    needPermissionRequest = true;
                }

                if ((Boolean) permissonsRationale.get(i) == false) {
                    needRationaleRequest = true;
                }
            }
                        Log.d(TAG,"requestPermissionString: " + arrayPermissions[i]);

        }
        if (needPermissionRequest) {
            ActivityCompat.requestPermissions(this, arrayPermissions, 0);
            //     Log.d(TAG,"requesting...");
        }
        return needPermissionRequest;
    }

    private int back_key_pressed = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "keyevent " + event.getAction() + " keycode" + keyCode);

        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                break;
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "KEYCODE_BACK " + folderDepth.size());
                if (folderDepth.size() > 1) {
                    folderDepth.remove(folderDepth.size() - 1);
                    mHandler.sendEmptyMessageDelayed(SEARCH_START, 0);
                } else {
                    back_key_pressed = 1;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        if (back_key_pressed == 1) {
            mHandler.sendEmptyMessageDelayed(CLOSE_APP, 0);
        }
    }

    @Override
    protected void onDestroy() {
        if (fileSearchTask != null) {
            if (fileSearchTask.getStatus() == AsyncTask.Status.RUNNING) {
                fileSearchTask.cancel(true);
            }
        }
        if (fileCopyTask != null) {
            if (fileCopyTask.getStatus() == AsyncTask.Status.RUNNING) {
                fileCopyTask.cancel(true);
            }
        }

        super.onDestroy();
    }

    public void run_activiy(int control) {
        try {
            switch (control) {
                case 1:
                    mHandler.sendEmptyMessageDelayed(SEARCH_START, 0);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        mHandler.sendEmptyMessageDelayed(SEARCH_START, 0);
        super.onResume();
    }

    public static void update_list() {
        MainActivity mainActivity = new MainActivity();
        mainActivity.run_activiy(1);

    }

    public int init_database() {
        int ret = 0;

        File file;

        file = new File(getFilesDir(), "systrace_setting.db");
        //       Log.d(TAG, "PATH: " +file.toString());
        try {
            sqliteDB_setting = SQLiteDatabase.openOrCreateDatabase(file, null) ;
        } catch (SQLiteException e) {
            ret = -1;
            e.printStackTrace() ;
        }

        if (sqliteDB_setting == null) {
            //         Log.d(TAG, "DB create failed: " +file.getAbsolutePath());
            ret = -1;
        } else {
            //       Log.d(TAG, "DB load success: " +file.getAbsolutePath());
        }


        return ret ;
    }



    public void init_tables() {
        Log.d(TAG, "init_tables");
        settingDBHelper = new SettingDBHelper(this);


        /*
            try {
                deleteDB("setting");
                deleteDB("setting_time");
                deleteDB("atrace_data");
            } catch (Exception e) {
                e.printStackTrace();
            }
*/

    }

    public String load_values(String data) {
        String return_string = "null";
        try {
            return_string = readDB(data);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return return_string;
    }
    public static String settingDB_translation(String data) {
        String id = null;

        try {

            /* setting DB */
            if (data.equals("setting")) {
                id = "<setting>";
            } else if (data.equals("setting_time")){
                id = "<setting_time>";
            } else if (data.equals("atrace_data")) {
                id = "<atrace_data>";
            }
            Log.d(TAG, id);

        } catch (Exception e) {
               e.printStackTrace();
        }

        return id;
    }

    public static void deleteDB(String id) {
        sqliteDB_setting = settingDBHelper.getWritableDatabase();
        Log.d(TAG, "try to deleteDB " + id);
        String new_id = settingDB_translation(id);

        if (new_id.contains("null"))
            return;

        try {
            sqliteDB_setting.delete("setting", "id='" + new_id + "'", null);
            Log.d(TAG, "memoDB delete success: " + new_id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "memoDB delete failed: " + new_id);
        }

    }

    public static void writeDB(String id, String info) throws Exception {
        ContentValues values = new ContentValues();
        sqliteDB_setting = settingDBHelper.getWritableDatabase() ;

        String new_id = settingDB_translation(id);

        if (new_id.contains("null")) {
            return;
        }
        values.put("id", new_id);
        values.put("info", info);
        //     int colNum = db.update("memo", values, null, null);
        //     Log.d(TAG, "update return: " + colNum);
        //    if (colNum == 0) {
        //     Log.d(TAG, "wrtieMemoDB new_id " + new_id);
        try {
            sqliteDB_setting.insertOrThrow("setting", "", values);
            //       Log.d(TAG, "insert memoDB");
        } catch (Exception e) {
            try {
                sqliteDB_setting.replaceOrThrow("setting", "", values);
                //          Log.d(TAG, "replace memoDB");
            } catch (Exception ex) {
                //        Log.d(TAG, "replace memoDB failed");
                ex.printStackTrace();
            }
        }
        //  }
    }

    public static String readDB(String id) throws Exception {
        sqliteDB_setting = settingDBHelper.getWritableDatabase() ;

        String new_id = settingDB_translation(id);
        Cursor c = null;
        if (new_id.contains("null"))
            return null;

        try {
            c = sqliteDB_setting.query("setting", new String[]{"id", "info"}, "id='" + new_id + "'", null, null, null, null);
            int cnt = c.getCount();
            //        Log.d(TAG, "memoDB cnt: " + cnt);

            if (cnt == 0) {
                return null;
            }

            c.moveToFirst();
            String str = c.getString(1);
            c.close();
/*
            Cursor cursor = db.rawQuery("SELECT * FROM " + "memo", null);
            int total_cnt = cursor.getCount();
            Log.d(TAG, "memoDB total cnt: " + total_cnt);


        if (total_cnt > 0) {
            cursor.moveToFirst();

            for (int i = 0; i < total_cnt; i++) {
                Log.d(TAG, "id: " + cursor.getString(0) + "\ninfo: " + cursor.getString(1));
                cursor.moveToNext();
            }
        }

            cursor.close();
            */
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            c.close();
            return null;
        }

    }

    public ArrayList<Listviewitem> get_atrace() {
        return mAtraceData;
    }

    public int get_trace_time() {
        int time = 0;

        for (int i = 0; i< mTimeData.size(); i++) {
            if (mTimeData.get(i).getName().equals("TraceTime")) {
                time = Integer.parseInt(mTimeData.get(i).getData());
            }
        }
        /*
        for (int i = 0; i < mTimeData.size(); i++) {
            Log.d(TAG, "read mTimeData: " + mTimeData.get(i).getName() + " data: " +mTimeData.get(i).getData());
        }

         */
        return time;
    }

    public float get_max_time() {
        float time = 0;

        for (int i = 0; i< mTimeData.size(); i++) {
            if (mTimeData.get(i).getName().equals("MaxWaitTime")) {
                time = Float.parseFloat(mTimeData.get(i).getData());
            }
        }
        /*
        for (int i = 0; i < mTimeData.size(); i++) {
            Log.d(TAG, "read mTimeData: " + mTimeData.get(i).getName() + " data: " +mTimeData.get(i).getData());
        }

         */
        return time;
    }

    public void set_crop_time (double x1, double x2) {
        time_1 = x1;
        time_2 = x2;
        Log.d(TAG, "set crop time " + time_1 + " " + time_2);
    }
}
