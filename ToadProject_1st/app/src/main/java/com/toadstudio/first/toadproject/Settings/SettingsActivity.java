package com.toadstudio.first.toadproject.Settings;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.toadstudio.first.toadproject.BackUp.GoogleSignInActivity;
import com.toadstudio.first.toadproject.CustomDialog.ListCustomDialog_2;
import com.toadstudio.first.toadproject.CustomDialog.ListDialogListener;
import com.toadstudio.first.toadproject.FileController;
import com.toadstudio.first.toadproject.Image.ImagesView;
import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by rangkast.jeong on 2018-03-16.
 */

public class SettingsActivity extends AppCompatActivity {
    public static String TAG = "ToadPrj_Settings";

    MainActivity mainActivity;

    private SettingsAdapter mAdapter;
    private AdView mAdView;
    private ContentResolver cr;
    private String photomap_path;
    private static Context mContext;


    private static Uri main_path = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final ListView listView = (ListView) findViewById(R.id.settings_listview);

        mContext = SettingsActivity.this;
        mAdapter = new SettingsAdapter(this);
//Settings List
        //       mAdapter.addSectionHeaderItem(new SettingsItem(("????????????"), ""));
        //      mAdapter.addItem(new SettingsItem(("?????? ??????"), "?????? 3?????? ?????? ?????? (??? ????????????/???????????????/?????????)"));
        mAdapter.addSectionHeaderItem(new SettingsItem(("????????????"), ""));
        mAdapter.addItem(new SettingsItem(("??????"), "?????? ????????? ?????? ??? ????????????."));
        mAdapter.addItem(new SettingsItem(("?????????"), "?????? ??????????????? ????????? ??? ????????????."));
        mAdapter.addItem(new SettingsItem(("????????????"), "???/?????? ?????? ????????? ???????????????."));
        mAdapter.addItem(new SettingsItem(("????????????"), "???????????? ?????? ???????????????."));
        /*
        mAdapter.addSectionHeaderItem(new SettingsItem(("????????????"), ""));
        mAdapter.addItem(new SettingsItem(("?????? ??????"), "?????? ????????? ???????????? ?????? ?????? ?????? ??????"));
        mAdapter.addItem(new SettingsItem(("?????? ??????"), "??? ?????? ?????? ??????"));
        */
        mAdapter.addSectionHeaderItem(new SettingsItem(("?????????"), ""));
        mAdapter.addItem(new SettingsItem(("????????? ?????????"), "????????? ?????? ????????? ?????? ?????????."));
        mAdapter.addItem(new SettingsItem(("?????? ??????"), "?????? ????????? ???????????????."));
        mAdapter.addSectionHeaderItem(new SettingsItem(("??? ??????"), ""));
        //app version ??????
        mAdapter.addItem(new SettingsItem(("??? ??????"), getBaseContext().getResources().getString(R.string.app_version)));
        mAdapter.addItem(new SettingsItem(("????????????"), "Apache license v2.0"));
        mAdapter.addItem(new SettingsItem(("???????????? ????????????"), "?????? ??? ???????????????."));
        mAdapter.addItem(new SettingsItem(("?????? ?????????"), "?????? ??? ???????????????."));
        mAdapter.addItem(new SettingsItem(("ToadStudio"), "toadstudio.rangkast@gmail.com"));

        cr = getApplicationContext().getContentResolver();

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //      Log.d(TAG, "onItemClick " + i);
                mainActivity = new MainActivity();
                if (mAdapter.getItem(i).getText_1().equals("????????? ?????????")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("????????? ??????");
                    // Setting Dialog Message
                    alertDialog.setMessage("????????? ?????? ????????????????????");
                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.toad_app_icon_v4);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mainActivity.delete_values();
                            Toast.makeText(getApplicationContext(), "?????? ???????????????. 3?????? ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();

                            KillApp();
                            // Write your code here to invoke YES event
                            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            //               Toast.makeText(getApplicationContext(), "?????? ???????????????.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    alertDialog.show();

                } else if (mAdapter.getItem(i).getText_1().equals("??????")) {
                    Intent intent = new Intent(getBaseContext(), Settings_marker_Activity.class);
                    intent.putExtra("marker_choice", mainActivity.get_marker_choice());
                    startActivityForResult(intent, FOLDER_MARKER);
                } else if (mAdapter.getItem(i).getText_1().equals("?????? ??????")) {
                    Intent intent = new Intent(getBaseContext(), Settings_marker_Activity.class);
                    intent.putExtra("level_choice", mainActivity.get_level_choice());
                    startActivityForResult(intent, FOLDER_LEVEL);
                } else if (mAdapter.getItem(i).getText_1().equals("?????? ??????")) {
                    try {
             //           mainActivity.add_folders();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (mAdapter.getItem(i).getText_1().equals("?????? ??????")) {
                    try {
           //             mainActivity.delete_folders();
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (mAdapter.getItem(i).getText_1().equals("????????????")) {
                    Intent intent = new Intent(getBaseContext(), InfoActivity.class);
                    intent.putExtra("from", 1);
                    getBaseContext().startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                } else if (mAdapter.getItem(i).getText_1().equals("???????????? ????????????")) {
                    try {
                        Intent intent_url = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/rangkast85/221480562737"));
                        intent_url.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent_url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (mAdapter.getItem(i).getText_1().equals("ToadStudio")) {
                    try {
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "????????? ?????? - Android");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"toadstudio.rangkast@gmail.com"});
                        startActivityForResult(emailIntent, EMAIL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (mAdapter.getItem(i).getText_1().equals("?????? ?????????")) {
                    try {
                        Intent intent_url = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/rangkast85/221501308118"));
                        intent_url.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent_url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (mAdapter.getItem(i).getText_1().equals("?????????")) {
                    Intent intent = new Intent(getBaseContext(), Settings_camera_Activity.class);
                    intent.putExtra("cam_choice", mainActivity.get_cam_choice());
                    startActivityForResult(intent, CAMERA);
                } else if (mAdapter.getItem(i).getText_1() == "?????? ??????") {
                    Intent intent_backup = new Intent(getBaseContext(), GoogleSignInActivity.class);
                    intent_backup.putExtra("data", "photomap_data.db");
                    intent_backup.putExtra("setting", "photomap_setting.db");
                    intent_backup.putExtra("memo", "photomap_memo.db");
                    startActivity(intent_backup);
                } else if (mAdapter.getItem(i).getText_1().equals("????????????")) {

                    if (mainActivity.get_cam_choice() == 1) {
                        Toast.makeText(getBaseContext(), "?????? ????????? ?????? ??? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getBaseContext(), Settings_storage_Activity.class);
                        intent.putExtra("storage_choice", mainActivity.get_storage_choice());
                        startActivityForResult(intent, STORAGE);
                    }
                } else if (mAdapter.getItem(i).getText_1().equals("????????????")) {
                    Intent intent = new Intent(getBaseContext(), Settings_map_Activity.class);
                    intent.putExtra("map_choice", mainActivity.get_map_choice());
                    startActivityForResult(intent, MAP);

                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //    Log.d(TAG, "onItemLongClick " + i);
                return true;
            }
        });

        listView.setOnTouchListener(new AdapterView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "x:" + event.getX() + " y:" + event.getY() +
                        " tilx:" + event.getAxisValue(event.AXIS_TILT) + " ori:" + event.getOrientation() + " pre:" + event.getPressure());
                return false;
            }
        });

        listView.setOnHoverListener(new AdapterView.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                Log.d(TAG, "hover x:" + event.getX() + " y:" + event.getY() +
                " tilx:" + event.getAxisValue(event.AXIS_TILT) + " ori:" + event.getOrientation() + " pre:" + event.getPressure());
                return false;
            }
        });

        MobileAds.initialize(this, "ca-app-pub-4387339919511881~4433795886");
        setAds();
    }

    private void setAds() {
        mAdView = (AdView) findViewById(R.id.adView_1);
        AdRequest adRequest = new AdRequest.Builder()
                //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //         .addTestDevice("86AFEB741F059FAD6147AAA3A2B25BD4")
                .build();
        mAdView.loadAd(adRequest);
    }
    private void KillApp() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                android.os.Process.killProcess(android.os.Process.myPid());

            }
        }, 3000);
    }
    private final int FOLDER_MARKER = 1;
    private final int FOLDER_LEVEL = 2;
    private final int CAMERA = 3;
    private final int STORAGE = 4;
    private final int EMAIL = 5;
    private final int MAP = 6;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FOLDER_MARKER:
                if (resultCode == RESULT_OK) {
                    //       Log.d(TAG, "result: " + data.getStringExtra("data"));

                    MainActivity mainActivity = new MainActivity();
                    mainActivity.set_marker_type(Integer.parseInt(data.getStringExtra("data")) - 1);

                } else if (resultCode == RESULT_CANCELED) {
                    //        Log.d(TAG, "canceled");
                }
                break;

            case FOLDER_LEVEL:
                if (resultCode == RESULT_OK) {
                    //    Log.d(TAG, "result: " + data.getStringExtra("data"));

                    MainActivity mainActivity = new MainActivity();
                    mainActivity.set_level(Integer.parseInt(data.getStringExtra("data")));

                } else if (resultCode == RESULT_CANCELED) {
                    //     Log.d(TAG, "canceled");
                }
                break;
            case CAMERA:
                if (resultCode == RESULT_OK) {
             //       Log.d(TAG, "result: " + data.getStringExtra("data"));

                    MainActivity mainActivity = new MainActivity();
                    mainActivity.set_default_camera(Integer.parseInt(data.getStringExtra("data")));

                } else if (resultCode == RESULT_CANCELED) {
                    //     Log.d(TAG, "canceled");
                }
                break;
            case STORAGE:
     //           Log.d(TAG, "Request_file_path");
                try {
                    if (resultCode == RESULT_OK) {
           //             Log.d(TAG, "result: " + data.getStringExtra("data"));

                        MainActivity mainActivity = new MainActivity();
                        mainActivity.set_default_storage(Integer.parseInt(data.getStringExtra("data")));

                    } else if (resultCode == RESULT_CANCELED) {
                        //     Log.d(TAG, "canceled");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EMAIL:
                try {
                    if (resultCode == RESULT_OK) {
             //           Log.d(TAG, "result: " + data.getStringExtra("data"));

                    } else if (resultCode == RESULT_CANCELED) {
                        //     Log.d(TAG, "canceled");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MAP:
                try {
                    if (resultCode == RESULT_OK) {
             //           Log.d(TAG, "result: " + data.getStringExtra("data"));

                        MainActivity mainActivity = new MainActivity();
                        mainActivity.set_default_map(Integer.parseInt(data.getStringExtra("data")));

                    } else if (resultCode == RESULT_CANCELED) {
                        //     Log.d(TAG, "canceled");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
